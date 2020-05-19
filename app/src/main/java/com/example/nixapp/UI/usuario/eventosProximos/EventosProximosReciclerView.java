package com.example.nixapp.UI.usuario.eventosProximos;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;

import java.util.List;

import static com.example.nixapp.R.drawable.ic_privado;
import static com.example.nixapp.R.drawable.ic_public;

public class EventosProximosReciclerView extends RecyclerView.Adapter<EventosProximosReciclerView.ViewHolder> {

    private final List<Eventos> mValues;
    private final EventosProximosFragment.OnListFragmentInteractionListener mListener;

    public EventosProximosReciclerView(List<Eventos> items, EventosProximosFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_eventos_list_tendencia, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if(holder.mItem.getPrivacidad() == 0)
        {
            holder.privacidad.setImageResource(ic_public);
        }
        else
        {
            holder.privacidad.setImageResource(ic_privado);
        }
        holder.nombre.setText(String.valueOf(holder.mItem.getNombre_evento()));
        holder.statusUsuario.setText(String.valueOf(holder.mItem.getFecha()));
        holder.direccion.setText(String.valueOf(holder.mItem.getLugar()));
        holder.cupo.setText(String.valueOf(holder.mItem.getCupo())+" Personas");


        Glide.with(holder.eventImage)
                .load(String.valueOf(holder.mItem.getFotoPrincipal()))
                .into(holder.eventImage);

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
        public final TextView nombre;
        public final TextView statusUsuario, direccion, cupo;
        public final ImageView eventImage, privacidad;
        public  final Button btnBorrar, btnEditar;
        public Eventos mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nombre = view.findViewById(R.id.textViewTitulo);
            statusUsuario= view.findViewById(R.id.textViewFecha);
            direccion=view.findViewById(R.id.textViewContenido);
            privacidad= view.findViewById(R.id.textViewPrivacidad);
            cupo= view.findViewById(R.id.textViewCupo);
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
