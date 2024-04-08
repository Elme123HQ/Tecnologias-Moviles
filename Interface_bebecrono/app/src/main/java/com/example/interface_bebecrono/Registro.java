package com.example.interface_bebecrono;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import java.util.Calendar;

public class Registro extends AppCompatActivity {
    EditText editTextBirthDate, editTextBirthTime, editTextGender, editTextWeight, editTextHeight;
    RadioGroup radioGroupBirthType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
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
                // Al hacer clic en el botón Guardar, inicia la actividad CuartaActivity
                Intent intent = new Intent(Registro.this, aplicativos.class);
                startActivity(intent);
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
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    editTextBirthDate.setText(selectedDate);
                }, year, month, day);
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
    private int getWeightInGrams() {
        String weightText = editTextWeight.getText().toString().trim();
        if (!weightText.isEmpty()) {
            try {
                return Integer.parseInt(weightText);
            } catch (NumberFormatException e) {
                // Error al convertir a número, manejar según sea necesario
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
    public void saveBabyDetails(View view) {
        int selectedId = radioGroupBirthType.getCheckedRadioButtonId();

        if (selectedId == -1) {
            // No se ha seleccionado ninguna opción
            Toast.makeText(this, "Please select birth type", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton radioButtonSelected = findViewById(selectedId);

        if (radioButtonSelected != null) {
            String selectedBirthType = radioButtonSelected.getText().toString();
            // Aquí puedes usar el valor de selectedBirthType según tus necesidades
        }
    }
}
