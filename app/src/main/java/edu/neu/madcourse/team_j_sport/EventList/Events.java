package edu.neu.madcourse.team_j_sport.EventList;

public class Events {
    public String title;
    public String summary;
    public String description;
    public String zipCode;
    public String limitPerson;
    public String time;
    public String contact;
    public String organizer;
    public String location;
    public String latitude;
    public String longitude;
    public Events(String tit, String sum, String des,
                  String zip, String lp, String time, String cont, String organizer){
        this.title = tit;
        this.summary = sum;
        this.description = des;
        this.zipCode = zip;
        this.limitPerson = lp;
        this.time = time;
        this.contact = cont;
        this.organizer = organizer;
    }
}
