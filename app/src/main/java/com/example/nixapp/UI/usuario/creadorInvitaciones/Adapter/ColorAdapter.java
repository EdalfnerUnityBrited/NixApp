package com.example.nixapp.UI.usuario.creadorInvitaciones.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nixapp.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewholder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
        return new ColorViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewholder holder, int position) {
        holder.color_section.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewholder extends RecyclerView.ViewHolder{

        public CardView color_section;

        public ColorViewholder(@NonNull View itemView) {
            super(itemView);
            color_section = (CardView) itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    private List<Integer> genColorList() {
        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#9f37d0"));
        colorList.add(Color.parseColor("#13181e"));
        colorList.add(Color.parseColor("#00997b"));
        colorList.add(Color.parseColor("#ffa500"));
        colorList.add(Color.parseColor("#ff0000"));
        colorList.add(Color.parseColor("#0000ff"));
        colorList.add(Color.parseColor("#ffc0cb"));
        colorList.add(Color.parseColor("#a52a2a"));
        colorList.add(Color.parseColor("#ffff00"));
        colorList.add(Color.parseColor("#00fff9"));
        colorList.add(Color.parseColor("#2fff00"));
        return  colorList;
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }
}
