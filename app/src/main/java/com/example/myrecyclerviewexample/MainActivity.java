package com.example.myrecyclerviewexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myrecyclerviewexample.base.BaseActivity;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, CallInterface {

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

        ActivityResultLauncher<Intent> someActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_CANCELED){

                    }else{
                        String nombre = (String)result.getData().getExtras().getSerializable("nombre");
                        String apellidos = (String)result.getData().getExtras().getSerializable("apellidos");
                        int oficio = (int)result.getData().getExtras().getSerializable("oficio");
                        int idUsuario = Model.getInstance().getUsuarios().size();
                        Usuario u = new Usuario(idUsuario, nombre, apellidos, oficio);

                        Model.getInstance().addUser(u);

                        myRecyclerViewAdapter.setUsuarios(Model.getInstance().getUsuarios());
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });

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
        ActivityResultLauncher<Intent> detailedViewLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode()==RESULT_OK){
                        myRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
        Intent intent = new Intent(getApplicationContext(), DetailedView.class);
        intent.putExtra("mode", DetailedView.MODO.UPDATE);
        intent.putExtra("user", u);
        detailedViewLaunch.launch(intent);
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