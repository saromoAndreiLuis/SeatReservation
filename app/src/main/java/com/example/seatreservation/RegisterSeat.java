package com.example.seatreservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterSeat extends AppCompatActivity {

    private EditText editTextName;
    private TextView seatTextView;
    private TextView eventNameTextView;
    private Button buttonRegister;
    private Button buttonReset;
    private String seat;
    private int eventId;
    private Connection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seat);

        // Initialize UI components
        seatTextView = findViewById(R.id.seatTextView);
        eventNameTextView = findViewById(R.id.eventNameTextView);
        editTextName = findViewById(R.id.editTextName);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonReset = findViewById(R.id.buttonReset);
        ImageButton backButton = findViewById(R.id.back);

        // Initialize database connection
        db = new Connection(this);

        // Retrieve seat value and event ID from Intent
        Intent intent = getIntent();
        seat = intent.getStringExtra("seat");
        eventId = intent.getIntExtra("event_id", -1);

        if (seat != null) {
            seatTextView.setText(seat);
        } else {
            seatTextView.setText("No seat selected");
        }

        if (eventId >= 0) {
            String eventName = db.getEventName(String.valueOf(eventId));
            eventNameTextView.setText(eventName);
        } else {
            eventNameTextView.setText("No event selected");
        }

        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(RegisterSeat.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                registerSeat(name);
            }
        });

        buttonReset.setOnClickListener(v -> {
            editTextName.setText("");
            Toast.makeText(RegisterSeat.this, "Form Reset", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> finish());
    }

//    register(String name, String eventName, String seat, int eventId)
    private void registerSeat(String name) {
        String eventName = db.getEventName(String.valueOf(eventId));

        boolean isRegistered = db.register(name, eventName, seat, eventId);
        if (isRegistered) {
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SeatReservationActivity.class);
            intent.putExtra("event_id", Integer.toString(eventId));
            finish();
            startActivity(intent);

        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show();
        }
    }
}