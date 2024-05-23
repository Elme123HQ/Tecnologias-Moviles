package com.example.interface_bebecrono;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Alimentos extends AppCompatActivity {

    private EditText editTextDetail, editTextFruta, editTextGranos, editTextVegetales, editTextLacteos, editTextCarnes, editTextTotal, editTextBiberon;
    private RadioGroup radioGroupPecho;
    private Spinner spinnerSource;
    private Button buttonSaveFood;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentos);

        // Referenciar los campos
        editTextDetail = findViewById(R.id.editTextDetail);
        editTextFruta = findViewById(R.id.editTextFruta);
        editTextGranos = findViewById(R.id.editTextGranos);
        editTextVegetales = findViewById(R.id.editTextVegetales);
        editTextLacteos = findViewById(R.id.editTextLacteos);
        editTextCarnes = findViewById(R.id.editTextCarne);
        editTextTotal = findViewById(R.id.editTextTotal);
        editTextBiberon = findViewById(R.id.editTextBiberon);
        radioGroupPecho = findViewById(R.id.radioGroupPecho);
        spinnerSource = findViewById(R.id.spinner_source);
        buttonSaveFood = findViewById(R.id.buttonSaveFood);

        // Inicializar Firestore
        mFirestore = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_scroll_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonSaveFood.setOnClickListener(v -> saveFoodData());
    }

    private void saveFoodData() {
        String detail = editTextDetail.getText().toString();
        String fruta = editTextFruta.getText().toString();
        String granos = editTextGranos.getText().toString();
        String vegetales = editTextVegetales.getText().toString();
        String lacteos = editTextLacteos.getText().toString();
        String carnes = editTextCarnes.getText().toString();
        String total = editTextTotal.getText().toString();
        String biberon = editTextBiberon.getText().toString();
        String pecho = ((RadioButton) findViewById(radioGroupPecho.getCheckedRadioButtonId())).getText().toString();
        String source = spinnerSource.getSelectedItem().toString();

        if (detail.isEmpty() || fruta.isEmpty() || granos.isEmpty() || vegetales.isEmpty() || lacteos.isEmpty() || carnes.isEmpty() || total.isEmpty() || biberon.isEmpty() || pecho.isEmpty() || source.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> foodData = new HashMap<>();
        foodData.put("detail", detail);
        foodData.put("fruta", fruta);
        foodData.put("Granos", granos);
        foodData.put("Vegetales", vegetales);
        foodData.put("Lacteos", lacteos);
        foodData.put("Carnes", carnes);
        foodData.put("Total", total);
        foodData.put("biberon", biberon);
        foodData.put("pecho", pecho);
        foodData.put("source", source);

        mFirestore.collection("foodRecords").add(foodData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Alimentos.this, "Registro de alimentos guardado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Alimentos.this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
                });
    }
}

