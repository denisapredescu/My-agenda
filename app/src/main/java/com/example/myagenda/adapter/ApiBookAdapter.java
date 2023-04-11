package com.example.myagenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myagenda.BookInfo;
import com.example.myagenda.R;
import com.example.myagenda.activity.FindBookDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ApiBookAdapter extends RecyclerView.Adapter<ApiBookAdapter.BookViewHolder> {
    private ArrayList<BookInfo> bookInfoArrayList;
    private Context mcontext;

    public ApiBookAdapter(ArrayList<BookInfo> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.api_item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookInfo bookInfo = bookInfoArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + bookInfo.getPageCount());
        holder.dateTV.setText(bookInfo.getPublishedDate());

        if (Objects.equals(bookInfo.getThumbnail(), "")) {
            Picasso.get().load(R.drawable.book)
                    .fit()
                    .centerCrop()
                    .into(holder.bookIV);
        } else {
            Picasso.get().load(bookInfo.getThumbnail())
                    .placeholder(R.drawable.book)
                    .fit()
                    .centerCrop()
                    .into(holder.bookIV);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save info and go to another page
                Intent i = new Intent(mcontext, FindBookDetailsActivity.class);

                i.putExtra("title", bookInfo.getTitle());
                i.putExtra("subtitle", bookInfo.getSubtitle());
                i.putExtra("authors", bookInfo.getAuthors());
                i.putExtra("publisher", bookInfo.getPublisher());
                i.putExtra("publishedDate", bookInfo.getPublishedDate());
                i.putExtra("description", bookInfo.getDescription());
                i.putExtra("pageCount", bookInfo.getPageCount());
                i.putExtra("thumbnail", bookInfo.getThumbnail());
                i.putExtra("previewLink", bookInfo.getPreviewLink());

                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookInfoArrayList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;

        public BookViewHolder(View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.bookTitle);
            publisherTV = itemView.findViewById(R.id.bookPublisher);
            pageCountTV = itemView.findViewById(R.id.bookPageCount);
            dateTV = itemView.findViewById(R.id.bookDate);
            bookIV = itemView.findViewById(R.id.bookImage);
        }
    }
}
