package com.example.seatreservation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connection extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reservations.db";
    private static final int DATABASE_VERSION = 3; // Incremented version
    private static final String TABLE_NAME = "reservations";
    private static final String TABLE_NAME1 = "admin";
    private static final String TABLE_NAME2 = "events";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EVENT_NAME = "event_name";
    private static final String COL_SEAT_NUMBER = "seat_number";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    public static final String COL_EVENT_ID = "event_id";
    private static final String COL_EVENT_NAME1 = "event_name";
    private static final String COL_EVENT_DATE = "event_date";
    private static final String COL_EVENT_TIME = "event_time";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT, "
            + COL_EVENT_NAME + " TEXT, "
            + COL_SEAT_NUMBER + " TEXT, "
            + COL_EVENT_ID + " INTEGER, "
            + "FOREIGN KEY(" + COL_EVENT_ID + ") REFERENCES " + TABLE_NAME2 + "(" + COL_EVENT_ID + "))";

    private static final String CREATE_TABLE_ADMIN = "CREATE TABLE " + TABLE_NAME1 + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_USERNAME + " TEXT, "
            + COL_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_NAME2 + "("
            + COL_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_EVENT_NAME1 + " TEXT, "
            + COL_EVENT_DATE + " TEXT, "
            + COL_EVENT_TIME + " TEXT)";

    private static final String CREATE_PHOLDER_EVENT = "INSERT INTO " + TABLE_NAME2 + " (" + COL_EVENT_NAME1 + ", " + COL_EVENT_DATE + ", " + COL_EVENT_TIME + ") VALUES (?, ?, ?)";

    public Connection(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create the reservations table
            db.execSQL(CREATE_TABLE);
            Log.d("DBHelper", "Created table: reservations");

            // Create the admin table
            db.execSQL(CREATE_TABLE_ADMIN);
            Log.d("DBHelper", "Created table: admin");

            // Create the events table
            db.execSQL(CREATE_TABLE_EVENTS);
            Log.d("DBHelper", "Created table: events");

            // Add a placeholder event to the events table
//            db.execSQL("INSERT INTO " + TABLE_NAME2 + " ("
//                    + COL_EVENT_NAME1 + ", " + COL_EVENT_DATE + ", " + COL_EVENT_TIME + ") VALUES " +
//                    "('Sample Event', '2024-01-01', '12:00 PM')");

            //seedEvents(db);

            Log.d("DBHelper", "Added placeholder event to events table");

            Log.d("DBHelper", "All tables created successfully.");

        } catch (Exception e) {
            Log.e("DBHelper", "Error during onCreate: " + e.getMessage(), e);
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    private void seedEvents(SQLiteDatabase db) {
        addEvent(db, "Music Concert", "2024-01-15", "7:00 PM");
        addEvent(db, "Art Exhibition", "2024-01-20", "6:00 PM");
        addEvent(db, "Tech Talk", "2024-02-10", "5:00 PM");
    }

    private void addEvent(SQLiteDatabase db, String name, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(COL_EVENT_NAME, name);
        values.put(COL_EVENT_DATE, date);
        values.put(COL_EVENT_TIME, time);
        db.insert(TABLE_NAME2, null, values);
    }

    public boolean register(String name, String eventName, String seat, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("event_name", eventName);
        values.put("seat_number", seat);
        values.put("event_id", eventId);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // Return true if insertion succeeded
    }

    public boolean addAdmin(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_NAME1, null, contentValues);
        db.close();
        return result != -1; // Return true if insertion was successful
    }

    public List<String> getTakenSeats(String eventName) {
        List<String> reservedSeats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT seat_number FROM reservations WHERE event_name = ?", new String[]{eventName});

        if (cursor.moveToFirst()) {
            do {
                reservedSeats.add(cursor.getString(0)); // Add seat number to the list
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d("DBHelper", "Reserved seats: " + reservedSeats.toString());
        return reservedSeats;
    }

    public Map<String, String> getSeatDetails(String seat) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, event_name FROM reservations WHERE seat_number = ?", new String[]{seat});

        Map<String, String> seatDetails = null;
        if (cursor.moveToFirst()) {
            seatDetails = new HashMap<>();
            seatDetails.put("name", cursor.getString(0));
            seatDetails.put("event_name", cursor.getString(1));
        }
        cursor.close();
        return seatDetails;
    }

    public boolean validateAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM admin WHERE username = ? AND password = ?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    // Fetch all reservations
    public List<Map<String, String>> getAllReservations() {
        List<Map<String, String>> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> reservation = new HashMap<>();
                reservation.put(COL_ID, cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)));
                reservation.put(COL_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
                reservation.put(COL_EVENT_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_NAME)));
                reservation.put(COL_SEAT_NUMBER, cursor.getString(cursor.getColumnIndexOrThrow(COL_SEAT_NUMBER)));
                reservations.add(reservation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reservations;
    }


    // Update a reservation
    public boolean updateReservation(String id, String name, String eventName, String seatNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_EVENT_NAME, eventName);
        contentValues.put(COL_SEAT_NUMBER, seatNumber);
        int result = db.update("reservations", contentValues, "id = ?", new String[]{id});
        db.close();
        return result > 0;
    }

    // Delete a reservation
    public boolean deleteReservation(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("reservations", "id = ?", new String[]{id});
        db.close();
        return result > 0;
    }

    public boolean addEvent(String eventName, String eventDate, String eventTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EVENT_NAME1, eventName);
        contentValues.put(COL_EVENT_DATE, eventDate);
        contentValues.put(COL_EVENT_TIME, eventTime);
        long result = db.insert(TABLE_NAME2, null, contentValues);
        db.close();
        return result != -1;
    }

    public List<Map<String, String>> getAllEvents() {
        List<Map<String, String>> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> event = new HashMap<>();
                event.put(COL_EVENT_ID, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_ID)));
                event.put(COL_EVENT_NAME1, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_NAME1)));
                event.put(COL_EVENT_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_DATE)));
                event.put(COL_EVENT_TIME, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_TIME)));
                events.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return events;
    }

//    public Map<String, String> getEventById(int eventId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " WHERE " + COL_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
//
//        Map<String, String> event = null;
//        if (cursor.moveToFirst()) {
//            event = new HashMap<>();
//            event.put(COL_EVENT_ID, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_ID)));
//            event.put(COL_EVENT_NAME1, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_NAME1)));
//            event.put(COL_EVENT_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_DATE)));
//            event.put(COL_EVENT_TIME, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_TIME)));
//        }
//
//        cursor.close();
//        db.close();
//        return event;
//    }

    public List<Map<String, String>> getEventsById(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Map<String, String>> events = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME2,
                null,
                COL_EVENT_ID + " = ?",
                new String[]{eventId},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> event = new HashMap<>();
                event.put(COL_EVENT_ID, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_ID)));
                event.put(COL_EVENT_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_NAME)));
                event.put(COL_EVENT_DATE, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_DATE)));
                event.put(COL_EVENT_TIME, cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_TIME)));
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    public String getEventName(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String eventName = null;
        String query = "SELECT event_name FROM events WHERE event_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{eventId});
        if (cursor.moveToFirst()) {
            eventName = cursor.getString(0);
        }
        cursor.close();
        return eventName;
    }

    public boolean deleteEvent(String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, COL_EVENT_ID + " = ?", new String[]{eventId});
        db.close();
        return false;
    }

    public boolean updateEvent(int eventId, String eventName, String eventDate, String eventTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EVENT_NAME1, eventName);
        contentValues.put(COL_EVENT_DATE, eventDate);
        contentValues.put(COL_EVENT_TIME, eventTime);
        int result = db.update(TABLE_NAME2, contentValues, COL_EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
        return result > 0;
    }



}
