package com.example.myagenda.database.Operations.UserOperations;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;
import com.example.myagenda.database.User;

public class GetUserOperation extends AsyncTask<String, Void, User> {

    UserOperationsInterface listener;

    public GetUserOperation(UserOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected User doInBackground(String... strings) {
        String email = strings[0];

        return MyApplication.getmAppDatabase().userDao().getUser(email);
    }

    @Override
    protected void onPostExecute(User user) {
        listener.getUserOperationFinished(user);
    }
}