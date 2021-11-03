package com.osaid.taskmaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> taskList = new ArrayList<>();
    private final Context context;

    public TasksAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private Task task;
        private final View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            View card = itemView.findViewById(R.id.cardView);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TaskDetails.class);
                    int position = getAdapterPosition();
                    String title = taskList.get(position).getTitle();
                    intent.putExtra("taskTitle", title);
                    Log.d("ADAPTER", "Position : " + position + "  title : " + title + " ID : ");
                    context.startActivity(intent);
                }
            });
        }


    }


    @NonNull
    @Override
    public TasksAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        holder.task = taskList.get(position);
        TextView taskTitle = holder.itemView.findViewById(R.id.fragementTitle);
        TextView taskBody = holder.itemView.findViewById(R.id.fragmentBody);
        TextView taskState = holder.itemView.findViewById(R.id.fragmentState);

        taskTitle.setText(holder.task.getTitle());
        taskBody.setText(holder.task.getBody());
        taskState.setText(holder.task.getState());


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
