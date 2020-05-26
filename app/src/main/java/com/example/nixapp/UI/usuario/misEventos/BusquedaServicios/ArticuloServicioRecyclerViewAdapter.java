package com.example.nixapp.UI.usuario.misEventos.BusquedaServicios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.Articulos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.CotizacionPorServico.CotizacionServicio;

import java.util.ArrayList;
import java.util.List;

public class ArticuloServicioRecyclerViewAdapter extends RecyclerView.Adapter<ArticuloServicioRecyclerViewAdapter.ViewHolder> {

    public List<Articulos> getmValues() {
        return mValues;
    }

    private final List<Articulos> mValues;
    private final CotizacionServicio.OnListFragmentInteractionListener mListener;
    String digitos;

    public List<ViewHolder> getVistas() {
        return vistas;
    }

    List<ViewHolder> vistas = new ArrayList<>();

    public ArticuloServicioRecyclerViewAdapter(List<Articulos> items, CotizacionServicio.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_articulos, parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        vistas.add(holder);
        holder.mItem = mValues.get(position);
        holder.nombre.setText(String.valueOf(holder.mItem.getNombre()));
        holder.price.setText(String.valueOf(holder.mItem.getPrecio()));
        holder.precioPor.setText(String.valueOf(holder.mItem.getPrecioPor()));
        holder.desc.setText(String.valueOf(holder.mItem.getDescripcion()));
        Glide.with(holder.articleImage)
                .load(String.valueOf(holder.mItem.getFotoArticulo()))
                .into(holder.articleImage);
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
        holder.btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    holder.CambioDigitoMas();
                    digitos = String.valueOf(holder.cantidad.getText());
                    mListener.onClickAdd(holder.mItem);
                }
            }
        });
        holder.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    digitos = String.valueOf(holder.cantidad.getText());
                    mListener.onClickSub(holder.mItem);
                    holder.CambioDigitoMenos();
                    digitos = String.valueOf(holder.cantidad.getText());
                }
            }
        });
    }

    public String numeros()
    {
        return digitos;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nombre, desc, precioPor, price, cantidad;
        public final ImageView articleImage;
        public final Button btnMas, btnMenos;
        public Articulos mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           nombre = view.findViewById(R.id.idProductName);
           btnMas=view.findViewById(R.id.idAddICon);
           btnMenos=view.findViewById(R.id.idMinusICon);
            articleImage=view.findViewById(R.id.idProductImage);
            desc= view.findViewById(R.id.idProductPrice);
            precioPor=view.findViewById(R.id.precio_por);
            price=view.findViewById(R.id.idProductWeight);
            cantidad= view.findViewById(R.id.numero_pedido);
            digitos = String.valueOf(cantidad.getText());

        }

        @Override
        public String toString() {
            return super.toString() + " '" + nombre.getText() + "'";
        }

        public void CambioDigitoMas()
        {
            String numeros = String.valueOf(cantidad.getText());
            int numeroActual = Integer.valueOf(numeros) + 1;
            cantidad.setText(String.valueOf(numeroActual));
        }
        public void CambioDigitoMenos()
        {
            String numeros = String.valueOf(cantidad.getText());
            if(!numeros.equals("0"))
            {
                int numeroActual = Integer.valueOf(numeros) - 1;
                cantidad.setText(String.valueOf(numeroActual));
            }

        }
        public void sumarArticulos()
        {

        }

    }
}
