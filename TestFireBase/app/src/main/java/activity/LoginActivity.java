package activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button m_login;
    private EditText m_enterEmail;
    private EditText m_enterPassword;

    // TODO: replace with progress bar
    private ProgressDialog m_progressDialog;

    // Firebase authentication object
    private FirebaseAuth m_firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_firebaseAuth = FirebaseAuth.getInstance();
        if (m_firebaseAuth.getCurrentUser() != null) {
            // User already logged int
            finish();
            Activities.startProfileActivity(this);
        }

        m_progressDialog = new ProgressDialog(this);
        m_firebaseAuth = FirebaseAuth.getInstance();

        m_login = findViewById(R.id.login_login);
        m_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginUser();
            }
        });

        m_enterEmail = findViewById(R.id.login_enterEmail);
        m_enterPassword = findViewById(R.id.login_enterPassword);
    }

    private void loginUser() {
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
        m_progressDialog.setMessage("Logging In...");
        m_progressDialog.show();

        // Login user using firebase
        Task<AuthResult> loginTask = m_firebaseAuth.signInWithEmailAndPassword(email, password);
        loginTask.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                m_progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // User successfully registered and logged in
                    // Start profile activity here
                    finish();
                    Activities.startProfileActivity(getApplicationContext());
                } else {

                    Toast.makeText(LoginActivity.this, "Registration failed, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
