package com.badgercubed.ContactWallet.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.UserContactsActivity;
import com.badgercubed.ContactWallet.model.ContactItem;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FieldValue;

import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    private Context m_context;
    private String m_activityName;
    private List<ContactItem> m_contactItems;

    public ContactItemAdapter(Context context, String activityName, List<ContactItem> contactItems) {
        this.m_context = context;
        this.m_activityName = activityName;
        this.m_contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // TODO : Refactor?
        if (m_activityName.equals(UserContactsActivity.class.getSimpleName())) {
            viewHolder.m_deleteBtn.setVisibility(View.GONE);
        }

        ContactItem contactItem = m_contactItems.get(i);
        viewHolder.m_descTextView.setText(contactItem.getDescription());

        String url = contactItem.getLink();
        viewHolder.m_linkBtn.setOnClickListener((View view) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            m_context.startActivity(browserIntent);
        });

        viewHolder.m_deleteBtn.setOnClickListener((View view) -> {
            OnCompleteListener<Void> deleteCompleteListener = deleteTask -> {
                if (deleteTask.isSuccessful()) {
                    m_contactItems.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, m_contactItems.size());
                }
            };

            // Remove contactItem from current user
            FBManager.getInstance().getCollection(User.m_collectionName)
                    .document(FBManager.getInstance().getCurrentFBUser().getUid())
                    .update("contactItemIds", FieldValue.arrayRemove(contactItem.getUid()));
            FBManager.getInstance().deleteFBObject(m_context, contactItem, deleteCompleteListener);
        });
    }

    @Override
    public int getItemCount() {
        return m_contactItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_descTextView;
        public Button m_linkBtn;
        public Button m_deleteBtn;
        View m_view;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            m_descTextView = m_view.findViewById(R.id.list_item_textview);
            m_deleteBtn = m_view.findViewById(R.id.contact_item_list_item_delete_btn);
            m_linkBtn = m_view.findViewById(R.id.contact_item_list_item_link_btn);
        }
    }
}
