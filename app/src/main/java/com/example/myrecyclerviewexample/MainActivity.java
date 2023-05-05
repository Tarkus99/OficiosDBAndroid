package com.example.myrecyclerviewexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, CallInterface {

    private ActivityResultLauncher<Intent> detailActivityLauncher, someActivityResult;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private FloatingActionButton addUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        addUser = findViewById(R.id.addUser);

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        myRecyclerViewAdapter.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);

        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(myLinearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Usuario u = Model.getInstance().getUsuarios().get(viewHolder.getAdapterPosition());
                showProgress();
                executeCall(new CallInterface() {
                    int result = 0;
                    @Override
                    public void doInBackground() {
                        result = Model.getInstance().deleteUser(u);
                    }

                    @Override
                    public void doInUI() {
                        hideProgress();
                        if (result==0) {
                            Toast.makeText(MainActivity.this, "Error al borra el usuario.", Toast.LENGTH_SHORT).show();
                        }else{
                            Model.getInstance().getUsuarios().remove(position);
                        }
                    }
                });
                myRecyclerViewAdapter.notifyItemRemoved(position);

                Snackbar.make(recyclerView, "Deleted " + u.getNombre(), Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showProgress();
                                executeCall(new CallInterface() {
                                    @Override
                                    public void doInBackground() {
                                        Model.getInstance().insertUser(u.getNombre(), u.getApellidos(), u.getOficio());
                                    }

                                    @Override
                                    public void doInUI() {
                                        hideProgress();
                                        Model.getInstance().getUsuarios().add(position, u);
                                        myRecyclerViewAdapter.notifyItemInserted(position);
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);





         someActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        String nombre = (String)result.getData().getExtras().getSerializable("nombre");
                        String apellidos = (String)result.getData().getExtras().getSerializable("apellidos");
                        int oficio = (int)result.getData().getExtras().getSerializable("oficio");
                        int idUsuario = Model.getInstance().getUsuarios().size();
                        Usuario u = new Usuario(idUsuario, nombre, apellidos, oficio);

                        Model.getInstance().addUser(u);
                        Model.getInstance().getUsuarios().sort(Usuario::compareTo);

                        myRecyclerViewAdapter.setUsuarios(Model.getInstance().getUsuarios());
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });

        detailActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Model.getInstance().getUsuarios().sort(Usuario::compareTo);
                        myRecyclerViewAdapter.setUsuarios(Model.getInstance().getUsuarios());
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
        );

        addUser.setOnClickListener(view->{
            Intent intent = new Intent(getApplicationContext(), DetailedView.class);
            intent.putExtra("mode", DetailedView.MODO.CREATE);
            someActivityResult.launch(intent);
        });
        showProgress();
        executeCall(this);
    }

    @Override
    public void onClick(View view) {
        Usuario u = Model.getInstance().getUsuarios().get(recyclerView.getChildAdapterPosition(view));

        Intent intent = new Intent(getApplicationContext(), DetailedView.class);
        intent.putExtra("mode", DetailedView.MODO.UPDATE);
        intent.putExtra("user", u);
        detailActivityLauncher.launch(intent);
    }

    @Override
    public void doInBackground() {
        Model.getInstance().getUsuarios();
        Model.getInstance().getOficios();
    }

    @Override
    public void doInUI() {
        hideProgress();
        List<Usuario> usuarioList = Model.getInstance().getUsuarios();
        myRecyclerViewAdapter.setUsuarios(usuarioList);
    }
}