package com.example.myrecyclerviewexample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.ImagenRecibida;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Empleado;
import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;

public class DetailedView extends BaseActivity {

    public static enum MODO{
        UPDATE, CREATE;
    }
    public static Context context;
    private Empleado empleado;
    private Spinner spinner;
    private ImageView imageView;
    private TextInputEditText nombre, apellidos;

    private Button create, cancelar, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        context = getApplicationContext();

        spinner = findViewById(R.id.spinner);
        create = findViewById(R.id.aceptar);
        update = findViewById(R.id.update);
        cancelar = findViewById(R.id.cancelar);
        nombre = findViewById(R.id.txtNombre);
        apellidos = findViewById(R.id.txtApellidos);
        imageView = findViewById(R.id.imageView);

        ArrayAdapter<Oficio> myAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, Model.getInstance().getOficios(getApplicationContext()));
        spinner.setAdapter(myAdapter);

        MODO m = (MODO)getIntent().getExtras().getSerializable("mode");
        if (m==MODO.UPDATE){
            create.setVisibility(View.GONE);
            empleado = (Empleado) getIntent().getExtras().getSerializable("user");
            nombre.setText(empleado.getNombre());
            apellidos.setText(empleado.getApellidos());
            spinner.setSelection(empleado.getIdOficio()-1);
        }else{
            update.setVisibility(View.GONE);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Oficio oficio = (Oficio)spinner.getSelectedItem();
                if (oficio.getImage()!=null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(oficio.getImage(), 0, oficio.getImage().length);
                    imageView.setImageBitmap(bitmap);
                }else{
                    executeCall(new CallInterface() {
                        ImagenRecibida imagen;
                        @Override
                        public void doInBackground() {
                            imagen =
                                    Connector.getConector().get
                                            (ImagenRecibida.class, "oficios/images/" + oficio.getIdOficio());
                        }

                        @Override
                        public void doInUI() {
                            if (imagen!=null){
                                String str = imagen.getImage();
                                byte[] miArray = str.getBytes(StandardCharsets.ISO_8859_1);
                                oficio.setImage(miArray);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(miArray, 0, miArray.length);
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                }
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

        create.setOnClickListener(view -> {
            if (!nombre.getText().toString().matches("") && !apellidos.getText().toString().matches("")){
                showProgress();
                executeCall(new CallInterface() {
                    Empleado e;
                    boolean result = false;
                    @Override
                    public void doInBackground() {
                       result = Model.getInstance().createUser(
                               new Empleado(
                                       nombre.getText().toString(),
                                       apellidos.getText().toString(),
                                       spinner.getSelectedItemPosition()+1
                               ),getApplicationContext());
                    }
                    @Override
                    public void doInUI() {
                        hideProgress();
                        if (e==null){
                            Toast.makeText(DetailedView.this, "No se ha podido insertar el usuario.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailedView.this, "Usuario insertado correctamente.", Toast.LENGTH_SHORT).show();
                            Model.getInstance().addUser(e);
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Debes rellenar todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });

        update.setOnClickListener(view->{
            if (!nombre.getText().toString().matches("") && !apellidos.getText().toString().matches("")){
                showProgress();
                executeCall(new CallInterface() {
                    boolean result;
                    @Override
                    public void doInBackground() {
                       result = Model.getInstance().updateUser(
                                new Empleado(empleado.getIdEmpleado(),
                                        nombre.getText().toString(),
                                        apellidos.getText().toString(),
                                        spinner.getSelectedItemPosition()+1), getApplicationContext());
                    }
                    @Override
                    public void doInUI() {
                        hideProgress();
                        if (!result){
                            Toast.makeText(DetailedView.this, "No se ha podido actualizar el usuario.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailedView.this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Debes rellenar todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}