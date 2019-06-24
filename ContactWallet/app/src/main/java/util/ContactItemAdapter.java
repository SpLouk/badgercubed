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

import java.util.List;

import model.ContactItem;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    private Context context;
    private List<model.ContactItem> contactItems;

    public ContactItemAdapter(Context context, List<ContactItem> contactItems) {
        this.context = context;
        this.contactItems = contactItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        model.ContactItem contactItem = contactItems.get(i);
        viewHolder.descTextView.setText(contactItem.getDescription());

        String url = contactItem.getLink();
        viewHolder.linkBtn.setOnClickListener((View view) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
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
        public Button moreBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            m_view = itemView;

            descTextView = m_view.findViewById(R.id.list_item_textview);
            moreBtn = m_view.findViewById(R.id.contact_item_list_item_more_btn);
            linkBtn = m_view.findViewById(R.id.contact_item_list_item_link_btn);
        }
    }
}
