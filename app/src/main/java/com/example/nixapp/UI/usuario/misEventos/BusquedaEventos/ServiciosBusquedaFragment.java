package com.example.nixapp.UI.usuario.misEventos.BusquedaEventos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Eventos;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.CrearEvento;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;

import java.util.ArrayList;
import java.util.List;

public class ServiciosBusquedaFragment extends Fragment implements View.OnClickListener {
    View view;
    RecyclerView recyclerView;
    List<Eventos> eventosList;
    ServiciosRecyclerViewAdapter adapterEventos;
    NixService nixService;
    NixClient nixClient;
    Eventos eventos;
    private OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos_buscar, container, false);
        recyclerView= view.findViewById(R.id.recyclerUserEvents);
        eventosList= new ArrayList<>();
        retrofitInit();
        BuscarServicios activity = (BuscarServicios) getActivity();
        eventosList= activity.getDataFragment();
        recyclerView.setAdapter(adapterEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;




    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nuevo_evento:{
                getActivity().finish();
                Intent intent = new Intent(getActivity(), CrearEvento.class);
                startActivity(intent);
                break;
            }
        }
    }
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Eventos item);
        void onClickDelete(Eventos item);
        void onClickEdit(Eventos item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiciosBusquedaFragment.OnListFragmentInteractionListener) {
            mListener = (ServiciosBusquedaFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
