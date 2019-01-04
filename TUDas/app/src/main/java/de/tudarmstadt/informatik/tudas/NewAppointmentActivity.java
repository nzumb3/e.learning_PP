package de.tudarmstadt.informatik.tudas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewAppointmentActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "appointment_title";
    public static final String EXTRA_DESCRIPTION = "appointment_description";
    public static final String EXTRA_STARTTIME = "appointment_starttime";
    public static final String EXTRA_ENDTIME = "appointment_endtime";


    private EditText mEditWordView;
    private Calendar start_date_calendar = Calendar.getInstance();
    private Calendar end_date_calendar = Calendar.getInstance();
    private EditText start_date_input;
    private EditText end_date_input;
    private EditText descriptionInput;
    private EditText start_time_input;
    private EditText end_time_input;

    private boolean hour24Format;

    private void updateLabel() {
        String myTimeFormat;
        SimpleDateFormat dateFormat;
        SimpleDateFormat timeFormat;
        if (android.text.format.DateFormat.is24HourFormat(NewAppointmentActivity.this)){
            myTimeFormat = "HH:mm";
            this.hour24Format = true;
        } else {
            myTimeFormat = "hh:mm a";
            this.hour24Format = false;
        }
        String myDateFormat = "dd.MM.yy";
        //dateFormat = new SimpleDateFormat(myDateFormat, Locale.GERMANY);
        dateFormat = new SimpleDateFormat(myDateFormat, Locale.getDefault());
        //timeFormat = new SimpleDateFormat(myTimeFormat, Locale.GERMANY);
        timeFormat = new SimpleDateFormat(myTimeFormat, Locale.getDefault());
        start_date_input.setText(dateFormat.format(start_date_calendar.getTime()));
        start_time_input.setText(timeFormat.format(start_date_calendar.getTime()));
        end_date_input.setText(dateFormat.format(end_date_calendar.getTime()));
        end_time_input.setText(timeFormat.format(end_date_calendar.getTime()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        mEditWordView = findViewById(R.id.appointment_title_input);
        descriptionInput = findViewById(R.id.appointment_description_input);

        final Button button = findViewById(R.id.button_save_appointment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String title = mEditWordView.getText().toString();
                    replyIntent.putExtra(EXTRA_TITLE, title);
                    String description = descriptionInput.getText().toString();
                    replyIntent.putExtra(EXTRA_DESCRIPTION, description);
                    String startTime = start_date_input.getText().toString() + " " + start_time_input.getText().toString();
                    replyIntent.putExtra(EXTRA_STARTTIME, start_date_calendar);
                    String endTime = end_date_input.getText().toString() + " " + end_time_input.getText().toString();
                    replyIntent.putExtra(EXTRA_ENDTIME, end_date_calendar);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
        this.start_date_input = (EditText) findViewById(R.id.start_date_input);
        DatePickerDialog.OnDateSetListener date_start = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                start_date_calendar.set(Calendar.YEAR, year);
                start_date_calendar.set(Calendar.MONTH, month);
                start_date_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        start_date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewAppointmentActivity.this, date_start, start_date_calendar.get(Calendar.YEAR),
                        start_date_calendar.get(Calendar.MONTH), start_date_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        this.end_date_input = (EditText) findViewById(R.id.end_date_input);
        DatePickerDialog.OnDateSetListener date_end = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                end_date_calendar.set(Calendar.YEAR, year);
                end_date_calendar.set(Calendar.MONTH, month);
                end_date_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        end_date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewAppointmentActivity.this, date_end, end_date_calendar.get(Calendar.YEAR),
                        end_date_calendar.get(Calendar.MONTH), end_date_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        this.start_time_input = (EditText) findViewById(R.id.start_time_input);
        TimePickerDialog.OnTimeSetListener time_start = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                start_date_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                start_date_calendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }
        };
        start_time_input.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new TimePickerDialog(NewAppointmentActivity.this, time_start, start_date_calendar.get(Calendar.HOUR_OF_DAY), start_date_calendar.get(Calendar.MINUTE), hour24Format).show();
            }
        });
        this.end_time_input = (EditText) findViewById(R.id.end_time_input);
        TimePickerDialog.OnTimeSetListener time_end = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                end_date_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                end_date_calendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }
        };
        end_time_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new TimePickerDialog(NewAppointmentActivity.this, time_end, end_date_calendar.get(Calendar.HOUR_OF_DAY), end_date_calendar.get(Calendar.MINUTE), hour24Format).show();
            }
        });
        updateLabel();
    }
}