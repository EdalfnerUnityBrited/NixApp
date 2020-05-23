package com.example.nixapp.UI.proveedor.misServicios.Paquetes;

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
import com.example.nixapp.DB.Eventos;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.misServicios.CrearServicioMenu;
import com.example.nixapp.UI.proveedor.misServicios.Paquetes.CrearPaquetesDatos;
import com.example.nixapp.UI.usuario.misEventos.EventosCerradosFragment;
import com.example.nixapp.UI.usuario.misEventos.PruebaEventosRecyclerViewAdapter;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ArticulosListResult;
import com.example.nixapp.conn.results.PaquetesListResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaquetesFragment extends Fragment {
    NixService nixService;
    NixClient nixClient;
    List<Articulos> articulosList;
    RecyclerView recyclerView;
    List<Paquetes> paquetesList;
    PaqueteRecyclerViewAdapter adapterEventos;
    Eventos eventos;
    private PaquetesFragment.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paquetes_proveedor,container,false);
        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.nuevo_paquete);
        recyclerView= view.findViewById(R.id.recyclerUserEvents);
        paquetesList= new ArrayList<>();
        retrofitInit();
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articulosList=new ArrayList<>();

                if (CrearServicioMenu.servicio!=0){
                    Articulos articulos= new Articulos(CrearServicioMenu.servicio);
                    Call<ArticulosListResult> call = nixService.articulosServicio(articulos);
                    call.enqueue(new Callback<ArticulosListResult>() {
                        @Override
                        public void onResponse(Call<ArticulosListResult> call, Response<ArticulosListResult> response) {
                            if (response.isSuccessful()){
                                articulosList=response.body().articulos;
                                if (!articulosList.isEmpty()){
                                    Intent intentArticulo = new Intent(getActivity(), CrearPaquetesDatos.class);
                                    intentArticulo.putExtra("id",CrearServicioMenu.servicio);
                                    startActivity(intentArticulo);
                                }else{
                                    Toast.makeText(getActivity(), "Antes de crear un paquete agrega articulos", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();   
                            }
                        }

                        @Override
                        public void onFailure(Call<ArticulosListResult> call, Throwable t) {

                        }
                    });


                }
                else{
                    Toast.makeText(getActivity(), "Debes haber creado un servicio primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Articulos articulos= new Articulos(CrearServicioMenu.servicio);
        Call<PaquetesListResult> callDos= nixService.paquetesServicio(articulos);
        callDos.enqueue(new Callback<PaquetesListResult>() {
            @Override
            public void onResponse(Call<PaquetesListResult> call, Response<PaquetesListResult> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(),"Paquetes obtenidos correctamente", Toast.LENGTH_SHORT).show();
                    paquetesList=  response.body().paquetes;
                    adapterEventos=  new PaqueteRecyclerViewAdapter(paquetesList, mListener);
                    recyclerView.setAdapter(adapterEventos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                else{
                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaquetesListResult> call, Throwable t) {

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
        void onListFragmentInteraction(Paquetes item);
        void onClickDelete(Paquetes item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PaquetesFragment.OnListFragmentInteractionListener) {
            mListener = (PaquetesFragment.OnListFragmentInteractionListener) context;
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
