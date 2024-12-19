package com.example.seatreservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private List<Map<String, String>> reservations;
    private OnReservationClickListener listener;

    public interface OnReservationClickListener {
        void onEditClicked(Map<String, String> reservation);
        void onDeleteClicked(String id);
    }

    public ReservationAdapter(List<Map<String, String>> reservations, OnReservationClickListener listener) {
        this.reservations = reservations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> reservation = reservations.get(position);

        holder.seatTextView.setText("Seat: " + reservation.get("seat_number"));
        holder.nameTextView.setText("Name: " + reservation.get("name"));
        holder.eventTextView.setText("Event: " + reservation.get("event_name"));

        holder.editButton.setOnClickListener(v -> listener.onEditClicked(reservation));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClicked(reservation.get("id")));
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView seatTextView, nameTextView, eventTextView;
        Button editButton, deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            seatTextView = itemView.findViewById(R.id.tv_seat);
            nameTextView = itemView.findViewById(R.id.tv_name);
            eventTextView = itemView.findViewById(R.id.tv_event);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}
