package com.badgercubed.ContactWallet.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.adapter.ConnectionAdapter;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.util.StoreManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsFragment extends Fragment {
    private final static String TAG = "T-ConnectionsFragment";

    private TextView m_contactInfoText;
    private RecyclerView m_recyclerView;
    private ConnectionAdapter m_connectionAdapter;

    private List<Connection> m_connections;
    private String m_followingUserUid = "";
    private ProtectionLevel m_followingProtectionLevel = null;

    public static ConnectionsFragment newInstance(String followingUserUid, ProtectionLevel relationshipProtectionLevel) {
        Bundle bundle = new Bundle();
        bundle.putString(Activities.INTENT_FOLLOWING_USER_UID, followingUserUid);
        bundle.putSerializable(Activities.INTENT_REL_PROT_LEVEL, relationshipProtectionLevel);

        ConnectionsFragment connectionsFragment = new ConnectionsFragment();
        connectionsFragment.setArguments(bundle);

        return connectionsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_connections, container, false);

        Bundle bundle = this.getArguments();

        if (bundle == null) {
            Toast.makeText(getActivity(), "ERROR: Bundle empty", Toast.LENGTH_SHORT);
            Log.e(TAG, "Bundle empty");
            getActivity().finish();
            return view;
        }

        m_followingUserUid = getArguments().get(Activities.INTENT_FOLLOWING_USER_UID).toString();
        m_followingProtectionLevel = (ProtectionLevel) getArguments().get(Activities.INTENT_REL_PROT_LEVEL);

        m_contactInfoText = view.findViewById(R.id.listConnections_contactInfoText);

        m_recyclerView = view.findViewById(R.id.listConnections_recyclerView);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        m_recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            private void updateUI() {
                if (m_connections.isEmpty()) {
                    m_contactInfoText.setVisibility(View.VISIBLE);
                } else {
                    m_contactInfoText.setVisibility(View.GONE);
                }
            }
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                updateUI();
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                updateUI();
            }
        });

        String activityName = getActivity().getClass().getSimpleName();
        m_connections = new ArrayList<>();
        m_connectionAdapter = new ConnectionAdapter(getActivity(), activityName, m_connections);
        m_recyclerView.setAdapter(m_connectionAdapter);

        // Add dividing line between each item
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        m_recyclerView.addItemDecoration(itemDecor);

        if (m_followingUserUid.isEmpty()) {
            Toast.makeText(getActivity(), "ERROR: user ID empty", Toast.LENGTH_SHORT);
            Log.e(TAG, "ERROR: user ID empty");
            getActivity().finish();
            return view;
        }

        if (m_followingUserUid == StoreManager.getInstance().getCurrentUser().getUid()) {
            m_followingProtectionLevel = ProtectionLevel.PUBLIC;
        }

        if (m_followingProtectionLevel == null) {
            Toast.makeText(getActivity(), "ERROR: protection level not found", Toast.LENGTH_SHORT);
            Log.e(TAG, "ERROR: protection level not found");
            getActivity().finish();
            return view;
        }

        if (m_followingProtectionLevel == null) {
            Toast.makeText(getActivity(), "ERROR: failed to determine protection level, num=" + m_followingProtectionLevel,
                    Toast.LENGTH_SHORT);
            Log.e(TAG, "ERROR: failed to determine protection level, num=" + m_followingProtectionLevel);
            getActivity().finish();
            return view;
        }

        queryContacts(m_followingProtectionLevel);

        return view;
    }

    private void queryContacts(ProtectionLevel protectionLevel) {
        for (ProtectionLevel p : ProtectionLevel.values()) {
            if (p.ordinal() > protectionLevel.ordinal()) {
                return;
            }
            Query query = StoreManager.getInstance().getCollection(Connection.m_collectionName);
            query = query.whereEqualTo("userId", m_followingUserUid);
            query = query.whereEqualTo("protectionLevel", p.toString());
            query.addSnapshotListener((QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) -> {
                if (queryDocumentSnapshots != null) {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            Connection connection = documentChange.getDocument().toObject(Connection.class);
                            m_connections.add(connection);
                        }
                    }
                    m_connectionAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
