package activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badgercubed.ContactWallet.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import util.ContactItemAdapter;

public class ContactItemsFragment extends Fragment {

    private RecyclerView m_recyclerView;
    private ContactItemAdapter contactItemAdapter;

    private String userId = "";

    // Firebase db
    private FirebaseFirestore m_db;
    private List<model.ContactItem> contactItems;

    public ContactItemsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_items_fragment, container, false);

        String activityName = getActivity().getClass().getSimpleName();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            userId = getArguments().get("userId").toString();
        }

        contactItems = new ArrayList<>();
        contactItemAdapter = new ContactItemAdapter(getActivity(), activityName, contactItems);

        m_recyclerView = view.findViewById(R.id.contact_items_fragment_recycler_view);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        m_recyclerView.setAdapter(contactItemAdapter);

        //.whereEqualTo("userId", userId)
        m_db = FirebaseFirestore.getInstance();
        Query query = m_db.collection("contactItems");

        if (!userId.trim().isEmpty()) {
            query = query.whereEqualTo("userId", userId);
        }

        // TODO : refactor to FBManager
        query.addSnapshotListener((QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) -> {
           for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
               if (documentChange.getType() == DocumentChange.Type.ADDED) {
                   model.ContactItem contactItem = documentChange.getDocument().toObject(model.ContactItem.class);

                   contactItems.add(contactItem);
                   contactItemAdapter.notifyDataSetChanged();
               }
           }
        });

        return view;
    }

    public static ContactItemsFragment newInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);

        ContactItemsFragment contactItemsFragment = new ContactItemsFragment();
        contactItemsFragment.setArguments(bundle);

        return contactItemsFragment;
    }
}
