package com.osaid.taskmaster;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task")
    List<Task> getAllTasks();


    @Query("SELECT * FROM task WHERE id = (:id)")
    List<Task> getTaskById(long id);


    @Insert
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);


}
