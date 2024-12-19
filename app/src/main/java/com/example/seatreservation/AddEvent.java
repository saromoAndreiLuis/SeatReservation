package com.example.seatreservation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEvent extends AppCompatActivity {
    private Connection db;
    private EditText eventNameEditText, eventDateEditText, eventTimeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        db = new Connection(this);

        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDateEditText = findViewById(R.id.eventDateEditText);
        eventTimeEditText = findViewById(R.id.eventTimeEditText);
        Button addEventButton = findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(v -> addEvent());
    }

    private void addEvent() {
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDate = eventDateEditText.getText().toString().trim();
        String eventTime = eventTimeEditText.getText().toString().trim();

        if (eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.addEvent(eventName, eventDate, eventTime);

        if (success) {
            Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

