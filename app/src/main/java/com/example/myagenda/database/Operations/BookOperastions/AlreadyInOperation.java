package com.example.myagenda.database.Operations.BookOperastions;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;

public class AlreadyInOperation extends AsyncTask<String, Void, Integer> {

    BookOperationsInterface listener;

    public AlreadyInOperation(BookOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String title = strings[0];
        String authors = strings[1];
        String description = strings[2];
        String email = strings[3];

        return MyApplication.getmAppDatabase().bookDao().alreadyIn(title, authors, description, email);
    }

    @Override
    protected void onPostExecute(Integer count) {
        listener.alreadyInOperationFinished(count);
    }
}