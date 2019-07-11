package com.badgercubed.ContactWallet.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.adapter.ProtetionLevelAdapter;
import com.badgercubed.ContactWallet.adapter.ServiceAdapter;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.model.Service;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.widget.PrefixEditText;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddConnectionDialog extends DialogFragment {
    private static final String TAG = "T-AddConnectionDialog";

    private Spinner m_serviceSpinner;
    private Spinner m_protectionLevelSpinner;
    private EditText m_description;
    private PrefixEditText m_link;

    private Service m_selectedService = null;
    private ProtectionLevel m_selectedProtectionLevel = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add A Connection");

        View dialogView = createDialogView();
        builder.setView(dialogView);

        builder.setPositiveButton("Create", (dialog, which) -> {
            createAndSaveConnections();
            dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }

    public View createDialogView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_contact, null);

        List<Service> services = new ArrayList<>(Arrays.asList(Service.values()));
        ArrayAdapter<Service> serviceAdapter = new ServiceAdapter(getActivity(), services);

        m_serviceSpinner = view.findViewById(R.id.addContact_serviceSpinner);
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

        m_link = view.findViewById(R.id.addContact_link);
        m_description = view.findViewById(R.id.addContact_description);

        List<ProtectionLevel> protectionLevels = new ArrayList<>(Arrays.asList(ProtectionLevel.values()));
        ArrayAdapter<ProtectionLevel> protLevelAdapter = new ProtetionLevelAdapter(getActivity(), protectionLevels);

        m_protectionLevelSpinner = view.findViewById(R.id.addContact_protectionLevelSpinner);
        m_protectionLevelSpinner.setAdapter(protLevelAdapter);
        m_protectionLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_selectedProtectionLevel = null;
                if (position > 0) {
                    // Account for first entry which isn't valid
                    position = position - 1;
                    m_selectedProtectionLevel = protectionLevels.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        selectedServiceChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setTitle("Add A Contact");
    }

    private void selectedServiceChanged() {
        if (m_selectedService == null) {
            m_link.setVisibility(View.GONE);
            m_description.setVisibility(View.GONE);
            m_protectionLevelSpinner.setVisibility(View.GONE);
            return;
        }

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

    private void valiateLink() {
        // TODO: something with OAuth

    }

    private void createAndSaveConnections() {
        // TODO: validate link

        UUID connectionUid = UUID.randomUUID();
        String currentUserUid = FBManager.getInstance().getCurrentFBUser().getUid();
        String link = m_link.getText().toString();
        String description = m_description.getText().toString();
        Integer protectionLevel = Integer.parseInt(m_selectedProtectionLevel.getName());

        // TODO : Add connection, still need to add to current users list of connectionIds
        Connection connection = new Connection(connectionUid.toString(), currentUserUid, "",
                link, description, protectionLevel);
        FBManager.getInstance().saveFBObject(getActivity(), connection, null);

        // Add new connection Id to current user
        FBManager.getInstance().getCollection(User.m_collectionName)
                .document(currentUserUid)
                .update("connectionIds", FieldValue.arrayUnion(connection.getUid()));
    }
}
