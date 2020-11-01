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
        currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(strCurrentDate);
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
            this.currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(strCurrentDate);
        }

        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year;
        int day;
        int month;
        if (currentDate != null) {
            year = currentDate.getYear();
            month = currentDate.getMonth();
            day = currentDate.getDay();
        } else if ( minDate != null && maxDate != null){
        year = minDate.getYear();
        month = minDate.getMonth();
        day = minDate.getDay();

        }else{
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }


        DatePickerDialog dp =  new DatePickerDialog(getActivity(), this, year, month, day);
        if ( minDate != null && maxDate != null){
            dp.getDatePicker().setMinDate(minDate.getTime());
            dp.getDatePicker().setMaxDate(maxDate.getTime());
        }
        dp.setCancelable(true);
        dp.setCanceledOnTouchOutside(true);
        return dp;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.i(TAG, "onDateSet called with values y0/m0/d0: " + year + "/" + month + "/" + day);
        this.year = view.getYear();
        this.month = view.getMonth();
        this.day = view.getDayOfMonth();
        Log.i(TAG, "onDateSet exiting with value y1/m1/d1: " + this.year + "/" + this.month + "/" + this.day);
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
