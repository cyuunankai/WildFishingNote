package com.simple.wildfishingnote.campaign.point;

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

public class RodLengthDialogFragment extends DialogFragment {
	

    public interface NoticeDialogListener {
        public void onRLDialogPositiveClick(DialogFragment dialog, String rodLength);
    }
    
    NoticeDialogListener mListener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
    private String name;

    public RodLengthDialogFragment(String _name) {
        this.name = _name;
    }

    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_rod_length, null);
        
        if (StringUtils.isNotBlank(this.name)) {
            BootstrapEditText et = (BootstrapEditText)v.findViewById(R.id.rodLengthName);
            et.setText(this.name);
        }
        builder.setView(v)
        // Add action buttons
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // sign in the user ...
                       BootstrapEditText et = (BootstrapEditText)v.findViewById(R.id.rodLengthName);
                       
                       
                       mListener.onRLDialogPositiveClick(RodLengthDialogFragment.this, et.getEditableText().toString());

                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       RodLengthDialogFragment.this.getDialog().cancel();
                   }
               });      
        return builder.create();
    }

}
