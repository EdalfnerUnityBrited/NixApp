package com.example.nixapp.UI.usuario.serviciosContratados;

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

import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ContratacionesListResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiciosProximosFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Contrataciones> contratacionesList;
    ServiciosProximosViewAdapter adapterChats;
    NixService nixService;
    NixClient nixClient;
    Usuario usuario;
    private ServiciosProximosFragment.OnListFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_servicios_proximos,container,false);
        retrofitinit();
        recyclerView= view.findViewById(R.id.recyclerCont);
        contratacionesList= new ArrayList<>();
        Call<ContratacionesListResult> call= nixService.contratacionesGeneral();
        call.enqueue(new Callback<ContratacionesListResult>() {
            @Override
            public void onResponse(Call<ContratacionesListResult> call, Response<ContratacionesListResult> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "Servicios Cargados", Toast.LENGTH_SHORT).show();
                    contratacionesList=  response.body().contrataciones;
                    adapterChats= new ServiciosProximosViewAdapter(contratacionesList, mListener);
                    recyclerView.setAdapter(adapterChats);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                else{
                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContratacionesListResult> call, Throwable t) {

            }
        });

        return view;
    }


    private void retrofitinit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Contrataciones item);
        void onClick(View v);

        void onClickDelete(Contrataciones mItem);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiciosProximosFragment.OnListFragmentInteractionListener) {
            mListener = (ServiciosProximosFragment.OnListFragmentInteractionListener) context;
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
