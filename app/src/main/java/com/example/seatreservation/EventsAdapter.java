package com.example.seatreservation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    Connection db;
    private List<Map<String, String>> events;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onViewDetailsClicked(Map<String, String> event);
        void onDeleteClicked(String eventId);
    }

    public EventsAdapter(List<Map<String, String>> events, OnEventClickListener listener) {
        this.events = events;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_events_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> event = events.get(position);
        Log.d("EventsAdapter", "EventId: " + event.get("event_id"));
        Log.d("EventsAdapter", "Event Name: " + event.get("event_name"));

        holder.eventNameTextView.setText("Event: " + event.get("event_name"));
        holder.eventDateTextView.setText("Date: " + event.get("event_date"));
        holder.eventTimeTextView.setText("Time: " + event.get("event_time"));

        holder.viewDetailsButton.setOnClickListener(v -> listener.onViewDetailsClicked(event));
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public void updateData(List<Map<String, String>> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, eventDateTextView, eventTimeTextView;
        Button viewDetailsButton, deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.tv_event_name);
            eventDateTextView = itemView.findViewById(R.id.tv_event_date);
            eventTimeTextView = itemView.findViewById(R.id.tv_event_time);
            viewDetailsButton = itemView.findViewById(R.id.btn_view_details);
        }
    }
}