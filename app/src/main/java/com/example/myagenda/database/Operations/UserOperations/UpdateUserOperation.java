package com.example.myagenda.database.Operations.UserOperations;

import android.os.AsyncTask;

import com.example.myagenda.MyApplication;
import com.example.myagenda.database.Operations.UserOperations.UserOperationsInterface;
import com.example.myagenda.database.User;

public class UpdateUserOperation extends AsyncTask<User, Void, String> {
    UserOperationsInterface listener;

    public UpdateUserOperation(UserOperationsInterface listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(User... users) {
        try {
            MyApplication.getmAppDatabase().userDao().update(users[0]);
        } catch (Exception e) {
            return "error";
        }

        return "success";
    }

    @Override
    protected void onPostExecute(String response) {
        listener.updateUserOperationFinished(response);
    }
}
