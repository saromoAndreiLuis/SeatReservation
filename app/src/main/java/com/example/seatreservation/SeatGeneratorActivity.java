package com.example.seatreservation;

import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.widget.AppCompatButton;

public class SeatGeneratorActivity extends AppCompatButton {
    private String seat;

    public SeatGeneratorActivity(Context context, String seat) {
        super(context);
        this.seat = seat;
        setTextColor(Color.BLACK);
        setAllCaps(false);
    }

    public String getSeat() {
        return seat;
    }

    public void seatTaken(boolean taken) {
        if (taken) {
            setBackgroundColor(Color.RED);
            setEnabled(false);
        } else {
            setBackgroundColor(Color.rgb(144, 238, 144)); // Light Green
            setEnabled(true);
        }
    }
}