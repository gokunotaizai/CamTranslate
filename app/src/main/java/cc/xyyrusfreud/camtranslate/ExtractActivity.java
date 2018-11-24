package cc.xyyrusfreud.camtranslate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtractActivity extends AppCompatActivity {

    static final int CAPTURE_IMAGE_REQUEST = 1;
    private static final String IMAGE_DIRECTORY_NAME = "JXL";
    Button btn;
    String st;
    EditText et;
    ImageView ivImage;
    TextView t1;
    int REQUEST_CAMERA = 1, SELECT_FILE = 0;
    File photoFile = null;
    String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        btn = (Button) findViewById(R.id.btn);
        t1 = (TextView) findViewById(R.id.et1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivty3();
            }
        });
    }

    public void openActivty3() {
        Intent intent = new Intent(this, ActivityTranslation.class);
        st = t1.getText().toString();
        intent.putExtra("Value", st);
        startActivity(intent);
    }

    private void SelectImage() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ExtractActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    captureImage();

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void captureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                    displayMessage(getBaseContext(), photoFile.getAbsolutePath());
                    Log.i("xyrus", photoFile.getAbsolutePath());

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "cc.xyyrusfreud.camtranslate.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
                    }
                } catch (Exception ex) {
                    // Error occurred while creating the File
                    displayMessage(getBaseContext(), ex.getMessage().toString());
                }


            } else {
                displayMessage(getBaseContext(), "Nullll");
            }
        }

    }

    public void getTextFromImage(View v) {
        try {
            Bitmap bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();

            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();


            if (!textRecognizer.isOperational()) {
                Toast.makeText(getApplicationContext(), "Could not grant the text", Toast.LENGTH_SHORT).show();
            } else {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                SparseArray<TextBlock> items = textRecognizer.detect(frame);

                StringBuilder sb = new StringBuilder();


                for (int i = 0; i < items.size(); i++) {
                    TextBlock item = items.valueAt(i);
                    sb.append(item.getValue());
                    sb.append("\n");

                    t1.setText(sb.toString());
                    //for edit text 2nd activity
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Select or Capture photo", Toast.LENGTH_SHORT);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ivImage.setImageBitmap(myBitmap);

        } else if (requestCode == SELECT_FILE) {

            Uri selectedImageUri = data.getData();
            ivImage.setImageURI(selectedImageUri);
        }

    }

    private void displayMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                SelectImage();
            }
        }

    }

}
