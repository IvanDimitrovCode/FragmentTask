package com.example.ivandimitrov.multithreading;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<SiteNode> {

    private final Activity context;
    private ArrayList<SiteNode> siteNodeList = new ArrayList<SiteNode>();

    public CustomListAdapter(Activity context, ArrayList<SiteNode> itemname) {
        super(context, R.layout.activity_listview, itemname);
        this.context = context;
        this.siteNodeList = itemname;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_listview, null, true);
            viewHolder.txtTitle = (TextView) view.findViewById(R.id.label);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtTitle.setText(siteNodeList.get(position).getNodeName());
        return view;
    }

    static class ViewHolder {
        TextView txtTitle;
    }
}
