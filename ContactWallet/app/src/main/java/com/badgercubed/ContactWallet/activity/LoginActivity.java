package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.util.StoreManager;
import com.badgercubed.ContactWallet.util.LoginCallback;
import com.badgercubed.ContactWallet.util.AuthManager;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements LoginCallback {
    private Button m_login;
    private TextView m_register;
    private EditText m_enterEmail;
    private EditText m_enterPassword;

    @Override
    public void loginResult(boolean result) {
        if (result) {
            // User logged in
            finish();

            Activities.startNavActivity(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Check if user is logged in
        if (AuthManager.getInstance().isLoggedIn()) {
            AuthManager.getInstance().updateUserAfterFBLogin(this, this);
        }

        m_register = findViewById(R.id.welcome_register);
        m_register.setOnClickListener(l -> Activities.startRegisterActivity(LoginActivity.this));

        m_login = findViewById(R.id.welcome_login);
        m_login.setOnClickListener(l -> loginUser());

        m_enterEmail = findViewById(R.id.login_username);
        m_enterPassword = findViewById(R.id.login_password);
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

        AuthManager.getInstance().login(this, email, password, this);
    }
}
