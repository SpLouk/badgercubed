package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import util.FBManager;

public class LoginActivity extends AppCompatActivity {
    private Button m_login;
    private EditText m_enterEmail;
    private EditText m_enterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (FBManager.getInstance().getCurrentUser() != null) {
            // User already logged int
            finish();
            Activities.startProfileActivity(this);
        }

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

        // Login
        OnCompleteListener<AuthResult> loginCompleteListener = task -> {
            if (task.isSuccessful()) {
                // User successfully logged in, start profile activity here
                finish();
                Activities.startProfileActivity(getApplicationContext());
            } else {
                Toast.makeText(LoginActivity.this, "Registration failed, please try again.", Toast.LENGTH_SHORT).show();
            }
        };
        FBManager.getInstance().loginWithEmailAndPassword(this, email, password, loginCompleteListener);
    }
}