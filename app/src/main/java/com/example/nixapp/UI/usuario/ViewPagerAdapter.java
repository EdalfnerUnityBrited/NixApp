package com.example.nixapp.UI.usuario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.nixapp.DB.ImagenEventos;
import com.example.nixapp.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.compromisos,R.drawable.galas,R.drawable.festejos};
    List<ImagenEventos> imagenes;

    public List<ImagenEventos> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenEventos> imagenes) {
        this.imagenes = imagenes;
    }

    public ViewPagerAdapter(Context context, List<ImagenEventos> imagenes) {
        this.context = context;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_carrusel, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imagen);
        //imageView.setImageResource(images[position]);
        Glide.with(imageView)
                .load(imagenes.get(position).getImagen())
                .into(imageView);
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
