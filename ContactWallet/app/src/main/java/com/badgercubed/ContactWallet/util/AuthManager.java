package com.badgercubed.ContactWallet.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.badgercubed.ContactWallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class AuthManager {
    private static AuthManager instance = null;
    private final String TAG = "T-AuthManager";
    private FirebaseAuth m_firebaseAuth;

    private AuthManager() {
        m_firebaseAuth = FirebaseAuth.getInstance();
    }

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public FirebaseUser getAuthUser() {
        return m_firebaseAuth.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return m_firebaseAuth.getCurrentUser() != null;
    }


    // AUTHENTICATION METHODS

    public void registerUserWithEmailAndPassword(Context context, String email, String password,
                                                 OnCompleteListener<AuthResult> registerCompleteListener) {
        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        Task<AuthResult> registerTask = m_firebaseAuth.createUserWithEmailAndPassword(email, password);
        registerTask.addOnCompleteListener(task -> progressDialog.dismiss());
        registerTask.addOnCompleteListener(registerCompleteListener);
    }

    public void updateUserAfterFBLogin(Context context, LoginCallback callBack) {
        FirebaseUser user = getAuthUser();

        String uid = user.getUid();
        OnCompleteListener<DocumentSnapshot> onCompleteListener = task -> {
            if (task.isSuccessful()) {
                StoreManager.getInstance().setCurrentUser(task.getResult().toObject(User.class));
            }
            callBack.loginResult(task.isSuccessful());
        };
        StoreManager.getInstance().getFBObject(context, User.m_collectionName, uid, onCompleteListener);
    }

    public void login(Context context, String email, String password, LoginCallback loginCallBack) {
        OnCompleteListener<AuthResult> loginCompleteListener = loginTask -> {
            if (loginTask.isSuccessful()) {
                updateUserAfterFBLogin(context, loginCallBack);
            } else {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                loginCallBack.loginResult(false);
            }
        };
        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        Task<AuthResult> loginTask = m_firebaseAuth.signInWithEmailAndPassword(email, password);
        loginTask.addOnCompleteListener(task -> progressDialog.dismiss());
        loginTask.addOnCompleteListener(loginCompleteListener);

    }

    public void logout() {
        m_firebaseAuth.signOut();
        StoreManager.destroyInstance();
    }

    public void registerUser(Context context, String email, String password, RegisterCallback registerCallBack) {
        OnCompleteListener<AuthResult> registerCompleteListener = registerTask -> {
            if (!registerTask.isSuccessful()) {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Registration failed", registerTask.getException());
            }
            registerCallBack.registerResult(registerTask.isSuccessful());
        };
        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        Task<AuthResult> registerTask = m_firebaseAuth.createUserWithEmailAndPassword(email, password);
        registerTask.addOnCompleteListener(task -> progressDialog.dismiss());
        registerTask.addOnCompleteListener(registerCompleteListener);
    }

    public void saveUserAfterFBRegistration(Context context, User newUser, LoginCallback loginCallback) {
        OnCompleteListener<Void> saveCompleteListener = saveTask -> {
            if (saveTask.isSuccessful()) {
                StoreManager.getInstance().setCurrentUser(newUser);
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
            }
            loginCallback.loginResult(saveTask.isSuccessful());
        };

        StoreManager.getInstance().saveFBObject(context, newUser, saveCompleteListener);
    }
}
