package com.badgercubed.ContactWallet.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.StoreManager;
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

        /*
                Handles
         */

        User user = StoreManager.getInstance().getCurrentUser();

        TextView publicHandle = view.findViewById(R.id.fragment_contacts_public_handle);
        publicHandle.setText("Public: " + user.getPublicHandle());
        ImageButton publicButton = view.findViewById(R.id.fragment_contacts_copy_public_handle);
        publicButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("Public access handle", user.getPublicHandle());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        TextView protectedHandle = view.findViewById(R.id.fragment_contacts_protected_handle);
        protectedHandle.setText("Protected: " + user.getProtectedHandle());
        ImageButton protectedButton = view.findViewById(R.id.fragment_contacts_copy_protected_handle);
        protectedButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("Protected access handle", user.getProtectedHandle());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        TextView privateHandle = view.findViewById(R.id.fragment_contacts_private_handle);
        privateHandle.setText("Private: " + user.getPrivateHandle());
        ImageButton privateButton = view.findViewById(R.id.fragment_contacts_copy_private_handle);
        privateButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)
                    getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("Private access handle", user.getPrivateHandle());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        /*
           Handles
         */

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

        String email = m_enterEmail.getText().toString().trim();
        String handle = m_enterHandle.getFullString().trim();

        if (TextUtils.isEmpty(email)) {
            String errMsg = "Email can't be empty";
            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.equals(StoreManager.getInstance().getCurrentUser())) {
            String errMsg = "Can't follow yourself";
            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(handle)) {
            String errMsg = "Handle can't be empty";
            Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        Task<QuerySnapshot> usersByEmail = StoreManager.getInstance().getUsersByEmail(email);
        usersByEmail.addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                String errMsg = "Error Querying DB: " + task.getException().getMessage();
                Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error Querying DB: ", task.getException());
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
            if (!saveFollowingRelationship(queriedContact, protLevel)) {
                Toast.makeText(getActivity(), "Could not save. Ensure all fields are valid.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean saveFollowingRelationship(User following, ProtectionLevel protLevel) {
        String followerId = StoreManager.getInstance().getCurrentUser().getUid();
        String followingId = following.getUid();
        Following followingRelationship = new Following(followerId, followingId, protLevel);

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
        try {
            followingRelationship.validate();
            StoreManager.getInstance().saveFBObject(getActivity(), followingRelationship).addOnCompleteListener(saveCompleteListener);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private ProtectionLevel getProtectionLevelFromHandle(User checkUser, String handle) {
        if (checkUser.getPublicHandle().equals(handle)) {
            return ProtectionLevel.PUBLIC;
        }
        if (checkUser.getProtectedHandle().equals(handle)) {
            return ProtectionLevel.PROTECTED;
        }
        if (checkUser.getPrivateHandle().equals(handle)) {
            return ProtectionLevel.PRIVATE;
        }
        return null;
    }
}
