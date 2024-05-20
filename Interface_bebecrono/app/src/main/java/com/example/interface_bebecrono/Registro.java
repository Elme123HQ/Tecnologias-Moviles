package com.example.interface_bebecrono;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    EditText editTextBabyName, editTextBirthDate, editTextBirthTime, editTextGender, editTextWeight, editTextHeight;
    RadioGroup radioGroupBirthType;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        mFirestore =FirebaseFirestore.getInstance();

        editTextBabyName = findViewById(R.id.editTextBabyName);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextBirthTime = findViewById(R.id.editTextBirthTime);
        editTextGender = findViewById(R.id.editTextGender);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);

        radioGroupBirthType = findViewById(R.id.radioGroupBirthType);
        Button cancelButton = findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Al hacer clic en el botón cancelar, termina la actividad actual
                finish();
            }
        });
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los detalles del bebé ingresados por el usuario
                String babyName = editTextBabyName.getText().toString().trim();
                String birthDate = editTextBirthDate.getText().toString().trim();
                String birthTime = editTextBirthTime.getText().toString().trim();
                String gender = editTextGender.getText().toString().trim();
                double weight = getWeightInGrams();
                int height = getHeightInCentimeters();


                // Realizar la validación de los campos (aquí puedes agregar tus propias validaciones)
                if (babyName.isEmpty() || birthDate.isEmpty() || birthTime.isEmpty() || gender.isEmpty()) {
                    // Si falta algún campo obligatorio, mostrar un mensaje de error
                    Toast.makeText(Registro.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Si todos los campos están completos, guardar los detalles del bebé
                    saveBabyDetails(babyName, birthDate, birthTime, gender, weight, height);
                }
            }
        });
    }
    public void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    // Se llama cuando el usuario selecciona una fecha
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, monthOfYear, dayOfMonth);

                    // Obtener la fecha actual
                    Calendar currentDate = Calendar.getInstance();

                    if (selectedDate.after(currentDate)) {
                        // Si la fecha seleccionada es futura, mostrar un mensaje de error
                        Toast.makeText(Registro.this, "No se puede seleccionar una fecha futura", Toast.LENGTH_SHORT).show();
                    } else {
                        // Si la fecha seleccionada es válida, actualizar el campo de texto
                        String selectedDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        editTextBirthDate.setText(selectedDateStr);
                    }
                }, year, month, day);

        // Limitar la selección de fecha al día actual o anterior
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view1, hourOfDay, minute1) -> {
                    // Se llama cuando el usuario selecciona una hora
                    String selectedTime = hourOfDay + ":" + minute1;
                    editTextBirthTime.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }
    public void showGenderSelectionDialog(View view) {
        final String[] genders = {"Maculino", "Femenino"}; // Las opciones de género

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione Genero")
                .setItems(genders, (dialog, which) -> {
                    // Se llama cuando el usuario selecciona un género
                    editTextGender.setText(genders[which]);
                });
        builder.show();
    }
    private double getWeightInGrams() {
        String weightText = editTextWeight.getText().toString().trim();
        if (!weightText.isEmpty()) {
            try {
                double weightValue = Double.parseDouble(weightText);
                // Aquí puedes realizar la conversión de unidades si es necesario
                return weightValue;
            } catch (NumberFormatException e) {
                // Manejar el error si no se puede convertir a número
            }
        }
        return 0; // Peso predeterminado si no se ingresa ningún valor
    }
    private int getHeightInCentimeters() {
        String heightText = editTextHeight.getText().toString().trim();
        if (!heightText.isEmpty()) {
            try {
                return Integer.parseInt(heightText);
            } catch (NumberFormatException e) {
                // Manejar el error si no se puede convertir a número
            }
        }
        return 0; // Talla predeterminada si no se ingresa ningún valor
    }
    private void saveBabyDetails(String babyName, String birthDate, String birthTime, String gender, double weight, int height) {
        String weightWithUnit = weight + " gramos";
        Map<String,Object> map = new HashMap<>();
        map.put("Nombre del bebe", babyName);
        map.put("Fecha de nacimiento", birthDate);
        map.put("hora de nacimiento", birthTime);
        map.put("Genero", gender);
        map.put("Peso", weightWithUnit);
        map.put("Talla", height);
        mFirestore.collection("bebe").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent = new Intent(Registro.this, aplicativos.class);
                intent.putExtra("BABY_NAME", babyName);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Registro exitoso del bebe",Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error al ingresar",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
