package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

public class BotonRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_boton_registro);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        // Configurar OnClickListener para el botón de "Registrar Bebé"
        buttonRegister.setOnClickListener(v -> openRegistroBebeDetalleActivity());
    }
    private void openRegistroBebeDetalleActivity() {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}
