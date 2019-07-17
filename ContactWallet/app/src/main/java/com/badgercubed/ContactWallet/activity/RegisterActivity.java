package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.AuthManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public class RegisterActivity extends AppCompatActivity {
    private Button m_register;
    private EditText m_enterEmail;
    private EditText m_enterPassword;
    private EditText m_enterName;
    private EditText m_enterPhoneNum;
    private TextView m_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (AuthManager.getInstance().isLoggedIn()) {
            // User already logged in
            finish();
            // TODO : go to profile fragment? necessary
            Activities.startNavActivity(this);
        }

        m_enterEmail = findViewById(R.id.register_enterEmail);
        m_enterPassword = findViewById(R.id.register_enterPassword);
        m_enterName = findViewById(R.id.register_enterName);
        m_enterPhoneNum = findViewById(R.id.register_enterPhoneNum);


        m_register = findViewById(R.id.register_register);
        m_register.setOnClickListener(l -> registerUser());

        m_login = findViewById(R.id.register_login);
        m_login.setOnClickListener(l -> Activities.startWelcomeActivity(RegisterActivity.this));
    }

    private void registerUser() {
        String email = m_enterEmail.getText().toString().trim();
        String password = m_enterPassword.getText().toString().trim();
        String name = m_enterName.getText().toString().trim();
        String phoneNum = m_enterPhoneNum.getText().toString().trim();

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
        if (TextUtils.isEmpty(name)) {
            // Password empty
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            // Password empty
            Toast.makeText(this, "Please enter phone #", Toast.LENGTH_SHORT).show();
            return;
        }

        OnSuccessListener<AuthResult> onSuccessListener = task -> {
            String uid = AuthManager.getInstance().getAuthUser().getUid();

            User newUser = new User(uid, email, name, phoneNum);

            AuthManager.getInstance().saveUserAfterFBRegistration(this, newUser).addOnSuccessListener(saveTask -> {
                // User logged in
                finish();
                Activities.startNavActivity(this);
            });
        };

        AuthManager.getInstance().registerUser(this, email, password, onSuccessListener);
    }
}
