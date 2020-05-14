package com.example.nixapp.UI.usuario.creadorInvitaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Adapter.ShapeAdapter;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.AddShapeListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShapeFragment extends BottomSheetDialogFragment implements ShapeAdapter.ShapeAdapterListener {

    RecyclerView recycler_frame;
    Button btn_add_frame;

    int frame_selected = -1;

    AddShapeListener listener;

    public void setListener(AddShapeListener listener) {
        this.listener = listener;
    }

    static ShapeFragment instance;
    public static ShapeFragment getInstance() {
        if (instance == null){
            instance = new ShapeFragment();
        }
        return instance;
    }

    public ShapeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_shape, container, false);
        recycler_frame = (RecyclerView) itemView.findViewById(R.id.recycler_shape_editor);
        btn_add_frame = (Button) itemView.findViewById(R.id.btn_add_shape);

        recycler_frame.setHasFixedSize(true);
        recycler_frame.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        recycler_frame.setAdapter(new ShapeAdapter(getContext(),this));

        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddShape(frame_selected);
            }
        });

        return itemView;
    }

    @Override
    public void onShapeSelected(int frame) {
        frame_selected = frame;
    }
}
