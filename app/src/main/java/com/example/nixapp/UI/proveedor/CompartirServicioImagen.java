package com.example.nixapp.UI.proveedor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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

        btn = (Button) findViewById(R.id.screenshot);
        btnCompartir= findViewById(R.id.compartir);
        editText= findViewById(R.id.editText);
        contador=1;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setBackground(null);
                editText.setEnabled(false);
                btnCompartir.setVisibility(View.GONE);
                String desc= editText.getText().toString();
                btn.setVisibility(View.INVISIBLE);
                Bitmap b = Screenshot.takescreenshotOfRootView(main);
                guardarEnGaleria(b);
                btn.setVisibility(View.VISIBLE);
                btnCompartir.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                contador++;
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), Compartir_Proveedor.class);
                startActivity(intent);
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
    private void guardarEnGaleria(Bitmap bitmap){
        try {
            final Bitmap comparticion = bitmap;
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                try {
                                    final String path = BitmapUtils.insertImage(getContentResolver(), comparticion, System.currentTimeMillis() + ".jpg", null,100);
                                    if (!TextUtils.isEmpty(path)) {
                                        Toast.makeText(CompartirServicioImagen.this, "Guardado en Galería", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(CompartirServicioImagen.this, "No se puedo guardar en galería", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(CompartirServicioImagen.this, "Permiso denegado de guardado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
