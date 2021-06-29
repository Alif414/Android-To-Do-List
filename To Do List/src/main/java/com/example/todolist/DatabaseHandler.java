package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "TaskDB";
    private static final String TABLE_NAME = "Task_Table";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_DATE = "Date";
    private static final String KEY_DETAILS = "Details";
    private static final String KEY_PRIORITY = "Priority";
    private static final String KEY_COMPLETE = "Complete";

    public DatabaseHandler(@Nullable Context context, @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table
        String create_table = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT, " + KEY_DATE + " TEXT, " + KEY_DETAILS + " TEXT, " + KEY_PRIORITY +
                " TEXT, " + KEY_COMPLETE + " TEXT)";
        db.execSQL(create_table);
        System.out.println("Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        System.out.println("Table dropped");
    }

    public long addTask(TaskItem task){
        //Add row
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, task.getTitle());
        contentValues.put(KEY_DATE, task.getDate());
        contentValues.put(KEY_DETAILS, task.getDetails());
        contentValues.put(KEY_PRIORITY, task.getPriority());
        contentValues.put(KEY_COMPLETE, task.getComplete());

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public TaskItem getTask(int taskID){
        //Get row
        TaskItem taskItem = null;
        SQLiteDatabase db = this.getReadableDatabase();
        //query of row being search
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_DETAILS,
                KEY_PRIORITY, KEY_COMPLETE}, KEY_ID + "=?", new String[]{String.valueOf(taskID)},
                null, null, null, null);

        if(cursor != null){
            //if there is a similar row on the table then it will return the item
            cursor.moveToFirst();
            taskItem = new TaskItem(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5));
        }

        return taskItem;
    }

    public ArrayList<TaskItem> getAllTasks(){
        //Get all rows
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                TaskItem taskItem = new TaskItem();
                taskItem.setId(Integer.parseInt(cursor.getString(0)));
                taskItem.setTitle(cursor.getString(1));
                taskItem.setDate(cursor.getString(2));
                taskItem.setDetails(cursor.getString(3));
                taskItem.setPriority(cursor.getString(4));
                taskItem.setComplete(cursor.getString(5));
                taskList.add(taskItem);
            }while (cursor.moveToNext());
        }
        return taskList;
    }

    public int deleteTask(TaskItem taskItem){
        //Delete a task
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(taskItem.getId())});
    }

    public int updateTask(TaskItem task){
        //Update existing row with the edited data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, task.getTitle());
        contentValues.put(KEY_DATE, task.getDate());
        contentValues.put(KEY_DETAILS, task.getDetails());
        contentValues.put(KEY_PRIORITY, task.getPriority());
        contentValues.put(KEY_COMPLETE, task.getComplete());
        return db.update(TABLE_NAME, contentValues, KEY_ID + "= ?", new String[]{String.valueOf(task.getId())});
    }

    public ArrayList<TaskItem> getIncompleteTasks(){
        //Get tasks that are not completed
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_COMPLETE + " = 'NO'";
        //Where clause used to find incomplete tasks

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                TaskItem taskItem = new TaskItem();
                taskItem.setId(Integer.parseInt(cursor.getString(0)));
                taskItem.setTitle(cursor.getString(1));
                taskItem.setDate(cursor.getString(2));
                taskItem.setDetails(cursor.getString(3));
                taskItem.setPriority(cursor.getString(4));
                taskItem.setComplete(cursor.getString(5));
                taskList.add(taskItem);
            }while (cursor.moveToNext());
        }
        return taskList;
    }

    public ArrayList<TaskItem> getCompleteTasks(){
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_COMPLETE + " = 'YES'";
        //Where clause used to find completed tasks

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                TaskItem taskItem = new TaskItem();
                taskItem.setId(Integer.parseInt(cursor.getString(0)));
                taskItem.setTitle(cursor.getString(1));
                taskItem.setDate(cursor.getString(2));
                taskItem.setDetails(cursor.getString(3));
                taskItem.setPriority(cursor.getString(4));
                taskItem.setComplete(cursor.getString(5));
                taskList.add(taskItem);
            }while (cursor.moveToNext());
        }
        return taskList;
    }
}
