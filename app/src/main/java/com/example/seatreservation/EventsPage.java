package com.example.seatreservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class EventsPage extends AppCompatActivity {
    private Connection db;
    private EventsAdapter adapter;
    private List<Map<String, String>> events;
    int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);

        db = new Connection(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageButton logoAdminImgBtn = findViewById(R.id.logo_admin_imgBtn);
        TextView textView3 = findViewById(R.id.textView3);

        logoAdminImgBtn.setOnClickListener(v -> {
            clickCount++;

            Log.d("EventsPage", "Clicked " + clickCount + " times");

            // Redirect to the admin login page after 4 clicks if not logged in as admin
            if (clickCount == 4) {
                Intent intent4 = new Intent(EventsPage.this, AdminLoginActivity.class);
                startActivity(intent4);
                finish();
            }
        });

        // Fetch events
        List<Map<String, String>> events = db.getAllEvents();

        if (events == null || events.isEmpty()) {
            //Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show();
            textView3.setVisibility(TextView.VISIBLE);
            Log.d("EventsPage", "No events found in the database.");
            return;
        }

        // Set up the adapter
        adapter = new EventsAdapter(events, new EventsAdapter.OnEventClickListener() {
            @Override
            public void onViewDetailsClicked(Map<String, String> event) {
                Intent intent = new Intent(EventsPage.this, SeatReservationActivity.class);
                intent.putExtra("event_id", event.get("event_id"));
                Log.d("EventsPage", "Event ID: " + event.get("event_id"));
//                Intent intent2 = new Intent(EventsPage.this, RegisterSeat.class);//send out to RegisterSeat the eventId
//                intent2.putExtra("event_id", event.get("id"));
                startActivity(intent);
            }

            @Override
            public void onDeleteClicked(String eventId) {
                db.deleteEvent(eventId);
                refreshEvents(); // Reload data
            }
        });

        recyclerView.setAdapter(adapter);
    }

//    public void eventId(){
//        String eventId = events.get("id");
//    }

    public void updateData(List<Map<String, String>> newEvents) {
        this.events = newEvents;
        adapter.notifyDataSetChanged();
    }

    private void refreshEvents() {
        List<Map<String, String>> updatedEvents = db.getAllEvents();
        adapter.updateData(updatedEvents);
    }

    @Override
    protected void onDestroy() {
        if (db != null) {
            db.close();
        }
        super.onDestroy();
    }
}
