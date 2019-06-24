package util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import model.User;

public class LoginManager {
    private final String TAG = "T-LoginManager";
    private static LoginManager instance = null;
    private User m_currentUser;

    private LoginManager () {
        m_currentUser = null;
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    public User getCurrentUser() {
        return m_currentUser;
    }

    public boolean isLoggedIn() {
        FirebaseUser fbUser = FBManager.getInstance().getCurrentFBUser();
        if (fbUser != null && m_currentUser != null && m_currentUser.getUid() == fbUser.getUid()) {
            return true;
        }
        return false;
    }

    public void updateCurrentUser(Context context, OnCompleteListener<DocumentSnapshot> readCompleteListener) {
        FirebaseUser fbUser = FBManager.getInstance().getCurrentFBUser();
        if (fbUser == null) {
            m_currentUser = null;
            return;
        }

        String  uid = fbUser.getUid();
        OnCompleteListener<DocumentSnapshot> getUserCompleteListener = task -> {
            if (task.isSuccessful()) {
                m_currentUser = task.getResult().toObject(User.class);
            }
        };
        FBManager.getInstance().getFBObject(context, User.m_collectionName, uid, getUserCompleteListener, readCompleteListener);
    }

    public void updateUserAfterFBLogin(Context context, LoginCallback callBack) {
        OnCompleteListener<DocumentSnapshot> updateUserCompletion = updateUser -> {
            if (updateUser.isSuccessful()) {
                m_currentUser = updateUser.getResult().toObject(User.class);
            }
            callBack.loginResult(updateUser.isSuccessful());
        };
        updateCurrentUser(context, updateUserCompletion);
    }

    public void login(Context context, String email, String password, LoginCallback loginCallBack) {
        OnCompleteListener<AuthResult> loginCompleteListener = loginTask -> {
            if (loginTask.isSuccessful()) {
                updateUserAfterFBLogin(context, loginCallBack);
            } else {
                m_currentUser = null;
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                loginCallBack.loginResult(false);
            }
        };

        FBManager.getInstance().loginWithEmailAndPassword(context, email, password, loginCompleteListener);
    }

    public void logout() {
        FBManager.getInstance().logout();
        m_currentUser = null;
    }

    public void registerUser(Context context, String email, String password, RegisterCallback registerCallBack) {
        OnCompleteListener<AuthResult> registerCompleteListener = registerTask -> {
            if (!registerTask.isSuccessful()) {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Registration failed", registerTask.getException());
            }
            registerCallBack.registerResult(registerTask.isSuccessful());
        };
        FBManager.getInstance().registerUserWithEmailAndPassword(context, email, password, registerCompleteListener);
    }

    public void saveUserAfterFBRegistration(Context context, User newUser, LoginCallback loginCallback) {
        OnCompleteListener<Void> saveCompleteListener = saveTask -> {
            if (saveTask.isSuccessful()) {
                m_currentUser = newUser;
            } else {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
            }
            loginCallback.loginResult(saveTask.isSuccessful());
        };

        FBManager.getInstance().saveFBObject(context, newUser, saveCompleteListener);
    }
}
