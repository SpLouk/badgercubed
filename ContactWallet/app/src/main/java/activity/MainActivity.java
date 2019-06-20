package activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button m_register;
    private EditText m_enterEmail;
    private EditText m_enterPassword;
    private TextView m_login;

    // TODO: replace with progress bar
    private ProgressDialog m_progressDialog;

    // Firebase authentication object
    private FirebaseAuth m_firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_firebaseAuth = FirebaseAuth.getInstance();
        if (m_firebaseAuth.getCurrentUser() != null) {
            // User already logged in
            finish();
            Activities.startProfileActivity(this);
        }

        m_progressDialog = new ProgressDialog(this);

        m_register = findViewById(R.id.register_register);
        m_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                registerUser();
            }
        });

        m_enterEmail = findViewById(R.id.register_enterEmail);
        m_enterPassword = findViewById(R.id.register_enterPassword);

        m_login = findViewById(R.id.register_login);
        m_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Activities.startLoginActivity(MainActivity.this);
            }
        });
    }

    private void registerUser() {
        String email = m_enterEmail.getText().toString().trim();
        String password = m_enterPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // Email empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            // Password empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        m_progressDialog.setMessage("Registering User...");
        m_progressDialog.show();

        // Register user using firebase
        Task<AuthResult> registerTask = m_firebaseAuth.createUserWithEmailAndPassword(email, password);
        registerTask.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                m_progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // User successfully registered and logged in
                    // Start profile activity here
                    finish();
                    Activities.startProfileActivity(getApplicationContext());
                } else {
                    Toast.makeText(MainActivity.this, "Registration failed, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
