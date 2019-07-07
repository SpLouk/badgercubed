package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.badgercubed.ContactWallet.R;

public class UserContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercontacts);

        // TODO : Get userid from savedInstanceState and pass to contactItemsFragment
        String uid = getIntent().getStringExtra(Activities.INTENT_USER_UID); //TODO: null check

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, ContactItemsFragment.newInstance(uid))
                .commit();
    }
}
