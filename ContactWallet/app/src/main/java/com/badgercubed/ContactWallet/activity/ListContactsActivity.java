package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.adapter.ContactAdapter;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListContactsActivity extends AppCompatActivity {
    private TextView m_currentUser;
    private RecyclerView m_recyclerView;
    private RecyclerView.LayoutManager m_layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        m_currentUser = findViewById(R.id.listContacts_currentUser);
        m_currentUser.setText(LoginManager.getInstance().getCurrentUser().getName());
        m_currentUser.setOnClickListener(l -> {
            Activities.startContactDetailsActivity(this,
                    LoginManager.getInstance().getCurrentUser().getUid(), ProtectionLevel.PUBLIC.getInt());
        });

        // use a linear layout manager
        m_layoutManager = new LinearLayoutManager(this);

        m_recyclerView = findViewById(R.id.listContacts_recycler);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(m_layoutManager);

        Button profile = findViewById(R.id.listContacts_profile);
        profile.setOnClickListener(l -> Activities.startProfileActivity(this));

        refreshDataSet();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshDataSet();
    }

    private void refreshDataSet() {
        List<Following> userDataSet = new ArrayList<>();
        RecyclerView.Adapter contactAdapter = new ContactAdapter(ListContactsActivity.this, userDataSet);

        m_recyclerView.setAdapter(contactAdapter);

        EventListener<QuerySnapshot> queryListener = (queryDocumentSnapshots, e) -> {
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Following contact = documentChange.getDocument().toObject(Following.class);
                    userDataSet.add(contact);
                }
            }
            contactAdapter.notifyDataSetChanged();
        };

        FBManager.getInstance().getFollowingUsers(this, new ArrayList<>(), queryListener);
    }
}
