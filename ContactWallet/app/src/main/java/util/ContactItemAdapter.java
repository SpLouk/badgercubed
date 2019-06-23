package util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;

import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    public List<model.ContactItem> contactItems;

    public ContactItemAdapter(List<model.ContactItem> contactItems) {
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
       viewHolder.descTextView.setText(contactItems.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View m_view;
        public TextView descTextView;
        public Button moreBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            descTextView = m_view.findViewById(R.id.list_item_textview);
            moreBtn = m_view.findViewById(R.id.list_item_btn);
        }
    }
}
