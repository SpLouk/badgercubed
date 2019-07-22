package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.dialog.AddConnectionDialog;
import com.badgercubed.ContactWallet.fragment.ConnectionsFragment;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.StoreManager;

public class ContactDetailsActivity extends AppCompatActivity implements EditConnectionCallback{
    private TextView m_userName;

    private ConnectionsFragment m_connectionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // TODO : Get userid from savedInstanceState and pass to connectionsFragment
        String uid = getIntent().getStringExtra(Activities.INTENT_FOLLOWING_USER_UID); //TODO: null check
        ProtectionLevel relationshipProtLevel = (ProtectionLevel) getIntent().getSerializableExtra(Activities.INTENT_REL_PROT_LEVEL);

        StoreManager.getInstance().getCollection(User.m_collectionName)
                .document(uid)
                .addSnapshotListener((documentSnapshot, ex) -> {
                    m_userName = findViewById(R.id.activity_contact_details_name);
                    m_userName.setText(documentSnapshot.get("name").toString());
                });

        m_connectionsFragment = ConnectionsFragment.newInstance(uid, relationshipProtLevel);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.activity_contact_details_layout, m_connectionsFragment).commit();
    }

    public void connectionEdited() {
        m_connectionsFragment.queryContacts();
    }
}
