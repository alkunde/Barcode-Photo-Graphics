package br.com.mobiletkbrazil.avaliacao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnRequestPermissionsResultCallback {

    private static final int REQUEST_TAKE_PHOTO = 1000;
    private String mCurrentPhotoPath = "";
    private ImageView ivPhoto;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_relatorio).setOnClickListener(this);
        findViewById(R.id.bt_galeria_fotos).setOnClickListener(this);
        findViewById(R.id.bt_leitor).setOnClickListener(this);
        findViewById(R.id.floatingActionButton).setOnClickListener(this);

        ivPhoto = findViewById(R.id.imageView);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_relatorio:
                Toast.makeText(MainActivity.this, "Relatórios", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, RelatorioActivity.class));
                break;
            case R.id.bt_galeria_fotos:
                Toast.makeText(MainActivity.this, "Galeria foto", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_leitor:
                Toast.makeText(MainActivity.this, "Leitor", Toast.LENGTH_SHORT).show();
                break;
            case R.id.floatingActionButton:
                getPermissions();
                return;
        }
    }

    private void getPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Toast.makeText(this, "Precisa da permissão para bater a foto", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile("PHOTOAPP", ".jpg", storageDir);
                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Erro ao tirar a foto", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap bm1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(mCurrentPhotoPath)));
                ivPhoto.setImageBitmap(bm1);
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Foto não encontrada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
