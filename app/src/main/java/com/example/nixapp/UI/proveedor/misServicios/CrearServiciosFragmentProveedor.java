package com.example.nixapp.UI.proveedor.misServicios;

import android.content.Context;
import android.content.Intent;
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

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.misServicios.Articulos.ArticuloFragment;
import com.example.nixapp.UI.proveedor.misServicios.Articulos.ArticuloRecyclerViewAdapter;
import com.example.nixapp.UI.proveedor.misServicios.Paquetes.PaqueteRecyclerViewAdapter;
import com.example.nixapp.UI.proveedor.misServicios.Paquetes.PaquetesFragment;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ServiciosListResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearServiciosFragmentProveedor extends Fragment {
    NixService nixService;
    NixClient nixClient;
    List<CatalogoServicios> serviciosList;
    RecyclerView recyclerView;
    ServiciosRecyclerViewAdapter adapterEventos;
    Eventos eventos;
    private CrearServiciosFragmentProveedor.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_servicios_proveedor,container,false);
        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.nuevo_servicio);
        recyclerView= view.findViewById(R.id.serviciosRecycler);
        serviciosList= new ArrayList<>();
        retrofitInit();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCrearServicioMenu = new Intent(getActivity(), CrearServicioMenu.class);
                intentCrearServicioMenu.putExtra("id",0);
                startActivity(intentCrearServicioMenu);
            }
        });
        Call<ServiciosListResult> call = nixService.servicioUsuario();
        call.enqueue(new Callback<ServiciosListResult>() {
            @Override
            public void onResponse(Call<ServiciosListResult> call, Response<ServiciosListResult> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "Servicios obtenidos correctamente", Toast.LENGTH_SHORT).show();
                    serviciosList=  response.body().servicios;
                    adapterEventos=  new ServiciosRecyclerViewAdapter(serviciosList, mListener);
                    recyclerView.setAdapter(adapterEventos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                else{
                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiciosListResult> call, Throwable t) {

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
        void onListFragmentInteraction(CatalogoServicios item);
        void onClickDelete(CatalogoServicios item);
        void onClickEdit(CatalogoServicios item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CrearServiciosFragmentProveedor.OnListFragmentInteractionListener) {
            mListener = (CrearServiciosFragmentProveedor.OnListFragmentInteractionListener) context;
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
