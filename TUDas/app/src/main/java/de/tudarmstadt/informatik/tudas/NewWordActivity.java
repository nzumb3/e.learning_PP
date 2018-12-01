package de.tudarmstadt.informatik.tudas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditWordView;

    private Calendar start_date_calendar = Calendar.getInstance();
    private Calendar end_date_calendar = Calendar.getInstance();
    private EditText start_date_input;
    private EditText end_date_input;

    private void updateLabel() {
        String myFormat = "dd.MM.yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        start_date_input.setText(sdf.format(start_date_calendar.getTime()));
        end_date_input.setText(sdf.format(end_date_calendar.getTime()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        mEditWordView = findViewById(R.id.appointment_title_input);

        final Button button = findViewById(R.id.button_save_appointment);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = mEditWordView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
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
                new DatePickerDialog(NewWordActivity.this, date_start, start_date_calendar.get(Calendar.YEAR),
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
                new DatePickerDialog(NewWordActivity.this, date_end, end_date_calendar.get(Calendar.YEAR),
                        end_date_calendar.get(Calendar.MONTH), end_date_calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        updateLabel();
    }
}