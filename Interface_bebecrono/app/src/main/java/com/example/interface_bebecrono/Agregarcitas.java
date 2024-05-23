package com.example.interface_bebecrono;

import android.app.DatePickerDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Agregarcitas extends AppCompatActivity {

    private EditText editTextCita;
    private EditText editTextContacto;
    private EditText editTextUbicacion;
    private EditText editTextFecha;
    private EditText editTextNota;
    private Button buttonRegisterCita;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregarcitas);

        // Inicializar las vistas
        editTextCita = findViewById(R.id.editTextCita);
        editTextContacto = findViewById(R.id.editTextContacto);
        editTextUbicacion = findViewById(R.id.editTextUbicacion);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextNota = findViewById(R.id.editTextNota);
        buttonRegisterCita = findViewById(R.id.buttonRegisterCita);

        // Inicializar Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Manejo de insets para el diseño edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_agregarcita), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar el campo de fecha para mostrar el selector de fecha
        editTextFecha.setOnClickListener(v -> showDatePickerDialog());

        // Configurar el botón para registrar la cita
        buttonRegisterCita.setOnClickListener(v -> registerCita());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Se llama cuando el usuario selecciona una fecha
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    editTextFecha.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void registerCita() {
        String cita = editTextCita.getText().toString();
        String contacto = editTextContacto.getText().toString();
        String ubicacion = editTextUbicacion.getText().toString();
        String fecha = editTextFecha.getText().toString();
        String nota = editTextNota.getText().toString();

        if (cita.isEmpty() || contacto.isEmpty() || ubicacion.isEmpty() || fecha.isEmpty() || nota.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> citaData = new HashMap<>();
        citaData.put("cita", cita);
        citaData.put("contacto", contacto);
        citaData.put("ubicacion", ubicacion);
        citaData.put("fecha", fecha);
        citaData.put("nota", nota);

        mFirestore.collection("Registro de citas").add(citaData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Agregarcitas.this, "Registro de cita guardado", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(Agregarcitas.this, aplicativos.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Agregarcitas.this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
                });
    }
}
