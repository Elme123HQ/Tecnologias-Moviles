package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class sueno extends AppCompatActivity {

    private EditText editTextDuration;
    private EditText editTextStartTime;
    private EditText editTextEndTime;
    private Button buttonRegisterSleep;
    private FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    private String babyName;

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

        buttonRegisterSleep.setOnClickListener(v -> registerSleep());
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
