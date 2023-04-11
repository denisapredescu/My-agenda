package com.example.myagenda.database.Operations.UserOperations;

import com.example.myagenda.database.User;

public interface UserOperationsInterface {
    void insertUserOperationFinished(String response);  // param: ce primesc inapoi de la functie

    void updateUserOperationFinished(String response);

    void existsUserOperationFinished(Integer response);

    void getUserOperationFinished(User user);
}
