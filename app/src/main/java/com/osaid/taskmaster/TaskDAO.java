package com.osaid.taskmaster;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM TaskOG")
    List<TaskOG> getAllTasks();


    @Query("SELECT * FROM TaskOG WHERE id = (:id)")
    List<TaskOG> getTaskById(long id);


    @Insert
    void insertTask(TaskOG taskOG);

    @Delete
    void deleteTask(TaskOG taskOG);


}
