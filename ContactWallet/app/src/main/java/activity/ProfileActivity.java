package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;

import model.User;
import util.FBManager;
import util.LoginManager;

public class ProfileActivity extends AppCompatActivity {
    private TextView m_email;
    private EditText m_name;
    private EditText m_phoneNum;
    private Button m_save;
    private Button m_logout;
    private Button m_userContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = LoginManager.getInstance().getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(ProfileActivity.this, "ERROR, user somehow logged out", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logout();
            finish();
            Activities.startLoginActivity(this);
            return;
        }

        m_email = findViewById(R.id.profile_email);
        m_email.setText(user.getEmail());

        m_name = findViewById(R.id.profile_enterName);
        m_phoneNum = findViewById(R.id.profile_enterPhoneNum);

        m_save = findViewById(R.id.profile_saveData);
        m_save.setOnClickListener(l -> {
            // Perform action on click
            saveData();
        });

        m_logout = findViewById(R.id.profile_logout);
        m_logout.setOnClickListener(l -> {
            // Perform action on click
            logoutUser();
        });

        m_userContact = findViewById(R.id.profile_userContact);
        m_userContact.setOnClickListener(l -> {
            // Perform action on click
            Activities.startUserContactsActivity(ProfileActivity.this);
        });
    }

    public void saveData() {
        /*User user = FBManager.getInstance().updateCurrentUser(this);
        String uid = user.getUid();
        String email = user.getEmail();
        String name = m_name.getText().toString().trim();
        String phoneNum = m_phoneNum.getText().toString().trim();

        User newUser = new User(uid, email, name, phoneNum, new ArrayList<String>());
        FBManager.getInstance().saveFBObject(this, newUser, null);*/
    }

    public void logoutUser() {
        FBManager.getInstance().logout();
        finish();
        Activities.startWelcomeActivity(this);
    }

    /*public void testAdd() {
        Map<String, Object> contactItem = new HashMap<>();
        contactItem.put("link", "https://www.facebook.com/randompage514/");
        contactItem.put("description", "FB");
        contactItem.put("protectionLevel", 1);

        m_db.collection("contactItems")
                .add(contactItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        addIdToUser(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    public void addIdToUser(final String id) {
        String uid = FirebaseAuth.getInstance().getCurrentFBUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final DocumentReference uidRef = rootRef.collection("users").document(uid);

        uidRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        List<String> contactItemIds = new ArrayList<>();
                        contactItemIds = (List<String>) document.getData().get("contactItemIds");
                        contactItemIds.add(id);

                        uidRef.update(
                                "contactItemIds", contactItemIds
                        );

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public void testRemove(final String id) {

        String uid = FirebaseAuth.getInstance().getCurrentFBUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final DocumentReference uidRef = rootRef.collection("users").document(uid);

        uidRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        List<String> contactItemIds = new ArrayList<>();
                        contactItemIds = (List<String>) document.getData().get("contactItemIds");
                        contactItemIds.remove(id);

                        uidRef.update(
                                "contactItemIds", contactItemIds
                        );

                        deleteId(id);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void deleteId(String id) {
        m_db.collection("contactItems").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }*/
}
