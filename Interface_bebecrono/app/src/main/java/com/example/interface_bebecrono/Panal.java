package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Panal extends AppCompatActivity {

    private TextView textViewFechaHora;
    private EditText editTextNota;
    private Spinner spinnerOpciones;
    private Button buttonRegisterPanal;
    private FirebaseFirestore mFirestore;
    private String tipoPanal = "";
    FirebaseAuth mAuth;
    private String babyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panal);

        textViewFechaHora = findViewById(R.id.textViewFechaHora);
        editTextNota = findViewById(R.id.editTextNota);
        spinnerOpciones = findViewById(R.id.spinnerOpciones);
        buttonRegisterPanal = findViewById(R.id.buttonRegisterPanal);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        babyName = getIntent().getStringExtra("BABY_NAME");


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_panal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonRegisterPanal.setOnClickListener(v -> registerPanal());
    }

    // Método para manejar el clic en las imágenes de opciones de pañal
    public void onOptionSelected(View view) {
        ImageView imageView = (ImageView) view;
        tipoPanal = (String) imageView.getTag();

        // Obtener la fecha y hora actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        // Establecer la fecha y hora en el TextView
        textViewFechaHora.setText(currentDateAndTime);
    }

    private void registerPanal() {
        String fechaHora = textViewFechaHora.getText().toString();
        String nota = editTextNota.getText().toString();
        String detalle = spinnerOpciones.getSelectedItem().toString();

        if (tipoPanal.isEmpty() || fechaHora.isEmpty() || detalle.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        Map<String, String> panalData = new HashMap<>();
        panalData.put("userId", userId);
        panalData.put("tipoPanal", tipoPanal);
        panalData.put("fechaHora", fechaHora);
        panalData.put("detalle", detalle);
        panalData.put("nota", nota);

        mFirestore.collection("Registro de pañal").add(panalData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Panal.this, "Registro de pañal guardado", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(Panal.this, aplicativos.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Panal.this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
                });
    }
}
