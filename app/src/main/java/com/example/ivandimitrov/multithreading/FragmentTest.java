package com.example.ivandimitrov.multithreading;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ivan Dimitrov on 12/6/2016.
 */

public class FragmentTest extends Fragment implements DownloadFilesTask.CustomListener {
    private ListView listView;
    private ArrayList<SiteNode> siteNodes = new ArrayList<SiteNode>();
    public final static String MY_URL = "https://developer.android.com/reference/java/net/HttpURLConnection.html";
    private URL url;
    CustomListAdapter adapter;
    AdapterView.OnItemClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        try {
            url = new URL(MY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new DownloadFilesTask(getActivity(), this).execute(url);

        listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                SiteNode node = (SiteNode) listView.getItemAtPosition(i);
                intent.putExtra("Object", node.getUrl().toString());
                startActivity(intent);
            }
        };
        adapter = new CustomListAdapter(getActivity(), siteNodes);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putParcelableArrayList("Users", members);
//        super.onSaveInstanceState(savedInstanceState);
    }


    public void onDataReceived(ArrayList<SiteNode> siteNodes) {
        adapter.clear();
        adapter.addAll(siteNodes);
        adapter.notifyDataSetChanged();
    }

    public void onElementReceived(SiteNode siteNode) {
        adapter.add(siteNode);
        adapter.notifyDataSetChanged();
    }
}
