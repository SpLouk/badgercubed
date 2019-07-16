package com.badgercubed.ContactWallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.model.Service;

import java.util.List;

public class ServiceAdapter extends ArrayAdapter<Service> {
    List<Service> m_items;

    public ServiceAdapter(Context context, List<Service> items) {
        super(context, R.layout.list_item_service, items);
        m_items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_service, parent, false);
        }

        TextView textView = rowView.findViewById(R.id.listItemService_name);

        if (position == 0) {
            textView.setText("Select Service");
            textView.setVisibility(View.GONE);
        } else {
            position = position - 1; // Adjust for initial selection item
            Service service = getItem(position);
            textView.setText(service.getName());
            textView.setVisibility(View.VISIBLE);
        }
        return rowView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);

        if (position == 0) {
            textView.setText("Select Service");
        } else {
            position = position - 1; // Adjust for initial selection item
            Service service = getItem(position);
            textView.setText(service.getName());
        }
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }
}
