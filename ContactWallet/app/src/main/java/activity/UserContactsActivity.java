package activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

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

import javax.annotation.Nullable;

import model.ContactItem;
import util.ContactItemAdapter;
import util.ContactItemAdapter2;

public class UserContactsActivity extends AppCompatActivity {

    private GridView m_gridView;
    private RecyclerView m_recyclerView;

    private ContactItemAdapter2 contactItemAdapter2;

    // Firebase db
    private FirebaseFirestore m_db;
    private List<model.ContactItem> contactItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercontacts);

        contactItems = new ArrayList<>();
        contactItemAdapter2 = new ContactItemAdapter2(contactItems);
        m_recyclerView = findViewById(R.id.recycler_view);
        m_recyclerView.setHasFixedSize(true);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        m_recyclerView.setAdapter(contactItemAdapter2);

        m_db = FirebaseFirestore.getInstance();
        m_db.collection("contactItems").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        //ContactItem contactItem = documentChange.getDocument().toObject(ContactItem.class);
                        ContactItem contactItem = new ContactItem("", "", "", documentChange.getDocument().getString("link"), documentChange.getDocument().getString("description"), 1);
                        contactItems.add(contactItem);

                        Log.d("FIRELOG", contactItem.getDescription());

                        contactItemAdapter2.notifyDataSetChanged();
                    }
                }
            }
        });

    /*    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("contactItems");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

            }
        })*/

      //  getContacts();

     //   RecyclerView recyclerView = findViewById(R.id.recycler_view);
     //   recyclerView.setLayoutManager(new LinearLayoutManager(this));

      //  m_gridView = findViewById(R.id.usercontact_gridview);
      //  ContactItemAdapter contactItemAdapter = new ContactItemAdapter(this, contactItems);
      //  m_gridView.setAdapter(contactItemAdapter);
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
