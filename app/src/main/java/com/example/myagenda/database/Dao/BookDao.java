package com.example.myagenda.database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myagenda.database.Book;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);
    @Update
    void update(Book book);
    @Delete
    void delete(Book book);
    @Query("SELECT * FROM book WHERE email LIKE :email and isRead LIKE 0")
    List<Book> getToReadBooks(String email);
    @Query("SELECT * FROM book WHERE email LIKE :email and isRead LIKE 1")
    List<Book> getFinishedBooks(String email);
    @Query("SELECT Count() FROM book WHERE email LIKE :email and title LIKE :title and authors LIKE :authors and description LIKE :description")
    Integer alreadyIn(String title, String authors, String description, String email);
    @Query("SELECT * FROM book WHERE id LIKE :id")
    Book getBook(int id);
    @Query("SELECT id FROM book WHERE email LIKE :email and title LIKE :title and authors LIKE :authors and description LIKE :description")
    Integer getId(String title, String authors, String description, String email);
}
