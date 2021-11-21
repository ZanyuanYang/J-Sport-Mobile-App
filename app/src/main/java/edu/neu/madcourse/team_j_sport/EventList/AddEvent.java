package edu.neu.madcourse.team_j_sport.EventList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Calendar;

import edu.neu.madcourse.team_j_sport.R;

public class AddEvent extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatePickerDialog dataPicker;
    private TimePickerDialog timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        setUpFirebase();
        initView();
        initTimePicker();
        initDatePicker();
    }
    private void initView(){
        Button btn = findViewById(R.id.Event_SubmitButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();

            }
        });
    }
    private void setUpFirebase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void addEvent(){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long childCnt = snapshot.getChildrenCount();
                Events newEvent = getEventInfo();
                mDatabase.child("Events")
                        .child(String.valueOf(childCnt + 1))
                        .setValue(newEvent);
                Toast.makeText(AddEvent.this,"Add Event Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child("Events")
                .addListenerForSingleValueEvent(listener);
    }
    private void initDatePicker(){
        EditText dateET = findViewById(R.id.event_DateEditText);
        dateET.setInputType(InputType.TYPE_NULL);
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                dataPicker = new DatePickerDialog(AddEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                dateET.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                dataPicker.show();
            }
        });
    }
    private void initTimePicker(){
        EditText timeET = findViewById(R.id.Event_TimeEditText);
        timeET.setInputType(InputType.TYPE_NULL);
        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);


                timePicker = new TimePickerDialog(AddEvent.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                timeET.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                timePicker.show();
            }
        });
    }
    private Events getEventInfo(){

        EditText titleET = findViewById(R.id.Event_TitleEditText);
        EditText sumET = findViewById(R.id.Event_SummaryEditText);
        EditText desET = findViewById(R.id.Event_DescriptionEditText);
        EditText locationET = findViewById(R.id.Event_LocationEditText);
        EditText limitPersonET = findViewById(R.id.Event_LimitPersonEditText);
        EditText timeET = findViewById(R.id.Event_TimeEditText);
        EditText dateET = findViewById(R.id.event_DateEditText);
        EditText contactET = findViewById(R.id.Event_ContactEditText);



        String title = titleET.getText().toString();
        String summary = sumET.getText().toString();
        String description = desET.getText().toString();
        String location = locationET.getText().toString();
        String limitPerson = limitPersonET.getText().toString();
        String time = timeET.getText().toString() + " " + dateET.getText().toString();
        String contact = contactET.getText().toString();
        Events event = new Events(title, summary, description, location, limitPerson, time, contact);
        return event;
    }

}