package edu.fsu.cs.mobile.snoozemenot;

public class AlarmObject {
    private String time;
    private String amPM;
    private boolean on;
    private String title;

    public AlarmObject(String time, String amPM, String title, boolean on){
        this.time = time;
        this.on = on;
        this.title = title;
        this.amPM = amPM;
    }

    public String getTime()
    { return time; }

    public boolean getOn()
    { return on; }

    public String getTitle()
    { return title; }

    public String getAmPM()
    { return amPM; }

    public void setTime(String time)
    { this.time = time; }

    public void setOn(boolean on)
    { this.on = on; }

    public void setMode(String title)
    { this.title = title; }

    public void setAmPM(String amPM)
    { this.amPM = amPM; }
}
