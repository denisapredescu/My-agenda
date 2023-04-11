package com.example.myagenda.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myagenda.adapter.BookAdapter;
import com.example.myagenda.OnItemClickListener;
import com.example.myagenda.activity.AddBookActivity;

import com.example.myagenda.R;
import com.example.myagenda.activity.FindBookDetailsActivity;
import com.example.myagenda.activity.MyBookDetailsActivity;
import com.example.myagenda.database.Book;
import com.example.myagenda.database.Operations.BookOperastions.BookOperationsInterface;
import com.example.myagenda.database.Operations.BookOperastions.DeleteBookOperation;
import com.example.myagenda.database.Operations.BookOperastions.GetMyBooksOperation;
import com.example.myagenda.database.Operations.BookOperastions.UpdateBookOperation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyBooksFragment extends Fragment  implements OnItemClickListener, BookOperationsInterface {
    SharedPreferences sharedPreferences;
    RecyclerView rv;
    public static List<Book> bookList = new ArrayList<>();
    public static String BOOK = "book";

    public MyBooksFragment() {
        super(R.layout.fragment_my_books);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_books, null);

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(FindBookDetailsActivity.NOTIFICATION_CHANNEL_ID));

        FloatingActionButton addItem = view.findViewById(R.id.add_item);

        rv = view.findViewById(R.id.recycler_view);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        new GetMyBooksOperation(this).execute(sharedPreferences.getString("email", ""));

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddBookActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void insertBookOperationFinished(String response) {

    }

    @Override
    public void updateBookOperationFinished(String response) {
        if (response.equals("success")) {
            new GetMyBooksOperation(this).execute(sharedPreferences.getString("email",""));

        } else {
            Toast.makeText(getActivity(), "Moving failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void deleteBookOperationFinished(String response) {
        if (response.equals("success")) {
            new GetMyBooksOperation(this).execute(sharedPreferences.getString("email",""));

        } else {
            Toast.makeText(getActivity(), "Removing failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getMyBooksOperationFinished(List<Book> books) {
        bookList = books;

        BookAdapter adapter = new BookAdapter(bookList, this, "my books");
        rv.setAdapter(adapter);
    }

    @Override
    public void getFinishedBooksOperationFinished(List<Book> books) {

    }

    @Override
    public void alreadyInOperationFinished(Integer count) {

    }

    @Override
    public void getIdOperationFinished(Integer id) {

    }

    @Override
    public void getBookOperationFinished(Book book) {

    }

    @Override
    public void onItemClick(Book item, String option) {
        if (Objects.equals(option, "move")) {
            item.setRead(true);
            new UpdateBookOperation(this).execute(item);
        } else if (Objects.equals(option, "remove")) {
            new DeleteBookOperation(this).execute(item);
        } else {
            Bundle bundle = new Bundle();
            int id = item.getId();
            bundle.putInt("myBookId", id);
            Intent intent = new Intent(getActivity(), MyBookDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}