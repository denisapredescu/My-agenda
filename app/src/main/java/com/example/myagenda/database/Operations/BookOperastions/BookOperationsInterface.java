package com.example.myagenda.database.Operations.BookOperastions;

import com.example.myagenda.database.Book;

import java.util.List;

public interface BookOperationsInterface {
    void insertBookOperationFinished(String response);  // param: ce primesc inapoi de la functie
    void updateBookOperationFinished(String response);
    void deleteBookOperationFinished(String response);
    void getMyBooksOperationFinished(List<Book> books);
    void getFinishedBooksOperationFinished(List<Book> books);
    void alreadyInOperationFinished(Integer count);
    void getIdOperationFinished(Integer id);
    void getBookOperationFinished(Book book);
}
