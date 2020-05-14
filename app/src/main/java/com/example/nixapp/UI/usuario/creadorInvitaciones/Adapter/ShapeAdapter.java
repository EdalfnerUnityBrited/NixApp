package com.example.nixapp.UI.usuario.creadorInvitaciones.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.R;

import java.util.ArrayList;
import java.util.List;

public class ShapeAdapter extends RecyclerView.Adapter<ShapeAdapter.ShapeViewHolder> {

    Context context;
    List<Integer> frameList;
    ShapeAdapterListener listener;

    int row_selected = -1;

    public ShapeAdapter(Context context, ShapeAdapterListener listener) {
        this.context = context;
        this.frameList = getShapeList();
        this.listener = listener;
    }

    private List<Integer> getShapeList() {
        List<Integer> result = new ArrayList<>();
        result.add(R.drawable.circulo);
        result.add(R.drawable.triangulo);
        result.add(R.drawable.rectangulo);
        result.add(R.drawable.cuadrado);
        result.add(R.drawable.rombo);
        return  result;
    }

    @NonNull
    @Override
    public ShapeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.shape_item,parent,false);
        return new ShapeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShapeViewHolder holder, int position) {
        if (row_selected==position){
            holder.img_check.setVisibility(View.VISIBLE);
        }
        else{
            holder.img_check.setVisibility(View.INVISIBLE);
        }
        holder.img_frame.setImageResource(frameList.get(position));
        
    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class ShapeViewHolder extends RecyclerView.ViewHolder{
        ImageView img_check,img_frame;

        public ShapeViewHolder(@NonNull View itemView) {
            super(itemView);
            img_check = (ImageView) itemView.findViewById(R.id.img_check_editor_shape);
            img_frame = (ImageView) itemView.findViewById(R.id.img_shape);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onShapeSelected(frameList.get(getAdapterPosition()));

                    row_selected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface ShapeAdapterListener{
        void onShapeSelected(int frame);
    }
}
