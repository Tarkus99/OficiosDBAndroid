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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Empleado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
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
                Empleado u = Model.getInstance().getEmpleados().get(position);
                executeCall(new CallInterface() {
                    Empleado e = null;
                    @Override
                    public void doInBackground() {
                        //Model.getInstance().deleteUser(u);
                         e = connector.delete(Empleado.class, "usuarios/"+u.getIdEmpleado());
                    }

                    @Override
                    public void doInUI() {
                        if (e != null) {
                            myRecyclerViewAdapter.notifyItemRemoved(position);
                            Model.getInstance().getEmpleados().remove(u);
                            Snackbar.make(recyclerView, "Deleted " + u.getNombre(), Snackbar.LENGTH_LONG)
                                    .setAction("Undo", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            executeCall(new CallInterface() {
                                                @Override
                                                public void doInBackground() {
                                                    e = connector.post(Empleado.class, u,"usuarios");
                                                }

                                                @Override
                                                public void doInUI() {
                                                    if (e!=null) {
                                                        Model.getInstance().addUser(u);
                                                        Toast.makeText(MainActivity.this, "Operación cancelada", Toast.LENGTH_SHORT).show();
                                                        myRecyclerViewAdapter.notifyItemInserted(position);
                                                    }
                                                }
                                            });
                                        }
                                    })
                                    .show();
                        }
                    }
                });
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);


        someActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == RESULT_OK) {
                        myRecyclerViewAdapter.setUsuarios(Model.getInstance().getEmpleados());
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });

        detailActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Model.getInstance().getEmpleados().sort(Empleado::compareTo);
                        myRecyclerViewAdapter.setUsuarios(Model.getInstance().getEmpleados());
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
        );

        addUser.setOnClickListener(view ->
        {
            Intent intent = new Intent(getApplicationContext(), DetailedView.class);
            intent.putExtra("mode", DetailedView.MODO.CREATE);
            someActivityResult.launch(intent);
        });

        showProgress();
        executeCall(this);
    }

    @Override
    public void onClick(View view) {
        Empleado u = Model.getInstance().getEmpleados().get(recyclerView.getChildAdapterPosition(view));

        Intent intent = new Intent(getApplicationContext(), DetailedView.class);
        intent.putExtra("mode", DetailedView.MODO.UPDATE);
        intent.putExtra("user", u);
        detailActivityLauncher.launch(intent);
    }

    @Override
    public void doInBackground() {
        Model.getInstance().getEmpleados();
        Model.getInstance().getOficios();
    }

    @Override
    public void doInUI() {
        hideProgress();
        List<Empleado> empleadoList = Model.getInstance().getEmpleados();
        Log.d("caca", empleadoList.get(1).getNombre());
        myRecyclerViewAdapter.setUsuarios(empleadoList);
    }
}