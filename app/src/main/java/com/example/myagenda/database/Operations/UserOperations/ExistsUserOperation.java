package com.example.myagenda.database.Operations.UserOperations;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;

public class ExistsUserOperation extends AsyncTask<String, Void, Integer> {

    UserOperationsInterface listener;

    public ExistsUserOperation(UserOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String email = strings[0];
        String password = strings[1];

        return MyApplication.getmAppDatabase().userDao().existsUser(email, password);
    }

    @Override
    protected void onPostExecute(Integer response) {
        listener.existsUserOperationFinished(response);
    }
}