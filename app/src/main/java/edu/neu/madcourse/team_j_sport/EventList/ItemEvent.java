package edu.neu.madcourse.team_j_sport.EventList;

public class ItemEvent {

    private final String title;
    private final String time;
    private final String summary;
    private final String organizer;
    private final String location;
    private final String eventId;

    public ItemEvent(String title, String time, String summary, String organizer, String location, String eventId) {
        this.title = title;
        this.time = time;
        this.summary = summary;
        this.organizer = organizer;
        this.location = location;
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getLocation() {
        return location;
    }

    public String getEventId() {
        return eventId;
    }
}
