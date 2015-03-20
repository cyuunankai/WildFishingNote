package com.simple.wildfishingnote.campaign.place;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.simple.wildfishingnote.R;

public class AreaDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        public void onAreaDialogPositiveClick(DialogFragment dialog, String title);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    private String title;

    public AreaDialogFragment(String _title) {
        this.title = _title;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_area, null);

        if (StringUtils.isNotBlank(this.title)) {
            BootstrapEditText et = (BootstrapEditText)v.findViewById(R.id.areaTitle);
            et.setText(this.title);
        }
        builder.setView(v)
                .setTitle("维护区域")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        BootstrapEditText et = (BootstrapEditText)v.findViewById(R.id.areaTitle);

                        mListener.onAreaDialogPositiveClick(AreaDialogFragment.this, et.getEditableText().toString());

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AreaDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
