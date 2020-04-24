package com.example.nixapp.UI.usuario.Calendario;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nixapp.UI.welcome.MainActivity;
import com.example.nixapp.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarioReciclerview extends RecyclerView.Adapter<CalendarioReciclerview.MyViewHolder> {
    List<Calendario.EventosCorregidos> EventosC = new ArrayList<>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public CalendarioReciclerview(List<Calendario.EventosCorregidos> eventos) {
        this.EventosC = eventos;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public View textView;
        public final TextView titulo;
        public final TextView direccion;
        public final TextView fecha;
        public final TextView cupo;
        public final TextView asistencia;
        public final ImageView tipo_evento;
        public MyViewHolder(View v) {
            super(v);
            titulo = v.findViewById(R.id.titulo);
            direccion = v.findViewById(R.id.textViewDireccion);
            fecha = v.findViewById(R.id.textViewFecha);
            cupo = v.findViewById(R.id.textViewCupo);
            tipo_evento = v.findViewById(R.id.imageViewEvento);
            asistencia = v.findViewById(R.id.estado_asistencia);
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public CalendarioReciclerview.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_calendario_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String nombre = EventosC.get(position).titulo;
        String direcc = EventosC.get(position).direccion;
        Calendar fecha = EventosC.get(position).fecha;
        int cupo = EventosC.get(position).cupo;
        int year = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        holder.titulo.setText(nombre);
        holder.direccion.setText(direcc);
        holder.cupo.setText(String.valueOf(cupo)+ " personas");
        holder.fecha.setText(dia+"/"+(mes+1)+"/"+year);
        if(EventosC.get(position).estado_asistencia.equals("creador"))
        {
            holder.asistencia.setText("Tu lo creaste");
            holder.asistencia.setTextColor(Color.parseColor("#1C8FD6"));
        }
        else if(EventosC.get(position).estado_asistencia.equals("Me interesa"))
        {
            holder.asistencia.setText("Te interesa");
            holder.asistencia.setTextColor(Color.parseColor("#F59710"));
        }
        else
        {
            holder.asistencia.setText("Asistiras");
            holder.asistencia.setTextColor(Color.parseColor("#3CDA3C"));
        }

        Glide.with(holder.tipo_evento)
                .load(EventosC.get(position).imagen)
                .into(holder.tipo_evento);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return EventosC.size();
    }
}

