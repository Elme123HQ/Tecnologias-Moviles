package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class aplicativos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicativos);

        // Obtener el nombre del bebé del Intent
        String babyName = getIntent().getStringExtra("BABY_NAME");

        // Referenciar el TextView donde quieres mostrar el nombre del bebé
        TextView textViewBabyName = findViewById(R.id.textbaby);
        textViewBabyName.setText("Nombre del bebé: " + babyName);

        // Referenciar el ImageView de "Sueño"
        ImageView imageViewSleep = findViewById(R.id.imagesueño);

        // Establecer OnClickListener para el ImageView de "Sueño"
        imageViewSleep.setOnClickListener(v -> {
            // Crear un Intent para iniciar la actividad "sueno"
            Intent intent = new Intent(aplicativos.this, sueno.class);
            startActivity(intent);
        });


    }
}
