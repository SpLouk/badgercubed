package util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;

import java.util.List;

public class ContactItemAdapter extends BaseAdapter {

    private final Context context;
    private final List<model.ContactItem> contactItems;

    public ContactItemAdapter(Context context, List<model.ContactItem> contactItems) {
        this.context = context;
        this.contactItems = contactItems;
    }

    @Override
    public int getCount() {
        return contactItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return contactItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final model.ContactItem contactItem = contactItems.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.contact_items, null);
        }

       // final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView descTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
        final TextView protectionLevelTextView = (TextView)convertView.findViewById(R.id.textview_book_author);

        // Get Service Image Here
        // imageView.setImageResource(contactItem);
        descTextView.setText(contactItem.getDescription());
        protectionLevelTextView.setText(contactItem.getProtectionLevel().toString());

        return convertView;
    }
}
