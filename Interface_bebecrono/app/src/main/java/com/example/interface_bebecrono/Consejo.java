package com.example.interface_bebecrono;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Consejo extends AppCompatActivity {

    private TextView consejoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejo);

        consejoTextView = findViewById(R.id.consejoTextView);

        // Ejecutar AsyncTask para obtener el consejo
        new GetConsejoTask().execute();
    }

    private class GetConsejoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String consejoJsonStr = null;

            try {
                URL url = new URL("https://api.adviceslip.com/advice");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Leer la respuesta
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // No hay datos
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // La respuesta estaba vacía
                    return null;
                }
                consejoJsonStr = buffer.toString();
            } catch (IOException e) {
                // Error al hacer la conexión
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return consejoJsonStr;
        }

        @Override
        protected void onPostExecute(String consejoJsonStr) {
            if (consejoJsonStr != null) {
                try {
                    JSONObject consejoJson = new JSONObject(consejoJsonStr);
                    String consejo = consejoJson.getJSONObject("slip").getString("advice");

                    // Mostrar el consejo en TextView
                    consejoTextView.setText(consejo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}