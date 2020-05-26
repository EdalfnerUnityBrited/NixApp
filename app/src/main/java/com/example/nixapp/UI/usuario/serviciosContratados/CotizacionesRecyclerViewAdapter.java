package com.example.nixapp.UI.usuario.serviciosContratados;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Cotizacion;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.BusquedaServicios.BuscarServicios;

import org.bouncycastle.asn1.x509.Holder;

import java.util.List;

public class CotizacionesRecyclerViewAdapter extends RecyclerView.Adapter<CotizacionesRecyclerViewAdapter.ViewHolder> {

    private final List<Cotizacion> mValues;
    private final CotizacionesGuardadasFragment.OnListFragmentInteractionListener mListener;

    public CotizacionesRecyclerViewAdapter(List<Cotizacion> items, CotizacionesGuardadasFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_articulos_servicios_list_buscar, parent, false);
        return new ViewHolder(view);

        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nombre.setText(String.valueOf(holder.mItem.getTotal()));
        holder.nombreServicio.setText(holder.mItem.getNombre());
        holder.mView.setOnClickListener(new View.OnClickListener() {
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
        public Cotizacion mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           nombre = view.findViewById(R.id.textViewTitulo);
           nombreServicio= view.findViewById(R.id.nombreServicio);
        eventImage=view.findViewById(R.id.imageViewEvento);


        }



        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }

    }
}
