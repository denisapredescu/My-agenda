package com.example.myagenda.activity;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myagenda.R;
import com.example.myagenda.database.Book;
import com.example.myagenda.database.Operations.BookOperastions.BookOperationsInterface;
import com.example.myagenda.database.Operations.BookOperastions.DeleteBookOperation;
import com.example.myagenda.database.Operations.BookOperastions.GetBookOperation;
import com.example.myagenda.database.Operations.BookOperastions.UpdateBookOperation;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MyBookDetailsActivity  extends AppCompatActivity implements BookOperationsInterface {
    SharedPreferences sharedPreferences;
    Button removeButton, moveToFinishedButton;
    ImageView bookImage;
    Book myBook;
    int id = 0;
    final Calendar myCalendar = Calendar.getInstance();
    EditText finishedDateEditText;
    String finishedDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_details);

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        removeButton = findViewById(R.id.removeButton);
        moveToFinishedButton = findViewById(R.id.moveToFinished);
        bookImage = findViewById(R.id.bookImage);
        finishedDateEditText = findViewById(R.id.finishedBookDate);

        onNewIntent(getIntent());

        new GetBookOperation(this).execute(id);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(FindBookDetailsActivity.NOTIFICATION_CHANNEL_ID));
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBook.getRead()) {
                    myBook.setFinishedDate(finishedDateString);
                    new UpdateBookOperation(MyBookDetailsActivity.this).execute(myBook);
                } else {
                    new DeleteBookOperation(MyBookDetailsActivity.this).execute(myBook);
                }
            }
        });

        moveToFinishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                String finishedDate = df.format(c);

                myBook.setRead(true);
                myBook.setFinishedDate(finishedDate);
                new UpdateBookOperation(MyBookDetailsActivity.this).execute(myBook);
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(finishedDateEditText);
            }
        };
        finishedDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MyBookDetailsActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    void insertInfo() {


        if (myBook.getSubtitle().equals("")) {
            ((TextView) findViewById(R.id.bookTitle)).setText(myBook.getTitle());
        } else {
            ((TextView) findViewById(R.id.bookTitle)).setText(myBook.getTitle() +
                    " ~ " + myBook.getSubtitle());
        }

        ((TextView) findViewById(R.id.bookAuthors)).setText(myBook.getAuthors());
        ((TextView) findViewById(R.id.bookDescription)).setText(myBook.getDescription());
        ((TextView) findViewById(R.id.finishedBookDate)).setText("Finished date: " + myBook.getFinishedDate());
        finishedDateString =  myBook.getFinishedDate();

        if (myBook.getIsHttpsImage()) {
            if(Objects.equals(myBook.getThumbnail(), "")) {
                Picasso.get().load(R.drawable.book)
                        .fit()
                        .centerCrop()
                        .into(bookImage);
            } else {
                Picasso.get().load(myBook.getThumbnail())
                        .placeholder(R.drawable.book)
                        .fit()
                        .centerCrop()
                        .into(bookImage);
            }
        } else {
            if (myBook.getThumbnail().equals("")) {
                Picasso.get().load(R.drawable.book)
                        .fit()
                        .centerCrop()
                        .into(bookImage);
            } else {
                Bitmap bitmapImage = StringToBitmap(myBook.getThumbnail());
                bookImage.setImageBitmap(bitmapImage);
            }
        }


        if (myBook.getRead()) {
            finishedDateEditText.setVisibility(View.VISIBLE);
            moveToFinishedButton.setVisibility(View.INVISIBLE);
            removeButton.setText("Save changes");

        } else {
            finishedDateEditText.setVisibility(View.INVISIBLE);
            moveToFinishedButton.setVisibility(View.VISIBLE);

        }

    }

    private void updateLabel(EditText editText) {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        finishedDateString = dateFormat.format(myCalendar.getTime());
        editText.setText("Finished date: " + finishedDateString);
    }

    @Override
    public void insertBookOperationFinished(String response) {

    }

    @Override
    public void updateBookOperationFinished(String response) {
        if (response.equals("success")) {
            Toast.makeText(this, "Change made :)", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Moving failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void deleteBookOperationFinished(String response) {
        if (response.equals("success")) {
            Toast.makeText(this, "Change made :)", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Removing failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getMyBooksOperationFinished(List<Book> books) {

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
        if(book != null) {
            myBook = book;

            insertInfo();
        } else {
            Toast.makeText(this, "Something went wrong... :(", Toast.LENGTH_SHORT).show();
        }
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("myBookId");

    }

    public Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}