package com.example.myagenda.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.myagenda.R;
import com.example.myagenda.database.Book;
import com.example.myagenda.database.Operations.BookOperastions.AlreadyInOperation;
import com.example.myagenda.database.Operations.BookOperastions.BookOperationsInterface;
import com.example.myagenda.database.Operations.BookOperastions.GetIdOperation;
import com.example.myagenda.database.Operations.BookOperastions.InsertBookOperation;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Objects;

public class FindBookDetailsActivity extends AppCompatActivity implements BookOperationsInterface {
    public static String NOTIFICATION_CHANNEL_ID  = "10001";
    public static String NOTIFICATION_CHANNEL_NAME  = "NOTIFICATION_CHANNEL_NAME";
    private SharedPreferences sharedPreferences;
    private Button previewButton, saveInMyBooksButton;
    private ImageView bookImage;
    private String title, subtitle, publisher, publishedDate, description, thumbnail, authors, previewLink;
    int pageCount;
    private Integer bookId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_book_details);

        createNotificationChannel();
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        previewButton = findViewById(R.id.previewButton);
        saveInMyBooksButton = findViewById(R.id.saveInMyBooksButton);
        bookImage = findViewById(R.id.bookImage);
        insertInfo();

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewLink.isEmpty()) {
                    Toast.makeText(FindBookDetailsActivity.this, "No preview Link present", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening that link via an intent.
                Uri uri = Uri.parse(previewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        saveInMyBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save in database, send notification and go to My Books Page
                if(!sharedPreferences.getBoolean("logged", false)) {
                    Toast.makeText(FindBookDetailsActivity.this, "Login first!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindBookDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    new AlreadyInOperation(FindBookDetailsActivity.this).execute(title, authors, description, sharedPreferences.getString("email", ""));
                }
            }
        });
    }

    void insertInfo() {
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        authors = getIntent().getStringExtra("authors");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");

        ((TextView) findViewById(R.id.bookTitle)).setText(title);
        ((TextView) findViewById(R.id.bookSubtitle)).setText(subtitle);
        ((TextView) findViewById(R.id.bookAuthors)).setText(authors);
        ((TextView) findViewById(R.id.bookPublisher)).setText(publisher);
        ((TextView) findViewById(R.id.bookDescription)).setText(description);
        ((TextView) findViewById(R.id.bookNoOfPage)).setText("No Of Pages : " + pageCount);
        ((TextView) findViewById(R.id.bookPublishDate)).setText("Published On : " + publishedDate);

        if(Objects.equals(thumbnail, "")) {
            Picasso.get().load(R.drawable.book)
                    .fit()
                    .centerCrop()
                    .into(bookImage);
        } else {
            Picasso.get().load(thumbnail)
                    .placeholder(R.drawable.book)
                    .fit()
                    .centerCrop()
                    .into(bookImage);
        }

    }

    @Override
    public void insertBookOperationFinished(String response) {
        if (response.equals("success")) {
            new GetIdOperation(FindBookDetailsActivity.this).execute(title, authors, description, sharedPreferences.getString("email", ""));

        } else {
            Toast.makeText(this, "Insert failed", Toast.LENGTH_LONG).show();
        }
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

    }

    @Override
    public void alreadyInOperationFinished(Integer count) {
        if (count == 0) {
            Book addedBook = new Book(
                    title,
                    subtitle,
                    authors,
                    description,
                    thumbnail,
                    true,
                    sharedPreferences.getString("email", ""),
                    false
            );
            new InsertBookOperation(FindBookDetailsActivity.this).execute(addedBook);

        } else {
            Toast.makeText(this, "Already in your list!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getIdOperationFinished(Integer id) {
        bookId = id;
        sendNotification();

    }

    @Override
    public void getBookOperationFinished(Book book) {

    }

    private void sendNotification() {
        Intent intent = new Intent(this, MyBookDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);


        Intent myBooksFragmentIntent = new Intent(this, MyBookDetailsActivity.class);
        myBooksFragmentIntent.putExtra("myBookId", bookId);
        PendingIntent myListPendingIntent =
                PendingIntent.getActivity(this, 2, myBooksFragmentIntent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        int id = 1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("MyAgenda")
                .setContentText("Added a new book in your list")
                .setTicker( "Notification Listener" )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_background, "Go to My Books", myListPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(id, builder.build());   // verifica daca am permission

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("chanel description");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}