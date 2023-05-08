package com.example.myrecyclerviewexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    private ImageView imageView;
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
        imageView = findViewById(R.id.imageView);

        MODO m = (MODO)getIntent().getExtras().getSerializable("mode");
        if (m==MODO.UPDATE){
            create.setVisibility(View.GONE);
            usuario = (Usuario) getIntent().getExtras().getSerializable("user");
            nombre.setText(usuario.getNombre());
            apellidos.setText(usuario.getApellidos());
            spinner.setSelection(usuario.getOficio()-1);
        }else{
            update.setVisibility(View.GONE);
        }

        ArrayAdapter<CharSequence> myAdapter =
                ArrayAdapter.createFromResource(this, R.array.oficios, android.R.layout.simple_spinner_item);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        imageView.setImageResource(R.mipmap.ic_1_foreground);
                        break;
                    case 1:
                        imageView.setImageResource(R.mipmap.ic_2_foreground);
                        break;
                    case 2:
                        imageView.setImageResource(R.mipmap.ic_3_foreground);
                        break;
                    case 3:
                        imageView.setImageResource(R.mipmap.ic_4_foreground);
                        break;
                    case 4:
                        imageView.setImageResource(R.mipmap.ic_5_foreground);
                        break;
                    case 5:
                        imageView.setImageResource(R.mipmap.ic_6_foreground);
                        break;
                    case 6:
                        imageView.setImageResource(R.mipmap.ic_7_foreground);
                        break;
                    case 7:
                        imageView.setImageResource(R.mipmap.ic_8_foreground);
                        break;
                    case 8:
                        imageView.setImageResource(R.mipmap.ic_9_foreground);
                        break;
                    case 9:
                        imageView.setImageResource(R.mipmap.ic_10_foreground);
                        break;
                    case 10:
                        imageView.setImageResource(R.mipmap.ic_11_foreground);
                        break;
                    case 11:
                        imageView.setImageResource(R.mipmap.ic_12_foreground);
                        break;
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