package com.example.perfectfitapp_android.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("select * from User")
    User getUserRoom();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

//    @Delete
//    void delete(User user);

    @Query("DELETE FROM User WHERE email = :userEmail")
    void deleteByUserEmail(String userEmail);
}
