package com.example.myagenda.database.Operations.BookOperastions;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Book;

import java.util.List;

public class GetBookOperation extends AsyncTask<Integer, Void, Book> {

    BookOperationsInterface listener;

    public GetBookOperation(BookOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected Book doInBackground(Integer... integers) {
        Integer id = integers[0];

        return MyApplication.getmAppDatabase().bookDao().getBook(id);
    }

    @Override
    protected void onPostExecute(Book book) {
        listener.getBookOperationFinished(book);
    }
}