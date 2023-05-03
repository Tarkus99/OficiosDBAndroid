package com.example.myrecyclerviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class FormularioActivity extends AppCompatActivity {

    private Spinner spinner;
    private String oficio;
    private TextInputEditText nombre, apellidos;

    private Button aceptar, cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        spinner = findViewById(R.id.spinner);
        aceptar = findViewById(R.id.cancelar);
        cancelar = findViewById(R.id.cancelar);
        nombre = findViewById(R.id.txtNombre);
        apellidos = findViewById(R.id.txtApellidos);

        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this, R.array.oficios, android.R.layout.simple_spinner_item);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                oficio = (String) spinner.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cancelar.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            setResult(RESULT_CANCELED,intent);
            finish();
        });

        aceptar.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("nombre", nombre.getText());
            intent.putExtra("apellidos", apellidos.getText());
            intent.putExtra("oficio", oficio);
            setResult(RESULT_OK,intent);
            finish();
        });

    }
}