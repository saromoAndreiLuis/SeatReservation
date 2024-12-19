package com.example.seatreservation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private Connection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        db = new Connection(this); // Initialize database connection

        EditText usernameEditText = findViewById(R.id.admin_username);
        EditText passwordEditText = findViewById(R.id.admin_password);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register);
        ImageButton backButton = findViewById(R.id.backButton);





        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
            // Validate admin credentials
            else if (db.validateAdmin(username, password)) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Redirect to admin dashboard
                Intent intent = new Intent(AdminLoginActivity.this, AdminDashboard.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = db.addAdmin(username, password);
                    if (isInserted) {
                        Log.d("Admin", "Admin added successfully.");
                    } else {
                        Log.d("Admin", "Failed to add admin.");
                    }
            }

        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginActivity.this, EventsPage.class);
            startActivity(intent);
            finish();
        });

    }


}
