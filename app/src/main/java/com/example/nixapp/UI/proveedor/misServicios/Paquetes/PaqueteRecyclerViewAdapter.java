package com.example.nixapp.UI.proveedor.misServicios.Paquetes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.EventosCerradosFragment;

import java.util.List;

import static com.example.nixapp.R.drawable.ic_privado;
import static com.example.nixapp.R.drawable.ic_public;

public class PaqueteRecyclerViewAdapter extends RecyclerView.Adapter<PaqueteRecyclerViewAdapter.ViewHolder> {

    private final List<Paquetes> mValues;
    private final PaquetesFragment.OnListFragmentInteractionListener mListener;

    public PaqueteRecyclerViewAdapter(List<Paquetes> items, PaquetesFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_paquetes_list, parent, false);
        return new ViewHolder(view);

        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nombre.setText(String.valueOf(holder.mItem.getNombre()));
        Glide.with(holder.eventImage)
                .load(String.valueOf(holder.mItem.getFotoPaquete()))
                .into(holder.eventImage);
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
        holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClickDelete(holder.mItem);
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
        public  final Button btnBorrar;
        public Paquetes mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           nombre = view.findViewById(R.id.textViewTitulo);
           btnBorrar=view.findViewById(R.id.buttonDelete);
        eventImage=view.findViewById(R.id.imageViewEvento);


        }



        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }

    }
}
