package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

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
import util.LoginManager;

public class ListContactsActivity extends AppCompatActivity {
    private TextView m_currentUser;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter m_contactAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);

        m_currentUser = findViewById(R.id.listContacts_currentUser);
        m_currentUser.setText(LoginManager.getInstance().getCurrentUser().getName());
        m_currentUser.setOnClickListener(l -> {
            Activities.startUserContactsActivity(this, LoginManager.getInstance().getCurrentUser().getUid());
        });

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);

        List<User> userDataSet = new ArrayList<>();
        m_contactAdapter = new ContactAdapter(userDataSet);

        recyclerView = findViewById(R.id.listContacts_recycler);
        recyclerView.setAdapter(m_contactAdapter);
        recyclerView.setLayoutManager(layoutManager);

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

        Button profile = findViewById(R.id.listContacts_profile);
        profile.setOnClickListener(l -> Activities.startProfileActivity(this));
    }
}
