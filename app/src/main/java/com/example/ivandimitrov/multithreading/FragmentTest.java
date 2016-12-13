package com.example.ivandimitrov.multithreading;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ivandimitrov.multithreading.Adapters.CustomListAdapter;
import com.example.ivandimitrov.multithreading.NodeTypes.CitiesWeather;

import java.util.ArrayList;

/**
 * Created by Ivan Dimitrov on 12/6/2016.
 */

public class FragmentTest extends Fragment implements CallAPI.CustomListener {
    public static int STATE_NEUTRAL = 1;
    public static int STATE_LOADING = 2;
    public static int STATE_SHOWN = 3;
    public static int STATE_INITIAL = 4;

    int currentState = STATE_INITIAL;
    private ListView listView;
    private ArrayList<CitiesWeather> cityList = new ArrayList<CitiesWeather>();
    private CustomListAdapter adapter;
    private AdapterView.OnItemClickListener listener;
    private CallAPI.CustomListener customListener;
    private ProgressDialog ringProgressDialog;
    private CitiesWeather currentCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        customListener = this;
        refreshViewForState();

        listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CitiesWeather node = (CitiesWeather) listView.getItemAtPosition(i);
                currentState = STATE_LOADING;
                refreshViewForState();
                update(node);
            }
        };
        adapter = new CustomListAdapter(getActivity(), cityList);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
        return view;
    }

    public void refreshViewForState() {
        if (currentState == STATE_LOADING) {
            ringProgressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Downloading data ...", true);
            ringProgressDialog.setCancelable(true);
        } else if (currentState == STATE_SHOWN) {
            ringProgressDialog.dismiss();
            showInfoDialog(currentCity);
        } else if (currentState == STATE_NEUTRAL) {

        } else if (currentState == STATE_INITIAL) {
            CitiesWeather sofia = new CitiesWeather();
            sofia.setName("Sofia");
            cityList.add(sofia);

            CitiesWeather london = new CitiesWeather();
            london.setName("London");
            cityList.add(london);

            CitiesWeather moscow = new CitiesWeather();
            moscow.setName("Moscow");
            cityList.add(moscow);
        }
    }

    void update(CitiesWeather node) {
        new CallAPI(customListener).execute(node);
    }

    @Override
    public void onPause() {
        super.onPause();

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


    public void onDataReceived(ArrayList<CitiesWeather> city) {
        adapter.clear();
        adapter.addAll(city);
        adapter.notifyDataSetChanged();
    }

    public void onElementReceived(CitiesWeather city) {
        currentCity = city;
        currentState = STATE_SHOWN;
        refreshViewForState();
    }

    public void showInfoDialog(CitiesWeather city) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("City information");
        alertDialogBuilder.setMessage("Lat: " + city.getLat() + " Lon: " + city.getLon() +
                "\nTemperature: " + city.getTemperature() + ", Wind speed: " + city.getWindSpeed())
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currentState = STATE_NEUTRAL;
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
