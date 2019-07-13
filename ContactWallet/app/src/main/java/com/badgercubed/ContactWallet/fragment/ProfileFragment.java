package com.badgercubed.ContactWallet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.dialog.AddConnectionDialog;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.FollowingManager;
import com.badgercubed.ContactWallet.util.LoginManager;

public class ProfileFragment extends Fragment {

    private TextView m_email;
    private Button m_addConnection;

    private EditText m_newFollowingInput;
    private Button m_saveFollowing;

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

        m_email = view.findViewById(R.id.fragment_profile_email);
        m_email.setText(user.getName());

        m_newFollowingInput = view.findViewById(R.id.fragment_profile_newFollowingText);
        m_saveFollowing = view.findViewById(R.id.fragment_profile_saveFollowing);
        m_saveFollowing.setOnClickListener(l ->
                FollowingManager.getInstance().addFollowing(getContext(), user, m_newFollowingInput.getText().toString())
        );


        // Dialog to allow current user to add contacts
        m_addConnection = view.findViewById(R.id.fragment_profile_add_connection);
        m_addConnection.setOnClickListener(v -> {
            AddConnectionDialog dialog = new AddConnectionDialog();
            dialog.show(getActivity().getFragmentManager(), "Add A Connection");
        });

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        ConnectionsFragment.newInstance(FBManager.getInstance().getCurrentFBUser().getUid()))
                .commit();

        return view;
    }
}
