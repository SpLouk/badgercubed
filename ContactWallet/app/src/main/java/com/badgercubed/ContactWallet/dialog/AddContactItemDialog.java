package com.badgercubed.ContactWallet.dialog;

import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.model.ContactItem;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.google.firebase.firestore.FieldValue;

import java.util.UUID;

public class AddContactItemDialog extends DialogFragment {
    private static final String TAG = "T-AddContactItemDialog";

    //widgets
    private EditText m_description;
    private EditText m_link;
    private EditText m_protectionLevel;

    private Button m_confirm;
    private Button m_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_contact, container, false);

        m_link = view.findViewById(R.id.addContact_link);
        m_description = view.findViewById(R.id.addContact_description);
        m_protectionLevel = view.findViewById(R.id.addContact_protectionLevel);

        m_cancel = view.findViewById(R.id.addContact_cancel);
        m_cancel.setOnClickListener(l -> dismiss());

        m_confirm = view.findViewById(R.id.addContact_confirm);
        m_confirm.setOnClickListener(l -> {
            // TODO: validate link, description, and protection level
            createAndSaveContactItem();
            dismiss();
        });

        return view;
    }

    private void valiateLink() {

    }

    private void createAndSaveContactItem() {
        UUID contactItemUid = UUID.randomUUID();
        String currentUserUid = FBManager.getInstance().getCurrentFBUser().getUid();
        String link = m_link.getText().toString();
        String description = m_description.getText().toString();
        Integer protectionLevel = Integer.parseInt(m_protectionLevel.getText().toString());

        // TODO : Add contact item, still need to add to current users list of contact itemId
        ContactItem contactItem = new ContactItem(contactItemUid.toString(), currentUserUid, "",
                link, description, protectionLevel);
        FBManager.getInstance().saveFBObject(getActivity(), contactItem, null);

        // Add new contactItem Id to current user
        FBManager.getInstance().getCollection(User.m_collectionName)
                .document(currentUserUid)
                .update("contactItemIds", FieldValue.arrayUnion(contactItem.getUid()));
    }
}
