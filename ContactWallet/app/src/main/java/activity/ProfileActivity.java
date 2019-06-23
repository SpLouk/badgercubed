package activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import model.ContactItem;
import model.User;
import util.FBManager;

public class ProfileActivity extends AppCompatActivity {
    private TextView m_email;
    private EditText m_name;
    private EditText m_phoneNum;
    private Button m_save;
    private Button m_logout;
    private Button m_userContact;
    private Button m_addContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FBManager.getInstance().getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(ProfileActivity.this, "ERROR, user somehow logged out", Toast.LENGTH_SHORT).show();
            finish();
            Activities.startLoginActivity(this);
        }

        m_email = findViewById(R.id.profile_email);
        m_email.setText(user.getEmail());

        m_name = findViewById(R.id.profile_enterName);
        m_phoneNum = findViewById(R.id.profile_enterPhoneNum);

        m_save = findViewById(R.id.profile_saveData);
        m_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                saveData();
            }
        });

        m_logout = findViewById(R.id.profile_logout);
        m_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                logoutUser();
            }
        });

        m_userContact = findViewById(R.id.profile_userContact);
        m_userContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Activities.startUserContactsActivity(ProfileActivity.this);
            }
        });

        // Dialog to allow current user to add contacts
        m_addContact = findViewById(R.id.profile_addContact);
        m_addContact.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
            View mView = getLayoutInflater().inflate(R.layout.dialog_add_contact, null);

            alertDialog.setView(mView);

            EditText description = mView.findViewById(R.id.add_contact_description);
            EditText link = mView.findViewById(R.id.add_contact_link);
            EditText protectionLevel = mView.findViewById(R.id.add_contact_protectionLevel);

            alertDialog.setTitle("Add Contact");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (DialogInterface dialog, int which) -> {
                        // TODO : Add contact item, still need to add to current users list of contact itemId
                        ContactItem contactItem = new ContactItem(UUID.randomUUID().toString(),
                                FBManager.getInstance().getCurrentUser().getUid(), "",
                                link.getText().toString(), description.getText().toString(),
                                Integer.parseInt(protectionLevel.getText().toString()));

                        FBManager.getInstance().saveFBObject(ProfileActivity.this, contactItem, null);

                        dialog.dismiss();
                    });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    (DialogInterface dialog, int which) -> dialog.dismiss());

            alertDialog.show();
        });
    }

    public void saveData() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = fbUser.getUid();
        String email = fbUser.getEmail();
        String name = m_name.getText().toString().trim();
        String phoneNum = m_phoneNum.getText().toString().trim();

        User user = new User(uid, email, name, phoneNum, new ArrayList<String>());
        FBManager.getInstance().saveFBObject(this, user, null);
    }

    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Activities.startLoginActivity(this);
    }
/*
    public void testAdd() {
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
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
