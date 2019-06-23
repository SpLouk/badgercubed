package activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.badgercubed.ContactWallet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.ContactItem;
import util.ContactItemAdapter;

public class UserContactsActivity extends AppCompatActivity {

    private RecyclerView m_recyclerView;

    private ContactItemAdapter contactItemAdapter;

    // Firebase db
    private FirebaseFirestore m_db;
    private List<model.ContactItem> contactItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercontacts);

        contactItems = new ArrayList<>();

        contactItemAdapter = new ContactItemAdapter(contactItems);
        m_recyclerView = findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        m_recyclerView.setAdapter(contactItemAdapter);

        m_db = FirebaseFirestore.getInstance();
        m_db.collection("contactItems").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        //ContactItem contactItem = documentChange.getDocument().toObject(ContactItem.class);
                        ContactItem contactItem = new ContactItem("", "", "", documentChange.getDocument().getString("link"), documentChange.getDocument().getString("description"), 1);
                        contactItems.add(contactItem);

                        contactItemAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getContacts() {
        // Get USERID from Intent ? coming from when user is clicked on main view
        // Temp query by email, need to change? email in contacts
        String email = "test1@gmail.com";

        m_db = FirebaseFirestore.getInstance();

        CollectionReference usersCollectionRef = m_db.collection("users");
        Query query = usersCollectionRef.whereEqualTo("email", "testxxx@gmail.com");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();

                    if (!querySnapshot.isEmpty()) {

                        // Should return length 1 by email
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                        if (document.exists()) {
                            List<String> contactItemIds = (List<String>) document.get("contactItemIds");
                            getContactItems(contactItemIds);
                        }
                    }
                }
            }
        });
    }

    private void getContactItems(List<String> contactItemIds) {
        m_db = FirebaseFirestore.getInstance();

        for (String documentId : contactItemIds) {
            DocumentReference documentReference = m_db.collection("contactItems").document(documentId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ContactItem contactItem = new ContactItem("", "", "", (String) document.get("link"), "", document.getLong("protectionLevel").intValue());
                            contactItems.add(contactItem);
                        }
                    }
                }
            });
        }
    }
}
