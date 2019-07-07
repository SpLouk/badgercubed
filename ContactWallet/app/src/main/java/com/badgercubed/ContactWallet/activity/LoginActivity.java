package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginCallback;
import com.badgercubed.ContactWallet.util.LoginManager;

public class LoginActivity extends AppCompatActivity implements LoginCallback {
    private Button m_login;
    private EditText m_enterEmail;
    private EditText m_enterPassword;

    @Override
    public void loginResult(boolean result) {
        if (result) {
            // User logged in
            finish();
            Activities.startListContactsActivity(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (FBManager.getInstance().getCurrentFBUser() != null) {
            // User already logged int
            finish();
            Activities.startProfileActivity(this);
        }

        m_login = findViewById(R.id.login_login);
        m_login.setOnClickListener(l -> loginUser());

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

        LoginManager.getInstance().login(this, email, password, this);
    }
}