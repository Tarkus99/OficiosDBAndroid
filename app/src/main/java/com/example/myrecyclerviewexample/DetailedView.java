package com.example.myrecyclerviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class DetailedView extends BaseActivity {

    public static enum MODO{
        UPDATE, CREATE;
    }
    private Usuario usuario;
    private Spinner spinner;
    private String oficio;
    private TextInputEditText nombre, apellidos;

    private Button create, cancelar, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        spinner = findViewById(R.id.spinner);
        create = findViewById(R.id.aceptar);
        update = findViewById(R.id.update);
        cancelar = findViewById(R.id.cancelar);
        nombre = findViewById(R.id.txtNombre);
        apellidos = findViewById(R.id.txtApellidos);

        MODO m = (MODO)getIntent().getExtras().getSerializable("mode");
        if (m==MODO.UPDATE){
            create.setVisibility(View.GONE);
            usuario = (Usuario) getIntent().getExtras().getSerializable("user");
            nombre.setText(usuario.getNombre());
            apellidos.setText(usuario.getApellidos());
        }else{
            update.setVisibility(View.GONE);
        }

        ArrayAdapter<CharSequence> myAdapter =
                ArrayAdapter.createFromResource(this, R.array.oficios, android.R.layout.simple_spinner_item);
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

        create.setOnClickListener(view -> {
            if (!nombre.getText().toString().matches("") && !apellidos.getText().toString().matches("")){
                showProgress();
                executeCall(new CallInterface() {
                    boolean result = false;
                    @Override
                    public void doInBackground() {
                       result = Model.getInstance().insertUser(
                               new Usuario(nombre.getText().toString(),
                                apellidos.getText().toString(),
                                spinner.getSelectedItemPosition()+1));
                    }
                    @Override
                    public void doInUI() {
                        hideProgress();
                        if (!result){
                            Toast.makeText(DetailedView.this, "No se ha podido insertar el usuario.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailedView.this, "Usuario insertado correctamente.", Toast.LENGTH_SHORT).show();
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
                    int result = 0;
                    @Override
                    public void doInBackground() {
                        result = Model.getInstance().updateUser(
                                new Usuario(usuario.getId(),
                                        nombre.getText().toString(),
                                        apellidos.getText().toString(),
                                        spinner.getSelectedItemPosition()+1));
                    }
                    @Override
                    public void doInUI() {
                        hideProgress();
                        if (result==0){
                            Toast.makeText(DetailedView.this, "No se ha podido actualizar el usuario.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailedView.this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
                            //usuario = Model.getInstance().getUsuarios().get(usuario.getId()-1);

                            usuario.setNombre(nombre.getText().toString());
                            usuario.setApellidos(apellidos.getText().toString());
                            usuario.setOficio(spinner.getSelectedItemPosition()+1);

                            Toast.makeText(DetailedView.this, usuario.getApellidos(), Toast.LENGTH_LONG).show();
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