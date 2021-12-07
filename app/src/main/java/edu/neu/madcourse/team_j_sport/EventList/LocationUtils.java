package edu.neu.madcourse.team_j_sport.EventList;

import android.content.SharedPreferences;

import java.util.HashMap;

public class LocationUtils {

    public static final String getDistanceTo(String current_latitude, String current_longitude
            ,String latitude, String longitude){

        //base case
        if(current_latitude == null
                || current_latitude.length() == 0
                || current_longitude == null
                || current_longitude.length() == 0){
            return "";
        }
        if(latitude == null
                || latitude.length() == 0
                || longitude == null
                || longitude.length() == 0){
            return "";
        }
        double lat1 = Double.valueOf(current_latitude);
        double lon1 = Double.valueOf(current_longitude);
        double lat2 = Double.valueOf(latitude);
        double lon2 = Double.valueOf(longitude);
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        double res = rad * c;
        return String.valueOf(res) + " KM";
    }

}
