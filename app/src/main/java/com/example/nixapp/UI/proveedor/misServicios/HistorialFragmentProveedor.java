package com.example.nixapp.UI.proveedor.misServicios;

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
import com.example.nixapp.R;
import com.example.nixapp.UI.proveedor.serviciosProximos.ContratacionesProveedorRecyclerViewAdapter;
import com.example.nixapp.UI.proveedor.serviciosProximos.ServiciosPendientesFragmentProveedor;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ContratacionesListResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialFragmentProveedor extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Contrataciones> contratacionesList;
    ContratacionesHistorialRecyclerViewAdapter adapterChats;
    NixService nixService;
    NixClient nixClient;
    private HistorialFragmentProveedor.OnListFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_historial_proveedor,container,false);
        retrofitinit();
        recyclerView= view.findViewById(R.id.recyclerHist);
        contratacionesList= new ArrayList<>();
        Call<ContratacionesListResult> call= nixService.proveedorHistorialcontrataciones();
        call.enqueue(new Callback<ContratacionesListResult>() {
            @Override
            public void onResponse(Call<ContratacionesListResult> call, Response<ContratacionesListResult> response) {
                if (response.isSuccessful()){
                    contratacionesList=  response.body().contrataciones;
                    adapterChats= new ContratacionesHistorialRecyclerViewAdapter(contratacionesList, mListener);
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
        if (context instanceof HistorialFragmentProveedor.OnListFragmentInteractionListener) {
            mListener = (HistorialFragmentProveedor.OnListFragmentInteractionListener) context;
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
