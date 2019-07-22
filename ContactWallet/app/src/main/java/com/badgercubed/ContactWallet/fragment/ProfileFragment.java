package com.badgercubed.ContactWallet.fragment;

import android.os.Bundle;

import com.badgercubed.ContactWallet.activity.EditConnectionCallback;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.dialog.AddConnectionDialog;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.AuthManager;
import com.badgercubed.ContactWallet.util.StoreManager;

public class ProfileFragment extends Fragment implements EditConnectionCallback {
    private ExtendedFloatingActionButton m_addConnection;
    private ConnectionsFragment m_connectionsFragment;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        User user = StoreManager.getInstance().getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(getContext(), "ERROR, user somehow logged out", Toast.LENGTH_SHORT).show();
            AuthManager.getInstance().logout();
            getActivity().finish();
            Activities.startWelcomeActivity(getContext());
            // return;
        }

        getActivity().setTitle("My Info");

        // Dialog to allow current user to add connections
        m_addConnection = view.findViewById(R.id.fragment_profile_add_connection);
        m_addConnection.setOnClickListener(l -> {
            AddConnectionDialog dialog = AddConnectionDialog.newInstance();
            dialog.show(getActivity().getFragmentManager(), "Add Contact Information");
        });

        m_connectionsFragment = ConnectionsFragment.newInstance( AuthManager.getInstance().getAuthUser().getUid(), ProtectionLevel.PRIVATE);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_connections_container, m_connectionsFragment)
                .commit();

        return view;
    }

    public void connectionEdited() {
        refreshFragment();
    }

    private void refreshFragment() {
        m_connectionsFragment = ConnectionsFragment.newInstance( AuthManager.getInstance().getAuthUser().getUid(), ProtectionLevel.PRIVATE);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_connections_container, m_connectionsFragment)
                .commit();

    }
}