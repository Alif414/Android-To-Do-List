package com.example.todolist;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskItem implements Parcelable {
    private int id;
    private String title;
    private String date;
    private String details;
    private String priority;
    private String complete;

    public TaskItem(String title, String date, String details, String priority, String complete) {
        this.title = title;
        this.date = date;
        this.details = details;
        this.priority = priority;
        this.complete = complete;
    }

    public TaskItem() {

    }

    public TaskItem(int id, String title, String date, String details, String priority, String complete) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.details = details;
        this.priority = priority;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public String getPriority() {
        return priority;
    }

    public String getComplete() {
        return complete;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Functions to make task parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(details);
        dest.writeString(priority);
        dest.writeString(complete);
    }

    public TaskItem(Parcel parcel){
        id = parcel.readInt();
        title = parcel.readString();
        date = parcel.readString();
        details = parcel.readString();
        priority = parcel.readString();
        complete = parcel.readString();
    }

    public static final Parcelable.Creator<TaskItem> CREATOR = new Parcelable.Creator<TaskItem>() {

        @Override
        public TaskItem createFromParcel(Parcel parcel) {
            return new TaskItem(parcel);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[0];
        }
    };
}
