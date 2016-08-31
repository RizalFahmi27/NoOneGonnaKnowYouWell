package id.developer.tanitionary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ActivityCropImage extends AppCompatActivity {

    private static final String TAG_LINE = "Act Crop Image";

    CropImageView mCropImageView;
    LinearLayout linearOk, linearCancel;
    String path;
    boolean isForPP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        initBundle();
        initView();
    }

    private void initBundle(){
        Bundle extras = getIntent().getExtras();
        path = extras.getString("path");

        try {
            isForPP = extras.getBoolean("rectangle");
        }catch (Exception e){}
    }

    private void initView(){
        mCropImageView = (CropImageView)findViewById(R.id.crop_act_crop_image);
        linearOk = (LinearLayout)findViewById(R.id.linear_act_crop_image_ok);
        linearCancel = (LinearLayout)findViewById(R.id.linear_act_crop_image_cancel);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() / 1), (int)(bitmap.getHeight() / 1), false);

        Log.d(TAG_LINE, "This is the size of bitmap : " +bitmap.getWidth()+ "x" +bitmap.getHeight());
        Log.d(TAG_LINE, "This is the size of bitmap : " + scaledBitmap.getWidth() + "x" + scaledBitmap.getHeight());

        mCropImageView.setImageBitmap(scaledBitmap);

        if(isForPP){
            mCropImageView.setFixedAspectRatio(true);
            mCropImageView.setAspectRatio(1, 1);
        }else{
            mCropImageView.setFixedAspectRatio(false);
        }

        mCropImageView.setOnGetCroppedImageCompleteListener(
                new CropImageView.OnGetCroppedImageCompleteListener() {
                    @Override
                    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {

//                        saveBitmapToFile();

                        //set the path where we want to save the file
                        File SDCardRoot = Environment.getExternalStorageDirectory();
                        File objectDir = new File(SDCardRoot+"/Tanitionary/");

                        // check directory existence
                        if(!objectDir.exists()){
                            objectDir.mkdirs();
                        }

                        SessionLoginManager mLoginManager = new SessionLoginManager(getApplicationContext());

                        //create a new file, to save the downloaded file
                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();
                        File file = new File(objectDir, "IMG_" +mLoginManager.getUserDetails().get(SessionLoginManager.KEY_EMAIL)+ "_" +ts);


                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ActivityCropImage.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent();
                        intent.putExtra("path", file.getPath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );

        linearCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                }
        );
        linearOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCropImageView.getCroppedImageAsync();
                    }
                }
        );
    }

    private void saveBitmapToFile(){
        try {

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            File objectDir = new File(SDCardRoot+"/Tanitionary/");

            // check directory existence
            if(!objectDir.exists()){
                objectDir.mkdirs();
            }

            SessionLoginManager mLoginManager = new SessionLoginManager(getApplicationContext());

            //create a new file, to save the downloaded file
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            File file = new File(objectDir, "IMG_" +mLoginManager.getUserDetails().get(SessionLoginManager.KEY_EMAIL)+ "_" +ts);

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (Exception e) {

        }
    }
}
