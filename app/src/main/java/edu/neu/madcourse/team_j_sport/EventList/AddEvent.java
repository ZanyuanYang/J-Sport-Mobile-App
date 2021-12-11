package edu.neu.madcourse.team_j_sport.EventList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Events newEvent = getEventInfo();
        if(!checkEvent(newEvent)) return;

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long childCnt = snapshot.getChildrenCount();

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
                    toastWithText("Add Event Failed, Please try again!");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithJSONObject(String jsonData, Events event, Long childCnt){
        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
//            System.out.println("ASDXC1");

//            System.out.println("ASDXC2");
            boolean success = jsonObject.getBoolean("success");

//            System.out.println("ASDXC3");
            System.out.println(success);
            if(success){

                JSONArray arr = jsonObject.getJSONArray("location");
//                System.out.println(arr);
                if(arr == null || arr.length() == 0){
//                    System.out.println(arr);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastWithText("Failed to add an event, please enter a valid zip code");
                            finish();
                        }
                    });

                }
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

            }else{
                toastWithText("Failed to add an event");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkEvent(Events event){
        EditText timeET = findViewById(R.id.Event_TimeEditText);
        EditText dateET = findViewById(R.id.event_DateEditText);
        if(event.title == null || event.title.length() == 0){
            toastWithText("Please enter the title of the event!");;
            return false;
        }else if(timeET.getText().toString() == null || timeET.getText().toString().length() == 0){
            toastWithText("Please select the time of the event!");
            return false;
        }else if(dateET.getText().toString() == null || dateET.getText().toString().length() == 0){
            toastWithText("Please select the date of the event!");
            return false;
        }else if(event.summary == null || event.summary.length() == 0){
            toastWithText("Please enter the summary of the event!");
            return false;
        }else if(event.description == null || event.description.length() == 0){
            toastWithText("Please enter the description of the event!");
            return false;
        }else if(event.limitPerson == null || event.limitPerson.length() == 0){
            toastWithText("Please enter the limit person of the event!");
            return false;
        }else if(event.contact == null || event.contact.length() == 0){
            toastWithText("Please enter the contact of the event!");
            return false;
        }else if(event.zipCode == null || event.zipCode.length() == 0){
            toastWithText("Please enter the zipcode of the event!");
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
        Matcher matcher = pattern.matcher(event.zipCode);
        boolean zipValid = matcher.matches();

        if(!zipValid){
            toastWithText("Please enter a valid zipcode!");
            return false;
        }

        if(!PhoneNumberUtils.isGlobalPhoneNumber(event.contact)){
            toastWithText("Please enter a valid phone number");
            return false;
        }

        pattern = Pattern.compile("^[1-9]+[0-9]*$|^0$");
        matcher = pattern.matcher(event.limitPerson);
        boolean personValid = matcher.matches();
        if(!personValid){
            toastWithText("Please enter a valid limit person!");
            return false;
        }

        return true;

    }
    private void toastWithText(String str){
        Toast.makeText(AddEvent.this,str, Toast.LENGTH_SHORT).show();
    }
}