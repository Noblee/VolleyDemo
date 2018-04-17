package com.noble.demo.volley.volleydemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private String path;
    private VideoView mVideoView;
    private RequestQueue queue;
    private Cache cache;
    private Network network;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mImageView = (ImageView) findViewById(R.id.iv);
        mTextView.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
        initLive();
        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        network = new BasicNetwork(new HurlStack());
        queue = Volley.newRequestQueue(this);
    }

    private void initLive() {
        Vitamio.isInitialized(getApplicationContext());
        mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
        path = "rtmp://10.41.45.6:1935/zbcs/room";
        mVideoView.setVideoPath(path);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
    }

    public void Request_String(View view) {
        mImageView.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://nobler.me/android/string";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTextView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

    public void Request_JSON(View view) {
        mImageView.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
        String url = "http://nobler.me/android/json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTextView.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void Request_Image(View view) {
        String url = "http://nobler.me/android/image.png";
        mImageView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
        ImageRequest imageRequest = new ImageRequest(
                url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // TODO Auto-generated method stub
                mImageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, null
        );
        queue.add(imageRequest);
    }
}
