package com.badgercubed.ContactWallet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.dialog.AddConnectionDialog;
import com.badgercubed.ContactWallet.dialog.AddContactDialog;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginManager;

public class ProfileFragment extends Fragment {
    private Button m_addContact;
    private Button m_addConnection;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        User user = LoginManager.getInstance().getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(getContext(), "ERROR, user somehow logged out", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logout();
            getActivity().finish();
            Activities.startWelcomeActivity(getContext());
            // return;
        }

        getActivity().setTitle(user.getName());

        m_addContact = view.findViewById(R.id.profile_addContact);
        m_addContact.setOnClickListener(l -> {
            AddContactDialog dialog = new AddContactDialog();
            dialog.show(getActivity().getFragmentManager(), "Add A Contact");
        });

        // Dialog to allow current user to add contacts
        m_addConnection = view.findViewById(R.id.profile_addConnection);
        m_addConnection.setOnClickListener(l -> {
            AddConnectionDialog dialog = new AddConnectionDialog();
            dialog.show(getActivity().getFragmentManager(), "Add A Connection");
        });

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ConnectionsFragment.newInstance(
                        FBManager.getInstance().getCurrentFBUser().getUid(), ProtectionLevel.PUBLIC.getInt()))
                .commit();

        return view;
    }
}