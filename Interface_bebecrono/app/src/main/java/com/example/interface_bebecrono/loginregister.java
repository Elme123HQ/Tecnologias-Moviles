package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class loginregister extends AppCompatActivity {

    EditText editTextFirstName, editTextLastName, editTextAge, editTextDNI, editTextNewCorreo, editTextNewPassword;
    Button buttonCreateAccount;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextDNI = findViewById(R.id.editTextDNI);
        editTextNewCorreo = findViewById(R.id.editTextNewCorreo);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String age = editTextAge.getText().toString().trim();
                String dni = editTextDNI.getText().toString().trim();
                String correou = editTextNewCorreo.getText().toString().trim();
                String password = editTextNewPassword.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || age.isEmpty() || dni.isEmpty() || correou.isEmpty() || password.isEmpty()) {
                    Toast.makeText(loginregister.this, "Complete los datos", Toast.LENGTH_SHORT).show();
                } else if (!isValidName(firstName)) {
                    Toast.makeText(loginregister.this, "Nombre inválido. Solo letras y espacios son permitidos.", Toast.LENGTH_SHORT).show();
                } else if (!isValidName(lastName)) {
                    Toast.makeText(loginregister.this, "Apellido inválido. Solo letras y espacios son permitidos.", Toast.LENGTH_SHORT).show();
                } else if (!isValidDNI(dni)) {
                    Toast.makeText(loginregister.this, "DNI inválido. Debe contener exactamente 8 números.", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(correou)) {
                    Toast.makeText(loginregister.this, "Correo inválido. Debe seguir el formato usuario@dominio.com.", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(password)) {
                    Toast.makeText(loginregister.this, "La contraseña debe tener al menos 8 caracteres, incluyendo un número, una letra y un carácter especial", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(firstName, lastName, age, dni, correou, password);
                }
            }
        });
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    private boolean isValidDNI(String dni) {
        return dni.matches("\\d{8}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[!_@#$%^&*+=?-].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return false;
        }
        return true;
    }

    private void registerUser(String firstName, String lastName, String age, String dni, String correou, String password) {
        mAuth.createUserWithEmailAndPassword(correou, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("Nombre", firstName);
                map.put("Apellido", lastName);
                map.put("Edad", age);
                map.put("DNI", dni);
                map.put("Correo", correou);
                map.put("contraseña", password);

                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(loginregister.this, MainActivity.class));
                        Toast.makeText(loginregister.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginregister.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginregister.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
