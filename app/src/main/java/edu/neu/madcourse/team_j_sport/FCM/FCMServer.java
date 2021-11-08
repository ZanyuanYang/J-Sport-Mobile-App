package edu.neu.madcourse.team_j_sport.FCM;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class FCMServer {

    private static final String TAG = "FCMServer";
    private static final String SERVER_KEY = "key=AAAAY6KLNCE:APA91bGFrpvYpLlc4_56fAw7_LV82iD1sMKf4pn-ATM3LrvKvfhrbOGzS3cauohKrE8AgBVkawpuXsfQP--_aIvksPpUJN2dKQtbcdtYsuRt1YY5B-m5fAtHVjl2qL70wo63w7o1xjmw";

    public static final String TOPIC_DESCRIPTION_PRE = "Notification ";

    public static void testSend(Long id) {
        for (long i = 39; i <= 42; i++) {
            sendNotificationToUserThread((long)38, i);
        }
    }

//    public void createNotificationChannel(Long userId, String name) {
//        // This must be called early because it must be called before a notification is sent.
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }



    /**
     * Button Handler; creates a new thread that sends off a message
     */
    public static void sendNotificationToUserThread(Long sId, Long rId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendNotificationToUserTask(sId, rId);
            }
        }).start();
    }

    private static void sendNotificationToUserTask(Long sId, Long rId){
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        try {
            // TODO: remove senderId
            jNotification.put("title", "You received a new sticker (from " + sId + " + !");
            jNotification.put("message", "This is a message from FCM topic \"user-" + rId + "\"!");
            jNotification.put("body", "Click for details.");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");

            // Populate the Payload object.
            // Note that "to" is a topic, not a token representing an app instance
            jPayload.put("to", "/topics/" + rId);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);

            // Open the HTTP connection and send the payload
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: " + resp);
                }
            });
        } catch (JSONException | IOException e) {
            Log.e(TAG,"sendMessageToNews threw error",e);
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

}
