package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.adapter.ContactItemAdapter;
import com.badgercubed.ContactWallet.model.ContactItem;
import com.badgercubed.ContactWallet.util.FBManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactItemsFragment extends Fragment {

    private RecyclerView m_recyclerView;
    private ContactItemAdapter m_contactItemAdapter;

    private String m_userId = "";
    private List<ContactItem> m_contactItems;

    public ContactItemsFragment() {
    }

    public static ContactItemsFragment newInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);

        ContactItemsFragment contactItemsFragment = new ContactItemsFragment();
        contactItemsFragment.setArguments(bundle);

        return contactItemsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_items_fragment, container, false);

        String activityName = getActivity().getClass().getSimpleName();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            m_userId = getArguments().get("userId").toString();
        }

        m_contactItems = new ArrayList<>();
        m_contactItemAdapter = new ContactItemAdapter(getActivity(), activityName, m_contactItems);

        m_recyclerView = view.findViewById(R.id.contact_items_fragment_recycler_view);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        m_recyclerView.setAdapter(m_contactItemAdapter);

        Query query = FBManager.getInstance().getCollection(ContactItem.m_collectionName);

        if (!m_userId.trim().isEmpty()) {
            query = query.whereEqualTo("userId", m_userId);


            // TODO : use userid to get the user and the protection level between current user and following,
            //  each contact item has to be <= the following protectionlevel to display
        }

        // TODO : refactor to general method?
        query.addSnapshotListener((QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) -> {
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ContactItem contactItem = documentChange.getDocument().toObject(ContactItem.class);

                    m_contactItems.add(contactItem);
                    m_contactItemAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
