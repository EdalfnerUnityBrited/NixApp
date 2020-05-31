package com.example.nixapp.UI.usuario.misEventos;

import android.content.Context;
import android.os.Bundle;
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
import com.example.nixapp.UI.usuario.eventosProximos.EventosProximosFragment;
import com.example.nixapp.UI.usuario.eventosProximos.EventosProximosReciclerView;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosListResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialFragment extends Fragment {
    NixClient nixClient;
    NixService nixService;
    List<Eventos> eventosUsuario= new ArrayList<>();
    RecyclerView eventosP;
    EventosHistorialReciclerView adapterEventos;
    Calendar currentTime = Calendar.getInstance();
    private HistorialFragment.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view= inflater.inflate(R.layout.fragment_historial,container,false);
        retrofitinit();
        eventosP=view.findViewById(R.id.eventosRecycler);
        Call<EventosListResult> call = nixService.eventosHistorialUsuario();
        call.enqueue(new Callback<EventosListResult>() {
            @Override
            public void onResponse(Call<EventosListResult> call, Response<EventosListResult> response) {
                if(response.isSuccessful()){
                    List<Eventos> eventosList = new ArrayList<>();
                    eventosList= response.body().eventos;
                    adapterEventos=  new EventosHistorialReciclerView(eventosList, mListener);
                    eventosP.setAdapter(adapterEventos);
                    eventosP.setLayoutManager(new LinearLayoutManager(getActivity()));

                }
                else{
                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventosListResult> call, Throwable t) {

            }
        });

        return view;
    }
    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name

        void onFragmentInteraction(Eventos mItem);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistorialFragment.OnListFragmentInteractionListener) {
            mListener = (HistorialFragment.OnListFragmentInteractionListener) context;
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
