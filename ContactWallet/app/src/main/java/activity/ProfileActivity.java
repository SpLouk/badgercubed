package activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import model.User;

public class ProfileActivity extends AppCompatActivity {
    private TextView m_email;
    private EditText m_name;
    private EditText m_phoneNum;
    private Button m_save;
    private Button m_logout;

    // TODO: replace with progress bar
    private ProgressDialog m_progressDialog;

    // Firebase authentication object
    private FirebaseAuth m_firebaseAuth;
    // Firebase db
    private FirebaseFirestore m_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        m_firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = m_firebaseAuth.getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(ProfileActivity.this, "ERROR, user somehow logged out", Toast.LENGTH_SHORT).show();
            finish();
            Activities.startLoginActivity(this);
        }
        m_db = FirebaseFirestore.getInstance();
        if (m_db == null) {Toast.makeText(ProfileActivity.this, "ERROR, no db connection", Toast.LENGTH_SHORT).show();
            finish();
            Activities.startLoginActivity(this);
        }

        m_progressDialog = new ProgressDialog(this);

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
    }

    public void saveData() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = fbUser.getUid();
        String email = fbUser.getEmail();
        String name = m_name.getText().toString().trim();
        String phoneNum = m_phoneNum.getText().toString().trim();

        if (!User.isValidInputs(uid, email, name, phoneNum)) {
            Toast.makeText(this, "Error creating user: inputs invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(uid, email, name, phoneNum, new ArrayList<String>());
        saveUser(user);
    }

    public void saveUser(final User user) {
        CollectionReference usersCollection = m_db.collection("users");
        usersCollection
                .document(user.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileActivity.this, "Created/updated user with uid: " + user.getUid(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error adding document" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Activities.startLoginActivity(this);
    }
}
