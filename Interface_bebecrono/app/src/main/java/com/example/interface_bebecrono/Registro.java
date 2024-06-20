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
        setContentView(R.layout.activity_registro);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
                finish();
            }
        });

        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String babyName = editTextBabyName.getText().toString().trim();
                String birthDate = editTextBirthDate.getText().toString().trim();
                String birthTime = editTextBirthTime.getText().toString().trim();
                String gender = editTextGender.getText().toString().trim();
                double weight = getWeightInGrams();
                int height = getHeightInCentimeters();

                if (babyName.isEmpty() || birthDate.isEmpty() || birthTime.isEmpty() || gender.isEmpty()) {
                    Toast.makeText(Registro.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!isNameValid(babyName)) {
                    Toast.makeText(Registro.this, "El nombre solo debe contener letras y espacios", Toast.LENGTH_SHORT).show();
                } else {
                    saveBabyDetails(babyName, birthDate, birthTime, gender, weight, height);
                }
            }
        });
    }

    private boolean isNameValid(String name) {
        return name.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    public void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, monthOfYear, dayOfMonth);
                    Calendar currentDate = Calendar.getInstance();

                    if (selectedDate.after(currentDate)) {
                        Toast.makeText(Registro.this, "No se puede seleccionar una fecha futura", Toast.LENGTH_SHORT).show();
                    } else {
                        String selectedDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        editTextBirthDate.setText(selectedDateStr);
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view1, hourOfDay, minute1) -> {
                    String selectedTime = hourOfDay + ":" + minute1;
                    editTextBirthTime.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void showGenderSelectionDialog(View view) {
        final String[] genders = {"Masculino", "Femenino"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione Género")
                .setItems(genders, (dialog, which) -> editTextGender.setText(genders[which]));
        builder.show();
    }

    private double getWeightInGrams() {
        String weightText = editTextWeight.getText().toString().trim();
        if (!weightText.isEmpty()) {
            try {
                return Double.parseDouble(weightText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private int getHeightInCentimeters() {
        String heightText = editTextHeight.getText().toString().trim();
        if (!heightText.isEmpty()) {
            try {
                return Integer.parseInt(heightText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void saveBabyDetails(String babyName, String birthDate, String birthTime, String gender, double weight, int height) {
        String weightWithUnit = weight + " gramos";
        String heightWithUnit = height + " centímetros";
        String userId = mAuth.getCurrentUser().getUid();

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("Nombre del bebe", babyName);
        map.put("Fecha de nacimiento", birthDate);
        map.put("Hora de nacimiento", birthTime);
        map.put("Género", gender);
        map.put("Peso", weightWithUnit);
        map.put("Talla", heightWithUnit);

        mFirestore.collection("bebe")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(Registro.this, aplicativos.class);
                        intent.putExtra("BABY_NAME", babyName);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Registro exitoso del bebé", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
