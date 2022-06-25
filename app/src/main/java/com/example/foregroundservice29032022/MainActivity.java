package com.example.foregroundservice29032022;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnCreateNotification, btnStartForeground, btnStopForeground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateNotification = findViewById(R.id.button_create_notification);
        btnStartForeground = findViewById(R.id.button_start_foreground_service);
        btnStopForeground = findViewById(R.id.button_stop_foreground_service);

        btnCreateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
