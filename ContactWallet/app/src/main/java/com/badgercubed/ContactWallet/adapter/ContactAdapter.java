package com.badgercubed.ContactWallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.model.Following;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context m_context;
    private List<Following> m_dataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(Context context, List<Following> myDataset) {
        m_context = context;
        m_dataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_contact, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setPosition(position);

        holder.m_deleteBtn.setOnClickListener(view -> {
            OnCompleteListener<Void> deleteCompleteListener = deleteTask -> {
                if (deleteTask.isSuccessful()) {
                    m_dataset.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, m_dataset.size());
                }
            };

            FBManager.getInstance().deleteFBObject(m_context, m_dataset.get(position), deleteCompleteListener);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return m_dataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView m_textView;
        public Button m_deleteBtn;
        public ImageView m_imageIcon;

        public ViewHolder(View v) {
            super(v);
            m_textView = v.findViewById(R.id.my_text_view_id);
            m_deleteBtn = v.findViewById(R.id.btn_list_item_contact_delete);
            m_imageIcon = v.findViewById(R.id.image_icon);
        }

        public void setPosition(int position) {
            FBManager.getInstance().getCollection(User.m_collectionName)
                    .document(m_dataset.get(position).getFollowingUid())
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String name = task.getResult().get("name").toString();
                    m_textView.setText(name);
                    m_textView.setOnClickListener(v1 -> {
                        // Start contact detail activity
                        Context context = m_textView.getContext();
                        Following relationship = m_dataset.get(position);
                        Integer protectionLevel = relationship.getProtectionLevel();
                        Activities.startContactDetailsActivity(context,
                                relationship.getFollowingUid(), relationship.getProtectionLevel());
                    });

                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    int color = generator.getColor(name);
                    TextDrawable icon = TextDrawable.builder()
                            .buildRound(name.charAt(0) + "", color);
                    m_imageIcon.setImageDrawable(icon);
                }
            });
        }
    }
}