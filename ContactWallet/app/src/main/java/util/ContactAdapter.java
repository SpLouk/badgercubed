package util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;

import java.util.List;

import activity.Activities;
import model.User;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<User> m_dataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView m_textView;

        public ViewHolder(View v) {
            super(v);
            m_textView = v.findViewById(R.id.my_text_view_id);
        }

        public void setPosition(int position) {
            m_textView.setText(m_dataset.get(position).getName());
            m_textView.setOnClickListener(v1 -> {
                // Start contact detail activity
                Context context = m_textView.getContext();
                User selectedUser = m_dataset.get(position);
                Activities.startUserContactsActivity(context, selectedUser.getUid());
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(List<User> myDataset) {
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
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return m_dataset.size();
    }
}