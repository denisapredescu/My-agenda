package com.example.myagenda.database.Operations.UserOperations;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;
import com.example.myagenda.database.User;

public class InsertUserOperation extends AsyncTask<User, Void, String> {
    UserOperationsInterface listener;

    public InsertUserOperation(UserOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(User... users) {
        try {
            MyApplication.getmAppDatabase().userDao().insert(users[0]);
        } catch (Exception e) {
            return "error";
        }

        return "success";
    }

    @Override
    protected void onPostExecute(String response) {
        listener.insertUserOperationFinished(response);
    }
}
