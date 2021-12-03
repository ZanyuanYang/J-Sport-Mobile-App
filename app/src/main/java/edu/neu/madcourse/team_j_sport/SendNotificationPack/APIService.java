package edu.neu.madcourse.team_j_sport.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAZxLNbJM:APA91bHr8d0N0Ezz04uNzGrh23q1voiuWMyLe5bT_OMcQ-xgGebq1ZjRSN_uN-TTIiNzDb9jKD0kNPBEk87HQz7xajNaC9CgsjkcjpAvcukmd2w9pHLD5GorUdbt_pDF7NmN-IyBmSsN"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}