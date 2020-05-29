package com.example.nixapp.UI.proveedor.serviciosProximos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Notificaciones;
import com.example.nixapp.R;

import java.util.List;

public class NotificacionesProveedorRecyclerViewAdapter extends RecyclerView.Adapter<NotificacionesProveedorRecyclerViewAdapter.ViewHolder> {

    private final List<Notificaciones> mValues;
    private final NotificacionesFragmentProveedor.OnListFragmentInteractionListener mListener;

    public NotificacionesProveedorRecyclerViewAdapter(List<Notificaciones> items, NotificacionesFragmentProveedor.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notificaciones_list, parent, false);
        return new ViewHolder(view);

        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.contenido.setText(String.valueOf(holder.mItem.getContenido()));
        if (holder.mItem.getTipoNotificacion()==1){
            holder.nombre.setText("Evento próximo");
        }
        else if (holder.mItem.getTipoNotificacion()==2){
            holder.nombre.setText("Invitacion");
        }
        else{
            holder.nombre.setText("Cita");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onNotificationFragmentInteraction(holder.mItem);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nombre, contenido;
        public Notificaciones mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           nombre = view.findViewById(R.id.textViewTitulo);
           contenido= view.findViewById(R.id.textViewContenido);



        }



        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }

    }
}
