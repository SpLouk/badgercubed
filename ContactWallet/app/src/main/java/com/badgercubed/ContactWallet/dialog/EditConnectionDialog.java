package com.badgercubed.ContactWallet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.adapter.ProtectionLevelAdapter;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.util.StoreManager;

import java.util.HashMap;
import java.util.Map;

public class EditConnectionDialog extends DialogFragment {
    private static final String TAG = "T-EditConnectionDialog";

    private Spinner m_protectionLevelSpinner;
    private EditText m_description;

    private ProtectionLevel m_selectedProtectionLevel = null;

    private Connection m_connection;
    private int position;

    private onModifyListener callback;

    public static EditConnectionDialog newInstance(int position, Connection connection) {
        EditConnectionDialog fragment = new EditConnectionDialog();
        fragment.setConnectionData(position, connection);
        return fragment;
    }

    public void setListener(onModifyListener listener) {
        this.callback = listener;
    }

    public void setConnectionData(int position, Connection connection) {
        this.position = position;
        this.m_connection = connection;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Contact Info");

        View dialogView = createDialogView();
        builder.setView(dialogView);

        builder.setPositiveButton("Update", (dialog, which) -> {
            if (updateConnection()) {
                if (callback != null) {
                    callback.onModify(m_description.getText().toString(), m_selectedProtectionLevel.toString(), position);
                }
                dismiss();
            } else {
                Toast.makeText(getActivity(), "Could not save. Make sure all fields are valid.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }

    private View createDialogView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_connection, null);

        // Set edit text field to current connection's description
        m_description = view.findViewById(R.id.dialog_edit_connection_description);
        m_description.setText(m_connection.getDescription());

        ProtectionLevel[] protectionLevels = ProtectionLevel.values();
        ArrayAdapter<ProtectionLevel> protectionLevelAdapter = new ProtectionLevelAdapter(getActivity(), protectionLevels);

        m_protectionLevelSpinner = view.findViewById(R.id.dialog_edit_connection_protection_level);
        m_protectionLevelSpinner.setAdapter(protectionLevelAdapter);
        // Set spinner to current connection's protection level
        int spinnerPosition = protectionLevelAdapter
                .getPosition(ProtectionLevel.valueOf(m_connection.getProtectionLevel().toString()));
        m_protectionLevelSpinner.setSelection(spinnerPosition + 1);

        m_protectionLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    m_selectedProtectionLevel = protectionLevels[position - 1];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                m_selectedProtectionLevel = null;
            }
        });

        return view;
    }

    private boolean updateConnection() {
        try {
            m_connection.validate();

            Map<String, Object> properties = new HashMap<>();
            properties.put("description", m_description.getText().toString());
            properties.put("protectionLevel", m_selectedProtectionLevel.toString());

            StoreManager.getInstance().updateFBObject(getActivity(), m_connection, properties);

        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
            return false;
        }
        return true;
    }

    public interface onModifyListener {
        void onModify(String description, String protectionLevel, int position);
    }
}
