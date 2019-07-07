package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginCallback;
import com.badgercubed.ContactWallet.util.LoginManager;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginCallback {
    private Button m_login;
    private Button m_register;

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
        setContentView(R.layout.activity_welcome);

        // Check if user is logged in
        FirebaseUser fbUser = FBManager.getInstance().getCurrentFBUser();
        if (fbUser != null) {
            LoginManager.getInstance().updateUserAfterFBLogin(this, this);
        }

        m_register = findViewById(R.id.welcome_register);
        m_register.setOnClickListener(l -> Activities.startRegisterActivity(MainActivity.this));

        m_login = findViewById(R.id.welcome_login);
        m_login.setOnClickListener(l -> Activities.startLoginActivity(MainActivity.this));
    }
}
