package com.example.todolist;

import android.content.Context;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<TaskItem> taskItemList;
    private OnItemClickListener myListener;
    private OnItemLongClickListener myLongClickListener;
    private OnCheckedChangeListener myCheckedChangeListener;
    private Context context;
    DatabaseHandler dbHandler;
    public boolean completed;

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ArrayList<TaskItem> getTaskItemList() {
        return taskItemList;
    }

    public void setTaskItemList(ArrayList<TaskItem> taskItemList) {
        this.taskItemList = taskItemList;
    }

    public TaskAdapter(ArrayList<TaskItem> taskList, Context context){
        taskItemList = taskList;
        this.context = context;
    }

    //Custom listeners for Main Activity
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);

    }

    public interface OnCheckedChangeListener {
        boolean onCheckedChange(int position);
    }

    public boolean setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener){
        myCheckedChangeListener = onCheckedChangeListener;
        return true;
    }

    public boolean setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        myLongClickListener = longClickListener;
        return true;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtask, parent, false);
        TaskViewHolder tvh = new TaskViewHolder(v, myListener, myLongClickListener, myCheckedChangeListener);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        //Finding current task
        TaskItem currentTask = taskItemList.get(position);
        String currentPriority = currentTask.getPriority();
        String taskDate = currentTask.getDate();
        String currComp = currentTask.getComplete();

        holder.titleView.setText(currentTask.getTitle());
        holder.dateView.setText(currentTask.getDate());
        holder.titleView.setText(currentTask.getTitle());
        holder.dateView.setText(currentTask.getDate());

        //Changing colour based on priority
        if(currentPriority.equals("HIGH")){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
        }

        //Completed tasks will not be able to be changed
        if(currComp.equals("YES")){
            holder.aSwitch.setEnabled(false);
        }

        if(currComp.equals("NO")){
            holder.aSwitch.setEnabled(true);
        }

        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date today = c.getTime();

        Date dateObj = new Date(taskDate);

        SimpleDateFormat taskForm = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateObj = taskForm.parse(taskDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Late tasks are in red
        if(dateObj.before(today)){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, dateView;
        public CardView cardView;
        public Switch aSwitch;

        public TaskViewHolder(@NonNull View itemView, OnItemClickListener myListener,
                              OnItemLongClickListener myLongClickListener, OnCheckedChangeListener myCheckedChangeListener) {
            super(itemView);

            titleView = itemView.findViewById(R.id.titleView);
            dateView = itemView.findViewById(R.id.date);
            cardView = itemView.findViewById(R.id.taskView);
            aSwitch = itemView.findViewById(R.id.switch1);

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (myCheckedChangeListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            myCheckedChangeListener.onCheckedChange(position);
                        }
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            myListener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (myLongClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            myLongClickListener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
