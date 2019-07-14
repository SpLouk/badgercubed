package com.badgercubed.ContactWallet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginManager;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.widget.PrefixEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AddContactDialog extends DialogFragment {
    private static final String TAG = "T-AddContactDialog";

    private EditText m_enterEmail;
    private PrefixEditText m_enterHandle;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add A New Contact");

        View dialogView = createDialogView();
        builder.setView(dialogView);

        builder.setPositiveButton("Add", null); //OnclickListener added in 'onStart'
        builder.setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }

    public View createDialogView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_contact, null);

        m_enterEmail = view.findViewById(R.id.addContact_email);
        m_enterHandle = view.findViewById(R.id.addContact_handle);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Set here so dialog doesn't terminate automatically when button is clicked
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> createFollowingRelationship());
    }

    private void createFollowingRelationship() {
        // TODO : Check for repeats when adding, currently can add multiple entries in following table
        //  with same relationship

        String email = m_enterEmail.getText().toString();
        String handle = m_enterHandle.getFullString();

        if (TextUtils.isEmpty(email)) {
            String errMsg = "Email can't be empty";
            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.equals(LoginManager.getInstance().getCurrentUser())) {
            String errMsg = "Can't follow yourself!";
            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(handle)) {
            String errMsg = "Handle can't be empty";
            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        Task<QuerySnapshot> usersByEmail = FBManager.getInstance().getUsersByEmail(email);
        usersByEmail.addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                String errMsg = "Error Querying DB: " + task.getException().getMessage();
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error Querying DB: ",  task.getException());
                return;
            }

            List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
            if (documentSnapshots.isEmpty()) {
                String errMsg = "Contact with email: " + email + " doesn't exist";
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            User queriedContact = documentSnapshots.get(0).toObject(User.class);
            if (queriedContact == null) {
                String errMsg = "Error creating contact";
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, errMsg);
                return;
            }

            ProtectionLevel protLevel = getProtectionLevelFromHandle(queriedContact, handle);
            if (protLevel == null) {
                String errMsg = "Handle doesn't match";
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
                return;
            }
            saveFollowingRelationship(queriedContact, protLevel);
        });
    }

    private void saveFollowingRelationship(User following, ProtectionLevel protLevel) {
        String followerId = LoginManager.getInstance().getCurrentUser().getUid();
        String followingId = following.getUid();
        Following followingRelationship = new Following(followerId, followingId, protLevel.getInt());

        OnCompleteListener<Void> saveCompleteListener = task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Succesful", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                String errMsg = "Error saving relationship: " + task.getException().getMessage();
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error saving relationship: ", task.getException());
                return;
            }
        };
        FBManager.getInstance().saveFBObject(getActivity(), followingRelationship, saveCompleteListener);
    }

    private ProtectionLevel getProtectionLevelFromHandle(User checkUser, String handle) {
        if (checkUser.getPublicHandle().equals(handle)) {
            return  ProtectionLevel.PUBLIC;
        }
        if (checkUser.getProtectedHandle().equals(handle)) {
            return  ProtectionLevel.PROTECTED;
        }
        if (checkUser.getPrivateHandle().equals(handle)) {
            return  ProtectionLevel.PRIVATE;
        }
        return null;
    }
}
