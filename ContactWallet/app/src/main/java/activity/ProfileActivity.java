package activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.google.firebase.firestore.FieldValue;

import java.util.UUID;

import model.User;
import util.FBManager;
import util.FollowingManager;
import util.LoginManager;

public class ProfileActivity extends AppCompatActivity {
    private TextView m_email;
    private Button m_logout;
    private Button m_userContact;
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
            Activities.startLoginActivity(this);
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
            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
            View mView = getLayoutInflater().inflate(R.layout.dialog_add_contact, null);

            alertDialog.setView(mView);

            EditText description = mView.findViewById(R.id.add_contact_description);
            EditText link = mView.findViewById(R.id.add_contact_link);
            EditText protectionLevel = mView.findViewById(R.id.add_contact_protectionLevel);

            alertDialog.setTitle("Add Contact");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (DialogInterface dialog, int which) -> {
                        // TODO : Add contact item, still need to add to current users list of contact itemId
                        model.ContactItem contactItem = new model.ContactItem(UUID.randomUUID().toString(),
                                FBManager.getInstance().getCurrentFBUser().getUid(), "",
                                link.getText().toString(), description.getText().toString(),
                                Integer.parseInt(protectionLevel.getText().toString()));

                        FBManager.getInstance().saveFBObject(ProfileActivity.this, contactItem, null);
                        // Add new contactItem Id to current user
                        FBManager.getInstance().getCollection(User.m_collectionName)
                                .document(FBManager.getInstance().getCurrentFBUser().getUid())
                                .update("contactItemIds", FieldValue.arrayUnion(contactItem.getUid()));

                        dialog.dismiss();
                    });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    (DialogInterface dialog, int which) -> dialog.dismiss());

            alertDialog.show();
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        activity.ContactItemsFragment.newInstance(FBManager.getInstance().getCurrentFBUser().getUid()))
                .commit();
    }

    public void logoutUser() {
        FBManager.getInstance().logout();
        finish();
        Activities.startWelcomeActivity(this);
    }
}
