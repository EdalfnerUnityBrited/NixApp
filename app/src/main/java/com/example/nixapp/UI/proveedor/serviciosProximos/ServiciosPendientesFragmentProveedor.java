package com.example.nixapp.UI.proveedor.serviciosProximos;

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

import com.example.nixapp.DB.Chat;
import com.example.nixapp.DB.Contrataciones;
import com.example.nixapp.DB.Cotizacion;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.serviciosContratados.CotizacionesGuardadasFragment;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ContratacionesListResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiciosPendientesFragmentProveedor extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Contrataciones> contratacionesList;
    ContratacionesProveedorRecyclerViewAdapter adapterChats;
    NixService nixService;
    NixClient nixClient;
    Usuario usuario;
    private ServiciosPendientesFragmentProveedor.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_pendientes_proveedor,container,false);
        retrofitinit();
        recyclerView= view.findViewById(R.id.recyclerCont);
        contratacionesList= new ArrayList<>();
        Call<ContratacionesListResult> call= nixService.contrataciones();
        call.enqueue(new Callback<ContratacionesListResult>() {
            @Override
            public void onResponse(Call<ContratacionesListResult> call, Response<ContratacionesListResult> response) {
                if (response.isSuccessful()){
                    contratacionesList=  response.body().contrataciones;
                    adapterChats= new ContratacionesProveedorRecyclerViewAdapter(contratacionesList, mListener);
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Contrataciones item);
        void onClick(View v);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ServiciosPendientesFragmentProveedor.OnListFragmentInteractionListener) {
            mListener = (ServiciosPendientesFragmentProveedor.OnListFragmentInteractionListener) context;
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
