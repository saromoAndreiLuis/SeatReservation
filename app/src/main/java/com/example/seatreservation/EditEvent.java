package com.example.seatreservation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class EditEvent extends AppCompatActivity {
    private Connection db;
    private EditText eventNameEditText, eventDateEditText, eventTimeEditText;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        db = new Connection(this);

        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDateEditText = findViewById(R.id.eventDateEditText);
        eventTimeEditText = findViewById(R.id.eventTimeEditText);
        Button updateEventButton = findViewById(R.id.updateEventButton);
        Button deleteEventButton = findViewById(R.id.deleteEventButton);

        eventId = getIntent().getStringExtra("eventId");
        loadEventDetails();

        updateEventButton.setOnClickListener(v -> updateEvent());
        deleteEventButton.setOnClickListener(v -> deleteEvent());
    }

    private void loadEventDetails() {
        Map<String, String> eventDetails = (Map<String, String>) db.getEventsById(eventId);
        if (eventDetails != null) {
            eventNameEditText.setText(eventDetails.get("event_name"));
            eventDateEditText.setText(eventDetails.get("event_date"));
            eventTimeEditText.setText(eventDetails.get("event_time"));
        }
    }

    private void updateEvent() {
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDate = eventDateEditText.getText().toString().trim();
        String eventTime = eventTimeEditText.getText().toString().trim();

        if (eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.updateEvent(Integer.parseInt(eventId), eventName, eventDate, eventTime);

        if (success) {
            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteEvent() {
        boolean success = db.deleteEvent(eventId);

        if (success) {
            Toast.makeText(this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete event", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

