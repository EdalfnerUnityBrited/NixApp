package com.example.nixapp.UI.usuario.eventosProximos;

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
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.EventosListResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosProximosFragment extends Fragment {

    NixClient nixClient;
    NixService nixService;
    List<Eventos> eventosUsuario= new ArrayList<>();
    RecyclerView eventosP;
    EventosProximosReciclerView adapterEventos;
    Calendar currentTime = Calendar.getInstance();
    private EventosProximosFragment.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_eventos_proximos, container, false);
        eventosP = view.findViewById(R.id.reciclerEventosP);
        retrofitinit();
        Call<EventosListResult> calls = nixService.eventosAsistenciaUsuario();
        calls.enqueue(new Callback<EventosListResult>() {
            @Override
            public void onResponse(Call<EventosListResult> call, Response<EventosListResult> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(getActivity(),"Objetos Obtenidos Correctamente",Toast.LENGTH_LONG).show();
                    eventosUsuario = response.body().eventos;
                    if(eventosUsuario == null)
                    {
                        Toast.makeText(getActivity(),"Aun no tienes eventos de los que seras parte :o",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        List<Eventos> eventosProximos= new ArrayList<>();
                        for (Eventos event : eventosUsuario)
                        {

                            if(event.getEstado().equals("confirmado"))
                            {
                                String[] fechanueva= event.getFecha().split("-");
                                int dia=Integer.parseInt(fechanueva[2]);
                                int mes= Integer.parseInt(fechanueva[1]);
                                int ano=Integer.parseInt(fechanueva[0]);
                                if(ano >= currentTime.get(Calendar.YEAR))
                                {
                                    if((currentTime.get(Calendar.MONTH)+1)< mes|| ((currentTime.get(Calendar.MONTH)+1) == mes && currentTime.get(Calendar.DAY_OF_MONTH) < dia))
                                    {
                                        eventosProximos.add(event);
                                    }
                                }


                            }
                        }
                        adapterEventos=  new EventosProximosReciclerView(eventosProximos, mListener);
                        eventosP.setAdapter(adapterEventos);
                        eventosP.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }

                }
                else
                {
                    Toast.makeText(getActivity(),response.errorBody().toString(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EventosListResult> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();
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
        void onListFragmentInteraction(Eventos item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventosProximosFragment.OnListFragmentInteractionListener) {
            mListener = (EventosProximosFragment.OnListFragmentInteractionListener) context;
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
