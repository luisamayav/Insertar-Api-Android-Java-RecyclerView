package com.example.insertconrecyclerview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText etNombre, etCorreo;
    Button btnEnviar;
    RecyclerView recyclerView;
    List<Student> lista;
    StudentAdapter adapter;

    String URL = "http://192.168.12.67/insertacinrecycler.php"; // Cambia por tu API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        btnEnviar = findViewById(R.id.btnEnviar);
        recyclerView = findViewById(R.id.recyclerView);

        lista = new ArrayList<>();
        adapter = new StudentAdapter(lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnEnviar.setOnClickListener(v -> insertarYMostrar());
    }

    private void insertarYMostrar() {
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        @SuppressLint("NotifyDataSetChanged") StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        lista.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            Student s = new Student();
                            s.name = obj.getString("name");
                            s.email = obj.getString("email");
                            lista.add(s);
                        }

                        adapter.notifyDataSetChanged();
                        etNombre.setText("");
                        etCorreo.setText("");
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error al parsear JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> datos = new HashMap<>();
                datos.put("name", nombre);
                datos.put("email", correo);
                return datos;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}