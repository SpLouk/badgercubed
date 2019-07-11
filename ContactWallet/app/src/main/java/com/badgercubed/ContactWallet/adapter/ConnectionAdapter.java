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
import com.badgercubed.ContactWallet.activity.ContactDetailsActivity;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FieldValue;

import java.util.List;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ViewHolder> {

    private Context m_context;
    private String m_activityName;
    private List<Connection> m_connections;

    public ConnectionAdapter(Context context, String activityName, List<Connection> connections) {
        this.m_context = context;
        this.m_activityName = activityName;
        this.m_connections = connections;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_connection, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // TODO : Refactor?
        if (m_activityName.equals(ContactDetailsActivity.class.getSimpleName())) {
            viewHolder.m_deleteBtn.setVisibility(View.GONE);
        }

        Connection connection = m_connections.get(i);
        viewHolder.m_descTextView.setText(connection.getDescription());

        String url = connection.getLink();
        viewHolder.m_linkBtn.setOnClickListener((View view) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            m_context.startActivity(browserIntent);
        });

        viewHolder.m_deleteBtn.setOnClickListener((View view) -> {
            OnCompleteListener<Void> deleteCompleteListener = deleteTask -> {
                if (deleteTask.isSuccessful()) {
                    m_connections.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, m_connections.size());
                }
            };

            // Remove connection from current user
            FBManager.getInstance().getCollection(User.m_collectionName)
                    .document(FBManager.getInstance().getCurrentFBUser().getUid())
                    .update("connectionIds", FieldValue.arrayRemove(connection.getUid()));
            FBManager.getInstance().deleteFBObject(m_context, connection, deleteCompleteListener);
        });
    }

    @Override
    public int getItemCount() {
        return m_connections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_descTextView;
        public Button m_linkBtn;
        public Button m_deleteBtn;
        View m_view;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            m_descTextView = m_view.findViewById(R.id.listItemConnection_textView);
            m_deleteBtn = m_view.findViewById(R.id.listItemConnection_deleteBtn);
            m_linkBtn = m_view.findViewById(R.id.listItemConnection_linkBtn);
        }
    }
}