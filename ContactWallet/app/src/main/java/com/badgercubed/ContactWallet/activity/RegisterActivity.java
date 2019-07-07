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
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginCallback;
import com.badgercubed.ContactWallet.util.LoginManager;
import com.badgercubed.ContactWallet.util.RegisterCallback;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements RegisterCallback, LoginCallback {
    private Button m_register;
    private EditText m_enterEmail;
    private EditText m_enterPassword;
    private EditText m_enterName;
    private EditText m_enterPhoneNum;
    private TextView m_login;

    @Override
    public void registerResult(boolean result) {
        if (!result) {
            return;
        }

        String email = m_enterEmail.getText().toString().trim();
        String name = m_enterName.getText().toString().trim();
        String phoneNum = m_enterPhoneNum.getText().toString().trim();

        String uid = FBManager.getInstance().getCurrentFBUser().getUid();

        User newUser = new User(uid, email, name, phoneNum, new ArrayList<String>(), new ArrayList<String>());
        LoginManager.getInstance().saveUserAfterFBRegistration(this, newUser, this);
    }

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
        setContentView(R.layout.activity_register);

        if (FBManager.getInstance().getCurrentFBUser() != null) {
            // User already logged in
            finish();
            Activities.startProfileActivity(this);
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

        LoginManager.getInstance().registerUser(this, email, password, this);
    }
}
