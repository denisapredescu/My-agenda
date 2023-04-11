package com.example.myagenda.database.Operations.BookOperastions;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Book;

import java.util.List;

public class GetFinishedBooksOperation extends AsyncTask<String, Void, List<Book>> {

    BookOperationsInterface listener;

    public GetFinishedBooksOperation(BookOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected List<Book> doInBackground(String... strings) {
        String email = strings[0];

        return MyApplication.getmAppDatabase().bookDao().getFinishedBooks(email);
    }

    @Override
    protected void onPostExecute(List<Book> books) {
        listener.getFinishedBooksOperationFinished(books);
    }
}