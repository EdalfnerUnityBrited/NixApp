package com.example.nixapp.UI.usuario.misEventos;

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
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosListResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosCerradosFragment extends Fragment implements View.OnClickListener {
    View view;
    RecyclerView recyclerView;
    List<Eventos> eventosList;
    PruebaEventosRecyclerViewAdapter adapterEventos;
    NixService nixService;
    NixClient nixClient;
    Eventos eventos;
    private OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventos_cerrados, container, false);
        recyclerView= view.findViewById(R.id.recyclerUserEvents);
        eventosList= new ArrayList<>();
        retrofitInit();
        Call<EventosListResult> call = nixService.eventosUsuario();
        call.enqueue(new Callback<EventosListResult>() {
            @Override
            public void onResponse(Call<EventosListResult> call, Response<EventosListResult> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Eventos Obtenidos correctamente",
                            Toast.LENGTH_SHORT).show();

                    eventosList=  response.body().eventos;
                   adapterEventos=  new PruebaEventosRecyclerViewAdapter(eventosList, mListener);
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
        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.nuevo_evento);
        actionButton.setOnClickListener(this);
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
        if (context instanceof EventosCerradosFragment.OnListFragmentInteractionListener) {
            mListener = (EventosCerradosFragment.OnListFragmentInteractionListener) context;
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
