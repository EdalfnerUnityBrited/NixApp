package com.example.nixapp.UI.usuario.creadorInvitaciones;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.nixapp.R;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Adapter.ViewPagerAdapter;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.AddFrameListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.AddShapeListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.AddTextFragmentListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.BrushFragmentListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.EditImageFragmentListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.EmojiFragmentListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.FiltersListFragmentListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.GenerateQRListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.SetImageSizeListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Interface.SetInvitationQualityListener;
import com.example.nixapp.UI.usuario.creadorInvitaciones.Utils.BitmapUtils;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.util.List;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class CreadorDeInvitaciones extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener, AddFrameListener, AddShapeListener, SetImageSizeListener, SetInvitationQualityListener, GenerateQRListener {



    public static String pictureName ="dientedeleon.jpeg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_INSERT_IMAGE = 1001;
    public static final int CAMERA_REQUEST = 1002;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;

    CoordinatorLayout coordinatorLayout;

    Bitmap originalBitmap,filteredBitmap,finalBitmap,marcaAgua;

    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;

    private boolean pedirImagen = false, pedirCalidad=true;

    private AlertDialog.Builder dialogo1;

    CardView btn_filters_list, btn_edit_card,btn_brush,btn_emoji,btn_add_text,btn_add_image,btn_add_frame,btn_crop,btn_shape,btn_qr;

    int brightnessFinal = 0,imageHeight = 0,imageWidth=0, quality =0;
    float contrastFinal = 1.0f, saturationFinal = 1.0f;

    Uri image_selected_uri;

    //cargar libreria nativa de filtros para imagen
    static {
        System.loadLibrary("NativeImageProcessor");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creador_de_invitaciones_activity);

        pictureName = (String) getIntent().getSerializableExtra("nombre_plantilla");

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backarrow2);
        getSupportActionBar().setTitle("Creador de Invitaciones");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //View
        photoEditorView = (PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this,photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                .build();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        btn_edit_card = (CardView) findViewById(R.id.btn_edit_card);
        btn_filters_list = (CardView) findViewById(R.id.btn_filters_list);
        btn_brush = (CardView) findViewById(R.id.btn_brush);
        btn_emoji = (CardView) findViewById(R.id.btn_emoji);
        btn_add_text = (CardView) findViewById(R.id.btn_add_text_editor);
        btn_add_image = (CardView) findViewById(R.id.btn_add_image_editor);
        btn_add_frame = (CardView) findViewById(R.id.btn_add_frame_editor);
        btn_crop = (CardView) findViewById(R.id.btn_crop_editor);
        btn_shape = (CardView) findViewById(R.id.btn_shape_editor);
        btn_qr = (CardView) findViewById(R.id.btn_qr_editor);

        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QrCodeFragment qrCodeFragment = QrCodeFragment.getInstance();
                qrCodeFragment.setListener(CreadorDeInvitaciones.this);
                qrCodeFragment.show(getSupportFragmentManager(),qrCodeFragment.getTag());
            }
        });

        btn_shape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShapeFragment shapeFragment = ShapeFragment.getInstance();
                shapeFragment.setListener(CreadorDeInvitaciones.this);
                shapeFragment.show(getSupportFragmentManager(),shapeFragment.getTag());
            }
        });

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrop(image_selected_uri);
            }
        });

        btn_filters_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filtersListFragment!=null){
                    filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());
                }
                else{
                    FiltersListFragment filtersListFragment = FiltersListFragment.getInstance(null);
                    filtersListFragment.setListener(CreadorDeInvitaciones.this);
                    filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());
                }
            }
        });

        btn_edit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(CreadorDeInvitaciones.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //habilitar el modo brocha
                photoEditor.setBrushDrawingMode(true);

                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(CreadorDeInvitaciones.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });

        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(CreadorDeInvitaciones.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });

        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTextFragment addTextFragment = AddTextFragment.getInstance();
                addTextFragment.setListener(CreadorDeInvitaciones.this);
                addTextFragment.show(getSupportFragmentManager(),addTextFragment.getTag());
            }
        });

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForImageSize();
                addImageToPicture();
            }
        });

        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameFragment frameFragment = FrameFragment.getInstance();
                frameFragment.setListener(CreadorDeInvitaciones.this);
                frameFragment.show(getSupportFragmentManager(),frameFragment.getTag());
            }
        });

        loadImage();

    }

    private void askForImageSize() {
        AskSizeImageFragment askSizeImageFragment = AskSizeImageFragment.getInstance();
        askSizeImageFragment.setListener(CreadorDeInvitaciones.this);
        askSizeImageFragment.show(getSupportFragmentManager(),askSizeImageFragment.getTag());
    }

    private void startCrop(Uri uri) {
        String destinationFileName = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop ucrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        ucrop.start(CreadorDeInvitaciones.this);
    }

    private void addImageToPicture() {
        if (pedirImagen) {
            pedirImagen=false;
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, PERMISSION_INSERT_IMAGE);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            Toast.makeText(CreadorDeInvitaciones.this, "Permiso denegado para insertar imagen", Toast.LENGTH_SHORT).show();
                        }
                    }).check();
        }
    }

    private void loadImage() {
        originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment,"FILTERS");
        adapter.addFragment(editImageFragment,"EDIT");

        viewPager.setAdapter(adapter);

    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onContrastChanged(float contrast) {
        contrastFinal = contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new ContrastSubFilter(contrastFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));

        finalBitmap = myFilter.processFilter(bitmap);
    }

    @Override
    public void onFiltersSelected(Filter filter) {
        //resetControls();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

    }

    private void resetControls() {
        if (editImageFragment != null){
            editImageFragment.resetControls();
        }
        brightnessFinal=0;
        contrastFinal=1.0f;
        saturationFinal=1.0f;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones_creador_invitaciones,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_open){
            openImageFromGallery();
            return true;
        }
        else if (id == R.id.action_save){
            //selectQuality();
            saveImageToGallery();
            return true;
        }
        else if (id == R.id.action_camera){
            openCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectQuality(){
        AskQualityInvitationFragment askQualityInvitationFragment = AskQualityInvitationFragment.getInstance();
        askQualityInvitationFragment.setListener(CreadorDeInvitaciones.this);
        askQualityInvitationFragment.show(getSupportFragmentManager(),askQualityInvitationFragment.getTag());
    }

    private void openCamera() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE,"Nueva foto");
                            values.put(MediaStore.Images.Media.DESCRIPTION,"De La Camera");
                            image_selected_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_selected_uri);
                            startActivityForResult(cameraIntent,CAMERA_REQUEST);
                        }
                        else{
                            Toast.makeText(CreadorDeInvitaciones.this,"Permiso denegado de guardado",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void saveImageToGallery() {
        if (pedirCalidad) {
            pedirCalidad=false;
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                    @Override
                                    public void onBitmapReady(Bitmap saveBitmap) {
                                        try {
                                            photoEditorView.getSource().setImageBitmap(saveBitmap);
                                            final String path = BitmapUtils.insertImage(getContentResolver(), saveBitmap, System.currentTimeMillis() + "_invitacion.jpg", null,100);
                                            if (!TextUtils.isEmpty(path)) {
                                                Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                        "Invitacion guardada a la galería",
                                                        Snackbar.LENGTH_LONG).setAction("ABRIR", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        openImage(path);
                                                    }
                                                });
                                                snackbar.show();
                                                dialogo1 = new AlertDialog.Builder(CreadorDeInvitaciones.this);
                                                dialogo1.setTitle("Importante");
                                                dialogo1.setMessage("¿Quieres crear un código QR?");
                                                dialogo1.setCancelable(false);
                                                dialogo1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogo1, int id) {
                                                        Intent qr = new Intent(CreadorDeInvitaciones.this,GenerarCodigoQR.class);
                                                        startActivity(qr);
                                                        finish();
                                                    }
                                                });
                                                dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialogo1, int id) {
                                                        finish();
                                                    }
                                                });
                                                dialogo1.show();
                                            } else {
                                                Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                        "No se pudo guardar en galería",
                                                        Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });
                            } else {
                                Toast.makeText(CreadorDeInvitaciones.this, "Permiso denegado de guardado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void openImage(String path) {
        Intent intent = new Intent ();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_PICK_IMAGE);
                        }
                        else{
                            Toast.makeText(CreadorDeInvitaciones.this,"Permiso de abrir denegado!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);
                image_selected_uri = data.getData();
                //limpio la memoria del bitmap
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();

                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                filtersListFragment = FiltersListFragment.getInstance(originalBitmap);
                filtersListFragment.setListener(this);


            }
            else if (requestCode == CAMERA_REQUEST) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, image_selected_uri, 800, 800);
                //limpio la memoria del bitmap
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();
                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();
                filtersListFragment = FiltersListFragment.getInstance(originalBitmap);
                filtersListFragment.setListener(this);
            }
            else if (requestCode == PERMISSION_INSERT_IMAGE){
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this,data.getData(),imageHeight,imageWidth);
                photoEditor.addImage(bitmap);
            }
            else if (requestCode== UCrop.REQUEST_CROP){
                handleCropResult(data);
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR){
            handleCropError(data);
        }
    }

    private void handleCropError(Intent data) {
        final Throwable cropError = UCrop.getError(data);
        if (cropError !=null){
            Toast.makeText(this, ""+cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Error inesperado", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri = UCrop.getOutput(data);
        if (resultUri != null){
            photoEditorView.getSource().setImageURI(resultUri);
            Bitmap bitmap = ((BitmapDrawable)photoEditorView.getSource().getDrawable()).getBitmap();
            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            filteredBitmap = originalBitmap;
            finalBitmap = originalBitmap;
        }
        else{
            Toast.makeText(this, "No se pudo recuperar la imagen recortada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);

    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if (isEraser){
            photoEditor.brushEraser();
        }
        else{
            photoEditor.setBrushDrawingMode(true);
        }
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }

    @Override
    public void onAddTextButtonClick(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface,text,color);
    }

    @Override
    public void onAddFrame(int frame) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),frame);
        photoEditor.addImage(bitmap);
    }

    @Override
    public void onAddShape(int frame) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),frame);
        photoEditor.addImage(bitmap);
    }

    @Override
    public void setImageSize(int alto, int ancho) {
        this.imageHeight = alto;
        this.imageWidth = ancho;
        pedirImagen=true;
        addImageToPicture();
    }

    @Override
    public void setImageQuality(int quality) {
        this.quality = quality;
        pedirCalidad=true;
        saveImageToGallery();
    }

    @Override
    public void generateCodeQr(String enlace) {
        Bitmap bitmap = null;
        QRGEncoder qrgEncoder = new QRGEncoder(enlace, null, QRGContents.Type.TEXT,400);
        try{
            bitmap = qrgEncoder.encodeAsBitmap();
        }
        catch (Exception e){
        }
        photoEditor.addImage(bitmap);
    }
}
