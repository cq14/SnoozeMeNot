package edu.fsu.cs.mobile.snoozemenot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class  PrintActivity extends AppCompatActivity {

    ImageView qrcode;
    Button share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        qrcode = (ImageView) findViewById(R.id.code_view);
        share = (Button) findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Drawable bgDrawable = qrcode.getBackground();
                if(bgDrawable!=null)
                    bgDrawable.draw(canvas);
                else
                    canvas.drawColor(Color.WHITE);
                qrcode.draw(canvas);
                try {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/png");
                    File file = new File(getExternalCacheDir(), "qrcode.png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    startActivity(Intent.createChooser(share, "Share image using"));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        //Commented out to prevent null object reference
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar();
    }

    @Override
    public boolean onSupportNavigateUp(){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        finish();
        return true;
    }
}
