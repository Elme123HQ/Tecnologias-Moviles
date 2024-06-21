package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Alimentos extends AppCompatActivity {

    private EditText editTextDetail, editTextFruta, editTextGranos, editTextVegetales, editTextLacteos, editTextCarnes, editTextTotal, editTextBiberon;
    private RadioGroup radioGroupPecho;
    private Spinner spinnerSource;
    private Button buttonSaveFood;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String babyName;

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

        // Configurar EditText para aceptar solo números
        editTextFruta.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextGranos.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextVegetales.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextLacteos.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCarnes.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextBiberon.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // Inicializar Firestore y Auth
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Obtener el nombre del bebé del Intent
        babyName = getIntent().getStringExtra("BABY_NAME");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_scroll_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_fuente_alimento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(adapter);

        // Añadir TextWatchers a los campos de entrada
        addTextWatchers();

        buttonSaveFood.setOnClickListener(v -> saveFoodData());
    }

    private void addTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        };

        editTextFruta.addTextChangedListener(textWatcher);
        editTextGranos.addTextChangedListener(textWatcher);
        editTextVegetales.addTextChangedListener(textWatcher);
        editTextLacteos.addTextChangedListener(textWatcher);
        editTextCarnes.addTextChangedListener(textWatcher);
        editTextBiberon.addTextChangedListener(textWatcher);
    }

    private void calculateTotal() {
        try {
            int fruta = parseEditTextToInt(editTextFruta);
            int granos = parseEditTextToInt(editTextGranos);
            int vegetales = parseEditTextToInt(editTextVegetales);
            int lacteos = parseEditTextToInt(editTextLacteos);
            int carnes = parseEditTextToInt(editTextCarnes);
            int total = fruta + granos + vegetales + lacteos + carnes;
            editTextTotal.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            editTextTotal.setText("");
        }
    }

    private int parseEditTextToInt(EditText editText) throws NumberFormatException {
        String text = editText.getText().toString().trim();
        if (text.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(text);
        }
    }

    private boolean validateFields() {
        if (editTextDetail.getText().toString().isEmpty()) {
            editTextDetail.setError("Este campo es obligatorio");
            return false;
        }
        if (editTextFruta.getText().toString().isEmpty()) {
            editTextFruta.setError("Este campo es obligatorio");
            return false;
        }
        if (editTextGranos.getText().toString().isEmpty()) {
            editTextGranos.setError("Este campo es obligatorio");
            return false;
        }
        if (editTextVegetales.getText().toString().isEmpty()) {
            editTextVegetales.setError("Este campo es obligatorio");
            return false;
        }
        if (editTextLacteos.getText().toString().isEmpty()) {
            editTextLacteos.setError("Este campo es obligatorio");
            return false;
        }
        if (editTextCarnes.getText().toString().isEmpty()) {
            editTextCarnes.setError("Este campo es obligatorio");
            return false;
        }
        if (editTextTotal.getText().toString().isEmpty()) {
            editTextTotal.setError("Este campo es obligatorio");
            return false;
        }
        if (editTextBiberon.getText().toString().isEmpty()) {
            editTextBiberon.setError("Este campo es obligatorio");
            return false;
        }
        if (radioGroupPecho.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Por favor, selecciona una opción de Pecho", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinnerSource.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Por favor, selecciona una fuente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveFoodData() {
        if (!validateFields()) {
            return;
        }

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

        String userId = mAuth.getCurrentUser().getUid();

        Map<String, String> foodData = new HashMap<>();
        foodData.put("userId", userId);
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

        mFirestore.collection("Registro de alimentos").add(foodData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Alimentos.this, "Registro de alimentos guardado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Alimentos.this, aplicativos.class);
                    intent.putExtra("BABY_NAME", babyName);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Alimentos.this, "Error al guardar el registro", Toast.LENGTH_SHORT).show();
                });
    }
}
