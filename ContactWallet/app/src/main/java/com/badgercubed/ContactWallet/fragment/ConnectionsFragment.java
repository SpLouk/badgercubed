package com.badgercubed.ContactWallet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.adapter.ConnectionAdapter;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsFragment extends Fragment {
    private final static String TAG = "T-ConnectionsFragment";

    private RecyclerView m_recyclerView;
    private ConnectionAdapter m_connectionAdapter;

    private String m_followingUserUid = "";
    private Integer m_followingProtectionLevel = null;
    private List<Connection> m_connections;

    public static ConnectionsFragment newInstance(String followingUserUid, Integer relationshipProtectionLevel) {
        Bundle bundle = new Bundle();
        bundle.putString(Activities.INTENT_FOLLOWING_USER_UID, followingUserUid);
        bundle.putInt(Activities.INTENT_REL_PROT_LEVEL, relationshipProtectionLevel);

        ConnectionsFragment connectionsFragment = new ConnectionsFragment();
        connectionsFragment.setArguments(bundle);

        return connectionsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_connections, container, false);

        String activityName = getActivity().getClass().getSimpleName();
        Bundle bundle = this.getArguments();

        if (bundle == null) {
            Toast.makeText(getActivity(), "ERROR: Bundle empty", Toast.LENGTH_SHORT);
            Log.e(TAG,  "Bundle empty");
            getActivity().finish();
            return view;
        }

        m_followingUserUid = getArguments().get(Activities.INTENT_FOLLOWING_USER_UID).toString();
        m_followingProtectionLevel = (Integer) getArguments().get(Activities.INTENT_REL_PROT_LEVEL);

        m_connections = new ArrayList<>();
        m_connectionAdapter = new ConnectionAdapter(getActivity(), activityName, m_connections);

        m_recyclerView = view.findViewById(R.id.listConnections_recyclerView);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        m_recyclerView.setAdapter(m_connectionAdapter);


        if (m_followingUserUid.isEmpty()) {
            Toast.makeText(getActivity(), "ERROR: user ID empty", Toast.LENGTH_SHORT);
            Log.e(TAG,  "ERROR: user ID empty");
            getActivity().finish();
            return view;
        }

        if (m_followingUserUid == LoginManager.getInstance().getCurrentUser().getUid()) {
            m_followingProtectionLevel = ProtectionLevel.PUBLIC.getInt();
        }

        if (m_followingProtectionLevel == null) {
            Toast.makeText(getActivity(), "ERROR: protection level not found", Toast.LENGTH_SHORT);
            Log.e(TAG,  "ERROR: protection level not found");
            getActivity().finish();
            return view;
        }

        ProtectionLevel protectionLevel = ProtectionLevel.fromInt(m_followingProtectionLevel);
        if (protectionLevel == null) {
            Toast.makeText(getActivity(), "ERROR: failed to determine protection level, num=" + m_followingProtectionLevel,
                    Toast.LENGTH_SHORT);
            Log.e(TAG,  "ERROR: failed to determine protection level, num=" + m_followingProtectionLevel);
            getActivity().finish();
            return view;
        }


        switch(protectionLevel) {
            case PRIVATE:
                queryContacts(ProtectionLevel.PRIVATE.getInt());
                // fall through
            case PROTECTED:
                queryContacts(ProtectionLevel.PROTECTED.getInt());
                // fall through
            case PUBLIC:
                queryContacts(ProtectionLevel.PUBLIC.getInt());
                break;
            default:
                break;
        }
        return view;
    }

    private void queryContacts(int protectionLevel) {
        Query query = FBManager.getInstance().getCollection(Connection.m_collectionName);
        query = query.whereEqualTo("userId", m_followingUserUid);
        query = query.whereEqualTo("protectionLevel", protectionLevel);
        query.addSnapshotListener((QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) -> {
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    Connection connection = documentChange.getDocument().toObject(Connection.class);

                    m_connections.add(connection);
                    m_connectionAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
