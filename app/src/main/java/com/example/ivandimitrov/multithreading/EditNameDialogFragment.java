package com.example.ivandimitrov.multithreading;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

/**
 * Created by Ivan Dimitrov on 12/13/2016.
 */
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;


public class EditNameDialogFragment extends DialogFragment {

    private EditText mEditText;

    public EditNameDialogFragment() {

    }

    public static EditNameDialogFragment newInstance(String title, String temperature, String lon, String lat) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("temperature", temperature);
        args.putString("lon", lon);
        args.putString("lat", lat);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String temperature = getArguments().getString("temperature");
        String lon = getArguments().getString("lon");
        String lat = getArguments().getString("lat");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title).setMessage("Lat: " + lat + " Lon: " + lon + " Temperature: " + temperature)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create();
    }

}
