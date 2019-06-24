package Activity;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import util.ContactItemAdapter;

public class ContactItemsFragment extends Fragment {

    private RecyclerView m_recyclerView;
    private ContactItemAdapter contactItemAdapter;

    // Firebase db
    private FirebaseFirestore m_db;
    private List<model.ContactItem> contactItems;

    public ContactItemsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_items_fragment, container, false);

        contactItems = new ArrayList<>();
        contactItemAdapter = new ContactItemAdapter(getActivity(), contactItems);

        m_recyclerView = view.findViewById(R.id.contact_items_fragment_recycler_view);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        m_recyclerView.setAdapter(contactItemAdapter);

        m_db = FirebaseFirestore.getInstance();
        m_db.collection("contactItems").addSnapshotListener((QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) -> {
           for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
               if (documentChange.getType() == DocumentChange.Type.ADDED) {
                   //ContactItem contactItem = documentChange.getDocument().toObject(ContactItem.class);
                   model.ContactItem contactItem = new model.ContactItem("", "", "", documentChange.getDocument().getString("link"), documentChange.getDocument().getString("description"), 1);
                   contactItems.add(contactItem);
                   contactItemAdapter.notifyDataSetChanged();
               }
           }
        });

        return view;
    }
}
