package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.fragment.ConnectionsFragment;

public class ContactDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // TODO : Get userid from savedInstanceState and pass to connectionsFragment
        String uid = getIntent().getStringExtra(Activities.INTENT_USER_UID); //TODO: null check

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contactDetails_fragmentFrame, ConnectionsFragment.newInstance(uid))
                .commit();
    }
}
