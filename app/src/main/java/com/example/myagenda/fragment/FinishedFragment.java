package com.example.myagenda.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myagenda.R;
import com.example.myagenda.adapter.BookAdapter;
import com.example.myagenda.OnItemClickListener;
import com.example.myagenda.activity.MyBookDetailsActivity;
import com.example.myagenda.database.Book;
import com.example.myagenda.database.Operations.BookOperastions.BookOperationsInterface;
import com.example.myagenda.database.Operations.BookOperastions.GetFinishedBooksOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FinishedFragment extends Fragment implements OnItemClickListener, BookOperationsInterface {
    SharedPreferences sharedPreferences;
    RecyclerView rv;
    public static List<Book> bookList = new ArrayList<>();

    public FinishedFragment() {
        super(R.layout.fragment_finished);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finished, null);


        rv = view.findViewById(R.id.recycler_view);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        new GetFinishedBooksOperation(this).execute(sharedPreferences.getString("email", ""));

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

    }

    @Override
    public void deleteBookOperationFinished(String response) {

    }

    @Override
    public void getMyBooksOperationFinished(List<Book> books) {

    }

    @Override
    public void getFinishedBooksOperationFinished(List<Book> books) {
        bookList = books;

        BookAdapter adapter = new BookAdapter(bookList, this, "finished");
        rv.setAdapter(adapter);
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
        if (Objects.equals(option, "container"))  {
            Bundle bundle = new Bundle();
            int id = item.getId();
            bundle.putInt("myBookId", id);
            Intent intent = new Intent(getActivity(), MyBookDetailsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}