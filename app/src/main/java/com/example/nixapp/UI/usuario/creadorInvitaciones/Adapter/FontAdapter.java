package com.example.nixapp.UI.usuario.creadorInvitaciones.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.R;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    Context context;
    FontAdapterClickListener listener;
    List<String> fontList;

    int row_selected = -1;

    public FontAdapter(Context context, FontAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        fontList = loadFontList();
    }

    private List<String> loadFontList() {
        List<String> result = new ArrayList<>();
        result.add("IriskaFreeFont.otf");
        result.add("TwilightFont-Regular.otf");
        result.add("Adolle Bright.otf");
        result.add("ArigatouGozaimasu-Rp0LW.otf");
        result.add("blackjack.otf");
        result.add("ChunkFive-Regular.otf");
        result.add("claudia script.otf");
        result.add("DancingScript-Regular.otf");
        result.add("DinosauceFont-Regular.otf");
        result.add("GrandHotel-Regular.otf");
        result.add("GreatVibes-Regular.otf");
        result.add("KaushanScript-Regular.otf");
        result.add("LeagueGothic-Regular.otf");
        result.add("learning_curve_regular_ot_ps.otf");
        result.add("Lobster_1.3.otf");
        result.add("lovee.otf");
        result.add("madani.otf");
        result.add("Mofita.otf");
        result.add("Rabbits Goody.otf");
        result.add("Sofia-Regular.otf");
        result.add("SourceSansPro-Regular.otf");
        result.add("Spring is Coming - OTF.otf");
        result.add("Taraxacum.otf");
        result.add("TequilaSunset.otf");
        return result;
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.font_item,parent,false);
        return new FontViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if (row_selected == position){
            holder.img_check.setVisibility(View.VISIBLE);
        }
        else{
            holder.img_check.setVisibility(View.INVISIBLE);
        }
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),new StringBuilder("Fonts/").append(fontList.get(position)).toString());
        holder.text_font_name.setText(fontList.get(position));
        holder.text_font_demo.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        TextView text_font_name,text_font_demo;
        ImageView img_check;

        public FontViewHolder(@NonNull View itemView) {
            super(itemView);
            text_font_name = (TextView) itemView.findViewById(R.id.txt_font_name);
            text_font_demo = (TextView) itemView.findViewById(R.id.txt_font_demo);
            img_check = (ImageView) itemView.findViewById(R.id.img_check_editor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFontSelected(fontList.get(getAdapterPosition()));
                    row_selected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface FontAdapterClickListener{
        public void onFontSelected(String fontName);
    }
}
