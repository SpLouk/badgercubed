package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.util.AuthManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    private Button m_login;
    private TextView m_register;
    private EditText m_enterEmail;
    private EditText m_enterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is logged in
        if (AuthManager.getInstance().isLoggedIn()) {
            AuthManager.getInstance().updateUserAfterFBLogin(this).addOnSuccessListener(task -> {
                // User logged in
                finish();
                Activities.startNavActivity(this);
            });
        }

        m_register = findViewById(R.id.welcome_register);
        m_register.setOnClickListener(l -> Activities.startRegisterActivity(LoginActivity.this));

        m_login = findViewById(R.id.welcome_login);
        m_login.setOnClickListener(l -> loginUser());

        m_enterEmail = findViewById(R.id.login_username);

        m_enterPassword = findViewById(R.id.login_password);
        m_enterPassword.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Login on enter through password field
                m_login.performClick();
                return true;
            }
            return false;
        });
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

        final ProgressBar progress = new ProgressBar(this);
        progress.setVisibility(View.VISIBLE);

        OnCompleteListener<AuthResult> loginCompleteListener = loginTask -> {
            progress.setVisibility(View.GONE);
            if (loginTask.isSuccessful()) {
                AuthManager.getInstance().updateUserAfterFBLogin(this).addOnSuccessListener(documentSnapshot -> {
                    // User logged in
                    finish();
                    Activities.startNavActivity(this);
                });
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        };
        AuthManager.getInstance().login(email, password).addOnCompleteListener(loginCompleteListener);
    }
}
