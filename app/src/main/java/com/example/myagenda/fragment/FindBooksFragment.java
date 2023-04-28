package com.example.myagenda.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myagenda.adapter.ApiBookAdapter;
import com.example.myagenda.BookInfo;
import com.example.myagenda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FindBooksFragment extends Fragment {
    private EditText bookInput;
    private Button searchButton;
    private RequestQueue mRequestQueue;
    private ArrayList<BookInfo> bookInfoArrayList;
    private RecyclerView rv;
    private final String HTTPS = "https://www.googleapis.com/books/v1/volumes?q=";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_find, null);

        bookInput = view.findViewById(R.id.bookInput);
        searchButton = view.findViewById(R.id.searchButton);
        rv = view.findViewById(R.id.recycler_view);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookInput.getText().toString().isEmpty()) {
                    bookInput.setError("Please enter search query");
                    return;
                }
                // if the search query is not empty then we are
                // calling get book info method to load all
                // the books from the API.
                getBooksInfo(bookInput.getText().toString());
            }
        });
        return view;
    }

    private void getBooksInfo(String query) {
        bookInfoArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        mRequestQueue.getCache().clear();
        String url = HTTPS + query;
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        String subtitle = volumeObj.optString("subtitle");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String thumbnail = "";
                        if (imageLinks != null) {
                             thumbnail = imageLinks.optString("thumbnail");
                        }

                        String previewLink = volumeObj.optString("previewLink");

                        List<String> authorsList = new ArrayList<>();
                        if (authorsArray.length() != 0) {
                            for (int j = 0; j < authorsArray.length(); j++) {
                                authorsList.add(authorsArray.optString(i));
                            }
                        }

                        BookInfo bookInfo = new BookInfo(title, subtitle, authorsList, publisher, publishedDate, description, pageCount, thumbnail, previewLink);
                        bookInfoArrayList.add(bookInfo);
                        ApiBookAdapter adapter = new ApiBookAdapter(bookInfoArrayList, getActivity());
                        rv.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No Data Found, try other name", Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error found is " + error, Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(booksObjrequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}