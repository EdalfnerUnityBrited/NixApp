package com.example.nixapp.UI.usuario.Tendencias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.example.nixapp.conn.results.EventosListResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosTendenciasFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Eventos> eventosList;
    EventosTendenciaRecyclerViewAdapter adapterEventos;
    NixService nixService;
    NixClient nixClient;
    Eventos eventos;
    private OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos_tendencia, container, false);
        recyclerView= view.findViewById(R.id.recyclerUserEvents);
        eventosList= new ArrayList<>();
        retrofitInit();
        Call<EventosListResult> call = nixService.eventosTendencia();
        call.enqueue(new Callback<EventosListResult>() {
            @Override
            public void onResponse(Call<EventosListResult> call, Response<EventosListResult> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Eventos Obtenidos correctamente",
                            Toast.LENGTH_SHORT).show();

                    eventosList=  response.body().eventos;
                   adapterEventos=  new EventosTendenciaRecyclerViewAdapter(eventosList, mListener);
                    recyclerView.setAdapter(adapterEventos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                else{
                    Toast.makeText(getActivity(), response.errorBody().toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.i("Error:" ,response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<EventosListResult> call, Throwable t) {
                Toast.makeText(getActivity(), "Eventos Obtenidos incorrectamente",
                        Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Eventos item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventosTendenciasFragment.OnListFragmentInteractionListener) {
            mListener = (EventosTendenciasFragment.OnListFragmentInteractionListener) context;
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
