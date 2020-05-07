package com.example.nixapp.UI.usuario.creadorInvitaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.nixapp.R;

public class Plantillas extends AppCompatActivity implements View.OnClickListener {

    private ImageButton plantilla_boda_1,plantilla_boda_2,plantilla_fiesta_1,plantilla_fiesta_2,
            plantilla_general_1,plantilla_general_2,plantilla_general_3,plantilla_infantil,
            plantilla_religioso_1,plantilla_religioso_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantillas);

        plantilla_boda_1 = (ImageButton) findViewById(R.id.plantilla_boda_1);
        plantilla_boda_2 = (ImageButton) findViewById(R.id.plantilla_boda_2);
        plantilla_fiesta_1 = (ImageButton) findViewById(R.id.plantilla_fiesta_1);
        plantilla_fiesta_2 = (ImageButton) findViewById(R.id.plantilla_fiesta_2);
        plantilla_general_1 = (ImageButton) findViewById(R.id.plantilla_general_1);
        plantilla_general_2 = (ImageButton) findViewById(R.id.plantilla_general_2);
        plantilla_general_3 = (ImageButton) findViewById(R.id.plantilla_general_3);
        plantilla_infantil = (ImageButton) findViewById(R.id.plantilla_infantil);
        plantilla_religioso_1 = (ImageButton) findViewById(R.id.plantilla_religioso_1);
        plantilla_religioso_2 = (ImageButton) findViewById(R.id.plantilla_religioso_2);

        plantilla_boda_1.setOnClickListener(this);
        plantilla_boda_2.setOnClickListener(this);
        plantilla_fiesta_1.setOnClickListener(this);
        plantilla_fiesta_2.setOnClickListener(this);
        plantilla_general_1.setOnClickListener(this);
        plantilla_general_2.setOnClickListener(this);
        plantilla_general_3.setOnClickListener(this);
        plantilla_infantil.setOnClickListener(this);
        plantilla_religioso_1.setOnClickListener(this);
        plantilla_religioso_2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent plantillaAUtilizar = new Intent (Plantillas.this,CreadorDeInvitaciones.class);;
        switch (view.getId()){
            case R.id.plantilla_boda_1:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_boda_1.jpeg");
                break;
            }
            case R.id.plantilla_boda_2:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_boda_2.jpeg");
                break;
            }
            case R.id.plantilla_fiesta_1:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_fiesta_1.jpeg");
                break;
            }
            case R.id.plantilla_fiesta_2:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_fiesta_2.jpeg");
                break;
            }
            case R.id.plantilla_general_1:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_general_1.jpeg");
                break;
            }
            case R.id.plantilla_general_2:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_general_2.jpeg");
                break;
            }
            case R.id.plantilla_general_3:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_general_3.jpeg");
                break;
            }
            case R.id.plantilla_infantil:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_infantil.jpeg");
                break;
            }
            case R.id.plantilla_religioso_1:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_religioso_1.jpeg");
                break;
            }
            case R.id.plantilla_religioso_2:{
                plantillaAUtilizar.putExtra("nombre_plantilla","plantilla_religioso_2.jpeg");
                break;
            }
        }
        startActivity(plantillaAUtilizar);
        finish();
    }
}
