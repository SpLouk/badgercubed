package com.badgercubed.ContactWallet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.adapter.ContactAdapter;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private TextView m_currentUser;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter m_contactAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        m_currentUser = view.findViewById(R.id.fragment_contacts_listContacts_currentUser);
        m_currentUser.setText(LoginManager.getInstance().getCurrentUser().getName());
        m_currentUser.setOnClickListener(l -> {
            Activities.startContactDetailsActivity(getContext(), LoginManager.getInstance().getCurrentUser().getUid());
        });

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());

        List<Following> userDataSet = new ArrayList<>();
        m_contactAdapter = new ContactAdapter(getContext(), userDataSet);

        recyclerView = view.findViewById(R.id.fragment_contacts_listContacts_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(m_contactAdapter);
        recyclerView.setLayoutManager(layoutManager);

        EventListener<QuerySnapshot> queryListener = (queryDocumentSnapshots, e) -> {
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Following contact = documentChange.getDocument().toObject(Following.class);
                    userDataSet.add(contact);
                    m_contactAdapter.notifyDataSetChanged();
                }
            }
        };

        FBManager.getInstance().getFollowingUsers(getContext(), new ArrayList<>(), queryListener);

        return view;
    }
}
