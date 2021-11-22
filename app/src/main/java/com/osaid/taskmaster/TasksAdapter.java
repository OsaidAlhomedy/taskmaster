package com.osaid.taskmaster;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<Task> taskOGList = new ArrayList<>();
    private final Context context;

    public TasksAdapter(Context context, List<Task> taskOGList) {
        this.context = context;
        this.taskOGList = taskOGList;
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private Task taskOG;
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

                    String title = taskOGList.get(position).getTitle();
                    String body = taskOGList.get(position).getBody();
                    String state = taskOGList.get(position).getState();
                    String url = taskOGList.get(position).getFileUrl();
                    String id = taskOGList.get(position).getId();
                    double longitude = taskOGList.get(position).getLongitude();
                    double latitude = taskOGList.get(position).getLatitude();

                    intent.putExtra("taskTitle", title);
                    intent.putExtra("taskBody", body);
                    intent.putExtra("taskState", state);
                    intent.putExtra("taskURL", url);
                    intent.putExtra("taskIDID", id);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("latitude",latitude);
                    Log.d("ADAPTER", "Position : " + position + "  title : " + title + " ID : " + id + "URL => " + url);
                    eventRecord1();
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

        holder.taskOG = taskOGList.get(position);
        TextView taskTitle = holder.itemView.findViewById(R.id.fragementTitle);
        TextView taskBody = holder.itemView.findViewById(R.id.fragmentBody);
        TextView taskState = holder.itemView.findViewById(R.id.fragmentState);

        taskTitle.setText(holder.taskOG.getTitle());
        taskBody.setText(holder.taskOG.getBody());
        taskState.setText(holder.taskOG.getState());


    }

    @Override
    public int getItemCount() {
        return taskOGList.size();
    }

    public List<Task> getTaskList() {
        return taskOGList;
    }

    public void setTaskOGList(List<Task> taskOGList) {
        this.taskOGList = taskOGList;
    }

    private void eventRecord1() {

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("User Launched Task Details Activity")
                .build();

        Amplify.Analytics.recordEvent(event);

    }

}
