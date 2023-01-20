package com.methylacetate.textcourierapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class SettingsScreen extends AppCompatActivity {
    String username, userId, token;
    Button saveButton;
    EditText usernameField, userIdField, tokenField;
    SharedPreferences infoPrefs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        usernameField = findViewById(R.id.usernameField);
        userIdField = findViewById(R.id.userIdField);
        tokenField = findViewById(R.id.tokenField);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoPrefs = getSharedPreferences("user", MODE_PRIVATE);
                username = usernameField.getText().toString();
                userId = userIdField.getText().toString();
                token = tokenField.getText().toString();
                if (username.isEmpty()) {
                    Snackbar.make(view, "Enter the username!", Snackbar.LENGTH_SHORT).show();
                } else if (userId.isEmpty()) {
                    Snackbar.make(view, "Enter the user ID!", Snackbar.LENGTH_SHORT).show();
                } else if (token.isEmpty()) {
                    Snackbar.make(view, "Enter the token!", Snackbar.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor infoPrefsEditor = infoPrefs.edit();
                    infoPrefsEditor.putString("username", username);
                    infoPrefsEditor.putString("userId", userId);
                    infoPrefsEditor.putString("token", token);
                    infoPrefsEditor.apply();
                    finish();
                }
            }
        });
    }
}
