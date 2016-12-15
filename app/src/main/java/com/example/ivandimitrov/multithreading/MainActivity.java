package com.example.ivandimitrov.multithreading;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ivandimitrov.multithreading.Adapters.CustomListAdapter;
import com.example.ivandimitrov.multithreading.NodeTypes.CitiesWeather;

import java.util.ArrayList;


public class MainActivity extends Activity implements CallAPI.CustomListener {
    private AdapterView.OnItemClickListener listener;
    private CustomListAdapter adapter;
    private ArrayList<CitiesWeather> cityList = new ArrayList<CitiesWeather>();
    private ListView listView;
    private CallAPI.CustomListener customListener;
    private CallAPI appi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        appi = new CallAPI();
        customListener = this;
        CitiesWeather sofia = new CitiesWeather();
        sofia.setName("Sofia");
        cityList.add(sofia);

        CitiesWeather london = new CitiesWeather();
        london.setName("London");
        cityList.add(london);

        CitiesWeather moscow = new CitiesWeather();
        moscow.setName("Moscow");
        cityList.add(moscow);

        listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CitiesWeather node = (CitiesWeather) listView.getItemAtPosition(i);
                appi = new CallAPI(customListener);
                appi.execute(node);
                /*ProgressDialogFragment rotationDialog = new ProgressDialogFragment();
                rotationDialog.show(getSupportFragmentManager(), "x");
                Log.v("asd", "asd");*/
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

                ProgressDialogFragment fragment = new ProgressDialogFragment();

                fragmentTransaction.add(fragment, "tag");
                fragmentTransaction.commit();
            }
        };

        adapter = new CustomListAdapter(this, cityList);
        listView = (ListView) this.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (appi != null) {
//            appi.setListener(customListener);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onResume();
//        if (appi != null) {
//            appi.setListener(null);
//        }
//    }

    private void showEditDialog(CitiesWeather node) {
        DialogFragment newFragment = EditNameDialogFragment.newInstance(node.getName(), node.getTemperature(), node.getLon(), node.getLat());
        newFragment.show(getFragmentManager(), "dialog");
    }

    public void onDataReceived(ArrayList<CitiesWeather> city) {
        adapter.clear();
        adapter.addAll(city);
        adapter.notifyDataSetChanged();
    }

    public void onElementReceived(CitiesWeather city) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialogFragment rotationDialog = (ProgressDialogFragment) getFragmentManager().findFragmentByTag("tag");
                rotationDialog.dismiss();
            }
        });
        showEditDialog(city);
    }
}
