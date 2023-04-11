package com.example.myagenda.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String title;
    @NonNull
    private String subtitle;
    @NonNull
    private String authors;
    @NonNull
    private String description;
    @NonNull
    private String thumbnail;
    @NonNull
    private boolean isHttpsImage;
    @NonNull
    private String email;
    @NonNull
    private Boolean isRead;
    @NonNull
    private  String finishedDate;

    public Book(@NonNull String title, @NonNull String subtitle, @NonNull String authors, @NonNull String description, @NonNull String thumbnail,@NonNull boolean isHttpsImage, @NonNull String email, @NonNull Boolean isRead) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.description = description;
        this.thumbnail = thumbnail;
        this.isHttpsImage = isHttpsImage;
        this.email = email;
        this.isRead = isRead;
        this.finishedDate = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(@NonNull String subtitle) {
        this.subtitle = subtitle;
    }

    @NonNull
    public String getAuthors() {
        return authors;
    }

    public void setAuthors(@NonNull String authors) {
        this.authors = authors;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(@NonNull String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public Boolean getRead() {
        return isRead;
    }

    public void setRead(@NonNull Boolean read) {
        isRead = read;
    }

    @NonNull
    public String getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(@NonNull String finishedDate) {
        this.finishedDate = finishedDate;
    }

    public boolean getIsHttpsImage() {
        return isHttpsImage;
    }

    public void setIsHttpsImage(boolean httpsImage) {
        isHttpsImage = httpsImage;
    }
}
