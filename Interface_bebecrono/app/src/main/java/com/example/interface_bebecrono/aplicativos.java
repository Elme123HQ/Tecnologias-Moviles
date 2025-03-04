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
            intent.putExtra("BABY_NAME", babyName);
            startActivity(intent);
        });

        // Referenciar el ImageView de "Alimentos"
        ImageView imageViewAlimento = findViewById(R.id.imageViewFood);
        // Establecer OnClickListener para el ImageView de "Alimentos"
        imageViewAlimento.setOnClickListener(v -> {
            // Crear un Intent para iniciar la actividad "Alimentos"
            Intent intent = new Intent(aplicativos.this, Alimentos.class);
            intent.putExtra("BABY_NAME", babyName);
            startActivity(intent);
        });
        // Referenciar el ImageView de "Pañales"
        ImageView imagenpanal = findViewById(R.id.imagepañal);
        // Establecer OnClickListener para el ImageView de "Alimentos"
        imagenpanal.setOnClickListener(v -> {
            // Crear un Intent para iniciar la actividad "Alimentos"
            Intent intent = new Intent(aplicativos.this, Panal.class);
            intent.putExtra("BABY_NAME", babyName);
            startActivity(intent);
        });
        // Referenciar el ImageView de "AgregarCita"
        ImageView imagenagregarcita = findViewById(R.id.imagecita);
        // Establecer OnClickListener para el ImageView de "Alimentos"
        imagenagregarcita.setOnClickListener(v -> {
            // Crear un Intent para iniciar la actividad "Alimentos"
            Intent intent = new Intent(aplicativos.this, Agregarcitas.class);
            intent.putExtra("BABY_NAME", babyName);
            startActivity(intent);
        });
        // Referenciar el ImageView de "AgregarCita"
        ImageView imagenconsejo = findViewById(R.id.imageconsejo);
        // Establecer OnClickListener para el ImageView de "Alimentos"
        imagenconsejo.setOnClickListener(v -> {
            // Crear un Intent para iniciar la actividad "Alimentos"
            Intent intent = new Intent(aplicativos.this, Consejo.class);
            intent.putExtra("BABY_NAME", babyName);
            startActivity(intent);
        });
    }
}

