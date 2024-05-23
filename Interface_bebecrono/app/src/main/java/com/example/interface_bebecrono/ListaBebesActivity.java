package com.example.interface_bebecrono;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ListaBebesActivity extends AppCompatActivity {

    private ListView listViewBebes;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> bebesList;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_bebes);

        listViewBebes = findViewById(R.id.listViewBebes);
        bebesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bebesList);
        listViewBebes.setAdapter(adapter);

        mFirestore = FirebaseFirestore.getInstance();

        loadBebes();

        listViewBebes.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBabyName = bebesList.get(position);
            Intent intent = new Intent(ListaBebesActivity.this, aplicativos.class);
            intent.putExtra("BABY_NAME", selectedBabyName);
            startActivity(intent);
        });
    }

    private void loadBebes() {
        mFirestore.collection("bebe")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String babyName = document.getString("Nombre del bebe");
                        if (babyName != null) {
                            bebesList.add(babyName);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ListaBebesActivity.this, "Error al cargar la lista de bebés", Toast.LENGTH_SHORT).show();
                });
    }

}

