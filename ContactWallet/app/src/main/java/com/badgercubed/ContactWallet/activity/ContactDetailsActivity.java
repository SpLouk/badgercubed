package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.fragment.ConnectionsFragment;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView m_userName;
    private TextView m_userEmail;
    private TextView m_userPhoneNumber;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // TODO : Get userid from savedInstanceState and pass to connectionsFragment
        String uid = getIntent().getStringExtra(Activities.INTENT_FOLLOWING_USER_UID); //TODO: null check
        Integer relationshipProtLevel = getIntent().getIntExtra(Activities.INTENT_REL_PROT_LEVEL, -1);

        FBManager.getInstance().getCollection(User.m_collectionName)
                .document(uid)
                .addSnapshotListener((documentSnapshot, ex) -> {
                    m_userName = findViewById(R.id.activity_contact_details_name);
                    m_userName.setText(documentSnapshot.get("name").toString());

                    m_userEmail = findViewById(R.id.activity_contact_details_email);
                    m_userEmail.setText(documentSnapshot.get("email").toString());

                    m_userPhoneNumber = findViewById(R.id.activity_contact_details_phone_number);
                    m_userPhoneNumber.setText(documentSnapshot.get("phoneNum").toString());
                });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_contact_details_connections_container,
                        ConnectionsFragment.newInstance(uid, relationshipProtLevel))
                .commit();
    }
}
