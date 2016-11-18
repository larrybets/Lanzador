package com.example.dam.lanzador;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgView;
    private Button btn_select, btn_photo;
    private boolean isResource = true;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = (ImageView) findViewById(R.id.imageView1);
        btn_select = (Button) findViewById(R.id.button2);
        btn_photo = (Button) findViewById(R.id.button3);
        btn_select.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        Button button_1 = (Button) findViewById(R.id.button_1);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.stucom.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Main activity.this
                mediaPlayer.start();

            }
        });

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.beat1);



    }


    public void onpPause() {
        emptyImageViewer();
        super.onPause();
    }

    public void emptyImageViewer() {
        if (this.isResource) return;
        Drawable drawable = imgView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (!bitmap.isRecycled()) bitmap.recycle();
        }
        imgView.setImageResource(android.R.drawable.ic_menu_camera);
        this.isResource = true;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btn_select)) takePhoto();
        else if (v.equals(btn_photo)) selectPhoto();
    }

    private final static int SELECT_PHOTO = 1;
    private final static int CAPTURE_PHOTO = 2;

    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select Source"), SELECT_PHOTO);
    }

    private Uri photoUri;

    public void takePhoto() {
        File dir = getExternalFilesDir(null);
        if (dir == null) {
            Toast.makeText(this, "Unable to mount the filesystem",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(dir, "photo.jpg");
        //photoUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
        //
        startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        emptyImageViewer();
        if (requestCode == CAPTURE_PHOTO) {
            imgView.setImageURI(photoUri);
            this.isResource = false;
        } else {
            Uri img = data.getData();
            if (img != null) {
                imgView.setImageURI(img);
                this.isResource = false;
            }
        }
        imgView.invalidate();
    }

}
