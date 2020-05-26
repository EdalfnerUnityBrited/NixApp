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

import com.example.nixapp.DB.Articulos;
import com.example.nixapp.DB.CatalogoServicios;
import com.example.nixapp.DB.Cotizacion;
import com.example.nixapp.DB.Paquetes;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.DashboardFragment;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.PruebaChatsRecyclerViewAdapter;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.CotizacionesListResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CotizacionesGuardadasFragment extends Fragment {
    NixService nixService;
    NixClient nixClient;
    List<Cotizacion> cotizaciones;
    private RecyclerView recyclerView;
    CotizacionesRecyclerViewAdapter adapterCotizaciones;
    private CotizacionesGuardadasFragment.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cotizaciones_guardadas,container,false);
        retrofitinit();
        recyclerView= view.findViewById(R.id.recyclerCotizaciones);
        Call<CotizacionesListResult> call= nixService.cotizacionesUsuario();
        call.enqueue(new Callback<CotizacionesListResult>() {
            @Override
            public void onResponse(Call<CotizacionesListResult> call, Response<CotizacionesListResult> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getActivity(), "Cotizaciones obtenidas correctamente", Toast.LENGTH_SHORT).show();
                    cotizaciones=  response.body().cotizaciones;
                    adapterCotizaciones=  new CotizacionesRecyclerViewAdapter(cotizaciones, mListener);
                    recyclerView.setAdapter(adapterCotizaciones);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                }
                else{
                    Toast.makeText(getActivity(), "Error en los datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CotizacionesListResult> call, Throwable t) {

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
        void onListFragmentInteraction(Cotizacion item);
        void onClick(View v);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CotizacionesGuardadasFragment.OnListFragmentInteractionListener) {
            mListener = (CotizacionesGuardadasFragment.OnListFragmentInteractionListener) context;
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
