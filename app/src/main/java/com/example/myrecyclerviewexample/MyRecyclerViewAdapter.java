package com.example.myrecyclerviewexample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecyclerviewexample.model.Model;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Usuario> list;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;

    public MyRecyclerViewAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = new ArrayList<>();
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

        List<Oficio> oficios = Model.getInstance().getOficios();
        Usuario u = list.get(position);
        holder.title.setText(u.getApellidos() + ", " + u.getNombre());
        holder.subtitle.setText(
                         oficios.stream()
                        .filter(o->o.getIdOficio()==u.getOficio())
                        .findFirst()
                        .get()
                        .getDescripcion()
        );
        switch (u.getOficio()){
            case 1 : holder.image.setImageResource(R.mipmap.ic_1_foreground);
            break;
            case 2 : holder.image.setImageResource(R.mipmap.ic_2_foreground);
                break;
            case 3 : holder.image.setImageResource(R.mipmap.ic_3_foreground);
                break;
            case 4 : holder.image.setImageResource(R.mipmap.ic_4_foreground);
                break;
            case 5 : holder.image.setImageResource(R.mipmap.ic_5_foreground);
                break;
            case 6 : holder.image.setImageResource(R.mipmap.ic_6_foreground);
                break;
            case 7 : holder.image.setImageResource(R.mipmap.ic_7_foreground);
                break;
            case 8 : holder.image.setImageResource(R.mipmap.ic_8_foreground);
                break;
            case 9 : holder.image.setImageResource(R.mipmap.ic_9_foreground);
                break;
            case 10 : holder.image.setImageResource(R.mipmap.ic_10_foreground);
                break;
            case 11 : holder.image.setImageResource(R.mipmap.ic_11_foreground);
                break;
            case 12 : holder.image.setImageResource(R.mipmap.ic_12_foreground);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setUsuarios(List<Usuario> usuarioList) {
        this.list = usuarioList;
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
}
