package com.example.seatreservation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import java.util.List;
import java.util.Map;

public class SeatReservationActivity extends AppCompatActivity {
    private Connection db; // Database connection
    private GridLayout gridLayout;
    private TextView seatDetailsTextView; // TextView to display seat details
    private int clickCount = 0; // for admin btn clicks
    private boolean isAdminLoggedIn = false; // Flag to indicate if admin is logged in
    private int eventId; // Event ID passed from the previous activity
    private String eventIdString;

    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_generator);

        db = new Connection(this); // Initialize database connection
        gridLayout = findViewById(R.id.gridLayout);
        ImageButton logoAdminImgBtn = findViewById(R.id.logo_admin_imgBtn);
        seatDetailsTextView = findViewById(R.id.seatDetailsTextView); // Reference TextView from the layout
        ImageButton logoutButton = findViewById(R.id.logout_button);
        ImageButton backButton = findViewById(R.id.back);

        try {
            Intent intent = getIntent();
            Log.d("SeatReservationActivity", "Intent content: " + intent);
            eventId = Integer.parseInt(intent.getStringExtra("event_id")); // Replace 1 with a default valid event ID
          //  Toast.makeText(this, ""+intent.getIntExtra("event_id", ), Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        if (eventId <= -1) {
            Toast.makeText(this, "Invalid event", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

//        Intent intent2 = getIntent();
//        String eventId = String.valueOf(intent2.getStringExtra("event_id"));



        // Check if admin is logged in
        isAdminLoggedIn = getIntent().getBooleanExtra("isAdminLoggedIn", false);

        // Configure logout button visibility
        if (isAdminLoggedIn) {
            Toast.makeText(this, "Admin mode enabled", Toast.LENGTH_SHORT).show();
            logoutButton.setVisibility(View.VISIBLE); // reveal the buttons
            backButton.setVisibility(View.VISIBLE);
        } else {
            logoutButton.setVisibility(View.GONE); // hide the buttons
            backButton.setVisibility(View.GONE);
        }

        // Set up logout button click listener
        logoutButton.setOnClickListener(v -> {
            isAdminLoggedIn = false;
            logoutButton.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            Toast.makeText(this, "Logged out as admin", Toast.LENGTH_SHORT).show();
            Intent intent3 = new Intent(this, SeatReservationActivity.class);
            startActivity(intent3);
            finish();
        });

        backButton.setOnClickListener(v -> {
            finish();
            Intent intent1 = new Intent(this, AdminDashboard.class);
            startActivity(intent1); // Close the current activity and return to the previous one
        });

        logoAdminImgBtn.setOnClickListener(v -> {
            clickCount++;
            // Show a toast message on each click
            //Toast.makeText(this, "Clicked " + clickCount + " times", Toast.LENGTH_SHORT).show();

            // Redirect to the admin login page after 4 clicks if not logged in as admin
            if (!isAdminLoggedIn && clickCount == 4) {
                Intent intent4 = new Intent(SeatReservationActivity.this, AdminLoginActivity.class);
                startActivity(intent4);
                finish();
            }
        });

        createSeats();
    }

    private void editReservation(String seat) {
        // Allow admin to edit the reservation
        Toast.makeText(this, "Editing reservation for seat: " + seat, Toast.LENGTH_SHORT).show();

        // Redirect to an edit reservation activity (to be implemented)
        Intent intent = new Intent(this, activity_edit_reservation_edit.class);
        intent.putExtra("seat", seat);
        startActivity(intent);
    }

    private void createSeats() {
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int numRows = rows.length;
        int numColumns = 8;

        // Fetch reserved seats for the selected event
        String eventName = db.getEventName(String.valueOf(eventId));
        List<String> reservedSeats = db.getTakenSeats(String.valueOf(eventName));
        //Toast.makeText(this, "Event Id: "+ eventId, Toast.LENGTH_SHORT).show(); SOLVED

        gridLayout.setRowCount(numRows);
        gridLayout.setColumnCount(numColumns);

        for (int x = 0; x < numRows; x++) {
            for (int y = 0; y < numColumns; y++) {
                final String seat = rows[x] + (y + 1);
                Button button = new Button(this);
                button.setText(seat);
                button.setTextSize(10);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(x), GridLayout.spec(y)
                );
                params.width = 105;
                params.height = 120;
                params.setMargins(10, 15, 10, 15);
                button.setLayoutParams(params);

                if (reservedSeats.contains(seat)) {
                    button.setBackgroundColor(Color.RED); // Reserved
                    button.setOnClickListener(v -> showSeatDetails(seat));
                } else {
                    button.setBackgroundColor(Color.GREEN); // Available
                    button.setOnClickListener(v -> seatClicked(seat, button));
                }

                gridLayout.addView(button);
            }
        }
    }

    private void showSeatDetails(String seat) {
        // Fetch details of the reserved seat from the database
        Map<String, String> seatDetails = db.getSeatDetails(seat); // A method to fetch seat details by seat number

        if (seatDetails != null) {
            String name = seatDetails.get("name");
            String eventName = seatDetails.get("event_name");

            // Display seat details in the TextView
            seatDetailsTextView.setText(
                    "Seat: " + seat + "\n" +
                            "Name: " + name + "\n" +
                            "Event: " + eventName
            );
        } else {
            seatDetailsTextView.setText("No details available for this seat.");
        }
    }

    public void seatClicked(String seat, Button button) {

        if (!isAdminLoggedIn) {
            button.setBackgroundColor(Color.YELLOW); // Highlight selected seat

            // For simplicity, update the TextView instead of redirecting
            seatDetailsTextView.setText("Selected seat: " + seat);


try{
            // Redirect to registration activity
            Intent intent = new Intent(SeatReservationActivity.this, RegisterSeat.class);
            intent.putExtra("event_id", eventId); // Pass the event_id
            intent.putExtra("seat", seat); // Pass the selected seat

            startActivity(intent);
            finish();
}
catch (RuntimeException e) {
    throw new RuntimeException(e);
}
        } else {
            editReservation(seat);
        }
    }

    private boolean isSeatTaken(String seat) {
        // Query the database for taken seats
        List<String> reservedSeats = db.getTakenSeats(String.valueOf(eventId)); // Retrieve a list of reserved seats from the database
        return reservedSeats.contains(seat); // Check if the given seat is in the reserved list
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
