package com.sjtu.se2017.positivetime.model;

import java.util.Date;

/**
 * Created by bonjour on 17-7-14.
 */

public class Uploadinfo {
    //private UsageStats usageStats;
    private String email;
    private String packageName;
    private Date day;
    private int frequency;
    //private long UsedTimebyDay;  //milliseconds
    //private Context context;
    private int weight;

    private int duration;

    public void setWeight(int weight) { this.weight = weight;}
    public void setEmail(String email){
        this.email = email;
    }
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
    public void setDay(Date day){
        this.day = day;
    }
    public void setFrequency(int frequency){
        this.frequency = frequency;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }

    public Uploadinfo(){

    }


    /*public Uploadinfo(UsageStats usageStats , Context context) {
        this.usageStats = usageStats;
        this.context = context;

        try {
            GenerateInfo();
        } catch (PackageManager.NameNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void GenerateInfo() throws PackageManager.NameNotFoundException, NoSuchFieldException, IllegalAccessException {
        PackageManager packageManager = context.getPackageManager();
        this.packageName = usageStats.getPackageName();
        if(this.packageName != null && !this.packageName.equals("")) {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.packageName, 0);
            long UsedTimebyDay = usageStats.getTotalTimeInForeground();
            this.frequency = (Integer) usageStats.getClass().getDeclaredField("mLaunchCount").get(usageStats);
            this.duration = (int) UsedTimebyDay;
            this.email = " ";
            Calendar calendar = Calendar.getInstance();
            this.day = calendar.getTime();

        }
    }

    public UsageStats getUsageStats() {
        return usageStats;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }


    public int getDuration() {
        return duration;
    }


    public String getPackageName() {
        return packageName;
    }*/
}
