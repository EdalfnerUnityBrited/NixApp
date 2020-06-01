package com.example.nixapp.UI.usuario.ayuda;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nixapp.R;

public class TutorialesFragment extends Fragment {
    VideoView crearEvento;
    public static String PACKAGE_NAME;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view= inflater.inflate(R.layout.fragment_tutoriales,container,false);
        crearEvento= (VideoView) view.findViewById(R.id.videoCrear);
        PACKAGE_NAME = getActivity().getApplicationContext().getPackageName();
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.crearevento;
        Uri uri= Uri.parse(path);
        crearEvento.setVideoURI(Uri.parse(path));
        crearEvento.start();



        MediaController mc=  new MediaController(getActivity());
        mc.setAnchorView(crearEvento);
        crearEvento.setMediaController(mc);
        crearEvento.requestFocus();
        return view;
    }
}
