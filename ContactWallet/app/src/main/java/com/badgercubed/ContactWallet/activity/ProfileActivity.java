package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.dialog.AddConnectionDialog;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.FollowingManager;
import com.badgercubed.ContactWallet.util.LoginManager;

public class ProfileActivity extends AppCompatActivity {

    private TextView m_email;
    private Button m_logout;
    private Button m_addContact;

    private EditText m_newFollowingInput;
    private Button m_saveFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        User user = LoginManager.getInstance().getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(ProfileActivity.this, "ERROR, user somehow logged out", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logout();
            finish();
            Activities.startWelcomeActivity(this);
            return;
        }

        setTitle(user.getName());

        m_email = findViewById(R.id.profile_email);
        m_email.setText(user.getName());

        m_logout = findViewById(R.id.profile_logout);
        m_logout.setOnClickListener(l -> {
            // Perform action on click
            logoutUser();
        });

        m_newFollowingInput = findViewById(R.id.profile_newFollowingText);
        m_saveFollowing = findViewById(R.id.profile_saveFollowing);
        m_saveFollowing.setOnClickListener(l -> {
            FollowingManager.getInstance().addFollowing(ProfileActivity.this, user, m_newFollowingInput.getText().toString());
        });

        // Dialog to allow current user to add contacts
        m_addContact = findViewById(R.id.profile_addContact);
        m_addContact.setOnClickListener(view -> {
            AddConnectionDialog dialog = new AddConnectionDialog();
            dialog.show(getFragmentManager(), "Add A Connection");
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        ConnectionsFragment.newInstance(FBManager.getInstance().getCurrentFBUser().getUid()))
                .commit();
    }

    public void logoutUser() {
        FBManager.getInstance().logout();
        finish();
        Activities.startWelcomeActivity(this);
    }
}
