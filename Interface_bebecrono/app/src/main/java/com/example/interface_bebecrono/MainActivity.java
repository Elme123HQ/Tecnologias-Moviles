package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonLogin, buttonGoogleLogin, buttonRegister;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Encontrar las vistas por sus identificadores
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonGoogleLogin = findViewById(R.id.buttonGoogleLogin);
        imageView = findViewById(R.id.imageView);

        // Configurar OnClickListener para el botón de inicio de sesión
        buttonLogin.setOnClickListener(v -> loginUser());

        // Configurar OnClickListener para el botón de ingresar con Google
        buttonGoogleLogin.setOnClickListener(v -> continueWithGoogle());
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        startRegisterActivity();
    }

    private void continueWithGoogle() {
        startRegisterActivity();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, BotonRegistro.class);
        startActivity(intent);
    }
}
