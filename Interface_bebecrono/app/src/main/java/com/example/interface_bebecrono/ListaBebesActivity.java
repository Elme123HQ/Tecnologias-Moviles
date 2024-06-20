package com.example.interface_bebecrono;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaBebesActivity extends AppCompatActivity {

    ListView listViewBebes;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    List<String> listaBebes;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_bebes);

        listViewBebes = findViewById(R.id.listViewBebes);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        listaBebes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaBebes);
        listViewBebes.setAdapter(adapter);

        loadBabyList();
    }

    private void loadBabyList() {
        String userId = mAuth.getCurrentUser().getUid();
        mFirestore.collection("bebe")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaBebes.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String babyName = document.getString("Nombre del bebe");
                            listaBebes.add(babyName);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ListaBebesActivity.this, "Error al cargar los bebés", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


