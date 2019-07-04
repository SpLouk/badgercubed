package util;

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
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.List;

import model.ContactItem;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    private Context context;
    private String activityName;
    private List<model.ContactItem> contactItems;

    public ContactItemAdapter(Context context, String activityName, List<ContactItem> contactItems) {
        this.context = context;
        this.activityName = activityName;
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        // TODO : Refactor?
        if (activityName.equals(activity.UserContactsActivity.class.getSimpleName())) {
            viewHolder.deleteBtn.setVisibility(View.GONE);
        }

        model.ContactItem contactItem = contactItems.get(i);
        viewHolder.descTextView.setText(contactItem.getDescription());

        String url = contactItem.getLink();
        viewHolder.linkBtn.setOnClickListener((View view) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        });

        viewHolder.deleteBtn.setOnClickListener((View view) -> {

            OnCompleteListener<Void> deleteCompleteListener = deleteTask -> {
                if (deleteTask.isSuccessful()) {
                    contactItems.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, contactItems.size());
                }
            };

            FBManager.getInstance().deleteFBObject(context, contactItem, deleteCompleteListener);
        });
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View m_view;
        public TextView descTextView;
        public Button linkBtn;
        public Button deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            descTextView = m_view.findViewById(R.id.list_item_textview);
            deleteBtn = m_view.findViewById(R.id.contact_item_list_item_delete_btn);
            linkBtn = m_view.findViewById(R.id.contact_item_list_item_link_btn);
        }
    }
}
