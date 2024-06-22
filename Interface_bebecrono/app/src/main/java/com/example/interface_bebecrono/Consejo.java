package com.example.interface_bebecrono;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Consejo extends AppCompatActivity {

    private TextView consejo1;
    private TextView consejo2;
    private TextView consejo3;
    private Set<String> consejosUsados = new HashSet<>(); // Para almacenar consejos usados y evitar repetición

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consejo);

        consejo1 = findViewById(R.id.consejo1);
        consejo2 = findViewById(R.id.consejo2);
        consejo3 = findViewById(R.id.consejo3);

        // Ejecutar AsyncTask para obtener los consejos
        new GetConsejoTask().execute();
    }

    private class GetConsejoTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids) {
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

            // Procesar el JSON para obtener el consejo
            try {
                JSONObject consejoJson = new JSONObject(consejoJsonStr);
                String consejo = consejoJson.getJSONObject("slip").getString("advice");

                // Crear un array de consejos en español
                String[] consejos = new String[3];
                consejos[0] =  obtenerConsejoEnEspanol();
                consejos[1] =  obtenerConsejoEnEspanol();
                consejos[2] =  obtenerConsejoEnEspanol();

                return consejos;

            } catch (JSONException e) {
                // Error al procesar JSON
                e.printStackTrace();
                return null;
            }
        }

        private String obtenerConsejoEnEspanol() {
            // Consejos en español (simulados)
            String[] consejosEnEspanol = {
                    "Aprende a escuchar a tu bebé, cada llanto tiene un significado.",
                    "No dudes en pedir ayuda a familiares y amigos cuando lo necesites.",
                    "Mantén una rutina estable para el bebé, ayudará en su desarrollo.",
                    "La paciencia es clave en la crianza de tu bebé.",
                    "Disfruta cada momento con tu bebé, el tiempo pasa rápido.",
                    "No te preocupes por ser perfecto/a, todos los padres cometen errores.",
                    "Cuida tu bienestar emocional, tu bebé depende de tu equilibrio.",
                    "Lee cuentos y canta canciones para estimular el desarrollo del lenguaje.",
                    "Observa las señales de sueño de tu bebé para establecer una rutina de descanso.",
                    "No olvides cuidar tu relación de pareja, es importante para la familia.",
                    "Comparte momentos de juego y exploración con tu bebé.",
                    "Promueve la exploración segura de nuevos ambientes para tu bebé.",
                    "Mantén un espacio limpio y seguro para jugar y crecer.",
                    "Consulta con profesionales de salud ante cualquier duda sobre la salud de tu bebé.",
                    "Aprende sobre el desarrollo motor de tu bebé y apóyalo en cada paso.",
                    "Fomenta la independencia gradual de tu bebé al explorar su entorno.",
                    "Sigue una dieta balanceada y saludable para tu bienestar y el de tu bebé si estás lactando.",
                    "Establece límites claros y consistentes en la crianza de tu bebé.",
                    "Celebra cada logro y avance de tu bebé, por pequeño que sea.",
                    "Busca momentos de relajación y descanso para ti, la maternidad/paternidad puede ser agotadora.",
                    "Comparte con otras familias experiencias y consejos sobre la crianza de bebés.",
                    "Consulta libros y recursos confiables sobre crianza para ampliar tu conocimiento.",
                    "Evita comparar el desarrollo de tu bebé con el de otros, cada niño es único.",
                    "Practica el contacto piel a piel con tu bebé para fortalecer el vínculo afectivo.",
                    "Aprovecha los paseos al aire libre para estimular los sentidos de tu bebé.",
                    "Asegúrate de que tu bebé tenga suficiente tiempo de juego físico para desarrollar habilidades motoras.",
                    "Aprende técnicas de relajación y respiración para manejar el estrés de la crianza.",
                    "Respeta el ritmo y las necesidades individuales de sueño de tu bebé.",
                    "Estimula el desarrollo cognitivo de tu bebé con juguetes apropiados para su edad.",
                    "Crea una rutina de baño relajante y agradable para tu bebé.",
                    "Promueve la exploración sensorial con texturas y materiales diversos para tu bebé.",
                    "Celebra y documenta los hitos y momentos especiales en el crecimiento de tu bebé.",
                    "Recuerda que cada etapa de la crianza tiene sus desafíos y recompensas únicas.",
                    "Fomenta la comunicación temprana con tu bebé respondiendo a sus balbuceos y gestos.",
                    "Apoya el desarrollo social de tu bebé permitiendo interacciones con otros niños y adultos.",
                    "Establece un ambiente tranquilo y libre de distracciones para las horas de sueño de tu bebé.",
                    "Crea un espacio seguro y accesible para que tu bebé explore libremente.",
                    "Comparte momentos de afecto y cariño con tu bebé, fortalece su seguridad emocional.",
                    "Explora técnicas de crianza positiva para fortalecer la relación con tu bebé.",
                    "Fomenta la curiosidad y la creatividad de tu bebé con actividades de exploración y juego.",
                    "Investiga sobre los hitos de desarrollo típicos en cada etapa para comprender mejor a tu bebé.",
                    "Establece una conexión emocional con tu bebé a través del contacto visual y el lenguaje corporal.",
                    "Apoya el desarrollo emocional de tu bebé ofreciéndole consuelo y seguridad cuando lo necesite.",
                    "Consulta regularmente con el pediatra para monitorear el crecimiento y desarrollo de tu bebé.",
                    "Confía en tus instintos como padre/madre, conoces mejor las necesidades de tu bebé.",
                    "Aprende técnicas de manejo del estrés para mantener un equilibrio emocional durante la crianza.",
                    "Fomenta la autoestima de tu bebé ofreciendo elogios y refuerzos positivos.",
                    "Recuerda que tu presencia y apoyo son fundamentales para el desarrollo saludable de tu bebé.",
                    "Busca tiempo para conectar individualmente con cada uno de tus hijos, incluido tu bebé.",
                    "Aprende sobre la importancia del juego en el desarrollo integral de tu bebé.",
                    "Establece límites claros y respetuosos en la crianza de tu bebé, promoviendo la seguridad y la confianza.",
                    "Participa en actividades grupales con otros padres para compartir experiencias y recursos sobre la crianza."
            };


            // Elegir un consejo que no haya sido usado previamente
            String consejoSeleccionado = null;
            while (consejoSeleccionado == null || consejosUsados.contains(consejoSeleccionado)) {
                Random random = new Random();
                int index = random.nextInt(consejosEnEspanol.length);
                consejoSeleccionado = consejosEnEspanol[index];
            }

            // Agregar el consejo seleccionado al conjunto de consejos usados
            consejosUsados.add(consejoSeleccionado);

            return consejoSeleccionado;
        }

        @Override
        protected void onPostExecute(String[] consejos) {
            if (consejos != null) {
                // Mostrar los consejos obtenidos
                consejo1.setText(consejos[0]);
                consejo2.setText(consejos[1]);
                consejo3.setText(consejos[2]);
            } else {
                Toast.makeText(Consejo.this, "Error al obtener consejos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

