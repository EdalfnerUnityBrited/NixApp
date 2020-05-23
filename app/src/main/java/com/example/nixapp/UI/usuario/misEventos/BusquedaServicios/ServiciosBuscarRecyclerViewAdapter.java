package com.example.nixapp.UI.usuario.misEventos.BusquedaServicios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.misServicios.CrearServiciosFragmentProveedor;

import java.util.List;

public class ServiciosBuscarRecyclerViewAdapter extends RecyclerView.Adapter<ServiciosBuscarRecyclerViewAdapter.ViewHolder> {

    private final List<CatalogoServicios> mValues;
    private final BuscarServicios.OnListFragmentInteractionListener mListener;

    public ServiciosBuscarRecyclerViewAdapter(List<CatalogoServicios> items, BuscarServicios.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_articulos_servicios_list, parent, false);
        return new ViewHolder(view);

        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nombre.setText(String.valueOf(holder.mItem.getNombre()));
        holder.nombre.setOnClickListener(new View.OnClickListener() {
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
        public final TextView nombre;
        public final ImageView eventImage;
        public  final Button btnBorrar, btnEditar;
        public CatalogoServicios mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           nombre = view.findViewById(R.id.textViewTitulo);
           btnBorrar=view.findViewById(R.id.buttonDelete);
           btnEditar=view.findViewById(R.id.buttonEdit);
        eventImage=view.findViewById(R.id.imageViewEvento);


        }



        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }

    }
}
