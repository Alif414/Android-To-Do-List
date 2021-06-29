package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHandler dbHandler;
    private ArrayList<TaskItem> taskList = new ArrayList<>();
    private FloatingActionButton fAButton;
    private static final String EDIT = "Edit";
    public String completeTasks = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fAButton = findViewById(R.id.floatingActionButton2);

        dbHandler = new DatabaseHandler(this, null, null, 1);
        populateDB();
        taskList = dbHandler.getIncompleteTasks();

        //Recycler view set and all views have the same size
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        taskAdapter = new TaskAdapter(taskList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(taskAdapter);

        if(completeTasks.equals("no")) {
            //Press on incomplete task to edit in edit activity
            taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    TaskItem editTask = taskList.get(position);
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra(EDIT, editTask);
                    //Item sent to edit activity
                    startActivity(intent);
                }
            });

            taskAdapter.setOnItemLongClickListener(new TaskAdapter.OnItemLongClickListener() {
                //Delete item with long click
                @Override
                public boolean onItemLongClick(int position) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    int result = dbHandler.deleteTask(taskList.get(position));
                                    System.out.println(result);
                                    taskList = dbHandler.getIncompleteTasks();
                                    taskAdapter.setTaskItemList(taskList);
                                    taskAdapter.notifyDataSetChanged();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });

            taskAdapter.setOnCheckedChangeListener(new TaskAdapter.OnCheckedChangeListener() {
                //Press switch button to change the task to complete
                @Override
                public boolean onCheckedChange(int position) {
                    taskList.get(position).setComplete("YES");
                    dbHandler.updateTask(taskList.get(position));
                    taskList = dbHandler.getIncompleteTasks();
                    taskAdapter.setTaskItemList(taskList);
                    taskAdapter.notifyDataSetChanged();

                    return true;
                }
            });
        }

        fAButton.setOnClickListener(new View.OnClickListener() {
            //Press to add new task in add activity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.completed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //Cases to show complete/incomplete tasks
            case R.id.complete:
                if (completeTasks.equals("no")){
                    item.setTitle("To Do");
                    taskList = dbHandler.getCompleteTasks();
                    taskAdapter.setTaskItemList(taskList);
                    taskAdapter.notifyDataSetChanged();
                    completeTasks = "yes";
                    System.out.println(completeTasks);
                }else{
                    item.setTitle("Complete");
                    taskList = dbHandler.getIncompleteTasks();
                    taskAdapter.setTaskItemList(taskList);
                    taskAdapter.notifyDataSetChanged();
                    completeTasks = "no";
                    System.out.println(completeTasks);
                }
                return true;
        }
        return true;
    }

    private void populateDB(){
        //Have a pre-populate DB with a few items
        dbHandler.addTask(new TaskItem("Finish Assignment 5", "4/6/2021",
                "Finish task 1 and task 2", "LOW", "NO"));
        dbHandler.addTask(new TaskItem("Finish Assignment 4", "1/6/2021",
                "Finish task 1 and task 2", "LOW", "NO"));
        dbHandler.addTask(new TaskItem("Submit portfolio", "10/6/2021", "Make the LSR then submit it",
                "HIGH", "NO"));
        dbHandler.addTask(new TaskItem("Finish Assignment 3", "24/5/2021",
                "Finish task 1 and task 2", "LOW", "YES"));
        dbHandler.addTask(new TaskItem("Finish Assignment 1", "4/5/2021",
                "Finish task 1 and task 2", "LOW", "YES"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String ADD = "add";
        final String EDITED = "Edited";
        Intent intent = getIntent();
        if(intent.getParcelableExtra(ADD) != null){
            //Add task is received and added to DB
            TaskItem addTask = intent.getParcelableExtra(ADD);
            dbHandler.addTask(addTask);
            taskList = dbHandler.getIncompleteTasks();
            taskAdapter.setTaskItemList(taskList);
            taskAdapter.notifyDataSetChanged();
        }
        if(intent.getParcelableExtra(EDITED) != null){
            //Edited task is updated on DB
            TaskItem editTask = intent.getParcelableExtra(EDITED);
            System.out.println(editTask);
            dbHandler.updateTask(editTask);
            taskList = dbHandler.getIncompleteTasks();
            taskAdapter.setTaskItemList(taskList);
            taskAdapter.notifyDataSetChanged();
        }

        taskAdapter.setTaskItemList(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}