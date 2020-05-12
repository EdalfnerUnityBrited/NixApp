package com.example.nixapp.UI.usuario.eventosProximos;

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
import com.example.nixapp.DB.Notificaciones;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.misEventos.CrearEvento;
import com.example.nixapp.UI.usuario.misEventos.EventosCerradosFragment;
import com.example.nixapp.UI.usuario.misEventos.PruebaEventosRecyclerViewAdapter;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosListResult;
import com.example.nixapp.conn.results.NotificationsResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisNotificacionesFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Notificaciones> notificacionesList;
    NotificacionesRecyclerViewAdapter adapterNotificaciones;
    NixService nixService;
    NixClient nixClient;

    private MisNotificacionesFragment.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_notificaciones, container, false);
        recyclerView= view.findViewById(R.id.recyclerNotifications);
        notificacionesList= new ArrayList<>();
        retrofitInit();
        Call<NotificationsResult> call = nixService.notificaciones();
        call.enqueue(new Callback<NotificationsResult>() {
            @Override
            public void onResponse(Call<NotificationsResult> call, Response<NotificationsResult> response) {
                if (response.isSuccessful()){

                    notificacionesList=response.body().notificaciones;
                    adapterNotificaciones=  new NotificacionesRecyclerViewAdapter(notificacionesList, mListener);
                    recyclerView.setAdapter(adapterNotificaciones);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                else{
                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationsResult> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.nuevo_evento);

        return view;
    }
    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNotificationFragmentInteraction(Notificaciones item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MisNotificacionesFragment.OnListFragmentInteractionListener) {
            mListener = (MisNotificacionesFragment.OnListFragmentInteractionListener) context;
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
