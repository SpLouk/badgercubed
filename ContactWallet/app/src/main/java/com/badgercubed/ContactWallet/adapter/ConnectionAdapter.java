package com.badgercubed.ContactWallet.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.ContactDetailsActivity;
import com.badgercubed.ContactWallet.model.Connection;
import com.badgercubed.ContactWallet.model.ProtectionLevel;
import com.badgercubed.ContactWallet.model.Service;
import com.badgercubed.ContactWallet.util.StoreManager;
import com.google.android.gms.tasks.OnCompleteListener;

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
        String description = TextUtils.isEmpty(connection.getDescription()) ?
                connection.getService().getName() :
                connection.getDescription();
        viewHolder.m_descTextView.setText(description);

        viewHolder.m_protectionLevelTextView.setText(
                connection.getProtectionLevel().getName()
        );

        if (!connection.getVerified()) {
            viewHolder.m_view.findViewById(R.id.listItemConnection_verified).setVisibility(View.GONE);
            viewHolder.m_view.findViewById(R.id.listItemConnection_verifiedText).setVisibility(View.GONE);
        }

        String link = connection.getLink();
        viewHolder.m_linkBtn.setOnClickListener((View view) -> connection.getService().openLink(link));

        viewHolder.m_deleteBtn.setOnClickListener((View view) -> {
            OnCompleteListener<Void> deleteCompleteListener = deleteTask -> {
                if (deleteTask.isSuccessful()) {
                    m_connections.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, m_connections.size());
                }
            };

            // Remove connection from current user
            StoreManager.getInstance().deleteFBObject(m_context, connection, deleteCompleteListener);
        });
    }

    @Override
    public int getItemCount() {
        return m_connections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView m_descTextView;
        public TextView m_protectionLevelTextView;
        public Button m_linkBtn;
        public ImageButton m_deleteBtn;
        View m_view;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            m_descTextView = m_view.findViewById(R.id.listItemConnection_textView);
            m_protectionLevelTextView = m_view.findViewById(R.id.listItemConnection_protectionLevel);
            m_deleteBtn = m_view.findViewById(R.id.listItemConnection_deleteBtn);
            m_linkBtn = m_view.findViewById(R.id.listItemConnection_linkBtn);
        }
    }
}
