package com.example.myrecyclerviewexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.ImagenRecibida;
import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Empleado;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Empleado> listaEmpleados;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Handler handler = new Handler(Looper.getMainLooper());

    public MyRecyclerViewAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listaEmpleados = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.simple_element,parent,false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        List<Oficio> listaOficios = Model.getInstance().getOficios();

        Empleado u = listaEmpleados.get(position);
        Log.d("bla", u.getIdOficio()+"");
        holder.title.setText(u.getApellidos() + ", " + u.getNombre());

        int posicionDelOficio = listaOficios.indexOf(new Oficio(u.getIdOficio(), ""));
        Oficio oficioEmpleado = listaOficios.get(posicionDelOficio);
        holder.subtitle.setText(oficioEmpleado.getDescripcion());
            executeCall(new CallInterface() {
                ImagenRecibida imagen;
                byte[] miArray;

                @Override
                public void doInBackground() {
                    imagen = Connector.getConector().get(ImagenRecibida.class, "oficios/images/" + u.getIdOficio());
                }
                @Override
                public void doInUI() {
                    String str = imagen.getImage();
                    miArray = str.getBytes(StandardCharsets.ISO_8859_1);
                    Bitmap btmp = BitmapFactory.decodeByteArray(miArray, 0, miArray.length);
                    holder.image.setImageBitmap(btmp);
                }
            });
    }

    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setUsuarios(List<Empleado> empleadoList) {
        this.listaEmpleados = empleadoList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView subtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }

    protected void executeCall(CallInterface callInterface){
        executor.execute(() -> {
            callInterface.doInBackground();
            handler.post(() -> {
                callInterface.doInUI();
            });
        });
    }
    public byte[] getByteFromString(String apiGet){
        byte[] miArray = apiGet.getBytes(StandardCharsets.ISO_8859_1);
        return miArray;
    }
}
