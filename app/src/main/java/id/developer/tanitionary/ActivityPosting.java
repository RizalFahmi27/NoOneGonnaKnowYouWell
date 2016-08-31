package id.developer.tanitionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityPosting extends AppCompatActivity {

    private static final Integer REQ_GALLERY_PHOTO = 202;
    private static final Integer REQ_CROP = 212;
    private boolean isLoadImage = false;
    private String encodedImage;

    EditText editPost;
    TextView textPoster;
    ImageView imgPoster, imgAdded, imgClose;
    RelativeLayout relativeAddPhoto, relativeVisOrGone;

    SessionLoginManager mLoginManager;
    Map<String, String> userDetails;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PD = new ProgressDialog(this);
        PD.setMessage("Mengirim data");
        PD.setCancelable(false);

        initSession();
        initView();
    }

    private void initSession(){
        mLoginManager = new SessionLoginManager(this);
        userDetails = mLoginManager.getUserDetails();
    }

    private void initView(){
        editPost = (EditText)findViewById(R.id.edit_act_posting_text);
        textPoster = (TextView)findViewById(R.id.text_act_posting_poster);
        imgPoster = (ImageView)findViewById(R.id.image_act_posting_poster);
        imgAdded = (ImageView)findViewById(R.id.image_act_posting_added_image);
        imgClose = (ImageView)findViewById(R.id.image_act_posting_close);
        relativeAddPhoto = (RelativeLayout)findViewById(R.id.relative_act_posting_add_photo);
        relativeVisOrGone = (RelativeLayout)findViewById(R.id.relative_act_posting_vis_or_gone);

        Picasso
                .with(this)
                .load(userDetails.get(SessionLoginManager.KEY_IMAGE))
                .into(imgPoster);

        textPoster.setText(userDetails.get(SessionLoginManager.KEY_NAME));

        relativeAddPhoto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, REQ_GALLERY_PHOTO);
                    }
                }
        );

        imgClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        relativeVisOrGone.setVisibility(View.GONE);
                        isLoadImage = false;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_posting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_post:
                PD.show();
                doPost();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQ_GALLERY_PHOTO){
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                Intent intent = new Intent(this, ActivityCropImage.class);
                intent.putExtra("path", picturePath);
                intent.putExtra("type", "posting");
                startActivityForResult(intent, REQ_CROP);

                Log.d(Utils.TAG_LINE, "This is the path : " + picturePath);
            }else if(requestCode == REQ_CROP){

                // get path from result
                String path = data.getStringExtra("path");
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                // remove file from sdcard
                File file = new File(path);
                file.delete();

                // encode image to string
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                // set visibility of layout and set image to imageview
                relativeVisOrGone.setVisibility(View.VISIBLE);
                imgAdded.setImageBitmap(bitmap);

                // set load image to true
                isLoadImage = true;
                Log.d(Utils.TAG_LINE, "This is the path in crop : " +path);
            }
        }
    }

    private void doPost(){
        String url = Utils.ROOT_URL+ "/setNewPost.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("ok")){
                            setResult(RESULT_OK);
                            finish();
                        }else{
                            Toast.makeText(ActivityPosting.this, getResources().getString(R.string.error_server_down), Toast.LENGTH_SHORT).show();
                        }

                        Log.d(Utils.TAG_LINE, "This is the response : " +response);
                        PD.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, "This is the error from volley : " +error.getMessage());
                        PD.hide();
                        Toast.makeText(ActivityPosting.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String text = editPost.getText().toString();
                text.replace("'","\'");

                map.put("email", userDetails.get(SessionLoginManager.KEY_EMAIL));
                map.put("date", getDateAndTime());
                map.put("post", text);

                if(isLoadImage){
                    map.put("image", encodedImage);
                }

                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private String getDateAndTime(){
        return Calendar.getInstance().get(Calendar.DATE)+ "-"
                +Calendar.getInstance().get(Calendar.MONTH)+ "-"
                +Calendar.getInstance().get(Calendar.YEAR)+ "_"
                +Calendar.getInstance().get(Calendar.HOUR)+ ":"
                +Calendar.getInstance().get(Calendar.MINUTE)+ ":"
                +Calendar.getInstance().get(Calendar.SECOND)+ "_"
                +Calendar.getInstance().get(Calendar.MILLISECOND);
    }
}
