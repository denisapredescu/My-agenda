package com.example.myagenda.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.myagenda.R;
import com.example.myagenda.database.Book;
import com.example.myagenda.database.Operations.BookOperastions.BookOperationsInterface;
import com.example.myagenda.database.Operations.BookOperastions.GetIdOperation;
import com.example.myagenda.database.Operations.BookOperastions.InsertBookOperation;

import org.jetbrains.annotations.Nullable;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddBookActivity extends AppCompatActivity implements BookOperationsInterface {

    private final int CAMERA_PERMISSION_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;

    private static String CHANNEL_ID = "10001";
    private static String NOTIFICATION_CHANNEL_ID  = "10001";

    public static String NOTIFICATION_CHANNEL_NAME  = "NOTIFICATION_CHANNEL_NAME";
    SharedPreferences sharedPreferences;

    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private ImageView image;
    private Button cameraButton;
    private Button saveButton;
    Bitmap thumbnail = null;
    int bookId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        createNotificationChannel();

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        bookTitle = findViewById(R.id.title);
        bookAuthor = findViewById(R.id.author);
        bookDescription = findViewById(R.id.description);
        image = findViewById(R.id.image);
        cameraButton = findViewById(R.id.btn_camera);
        saveButton = findViewById(R.id.btn_save);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        AddBookActivity.this,
                        android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, CAMERA_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(
                            AddBookActivity.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_CODE
                    );
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Book addBook = new Book(
                        bookTitle.getText().toString(),
                        "",
                        bookAuthor.getText().toString(),
                        bookDescription.getText().toString(),
                        BitMapToString(thumbnail),
                        false,
                        sharedPreferences.getString("email", ""),
                        false
                );
                new InsertBookOperation(AddBookActivity.this).execute(addBook);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(i);
            } else {
                Toast.makeText(this, "You just denied the permission for camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                thumbnail = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(thumbnail);
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        if (bitmap == null)
            return "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

    @Override
    public void insertBookOperationFinished(String response) {
        if (response.equals("success")) {
            new GetIdOperation(AddBookActivity.this)
                    .execute(bookTitle.getText().toString(),
                            bookAuthor.getText().toString(),
                            bookDescription.getText().toString(),
                            sharedPreferences.getString("email", ""));

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

    }

    @Override
    public void getIdOperationFinished(Integer id) {
        bookId = id;
        sendNotification();
    }

    @Override
    public void getBookOperationFinished(Book book) {

    }
}
