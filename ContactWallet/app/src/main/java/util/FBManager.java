package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import model.FBObject;

public class FBManager {
    private final String TAG = "T-FBManager";
    private static FBManager instance = null;

    private FirebaseAuth m_firebaseAuth;
    private FirebaseFirestore m_db;

    private FBManager () {
        m_firebaseAuth = FirebaseAuth.getInstance();
        m_db = FirebaseFirestore.getInstance();
    }

    public static FBManager getInstance() {
        if (instance == null) {
            instance = new FBManager();
        }
        return instance;
    }

    // AUTHENTICATION METHODS

    public FirebaseUser getCurrentUser() {
        return m_firebaseAuth.getCurrentUser();
    }

    public void registerUserWithEmailAndPassword(Context context, String email, String password,
                                          OnCompleteListener<AuthResult> registerCompleteListener) {
        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        Task<AuthResult> loginTask = m_firebaseAuth.createUserWithEmailAndPassword(email, password);
        loginTask.addOnCompleteListener(task -> progressDialog.dismiss());
        loginTask.addOnCompleteListener(registerCompleteListener);
    }

    public void loginWithEmailAndPassword(Context context, String email, String password,
                                          OnCompleteListener<AuthResult> loginCompleteListener) {
        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        Task<AuthResult> loginTask = m_firebaseAuth.signInWithEmailAndPassword(email, password);
        loginTask.addOnCompleteListener(task -> progressDialog.dismiss());
        loginTask.addOnCompleteListener(loginCompleteListener);
    }

    public void logout() {
        m_firebaseAuth.signOut();
    }

    // FIRESTORE METHODS

    public void saveFBObject(Context context, FBObject fbObject,
                             OnCompleteListener<Void> saveCompleteListener) {
        try {
            fbObject.validate();
        } catch (Exception e) {
            Log.e(TAG, "Failed to validate Firebase object", e);
        }

        final String collName = fbObject.getCollectionName();
        final String docRef = fbObject.getDocReference();

        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        CollectionReference usersCollection = m_db.collection(collName);
        Task<Void> saveTask = usersCollection.document(docRef).set(fbObject);
        saveTask.addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Log.i(TAG, "Succesfully saved doc " + docRef + " in collection " + collName);
            } else {
                String msg = "Succesfully saved doc " + docRef + " in collection " + collName;
                Log.e(TAG, msg,  task.getException());
            }
        });
        saveTask.addOnCompleteListener(saveCompleteListener);
    }
}