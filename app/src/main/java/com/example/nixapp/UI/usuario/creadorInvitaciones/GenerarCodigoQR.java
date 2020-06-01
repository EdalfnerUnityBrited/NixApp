package com.example.nixapp.UI.usuario.creadorInvitaciones;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Utils.BitmapUtils;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Tools.OnSaveBitmap;

public class GenerarCodigoQR extends AppCompatActivity {

    private Button generar;
    private EditText enlace;
    private Bitmap bitmap;

    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_codigo_q_r);

        generar = (Button) findViewById(R.id.crear_qr_activity);
        enlace = (EditText) findViewById(R.id.enlace_qr_activity);

        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enlace.getText().toString()==null){
                    Toast.makeText(GenerarCodigoQR.this, "Por favor ingrese un enlace", Toast.LENGTH_SHORT).show();
                }
                else {
                    QRGEncoder qrgEncoder = new QRGEncoder(enlace.getText().toString(), null, QRGContents.Type.TEXT, 400);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        guardarEnGaleria();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void guardarEnGaleria(){
        try {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                        try {
                                            final String path = BitmapUtils.insertImage(getContentResolver(), bitmap, System.currentTimeMillis() + "_qr.jpg", null,100);
                                            if (!TextUtils.isEmpty(path)) {
                                                Toast.makeText(GenerarCodigoQR.this, "Guardado en Galería", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(GenerarCodigoQR.this, "No se puedo guardar en galería", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                            } else {
                                Toast.makeText(GenerarCodigoQR.this, "Permiso denegado de guardado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
