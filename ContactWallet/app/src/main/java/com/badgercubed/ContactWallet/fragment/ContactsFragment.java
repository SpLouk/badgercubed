package com.badgercubed.ContactWallet.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import com.badgercubed.ContactWallet.model.User;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.adapter.ContactAdapter;
import com.badgercubed.ContactWallet.dialog.AddContactDialog;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.util.StoreManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private ExtendedFloatingActionButton m_addContact;
    private RecyclerView m_recyclerView;
    private RecyclerView.LayoutManager m_layoutManager;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        getActivity().setTitle("Contacts");

        m_addContact = view.findViewById(R.id.fragment_contacts_add_contact);
        m_addContact.setOnClickListener(l -> {
            AddContactDialog dialog = new AddContactDialog();
            dialog.show(getActivity().getFragmentManager(), "Add A Contact");
        });

        // use a linear layout manager
        m_layoutManager = new LinearLayoutManager(getContext());

        m_recyclerView = view.findViewById(R.id.fragment_contacts_listContacts_recycler);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(m_layoutManager);

        // Add dividing line between each item
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        m_recyclerView.addItemDecoration(itemDecor);

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

        m_recyclerView.setAdapter(contactAdapter);

        EventListener<QuerySnapshot> queryListener = (queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        Following contact = documentChange.getDocument().toObject(Following.class);
                        userDataSet.add(contact);
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }
        };


        Query following = StoreManager.getInstance().getFollowingUsers();
        following.addSnapshotListener(queryListener);
    }
}
