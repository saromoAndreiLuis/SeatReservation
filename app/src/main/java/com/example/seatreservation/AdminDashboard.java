package com.example.seatreservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

//        Button viewSeatsButton = findViewById(R.id.view_seats_button);
        Button editReservationsButton = findViewById(R.id.edit_reservations_button);
        Button logoutButton = findViewById(R.id.logout_button);
        ImageButton backButton = findViewById(R.id.backButton);
        Button addEventButton = findViewById(R.id.add_event_button);



//        viewSeatsButton.setOnClickListener(v -> {
//            Intent intent = new Intent(AdminDashboard.this, SeatReservationActivity.class);
//            intent.putExtra("isAdminLoggedIn", true);
//            startActivity(intent);
//            // View reserved seats
//            Toast.makeText(this, "View Seats Clicked", Toast.LENGTH_SHORT).show();
//        });

        editReservationsButton.setOnClickListener(v -> {
            // Edit reservations
            Toast.makeText(this, "Edit Reservations Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboard.this, EditReservationActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            // Logout and redirect to login page
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboard.this, AdminLoginActivity.class);
            startActivity(intent);
            finish();
        });

        backButton.setOnClickListener(v -> {
            finish();
            Intent intent1 = new Intent(this, AdminLoginActivity.class);
            startActivity(intent1);});
            // Close the current activity and return to the previous one

        // Add this button declaration after the existing button declarations

        // Add this click listener after the existing click listeners
        addEventButton.setOnClickListener(v -> {
            // Navigate to AddEventActivity
            Intent intent = new Intent(AdminDashboard.this, AddEvent.class);
            startActivity(intent);
        });
    }
}
