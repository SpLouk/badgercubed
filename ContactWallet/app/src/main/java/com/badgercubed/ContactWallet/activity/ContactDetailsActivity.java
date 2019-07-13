package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.model.ProtectionLevel;

public class ContactDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // TODO : Get userid from savedInstanceState and pass to connectionsFragment
        String uid = getIntent().getStringExtra(Activities.INTENT_FOLLOWING_USER_UID); //TODO: null check
        Integer relationshipProtLevel = getIntent().getIntExtra(Activities.INTENT_REL_PROT_LEVEL, -1);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contactDetails_fragmentFrame, ConnectionsFragment.newInstance(uid, relationshipProtLevel))
                .commit();
    }
}
