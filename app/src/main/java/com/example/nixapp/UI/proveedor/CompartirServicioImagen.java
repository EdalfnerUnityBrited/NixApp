package com.example.nixapp.UI.proveedor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nixapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CompartirServicioImagen extends AppCompatActivity {

    private View main;
    private ImageView imageView;
    TextView nombreServicio, nombreEvento, descripcion;
    Button btn, btnCompartir;
    int contador;
    EditText editText;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String nomEvento = (String) getIntent().getSerializableExtra("evento");
        final String nomServicio = (String) getIntent().getSerializableExtra("servicio");
        setContentView(R.layout.activity_compartir_servicio_imagen);
        main = findViewById(R.id.main);
        nombreServicio= findViewById(R.id.detalles);
        nombreServicio.setText("Proveeré el servicio: " + nomServicio);
        nombreEvento= findViewById(R.id.evento);
        nombreEvento.setText("Para el evento: " + nomEvento);
        descripcion= findViewById(R.id.descripcion);
        descripcion.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.screenshot);
        btnCompartir= findViewById(R.id.compartir);
        editText= findViewById(R.id.editText);
        contador=1;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setBackground(null);
                editText.setEnabled(false);
                editText.setVisibility(View.INVISIBLE);
                btnCompartir.setVisibility(View.GONE);
                String desc= editText.getText().toString();
                descripcion.setText(desc);
                descripcion.setVisibility(View.VISIBLE);
                btn.setVisibility(View.INVISIBLE);
                Bitmap b = Screenshot.takescreenshotOfRootView(main);
                storeScreenshot(b,"Imagen"+nomEvento+nomServicio);
                btn.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                contador++;
            }
        });
    }
    public void storeScreenshot(Bitmap bitmap, String filename) {
        String path = "/storage/emulated/0/DCIM" + "/" + filename+".jpeg";
        OutputStream out = null;
        File imageFile = new File(path);

        try {
            out = new FileOutputStream(imageFile);
            // choose JPEG format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No hay imagen", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } finally {

            try {
                if (out != null) {
                    out.close();
                }

            } catch (Exception exc) {
            }

        }
    }
}
