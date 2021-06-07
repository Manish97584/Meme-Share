package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    String res = null;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.memeImageView);
        loadMeme();
    }
    public void loadMeme()
    {
        progressBar.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                           res=response.getString("url");
                           Glide.with(MainActivity.this).load(res).listener(new RequestListener<Drawable>() {
                               @Override
                               public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                   progressBar.setVisibility(View.GONE);
                                   Toast toast = Toast.makeText(MainActivity.this, "An error occurred!", Toast.LENGTH_LONG);
                                   toast.setGravity(Gravity.CENTER,0, 0);
                                   toast.show();
                                   return false;
                               }

                               @Override
                               public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                   progressBar.setVisibility(View.GONE);
                                   return false;
                               }
                           }).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                       // textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                    }
                });

            // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    public void shareMeme(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"Hey! Check out this meme "+res);
        Intent shareIntent = Intent.createChooser(i,"Share this meme using ...");
        startActivity(shareIntent);
    }

    public void nextMeme(View view) {
        loadMeme();
    }

}