package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.fragment.ConnectionsFragment;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.StoreManager;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView m_userName;
    private TextView m_userEmail;
    private TextView m_userPhoneNumber;
    private ImageView m_dropdownArrow;
    private LinearLayout m_linearLayout;

    private boolean isDroppedDown = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // TODO : Get userid from savedInstanceState and pass to connectionsFragment
        String uid = getIntent().getStringExtra(Activities.INTENT_FOLLOWING_USER_UID); //TODO: null check
        Integer relationshipProtLevel = getIntent().getIntExtra(Activities.INTENT_REL_PROT_LEVEL, -1);

        StoreManager.getInstance().getCollection(User.m_collectionName)
                .document(uid)
                .addSnapshotListener((documentSnapshot, ex) -> {
                    m_userName = findViewById(R.id.activity_contact_details_name);
                    m_userName.setText(documentSnapshot.get("name").toString());

                    m_userEmail = findViewById(R.id.activity_contact_details_email);
                    m_userEmail.setText(documentSnapshot.get("email").toString());

                    m_userPhoneNumber = findViewById(R.id.activity_contact_details_phone_number);
                    m_userPhoneNumber.setText(documentSnapshot.get("phoneNum").toString());
                });

        ConnectionsFragment connectionsFragment = ConnectionsFragment.newInstance(uid, relationshipProtLevel);

        m_dropdownArrow = findViewById(R.id.activity_contact_details_dropdown_arrow);
        m_linearLayout = findViewById(R.id.activity_contact_details_dropdown);
        m_linearLayout.setOnClickListener(l -> {

            if (!isDroppedDown) {
                m_dropdownArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                isDroppedDown = true;
            } else {
                m_dropdownArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                isDroppedDown = false;
            }

            showHideFragment(connectionsFragment);
        });
    }

    private void showHideFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.activity_contact_details_connections_container, fragment);
            fragmentTransaction.show(fragment);
        } else {
            if (fragment.isHidden()) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.hide(fragment);
            }
        }

        fragmentTransaction.commit();
    }
}
