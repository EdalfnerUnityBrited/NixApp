package com.example.nixapp.UI.proveedor.serviciosProximos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Chat;
import com.example.nixapp.R;

import java.util.List;

public class PruebaChatsProveedorRecyclerViewAdapter extends RecyclerView.Adapter<PruebaChatsProveedorRecyclerViewAdapter.ViewHolder> {

    private final List<Chat> mValues;
    private final ChatsFragmentProveedor.OnListFragmentInteractionListener mListener;

    public PruebaChatsProveedorRecyclerViewAdapter(List<Chat> items, ChatsFragmentProveedor.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chats_list, parent, false);
        return new ViewHolder(view);

        
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.id_usuario.setText("Anfitrion: "+String.valueOf(holder.mItem.getName()));
        holder.statusUsuario.setText("Correo: "+ String.valueOf(holder.mItem.email));

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
        public Chat mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           id_usuario = view.findViewById(R.id.textViewTitulo);
           statusUsuario= view.findViewById(R.id.textViewFecha);

        }



        @Override
        public String toString() {
            return super.toString() + " '" + id_usuario.getText() + "'";
        }

    }
}
