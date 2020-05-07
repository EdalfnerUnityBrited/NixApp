package com.example.nixapp.UI.usuario.BusquedaEventos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.EventosItems;

import java.util.ArrayList;


public class EventosBusquedaAdapter extends ArrayAdapter<EventosItems> {

    public EventosBusquedaAdapter(Context context, ArrayList<EventosItems> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_custom, parent, false
            );
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        EventosItems currentItem = getItem(position);

        if (currentItem != null) {
            imageView.setImageResource(currentItem.getImages());
            textViewName.setText(currentItem.getEventoName());
        }

        return convertView;
    }
}