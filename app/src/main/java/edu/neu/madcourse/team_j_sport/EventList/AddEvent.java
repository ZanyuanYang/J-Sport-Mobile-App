package edu.neu.madcourse.team_j_sport.EventList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import edu.neu.madcourse.team_j_sport.R;

public class AddEvent extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatePickerDialog dataPicker;
    private TimePickerDialog timePicker;
    private SharedPreferences sp;
    public static final String FIRST_NAME_KEY = "firstname";
    public static final String LAST_NAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String USER_ID_KEY = "user id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        setUpFirebase();
        initView();
        initTimePicker();
        initDatePicker();
        //TODO : Organizer
        //TODO :

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
                setEventLocation(newEvent, childCnt);
//                mDatabase.child("Events")
//                        .child(String.valueOf(childCnt + 1))
//                        .setValue(newEvent);
//                Toast.makeText(AddEvent.this,"Add Event Successfully!", Toast.LENGTH_SHORT).show();
//                finish();
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
        EditText zipCodeET = findViewById(R.id.Event_LocationEditText);
        EditText limitPersonET = findViewById(R.id.Event_LimitPersonEditText);
        EditText timeET = findViewById(R.id.Event_TimeEditText);
        EditText dateET = findViewById(R.id.event_DateEditText);
        EditText contactET = findViewById(R.id.Event_ContactEditText);


        String uid = sp.getString(USER_ID_KEY, "");
        String title = titleET.getText().toString();
        String summary = sumET.getText().toString();
        String description = desET.getText().toString();
        String zipCode = zipCodeET.getText().toString();
        String limitPerson = limitPersonET.getText().toString();
        String time = timeET.getText().toString() + " " + dateET.getText().toString();
        String contact = contactET.getText().toString();
        String organizer = sp.getString(FIRST_NAME_KEY,"") + " "+ sp.getString(LAST_NAME_KEY, "");
        Events event = new Events(title, summary, description, zipCode, limitPerson, time, contact,organizer, uid);
        return event;
    }
    private void setEventLocation(Events event, long childCnt){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://thezipcodes.com/api/v1/search" +
                                    "?zipCode="+
                                    event.zipCode+ "&countryCode=US" +
                                    "&apiKey=5104b5b4a5e6e02281cb5a556b0f6713")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObject(responseData, event, childCnt);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String jsonData, Events event, Long childCnt){
        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            boolean success = jsonObject.getBoolean("success");
            if(success){
                JSONArray arr = jsonObject.getJSONArray("location");
                JSONObject dic = arr.getJSONObject(0);
                String latitude = dic.getString("latitude");
                String longitude = dic.getString("longitude");
                String city = dic.getString("city");
                event.latitude = latitude;
                event.longitude = longitude;
                event.location = city;
                event.participants = new HashMap<>();
                HashMap<String, String> map = new HashMap<>();
                map.put("email", sp.getString(EMAIL_KEY, ""));
                map.put("username", event.organizer);
                event.participants.put(sp.getString(USER_ID_KEY,""), map);

                String uuid = UUID.randomUUID().toString();
                mDatabase.child("Events")
                        .child(uuid)
                        .setValue(event);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddEvent.this,"Add Event Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}