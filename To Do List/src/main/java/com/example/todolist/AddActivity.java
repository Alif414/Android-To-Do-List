package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    public EditText addTitle, addDate, addDetail;
    public CheckBox priorityBox;
    DatePickerDialog picker;
    DatabaseHandler dbHandler;
    TaskAdapter taskAdapter;
    private ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
    public String title, date, detail, priority;
    public final String ADD = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHandler = new DatabaseHandler(this, null, null, 1);
        addTitle = findViewById(R.id.addTitle);
        addDate = findViewById(R.id.addDate);
        addDetail = findViewById(R.id.addDetails);
        priorityBox = findViewById(R.id.addPriority);

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        addDate.setText(day + "/" + (month + 1) + "/" + year);

        addDate.setInputType(InputType.TYPE_NULL);
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                picker = new DatePickerDialog(AddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                addDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                //Current date set on view
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis());
                //Cant pick before current date
                picker.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TaskItem taskItem = new TaskItem();
        title = addTitle.getText().toString();
        date = addDate.getText().toString();
        detail = addDetail.getText().toString();
        priority = "LOW";

        if (priorityBox.isChecked())
            priority = "HIGH";
        else
            priority = "LOW";
        //Priority set using checkbox

        taskItem.setTitle(title);
        taskItem.setDate(date);
        taskItem.setDetails(detail);
        taskItem.setPriority(priority);
        taskItem.setComplete("NO");

        if(title.isEmpty())
            Toast.makeText(AddActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
        else if(date.isEmpty())
        Toast.makeText(AddActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
        else if(detail.isEmpty())
            Toast.makeText(AddActivity.this, "Please enter the details", Toast.LENGTH_SHORT).show();
        else{
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            //Item sent to main
            intent.putExtra(ADD, taskItem);
            startActivity(intent);
        }

        return true;
    }
}