package com.example.f3app;

import android.content.DialogInterface;
import android.util.Log;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private final static String TAG = "IgB:DialogFormat";
    private int year;
    private int day;
    private int month;
    private Date maxDate = null;
    private Date minDate = null;
    private Date currentDate = null;
    private boolean startDate = false;
    private boolean dueDate = true;
    private boolean canceled = false;

    public DatePickerFragment(String strCurrentDate, String strMinDate, String strMaxDate) throws java.text.ParseException {
        super();
        if (strCurrentDate!=null) {
            Log.d(TAG, "DatePickerFragment constructor set currentDate(str): " + strCurrentDate);
            currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(strCurrentDate);
            Log.d(TAG, "DatePickerFragment constructor set currentDate(date): " + currentDate.toString());
        }
        minDate = new SimpleDateFormat("yyyy-MM-dd").parse(strMinDate);
        maxDate = new SimpleDateFormat("yyyy-MM-dd").parse(strMaxDate);

        //Date minDate = new SimpleDateFormat("dd/MM/yyyy").parse(strMinDate);
        //Date maxDate = new SimpleDateFormat("dd/MM/yyyy").parse(strMaxDate);
    }

    public DatePickerFragment(){
        super();
    }

    public DatePickerFragment startPicker(){
       this.startDate = true;
       this.dueDate = false;
       return this;
    }

    public DatePickerFragment currentDate(String strCurrentDate) throws java.text.ParseException{
        if(strCurrentDate!=null) {
            Log.d(TAG, "DatePickerFragment constructor set currentDate(str): " + strCurrentDate);
            currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(strCurrentDate);
            Log.d(TAG, "DatePickerFragment constructor set currentDate(date): " + currentDate.toString());
        }

        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year;
        int day;
        int month;
        if (currentDate != null) {
            Log.d(TAG, "onCreateDialog - using currentDate for calendar");
            year = currentDate.getYear() + 1900;
            month = currentDate.getMonth();
            day = currentDate.getDate();
        }
        else if ( minDate != null ) {
            Log.d(TAG, "onCreateDialog - using minDate for calendar");
        year = minDate.getYear() + 1900;
        month = minDate.getMonth();
        day = minDate.getDate();
        }
        else if ( maxDate != null ) {
            Log.d(TAG, "onCreateDialog - using maxDate for calendar");
            year = maxDate.getYear() +1900;
            month = maxDate.getMonth();
            day = maxDate.getDate();
        }
        else{
            Log.d(TAG, "onCreateDialog - using now() for calendar");
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        Log.d(TAG, "onCreateDialog - starting DatePicker with y-m-d: " + year + "-" + month + "-" + day);
        DatePickerDialog dp =  new DatePickerDialog(getActivity(), this, year, month, day);
        if ( minDate != null && maxDate != null){
            Log.d(TAG, "onCreateDialog set minDate : " + minDate.toString() +
                    " and maxDate: " + maxDate.toString() );
            dp.getDatePicker().setMinDate(minDate.getTime());
            dp.getDatePicker().setMaxDate(maxDate.getTime());
        }
        dp.setCancelable(true);
        dp.setCanceledOnTouchOutside(true);
        return dp;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.i(TAG, "onDateSet called with values y0-m0-d0: " + year + "-" + month + "-" + day);
        this.year = view.getYear();
        this.month = view.getMonth()+1;
        this.day = view.getDayOfMonth();
        Log.i(TAG, "onDateSet exiting with value y1-m1-d1: " + this.year + "-" + this.month + "-" + this.day);
    }


    public void onCancel(DialogInterface dialogI){
        super.onCancel(dialogI);
        Log.i(TAG, "onCancel called");
        this.canceled = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy started");
        if (canceled) {
            Log.i(TAG, "onDestroy started, but dialog canceled. not updating");
            return;
        }
        if (dueDate) {
            ((ActivityWithDates) getActivity()).setDueDate(year + "-" + month + "-" + day);
        }
        else {
            ((ActivityWithDates) getActivity()).setStartDate(year + "-" + month + "-" + day);
        }
    }

}
