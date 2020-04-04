package com.example.nixapp.UI.usuario.misEventos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;

import java.util.List;

public class PruebaEventosRecyclerViewAdapter extends RecyclerView.Adapter<PruebaEventosRecyclerViewAdapter.ViewHolder> {

    private final List<Eventos> mValues;
    private final EventosCerradosFragment.OnListFragmentInteractionListener mListener;

    public PruebaEventosRecyclerViewAdapter(List<Eventos> items, EventosCerradosFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_eventos_list, parent, false);
        return new ViewHolder(view);

        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.id_usuario.setText(String.valueOf(holder.mItem.getNombre_evento()));
        holder.statusUsuario.setText(String.valueOf(holder.mItem.getFecha()));

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
        public final TextView id_usuario;
        public final TextView statusUsuario;
        public Eventos mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           id_usuario = view.findViewById(R.id.textViewNo_temporada);
           statusUsuario= view.findViewById(R.id.textViewNombre);

        }



        @Override
        public String toString() {
            return super.toString() + " '" + id_usuario.getText() + "'";
        }

    }
}
