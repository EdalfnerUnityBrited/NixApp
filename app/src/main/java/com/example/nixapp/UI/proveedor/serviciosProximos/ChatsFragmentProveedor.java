package com.example.nixapp.UI.proveedor.serviciosProximos;

import android.content.Context;
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

import com.example.nixapp.DB.Chat;
import com.example.nixapp.DB.Usuario;
import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.serviciosContratados.chat.PruebaChatsRecyclerViewAdapter;
import com.example.nixapp.conn.NixClient;
import com.example.nixapp.conn.NixService;
import com.example.nixapp.conn.results.ChatResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsFragmentProveedor extends Fragment {
    View view;
    RecyclerView recyclerView;
    List<Chat> chatsList;
    PruebaChatsProveedorRecyclerViewAdapter adapterChats;
    NixService nixService;
    NixClient nixClient;
    Usuario usuario;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ChatsFragmentProveedor.OnListFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chats_proveedor, container, false);
        recyclerView= view.findViewById(R.id.recyclerChats);
        usuario = new Usuario();
        usuario= (Usuario) getActivity().getIntent().getSerializableExtra("usuario");
        chatsList= new ArrayList<>();
        retrofitInit();
        Call<ChatResult> call = nixService.chatProveedor();
        call.enqueue(new Callback<ChatResult>() {
            @Override
            public void onResponse(Call<ChatResult> call, Response<ChatResult> response) {
                if (response.isSuccessful()) {
                    Log.i("respuesta", response.body().toString());


                    chatsList=  response.body().chats;
                    adapterChats= new PruebaChatsProveedorRecyclerViewAdapter(chatsList, mListener);
                    recyclerView.setAdapter(adapterChats);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } else {
                    try {
                        Log.i("error",response.errorBody().string().toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "Chats no obtenidos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatResult> call, Throwable t) {

            }
        });
        return view;
    }

    private void retrofitInit() {
        nixClient= NixClient.getInstance();
        nixService= nixClient.getNixService();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= view.findViewById(R.id.recyclerChats);

    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Chat item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChatsFragmentProveedor.OnListFragmentInteractionListener) {
            mListener = (ChatsFragmentProveedor.OnListFragmentInteractionListener) context;
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
