package com.example.seatreservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class EditReservationActivity extends AppCompatActivity {

    private Connection db;
    private List<Map<String, String>> reservations;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        // Initialize database connection
        db = new Connection(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageButton backButton = findViewById(R.id.backButton);


        // Load reservations and set adapter
        loadReservations();
        backButton.setOnClickListener(v -> {
            finish();
            Intent intent1 = new Intent(this, AdminDashboard.class);
            startActivity(intent1);
            // Close the current activity and return to the previous one
        });
    }

    /**
     * Load all reservations from the database and set up the RecyclerView adapter.
     */
    private void loadReservations() {
        reservations = db.getAllReservations();
        adapter = new ReservationAdapter(reservations, new ReservationAdapter.OnReservationClickListener() {
            @Override
            public void onEditClicked(Map<String, String> reservation) {
                Intent intent = new Intent(EditReservationActivity.this, activity_edit_reservation_edit.class);
                intent.putExtra("id", reservation.get("id"));
                intent.putExtra("seat_number", reservation.get("seat_number"));
                intent.putExtra("name", reservation.get("name"));
                intent.putExtra("event_name", reservation.get("event_name"));
                startActivity(intent);
                finish();
            }

            @Override
            public void onDeleteClicked(String id) {
                if (db.deleteReservation(id)) {
                    Toast.makeText(EditReservationActivity.this, "Reservation deleted", Toast.LENGTH_SHORT).show();
                    loadReservations(); // Refresh the list
                } else {
                    Toast.makeText(EditReservationActivity.this, "Failed to delete reservation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
