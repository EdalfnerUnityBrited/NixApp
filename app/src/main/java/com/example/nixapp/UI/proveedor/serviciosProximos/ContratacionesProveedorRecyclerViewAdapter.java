package com.example.nixapp.UI.proveedor.serviciosProximos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.DB.Cotizacion;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.serviciosContratados.CotizacionesGuardadasFragment;

import java.util.List;

public class ContratacionesProveedorRecyclerViewAdapter extends RecyclerView.Adapter<ContratacionesProveedorRecyclerViewAdapter.ViewHolder> {

    private final List<Contrataciones> mValues;
    private final ServiciosPendientesFragmentProveedor.OnListFragmentInteractionListener mListener;

    public ContratacionesProveedorRecyclerViewAdapter(List<Contrataciones> items, ServiciosPendientesFragmentProveedor.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cotizaciones_buscar, parent, false);
        return new ViewHolder(view);

        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nombre.setText(String.valueOf(holder.mItem.getFecha()));
        holder.nombreServicio.setText(holder.mItem.getNombre_evento());
        holder.nombreServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        public final TextView nombre, nombreServicio;
        public final ImageView eventImage;
        public Contrataciones mItem;
        public final Button borrar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           nombre = view.findViewById(R.id.textViewTitulo);
           nombreServicio= view.findViewById(R.id.nombreServicio);
        eventImage=view.findViewById(R.id.imageViewEvento);
        borrar=view.findViewById(R.id.buttonDelete);


        }



        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }

    }
}
