package util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;

import java.util.List;

public class ContactItemAdapter2 extends RecyclerView.Adapter<ContactItemAdapter2.ViewHolder> {

    public List<model.ContactItem> contactItems;

    public ContactItemAdapter2(List<model.ContactItem> contactItems) {
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(contactItems.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View m_view;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            textView = m_view.findViewById(R.id.list_item_textview);
        }
    }
}
