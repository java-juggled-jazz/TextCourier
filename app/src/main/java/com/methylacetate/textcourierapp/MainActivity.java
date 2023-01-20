package com.methylacetate.textcourierapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    Button sendButton;
    EditText textToBot;
    String username, userId, token, text;
    SharedPreferences infoPrefs;
    public final String url =
            "https://api.telegram.org/bot";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        infoPrefs = getSharedPreferences("user", MODE_PRIVATE);
        sendButton = findViewById(R.id.sendButton);
        textToBot = findViewById(R.id.textToBot);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = infoPrefs.getString("username", "").trim();
                userId = infoPrefs.getString("userId", "").trim();
                token = infoPrefs.getString("token", "").trim();
                text = textToBot.getText().toString().trim();
                String[] data = new String[]{url, username, userId, token, text};
                try {
                    sendingCommand(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendingCommand(String[] data) throws IOException {
        String text = "Post by " + data[1] + "\n" + data[4];
        FormBody body = new FormBody.Builder()
                .add("chat_id", data[2])
                .add("text", text)
                .build();
        OkHttpClient client = new OkHttpClient();
        String url = data[0] + data[3] + "/sendMessage";
        Log.d("url", url);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = Objects.requireNonNull(response.body()).string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject responseObject = new JSONObject(responseString);
                                String status = responseObject.getBoolean("ok") ? "Successful" : "Error";
                                Snackbar.make(findViewById(android.R.id.content),
                                        status, Snackbar.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SettingsScreen.class);
        startActivity(intent);
        return true;
    }
}