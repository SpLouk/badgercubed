package com.badgercubed.ContactWallet.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.activity.EditConnectionCallback;
import com.badgercubed.ContactWallet.adapter.ProtectionLevelAdapter;
import com.badgercubed.ContactWallet.adapter.ServiceAdapter;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.model.Service;
import com.badgercubed.ContactWallet.util.App;
import com.badgercubed.ContactWallet.util.AuthManager;
import com.badgercubed.ContactWallet.util.OauthManager;
import com.badgercubed.ContactWallet.util.StoreManager;
import com.badgercubed.ContactWallet.widget.PrefixEditText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.badgercubed.ContactWallet.model.Service.GITHUB;
import static com.badgercubed.ContactWallet.model.Service.TWITTER;

public class AddConnectionDialog extends DialogFragment {
    private static final String TAG = "T-AddConnectionDialog";

    private Connection m_editConnection = null;
    private EditConnectionCallback m_editCallback = null;

    private Spinner m_serviceSpinner;
    private Spinner m_protectionLevelSpinner;
    private EditText m_description;
    private Switch m_switch;
    private PrefixEditText m_link;

    private Service m_selectedService = null;
    private ProtectionLevel m_selectedProtectionLevel = null;
    private boolean m_verified = false;

    public static AddConnectionDialog newInstance() {
        AddConnectionDialog fragment = new AddConnectionDialog();
        fragment.setConnectionData(null);
        return fragment;
    }

    public static AddConnectionDialog newInstance(Connection editConnection) {
        AddConnectionDialog fragment = new AddConnectionDialog();
        fragment.setConnectionData(editConnection);
        return fragment;
    }

    public void setConnectionData(Connection editConnection) {
        m_editConnection = editConnection;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditConnectionCallback) {
            m_editCallback = (EditConnectionCallback) context;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String positiveText;
        if (m_editConnection != null) {
            builder.setTitle("Edit Contact Info");
            positiveText = "Update";
        } else {
            builder.setTitle("Add Contact Info");
            positiveText = "Create";
        }

        View dialogView = createDialogView();
        builder.setView(dialogView);

        builder.setPositiveButton(positiveText, (dialog, which) -> {
            if (createAndSaveConnections()) {
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Could not save. Make sure all fields are valid.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }

    private View createDialogView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_connection, null);

        List<Service> services = new ArrayList<>(Arrays.asList(Service.values()));
        ArrayAdapter<Service> serviceAdapter = new ServiceAdapter(getActivity(), services);

        m_serviceSpinner = view.findViewById(R.id.addConnnection_serviceSpinner);
        m_serviceSpinner.setAdapter(serviceAdapter);
        m_serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Service selectedService = null;
                if (position > 0) {
                    // Account for first entry which isn't valid
                    position = position - 1;
                    selectedService = services.get(position);
                }

                if (selectedService != null && !selectedService.equals(m_selectedService)) {
                    m_selectedService = selectedService;
                    selectedServiceChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (m_editConnection != null) {
            m_selectedService = m_editConnection.getService();
            int pos = services.indexOf(m_selectedService);
            m_serviceSpinner.setSelection(pos+1);
        }

        m_link = view.findViewById(R.id.addConnection_link);
        if (m_editConnection != null) {
            m_link.setText(m_editConnection.getLink());
        }

        m_description = view.findViewById(R.id.addConnection_description);
        if (m_editConnection != null) {
            m_description.setText(m_editConnection.getDescription());
        }

        m_switch = view.findViewById(R.id.oauthVerify);
        m_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && (m_selectedService == TWITTER || m_selectedService == GITHUB)) {
                    OauthManager.getInstance().verifyService(getActivity(), m_selectedService)
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Map<String, Object> profile = authResult.getAdditionalUserInfo().getProfile();
                                            switch (m_selectedService) {
                                                case TWITTER:
                                                    m_link.setText((String)profile.get("screen_name"));
                                                    break;
                                                case GITHUB:
                                                    m_link.setText((String)profile.get("login"));
                                                    break;
                                            }
                                            m_link.setEnabled(false);
                                            m_switch.setEnabled(false);
                                            m_verified = true;
                                        }
                                    }
                            )
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    m_switch.setChecked(false);
                                    Toast.makeText(getActivity(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
        if (m_editConnection != null) {
            m_switch.setChecked(m_editConnection.getVerified());
        }

        List<ProtectionLevel> protectionLevels = new ArrayList<>(Arrays.asList(ProtectionLevel.values()));
        ArrayAdapter<ProtectionLevel> protLevelAdapter = new ProtectionLevelAdapter(getActivity(), protectionLevels);

        m_protectionLevelSpinner = view.findViewById(R.id.addConnnection_protectionLevelSpinner);
        m_protectionLevelSpinner.setAdapter(protLevelAdapter);
        m_protectionLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    m_selectedProtectionLevel = protectionLevels.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                m_selectedProtectionLevel = null;
            }
        });
        if (m_editConnection != null) {
            int pos = protectionLevels.indexOf(m_editConnection.getProtectionLevel());
            m_protectionLevelSpinner.setSelection(pos+1);
        }

        if (m_editConnection == null) {
            selectedServiceChanged();
        }
        return view;
    }

    private void selectedServiceChanged() {
        if (m_selectedService == null) {
            m_link.setVisibility(View.GONE);
            m_description.setVisibility(View.GONE);
            m_protectionLevelSpinner.setVisibility(View.GONE);
            m_switch.setVisibility(View.GONE);
        } else {
            m_link.setText("");
            m_link.setTag(m_selectedService.getLink());
            m_link.setVisibility(View.VISIBLE);
            m_description.setText("");
            m_description.setVisibility(View.VISIBLE);
            m_protectionLevelSpinner.setSelection(0);
            m_protectionLevelSpinner.setVisibility(View.VISIBLE);
            //TODO: this doesn't work
            if (m_link.hasFocus()) {
                m_link.clearFocus();
            }
        }

        if (m_selectedService == TWITTER || m_selectedService == GITHUB) {
            m_switch.setChecked(false);
            m_switch.setVisibility(View.VISIBLE);
        }
    }

    private boolean createAndSaveConnections() {
        // TODO: validate link
        String currentUserUid = AuthManager.getInstance().getAuthUser().getUid();
        String link = m_selectedService.isHttpLinkUsed() ?
                "http://www." + m_selectedService.getLink() + m_link.getText().toString() :
                m_link.getText().toString();
        String description = m_description.getText().toString();

        Connection connection = new Connection(currentUserUid, m_selectedService, link, description, m_selectedProtectionLevel, m_verified);
        if (m_editConnection!= null) {
            connection.setUid(m_editConnection.getUid());
        }
        try {
            Task<Void> saveTask = StoreManager.getInstance().saveFBObject(getActivity(), connection);
            saveTask.addOnSuccessListener(aVoid -> {
                if (m_editCallback!= null) {
                    m_editCallback.connectionEdited();
                }
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
