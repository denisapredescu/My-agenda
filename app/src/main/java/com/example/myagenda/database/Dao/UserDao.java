package com.example.myagenda.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myagenda.database.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);
    @Update
    void update(User user);
    @Query("SELECT count(*) FROM user WHERE email LIKE :email and password LIKE :password LIMIT 1")
    Integer existsUser(String email, String password);
    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    User getUser(String email);
}
