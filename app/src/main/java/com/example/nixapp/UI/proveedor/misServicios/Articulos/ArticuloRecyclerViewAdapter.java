package com.example.nixapp.UI.proveedor.misServicios.Articulos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.R;

import java.util.List;

public class ArticuloRecyclerViewAdapter extends RecyclerView.Adapter<ArticuloRecyclerViewAdapter.ViewHolder> {

    private final List<Articulos> mValues;
    private final ArticuloFragment.OnListFragmentInteractionListener mListener;

    public ArticuloRecyclerViewAdapter(List<Articulos> items, ArticuloFragment.OnListFragmentInteractionListener listener) {
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
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onClickEdit(holder.mItem);
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
        public TextView nombre;

        public final ImageView eventImage;
        public final Button btnBorrar, btnEditar;
        public Articulos mItem;

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
