package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.adapter.ConnectionAdapter;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.util.FBManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsFragment extends Fragment {

    private RecyclerView m_recyclerView;
    private ConnectionAdapter m_connectionAdapter;

    private String m_userId = "";
    private List<Connection> m_connections;

    public ConnectionsFragment() {
    }

    public static ConnectionsFragment newInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);

        ConnectionsFragment connectionsFragment = new ConnectionsFragment();
        connectionsFragment.setArguments(bundle);

        return connectionsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_connections, container, false);

        String activityName = getActivity().getClass().getSimpleName();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            m_userId = getArguments().get("userId").toString();
        }

        m_connections = new ArrayList<>();
        m_connectionAdapter = new ConnectionAdapter(getActivity(), activityName, m_connections);

        m_recyclerView = view.findViewById(R.id.listConnections_recyclerView);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        m_recyclerView.setAdapter(m_connectionAdapter);

        Query query = FBManager.getInstance().getCollection(Connection.m_collectionName);

        if (!m_userId.trim().isEmpty()) {
            query = query.whereEqualTo("userId", m_userId);


            // TODO : use userid to get the user and the protection level between current user and following,
            //  each connection has to be <= the following protectionlevel to display
        }

        // TODO : refactor to general method?
        query.addSnapshotListener((QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) -> {
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Connection connection = documentChange.getDocument().toObject(Connection.class);

                    m_connections.add(connection);
                    m_connectionAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
