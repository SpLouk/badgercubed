package com.badgercubed.ContactWallet.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.FBObject;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.model.Service;
import com.badgercubed.ContactWallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class StoreManager {
    private static StoreManager instance = null;
    private final String TAG = "T-StoreManager";
    private FirebaseFirestore m_db;
    private User m_currentUser = null;

    private StoreManager() {
        m_db = FirebaseFirestore.getInstance();
    }

    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    // GENERAL FIRESTORE METHODS


    public User getCurrentUser() {
        return m_currentUser;
    }

    public void setCurrentUser(User u) {
        m_currentUser = u;
    }

    public Task<Void> saveFBObject(Context context, FBObject fbObject) throws Exception {
        fbObject.validate();

        final String collName = fbObject.getCollectionName();
        final String docRef = fbObject.getDocReference();

        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        CollectionReference collection = m_db.collection(collName);
        Task<Void> saveTask = collection.document(docRef).set(fbObject);
        saveTask.addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Log.i(TAG, "Succesfully saved document " + docRef + " in collection " + collName);
            } else {
                String msg = "Failed to save document " + docRef + " in collection " + collName;
                Log.e(TAG, msg, task.getException());
            }
        });
        return saveTask;
    }

    public Task<Void> updateFBObject(Context context, FBObject fbObject, Map<String, Object> properties) {
        final String collName = fbObject.getCollectionName();
        final String docRef = fbObject.getDocReference();

        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        CollectionReference collection = m_db.collection(collName);
        Task<Void> updateTask = collection.document(docRef).update(properties);
        updateTask.addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Log.i(TAG, "Succesfully saved document " + docRef + " in collection " + collName);
            } else {
                String msg = "Failed to save document " + docRef + " in collection " + collName;
                Log.e(TAG, msg, task.getException());
            }
        });

        return updateTask;
    }

    public Task<Void> deleteFBObject(Context context, FBObject fbObject,
                                     OnCompleteListener<Void> deleteCompleteListener) {
        final String collName = fbObject.getCollectionName();
        final String docRef = fbObject.getDocReference();

        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        CollectionReference collectionReference = m_db.collection(collName);
        Task<Void> deleteTask = collectionReference.document(docRef).delete();
        deleteTask.addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Log.i(TAG, "Succesfully deleted document " + docRef + " in collection " + collName);
            } else {
                String msg = "Failed to delete document " + docRef + " in collection " + collName;
                Log.e(TAG, msg, task.getException());
            }
        });

        deleteTask.addOnCompleteListener(deleteCompleteListener);
        return deleteTask;
    }

    public CollectionReference getCollection(String colName) {
        return m_db.collection(colName);
    }

    public Task<DocumentSnapshot> getFBObject(Context context, final String collName, final String docRefId) {
        final ProgressDialog progressDialog = new ProgressDialog(context); // TODO: replace w progress bar
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        DocumentReference docRef = m_db.collection(collName).document(docRefId);
        Task<DocumentSnapshot> readTask = docRef.get();
        readTask.addOnCompleteListener(task -> {
            progressDialog.dismiss();
        });
        return readTask;
    }

    // SPECIFIC FIRESTORE QUERIES

    public Query getFollowingUsers() {
        // Need to get list of people that current user is following,
        // find following docs with followerid == current user

        return m_db.collection(Following.m_collectionName)
                .whereEqualTo("followerUid", StoreManager.getInstance().getCurrentUser().getUid());
    }

    public Task<QuerySnapshot> getConnectionByUserAndService(User user, Service service) {
        return m_db.collection(Connection.m_collectionName)
                .whereEqualTo("userId", user.getUid())
                .whereEqualTo("service", service)
                .get();
    }

    public Task<QuerySnapshot> getConnectionsForUser(User user) {
        return m_db.collection(Connection.m_collectionName).whereEqualTo("userId", user.getUid()).get();
    }

    public Task<QuerySnapshot> getUsersByEmail(String email) {
        return m_db.collection(User.m_collectionName).whereEqualTo("email", email).get();
    }

    public Task<QuerySnapshot> getFollowingProtectionLevel(String followerUid) {
        return m_db.collection(User.m_collectionName).whereEqualTo("followerUid", followerUid).get();
    }
}
