package com.example.interface_bebecrono;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class sueno extends AppCompatActivity {

    private EditText editTextDuration;
    private EditText editTextStartTime;
    private EditText editTextEndTime;
    private Button buttonRegisterSleep;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String babyName;

    private int startHour = -1, startMinute = -1;
    private int endHour = -1, endMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sueno);

        editTextDuration = findViewById(R.id.editTextDuration);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        buttonRegisterSleep = findViewById(R.id.buttonRegisterSleep);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        babyName = getIntent().getStringExtra("BABY_NAME");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextStartTime.setOnClickListener(v -> showTimePickerDialog(v, true));
        editTextEndTime.setOnClickListener(v -> showTimePickerDialog(v, false));

        buttonRegisterSleep.setOnClickListener(v -> registerSleep());
    }

    public void showTimePickerDialog(View view, boolean isStartTime) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view1, hourOfDay, minute1) -> {
                    if (isStartTime) {
                        startHour = hourOfDay;
                        startMinute = minute1;
                        editTextStartTime.setText(String.format("%02d:%02d", hourOfDay, minute1));
                    } else {
                        endHour = hourOfDay;
                        endMinute = minute1;
                        editTextEndTime.setText(String.format("%02d:%02d", hourOfDay, minute1));
                    }
                    calculateDuration();
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void calculateDuration() {
        if (startHour != -1 && startMinute != -1 && endHour != -1 && endMinute != -1) {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
            startCalendar.set(Calendar.MINUTE, startMinute);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
            endCalendar.set(Calendar.MINUTE, endMinute);

            long differenceInMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
            if (differenceInMillis < 0) {
                // If the end time is before the start time, assume the end time is on the next day
                endCalendar.add(Calendar.DAY_OF_MONTH, 1);
                differenceInMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
            }

            int durationInMinutes = (int) (differenceInMillis / (1000 * 60));
            editTextDuration.setText(String.valueOf(durationInMinutes));
        }
    }

    private void registerSleep() {
        String duration = editTextDuration.getText().toString();
        String startTime = editTextStartTime.getText().toString();
        String endTime = editTextEndTime.getText().toString();

        if (duration.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        Map<String, String> sleepData = new HashMap<>();
        sleepData.put("userId", userId);
        sleepData.put("duration", duration);
        sleepData.put("startTime", startTime);
        sleepData.put("endTime", endTime);

        mFirestore.collection("Registro de sueño").add(sleepData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(sueno.this, "Registro de sueño guardado", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(sueno.this, aplicativos.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(sueno.this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
                });
    }
}

