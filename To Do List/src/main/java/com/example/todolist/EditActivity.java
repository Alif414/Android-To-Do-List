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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    private static final String EDIT = "Edit";
    private static final String EDITED = "Edited";
    EditText editTitle, editDate, editDetails;
    DatabaseHandler dbHandler;
    String title, date, details;
    TaskItem taskItem;
    CheckBox checkBox;
    DatePickerDialog picker;
    String updateTitle, updateDate, updateDetail, updatePriority;
    ArrayList<TaskItem> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHandler = new DatabaseHandler(this, null, null, 1);
        editTitle = findViewById(R.id.addTitle);
        editDate = findViewById(R.id.addDate);
        editDetails = findViewById(R.id.addDetails);
        checkBox = findViewById(R.id.addPriority);

        Intent intent = getIntent();
        taskItem = intent.getParcelableExtra(EDIT);
        title = taskItem.getTitle();
        date = taskItem.getDate();
        details = taskItem.getDetails();

        editTitle.setText(title);
        editDate.setText(date);
        editDetails.setText(details);

        if(taskItem.getPriority().equals("HIGH")){
            checkBox.setChecked(true);
        } else
            checkBox.setChecked(false);

        //Calendar set
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        editDate.setInputType(InputType.TYPE_NULL);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date picker dialog
                picker = new DatePickerDialog(EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                //Picker used to set calendar
                picker.getDatePicker().setMinDate(System.currentTimeMillis());
                //Date before current day cannot be chosen
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
        updateTitle = editTitle.getText().toString();
        updateDate = editDate.getText().toString();
        updateDetail = editDetails.getText().toString();
        updatePriority = "LOW";

        if (checkBox.isChecked())
            updatePriority = "HIGH";
        else
            updatePriority = "LOW";
        //Checkbox sets priority

        taskItem.setTitle(updateTitle);
        taskItem.setDate(updateDate);
        taskItem.setDetails(updateDetail);
        taskItem.setPriority(updatePriority);
        taskItem.setComplete("NO");

        //If statements to make sure, there are no null entries
        if(updateTitle.isEmpty())
            Toast.makeText(EditActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
        else if(updateDate.isEmpty())
            Toast.makeText(EditActivity.this, "Please enter a date", Toast.LENGTH_SHORT).show();
        else if(updateDetail.isEmpty())
            Toast.makeText(EditActivity.this, "Please enter the details", Toast.LENGTH_SHORT).show();
        else {
            //Task item sent to main
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            intent.putExtra(EDITED, taskItem);
            startActivity(intent);
        }
        return true;
    }
}