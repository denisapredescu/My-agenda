package com.example.myagenda.database.Operations.BookOperastions;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Book;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;
import com.example.myagenda.database.User;

public class InsertBookOperation extends AsyncTask<Book, Void, String> {
    BookOperationsInterface listener;

    public InsertBookOperation(BookOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Book... books) {
        try {
            MyApplication.getmAppDatabase().bookDao().insert(books[0]);

        } catch (Exception e) {
            return "error";
        }

        return "success";
    }

    @Override
    protected void onPostExecute(String response) {
        listener.insertBookOperationFinished(response);
    }
}
