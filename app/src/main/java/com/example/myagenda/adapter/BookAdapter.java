package com.example.myagenda.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myagenda.OnItemClickListener;
import com.example.myagenda.R;
import com.example.myagenda.database.Book;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> localDataSet;

    private static OnItemClickListener itemClickListener;
    private static String option;

    public BookAdapter(List<Book> localDataSet, OnItemClickListener itemClickListener, String option) {
        this.localDataSet = localDataSet;
        this.itemClickListener = itemClickListener;
        this.option = option;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.bind(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView bookSubtitle;
        private final TextView bookAuthors;
        private final ImageView bookImage;
        private final Button removeButton;
        private final Button moveToFinishedButton;
        private final CardView layout;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.bookTitle);
            bookSubtitle = itemView.findViewById(R.id.bookSubtitle);
            bookAuthors = itemView.findViewById(R.id.bookAuthors);
            bookImage = itemView.findViewById(R.id.bookImage);

            removeButton = itemView.findViewById(R.id.deleteBook);
            moveToFinishedButton = itemView.findViewById(R.id.moveToFinished);
//            finishedDate = itemView.findViewById(R.id.finishedBookDate);

            layout = itemView.findViewById(R.id.container);

            if(Objects.equals(option, "finished")) {
                removeButton.setVisibility(View.INVISIBLE);
                moveToFinishedButton.setVisibility(View.INVISIBLE);
            } else if (Objects.equals(option, "my books"))  {
                removeButton.setVisibility(View.VISIBLE);
                moveToFinishedButton.setVisibility(View.VISIBLE);
            }
        }

        public void bind(Book item) {
            title.setText(item.getTitle());
            bookSubtitle.setText(item.getSubtitle());
            bookAuthors.setText(item.getAuthors());

            if (Objects.equals(item.getThumbnail(), "")) {
                Picasso.get().load(R.drawable.book)
                        .fit()
                        .centerCrop()
                        .into(bookImage);
            } else {
                Picasso.get().load(item.getThumbnail())
                        .placeholder(R.drawable.book)
                        .fit()
                        .centerCrop()
                        .into(bookImage);
            }

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(item, "remove");
                }
            });

            moveToFinishedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(item, "move");
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(item, "container");
                }
            });
        }
    }
}
