package br.com.mobiletkbrazil.avaliacao;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class ProdutosActivity extends AppCompatActivity {

    private CameraView cameraView;
    private AlertDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cameraView = findViewById(R.id.cameraView);

        waitingDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Aguarde")
                .setCancelable(false)
                .build();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override public void onEvent(CameraKitEvent cameraKitEvent) {}

            @Override public void onError(CameraKitError cameraKitError) {}

            @Override public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, cameraView.getWidth(), cameraView.getHeight(), false);
                cameraView.stop();

                runDetector(bitmap);
            }

            @Override public void onVideo(CameraKitVideo cameraKitVideo) {}
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                cameraView.captureImage();
            }
        });
    }

    private void runDetector(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_CODE_128,
                        FirebaseVisionBarcode.FORMAT_CODE_39,
                        FirebaseVisionBarcode.FORMAT_CODE_93,
                        FirebaseVisionBarcode.FORMAT_CODABAR,
                        FirebaseVisionBarcode.FORMAT_EAN_13,
                        FirebaseVisionBarcode.FORMAT_EAN_8,
                        FirebaseVisionBarcode.FORMAT_ITF,
                        FirebaseVisionBarcode.FORMAT_QR_CODE
                ).build();
        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        processResult(firebaseVisionBarcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        for (FirebaseVisionBarcode item : firebaseVisionBarcodes) {
            int value_type = item.getValueType();
            switch (value_type) {
                case FirebaseVisionBarcode.TYPE_TEXT:
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                    builder.setMessage(item.getRawValue());
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    android.support.v7.app.AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                case FirebaseVisionBarcode.TYPE_URL:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getDisplayValue()));
                    startActivity(intent);
                    break;
                case FirebaseVisionBarcode.TYPE_CONTACT_INFO:
                    String info = new StringBuilder("Name: ")
                            .append(item.getContactInfo().getName().getFormattedName())
                            .append("\n")
                            .append("Address: ")
                            .append(item.getContactInfo().getAddresses().get(0).getAddressLines())
                            .append("\n")
                            .append("Email: ")
                            .append(item.getContactInfo().getEmails().get(0).getAddress())
                            .toString();
                    android.support.v7.app.AlertDialog.Builder builderContact = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                    builderContact.setMessage(info);
                    builderContact.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    android.support.v7.app.AlertDialog dialogContact = builderContact.create();
                    dialogContact.show();
                    break;
            }
        }

        waitingDialog.dismiss();
    }

    @Override protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

}
