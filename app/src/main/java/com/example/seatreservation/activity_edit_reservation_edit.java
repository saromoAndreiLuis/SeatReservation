package com.example.seatreservation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_edit_reservation_edit extends AppCompatActivity {
    private Connection db;
    private String reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation_edit);

        db = new Connection(this);

        EditText seatEditText = findViewById(R.id.et_seat);
        EditText nameEditText = findViewById(R.id.et_name);
        EditText eventEditText = findViewById(R.id.et_event_name);
        Button saveButton = findViewById(R.id.btn_save);

        // Get data passed from RecyclerView
        reservationId = getIntent().getStringExtra("id");
        seatEditText.setText(getIntent().getStringExtra("seat_number"));
        nameEditText.setText(getIntent().getStringExtra("name"));
        eventEditText.setText(getIntent().getStringExtra("event_name"));

        saveButton.setOnClickListener(v -> {
            String seat = seatEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String event = eventEditText.getText().toString().trim();

            if (db.updateReservation(reservationId, name, event, seat)) {
                Toast.makeText(this, "Reservation updated successfully", Toast.LENGTH_SHORT).show();
                finish();
                // Handle success
                // For example, navigate back to the previous activity
                // or refresh the reservation list
            } else {
                Toast.makeText(this, "Failed to update reservation", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
