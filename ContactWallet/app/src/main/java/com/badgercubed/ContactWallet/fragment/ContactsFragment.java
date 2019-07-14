package com.badgercubed.ContactWallet.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.badgercubed.ContactWallet.dialog.AddContactDialog;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private FloatingActionButton m_addContact;
    private TextView m_currentUser;
    private RecyclerView recyclerView;
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
            Activities.startContactDetailsActivity(getContext(), LoginManager.getInstance().getCurrentUser().getUid(),
                    ProtectionLevel.PUBLIC.getInt());
        });

        m_addContact = view.findViewById(R.id.fragment_contacts_add_contact);
        m_addContact.setOnClickListener(l -> {
            AddContactDialog dialog = new AddContactDialog();
            dialog.show(getActivity().getFragmentManager(), "Add A Contact");
        });

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.fragment_contacts_listContacts_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshDataSet();
    }

    private void refreshDataSet() {
        List<Following> userDataSet = new ArrayList<>();
        RecyclerView.Adapter contactAdapter = new ContactAdapter(getActivity(), userDataSet);

        recyclerView.setAdapter(contactAdapter);

        EventListener<QuerySnapshot> queryListener = (queryDocumentSnapshots, e) -> {
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Following contact = documentChange.getDocument().toObject(Following.class);
                    userDataSet.add(contact);
                }
            }
            contactAdapter.notifyDataSetChanged();
        };

        FBManager.getInstance().getFollowingUsers(getActivity(), new ArrayList<>(), queryListener);
    }
}
