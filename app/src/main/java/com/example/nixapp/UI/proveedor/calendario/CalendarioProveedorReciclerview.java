package com.example.nixapp.UI.proveedor.calendario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarioProveedorReciclerview extends RecyclerView.Adapter<CalendarioProveedorReciclerview.MyViewHolder> {
    List<CalendarioProveedor.ServicioCorregido> ServiciosC = new ArrayList<>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public CalendarioProveedorReciclerview(List<CalendarioProveedor.ServicioCorregido> servicios) {
        this.ServiciosC = servicios;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //public View textView;
        public final TextView nombser;
        public final TextView nombevento;
        public final TextView fecha;
        public final TextView hora;
        public final TextView estado_servicio;
        public MyViewHolder(View v) {
            super(v);
            nombser = v.findViewById(R.id.nombre_serv);
            nombevento = v.findViewById(R.id.nomb_event);
            fecha = v.findViewById(R.id.textViewFecha);
            hora = v.findViewById(R.id.hora_even);
            estado_servicio = v.findViewById(R.id.estado_asistencia);
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public CalendarioProveedorReciclerview.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_calendario_proveedor_list, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombser.setText("Servicio: " + ServiciosC.get(position).nombre_servicio);
        holder.nombevento.setText("Al evento: "+ ServiciosC.get(position).nombre_evento);
        holder.fecha.setText("El dia: " + ServiciosC.get(position).fecha_servicio + " a las: " + ServiciosC.get(position).hora_servicio);
        holder.hora.setText("Lugar: " + ServiciosC.get(position).direccion);
        holder.estado_servicio.setText("Estado del servicio: "+ServiciosC.get(position).estado);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ServiciosC.size();
    }
}

