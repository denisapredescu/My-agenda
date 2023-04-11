package com.example.myagenda.database.Operations.BookOperastions;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Book;

public class DeleteBookOperation extends AsyncTask<Book, Void, String> {
    BookOperationsInterface listener;

    public DeleteBookOperation(BookOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(Book... books) {
        try {
            MyApplication.getmAppDatabase().bookDao().delete(books[0]);
        } catch (Exception e) {
            return "error";
        }

        return "success";
    }

    @Override
    protected void onPostExecute(String response) {
        listener.deleteBookOperationFinished(response);
    }
}
