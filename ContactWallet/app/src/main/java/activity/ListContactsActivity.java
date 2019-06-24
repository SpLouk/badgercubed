package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.badgercubed.ContactWallet.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import model.User;
import util.ContactAdapter;
import util.ContactItemAdapter;
import util.FBManager;

public class ListContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter m_contactAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);
        recyclerView = findViewById(R.id.listContacts_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<User> userDataSet = new ArrayList<>();
        m_contactAdapter = new ContactAdapter(userDataSet);
        recyclerView.setAdapter(m_contactAdapter);

        EventListener<QuerySnapshot> queryListener = (queryDocumentSnapshots, e) -> {
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    User contactItem = documentChange.getDocument().toObject(User.class);
                    userDataSet.add(contactItem);
                    m_contactAdapter.notifyDataSetChanged();
                }
            }
        };
        FBManager.getInstance().getFollowingUsers(this, null, queryListener);
    }
}
