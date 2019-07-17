package com.badgercubed.ContactWallet.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.badgercubed.ContactWallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public Task<DocumentSnapshot> updateUserAfterFBLogin(Context context) {
        FirebaseUser user = getAuthUser();

        String uid = user.getUid();
        Task<DocumentSnapshot> task = StoreManager
                .getInstance()
                .getFBObject(context, User.m_collectionName, uid);
        task.addOnSuccessListener(documentSnapshot -> {
            StoreManager.getInstance().setCurrentUser(documentSnapshot.toObject(User.class));
        });
        return task;
    }

    public Task<AuthResult> login(String email, String password) {
        Task<AuthResult> loginTask = m_firebaseAuth.signInWithEmailAndPassword(email, password);
        return loginTask;
    }

    public void logout() {
        m_firebaseAuth.signOut();
        StoreManager.destroyInstance();
    }

    public void registerUser(
            Context context,
            String email, String password,
            OnSuccessListener<AuthResult> onSuccessListener) {

        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        OnCompleteListener<AuthResult> onCompleteListener = task -> {
            progressDialog.dismiss();
            if (!task.isSuccessful()) {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Registration failed", task.getException());
            }
        };

        Task<AuthResult> registerTask = m_firebaseAuth.createUserWithEmailAndPassword(email, password);
        registerTask.addOnCompleteListener(onCompleteListener);
        registerTask.addOnSuccessListener(onSuccessListener);
    }

    public Task<Void> saveUserAfterFBRegistration(Context context, User newUser) {
        OnCompleteListener<Void> saveCompleteListener = saveTask -> {
            if (saveTask.isSuccessful()) {
                StoreManager.getInstance().setCurrentUser(newUser);
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        };

        Task<Void> task = StoreManager.getInstance().saveFBObject(context, newUser);
        task.addOnCompleteListener(saveCompleteListener);
        return task;
    }
}
