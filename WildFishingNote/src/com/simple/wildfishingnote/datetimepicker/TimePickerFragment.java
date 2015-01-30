package com.simple.wildfishingnote.datetimepicker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.simple.wildfishingnote.utils.StringUtils;

public class TimePickerFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    BootstrapButton mBtn = null;
    int mHour;
    int mMinute;

    public TimePickerFragment(BootstrapButton btn) {
        String buttonText = btn.getText().toString();
        String[] arr = buttonText.split(":");
        int hour = Integer.valueOf(arr[0]);
        int minute = Integer.valueOf(arr[1]);

        mBtn = btn;
        mHour = hour;
        mMinute = minute;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hour = mHour;
        int minute = mMinute;

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mBtn.setText(StringUtils.leftPadTwo(hourOfDay) + ":" + StringUtils.leftPadTwo(minute));
    }
}
