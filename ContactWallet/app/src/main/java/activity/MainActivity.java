package activity;

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
import com.google.firebase.auth.AuthResult;

import util.FBManager;

public class MainActivity extends AppCompatActivity {
    private Button m_register;
    private EditText m_enterEmail;
    private EditText m_enterPassword;
    private TextView m_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FBManager.getInstance().getCurrentUser() != null) {
            // User already logged in
            finish();
            Activities.startProfileActivity(this);
        }

        m_register = findViewById(R.id.register_register);
        m_register.setOnClickListener(l -> registerUser());

        m_enterEmail = findViewById(R.id.register_enterEmail);
        m_enterPassword = findViewById(R.id.register_enterPassword);

        m_login = findViewById(R.id.register_login);
        m_login.setOnClickListener(l -> Activities.startLoginActivity(MainActivity.this));
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

        // Register
        OnCompleteListener<AuthResult> registerCompleteListener = task -> {
            if (task.isSuccessful()) {
                // User successfully registered and logged in, start profile activity here
                finish();
                Activities.startProfileActivity(getApplicationContext());
            } else {
                Toast.makeText(MainActivity.this, "Registration failed, please try again.", Toast.LENGTH_SHORT).show();
            }
        };
        FBManager.getInstance().registerUserWithEmailAndPassword(this, email, password, registerCompleteListener);
    }
}
