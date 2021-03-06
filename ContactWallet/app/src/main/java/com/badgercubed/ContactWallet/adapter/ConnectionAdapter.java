package com.badgercubed.ContactWallet.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.ContactDetailsActivity;
import com.badgercubed.ContactWallet.dialog.AddConnectionDialog;
import com.badgercubed.ContactWallet.model.Connection;
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
        Connection connection = m_connections.get(i);

        if (m_activityName.equals(ContactDetailsActivity.class.getSimpleName())) {
            viewHolder.m_editBtn.setVisibility(View.GONE);
            viewHolder.m_deleteBtn.setVisibility(View.GONE);
            viewHolder.m_protectionLevelTextView.setVisibility(View.GONE);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.m_descTextView.getLayoutParams();
            if (connection.getVerified()) {
                params.addRule(RelativeLayout.LEFT_OF, R.id.listItemConnection_verified);
            } else {
                params.addRule(RelativeLayout.LEFT_OF, R.id.listItemConnection_protectionLevel);
            }
            viewHolder.m_descTextView.setLayoutParams(params);
        }

        int logo = connection.getService().getLogo();
        viewHolder.m_logo.setImageResource(logo);

        String description = TextUtils.isEmpty(connection.getDescription()) ?
                connection.getService().getName() :
                connection.getDescription();
        viewHolder.m_descTextView.setText(description);

        viewHolder.m_protectionLevelTextView.setText(
                connection.getProtectionLevel().getName()
        );

        if (!connection.getVerified()) {
            viewHolder.m_view.findViewById(R.id.listItemConnection_verified).setVisibility(View.GONE);
        }

        viewHolder.m_editBtn.setOnClickListener(l -> {
            AddConnectionDialog dialog = AddConnectionDialog.newInstance(m_connections.get(i));
            dialog.show(((AppCompatActivity) m_context).getFragmentManager(), "Edit Contact Information");
        });

        String link = connection.getLink();
        viewHolder.m_linkBtn.setOnClickListener((View view) -> connection.getService().openLink(m_context, link));

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
        return m_connections == null ? 0 : m_connections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView m_logo;
        public TextView m_descTextView;
        public TextView m_protectionLevelTextView;
        public ImageButton m_editBtn;
        public ImageButton m_deleteBtn;
        public ImageButton m_linkBtn;

        View m_view;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            m_logo = m_view.findViewById(R.id.list_item_connection_logo);
            m_descTextView = m_view.findViewById(R.id.listItemConnection_textView);
            m_protectionLevelTextView = m_view.findViewById(R.id.listItemConnection_protectionLevel);
            m_editBtn = m_view.findViewById(R.id.list_item_connection_edit);
            m_deleteBtn = m_view.findViewById(R.id.listItemConnection_deleteBtn);
            m_linkBtn = m_view.findViewById(R.id.listItemConnection_linkBtn);
        }
    }
}
