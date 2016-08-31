package id.developer.tanitionary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityShowPost extends AppCompatActivity {

    ObjectDiscussionLoaded objectDiscussionLoaded;
    Map<String, String> userDetails;
    LinearLayout layoutLikeButton, layoutCommendButton, layoutCommendVisOrGone, layoutParentComment;
    TextView textPost, textNameSender, textDate, textLike;
    ImageView imagePost, imageLike, imageAddComment, imageSendComment, imageCloseAddedImage, imageAddedImage;
    CircleImageView imageSender;
    EditText editComment;

    List<ObjectCommend> commends;
    Boolean isImageAdded = false;
    Bitmap bitmapImageAdded;
    String encodedImage;
    String w_descComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        commends = new ArrayList<>();

        initSession();
        initBundle();
        initToolbar();
        initView();
    }

    private void initSession(){
        userDetails = new SessionLoginManager(this).getUserDetails();
    }

    private void initBundle(){
        Bundle extras = getIntent().getExtras();
        objectDiscussionLoaded = (ObjectDiscussionLoaded)extras.get("post");
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(objectDiscussionLoaded.getSender().getName()+ "'s Post");
    }

    private void initView(){
        layoutCommendButton = (LinearLayout)findViewById(R.id.linear_act_show_post_comment);
        layoutCommendVisOrGone = (LinearLayout)findViewById(R.id.linear_act_show_post_comment_vis_or_gone);
        layoutLikeButton = (LinearLayout)findViewById(R.id.linear_act_show_post_like);
        layoutParentComment = (LinearLayout)findViewById(R.id.linear_act_show_post_parent_coment);
        textPost = (TextView)findViewById(R.id.text_act_show_post_post);
        textDate = (TextView)findViewById(R.id.text_act_show_post_date);
        textLike = (TextView)findViewById(R.id.text_act_show_post_like);
        textNameSender = (TextView)findViewById(R.id.text_act_show_post_name);
        imagePost = (ImageView)findViewById(R.id.image_act_show_post_post);
        imageLike = (ImageView)findViewById(R.id.image_act_show_post_like);
        imageAddComment = (ImageView)findViewById(R.id.image_act_show_post_add);
        imageSendComment = (ImageView)findViewById(R.id.image_act_show_post_send_comment);
        imageAddedImage = (ImageView)findViewById(R.id.image_act_show_post_comment);
        imageCloseAddedImage = (ImageView)findViewById(R.id.image_act_show_post_comment_close);
        imageSender = (CircleImageView)findViewById(R.id.image_act_show_post_profile);
        editComment = (EditText)findViewById(R.id.edit_act_show_post_add);

        textNameSender.setText(objectDiscussionLoaded.getSender().getName());
        textDate.setText(objectDiscussionLoaded.getTimePost());
        textPost.setText(objectDiscussionLoaded.getDescPost());
        textLike.setText("" +objectDiscussionLoaded.getLikes().size());

        if(objectDiscussionLoaded.getIsLiked()){
            imageLike.setImageResource(R.mipmap.ic_like_red);
        }else {
            imageLike.setImageResource(R.mipmap.ic_like_gray);
        }

        doLoadImageFromURL();
        doLoadCommentFromServer();
        doButtonLikeClicked();
        doButtonCommentClicked();
        doButtonAddImageCommentClicked();
        doButtonRemoveImageCommentClicked();
        doButtonSentCommentClicked();
    }

    private void doLoadImageFromURL(){
        Picasso.with(this).load(objectDiscussionLoaded.getSender().getUrlPhoto()).into(imageSender);
        Log.d(Utils.TAG_LINE, Utils.LOG_CHECK_URL + objectDiscussionLoaded.getUrlPostPic());

        if(!objectDiscussionLoaded.getUrlPostPic().equalsIgnoreCase(Utils.ROOT_URL+ "/posts/")){
            Picasso.with(this).load(objectDiscussionLoaded.getUrlPostPic()).into(imagePost);
            imagePost.setVisibility(View.VISIBLE);
        }else{
            imagePost.setVisibility(View.GONE);
        }
    }

    private void doLoadCommentFromServer(){
        String url = Utils.ROOT_URL+ "/getCommentOfPost.php?id_post=" +objectDiscussionLoaded.getIdPost();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject commend = response.getJSONObject(i);
                                String emailSender = commend.getString("id_user");
                                String nameSender = commend.getString("name_user");
                                String picSender = Utils.ROOT_URL+ "/users/" +commend.getString("pic_user");
                                Integer idRel = commend.getInt("id_rel");
                                String descRel = commend.getString("desc_rel");
                                String picRel = Utils.ROOT_URL+ "/comments/" +commend.getString("pic_rel");
                                String dateRel = commend.getString("date_rel");

                                commends.add(new ObjectCommend(idRel, descRel, dateRel, picRel, new ObjectSender(emailSender, nameSender, picSender)));
                            }

                            doShowComment();
                        }catch (Exception e){
                            Toast.makeText(ActivityShowPost.this, getResources().getString(R.string.error_server_down), Toast.LENGTH_SHORT).show();
                        }

                        Log.d(Utils.TAG_LINE, Utils.LOG_RESPONSE_VOLLEY+ response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, Utils.LOG_ERROR_VOLLEY+ error.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void doShowComment(){
        for (int i = 0; i < commends.size(); i++) {
            ObjectCommend commend = commends.get(i);

            View child = LayoutInflater.from(this).inflate(R.layout.layout_child_act_show_post_comment, null);
            TextView textNameChild = (TextView)child.findViewById(R.id.text_layout_child_act_show_post_name);
            TextView textDateChild = (TextView)child.findViewById(R.id.text_layout_child_act_show_post_date);
            TextView textPostChild = (TextView)child.findViewById(R.id.text_layout_child_act_show_post_post);
            CircleImageView imageSenderChild = (CircleImageView)child.findViewById(R.id.image_layout_child_act_show_post_profile);
            ImageView imagePostChild = (ImageView)child.findViewById(R.id.image_layout_child_act_show_post_post);

            textNameChild.setText(commend.getSender().getName());
            textDateChild.setText(commend.getDateCommend());
            textPostChild.setText(commend.getTextCommend());

            Picasso.with(this).load(commend.getSender().getUrlPhoto()).into(imageSenderChild);

            if(!commend.getUrlPhotoCommend().toString().equals("")){
                Picasso.with(this).load(commend.getUrlPhotoCommend()).into(imagePostChild);
                imagePostChild.setVisibility(View.VISIBLE);
            }else{
                imagePostChild.setVisibility(View.GONE);
            }

            layoutParentComment.addView(child);
        }
    }

    private void doButtonLikeClicked(){
        imageLike.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (objectDiscussionLoaded.getIsLiked()) {
                            objectDiscussionLoaded.setIsLiked(false);
                            imageLike.setImageResource(R.mipmap.ic_like_gray);
                            textLike.setText("" + (Integer.parseInt(textLike.getText().toString()) - 1));
                        } else {
                            objectDiscussionLoaded.setIsLiked(true);
                            imageLike.setImageResource(R.mipmap.ic_like_red);
                            textLike.setText("" + (Integer.parseInt(textLike.getText().toString()) + 1));
                        }

                        doLike();
                    }
                }
        );
    }

    private void doLike(){
        String url = Utils.ROOT_URL+ "/setLikeAtPost.php?email=" +userDetails.get(SessionLoginManager.KEY_EMAIL)+ "&post_id=" +objectDiscussionLoaded.getIdPost();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Utils.TAG_LINE, Utils.LOG_RESPONSE_VOLLEY+ response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, Utils.LOG_ERROR_VOLLEY+ error.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void doButtonCommentClicked(){
        layoutCommendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) ActivityShowPost.this
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(layoutCommendButton, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
        );
    }

    private void doButtonAddImageCommentClicked(){
        imageAddComment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, Utils.REQ_GALLERY);
                    }
                }
        );
    }

    private void doButtonRemoveImageCommentClicked(){
        imageCloseAddedImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutCommendVisOrGone.setVisibility(View.GONE);
                        isImageAdded = false;
                    }
                }
        );
    }

    private void doButtonSentCommentClicked(){
        imageSendComment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        w_descComment = editComment.getText().toString().replace("'", "\'");


                        View child = LayoutInflater.from(ActivityShowPost.this).inflate(R.layout.layout_child_act_show_post_comment, null);
                        child = addCommentChild(child);
                        layoutParentComment.addView(child);

                        editComment.setText("");
                        layoutCommendVisOrGone.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);

                        doSendComment(false, child);
                    }
                }
        );
    }

    private void doSendComment(boolean resend, final View view){
        String url = Utils.ROOT_URL+ "/setCommentAtPost.php";
        final String date = getDateAndTime();

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("ok")){
                            ((TextView)view.findViewById(R.id.text_layout_child_act_show_post_date)).setText(date);
                            ((TextView)view.findViewById(R.id.text_layout_child_act_show_post_retry)).setVisibility(View.GONE);
                        }else{
                            ((TextView)view.findViewById(R.id.text_layout_child_act_show_post_date)).setText("Tidak terkirim");
                            ((TextView)view.findViewById(R.id.text_layout_child_act_show_post_retry)).setVisibility(View.VISIBLE);
                        }
                        Log.d(Utils.TAG_LINE, Utils.LOG_RESPONSE_VOLLEY+ response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(Utils.TAG_LINE, Utils.LOG_ERROR_VOLLEY+ error.getMessage());
                        ((TextView)view.findViewById(R.id.text_layout_child_act_show_post_date)).setText("Tidak terkirim");
                        ((TextView)view.findViewById(R.id.text_layout_child_act_show_post_retry)).setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("email", userDetails.get(SessionLoginManager.KEY_EMAIL));
                map.put("id_post", ""+objectDiscussionLoaded.getIdPost());
                map.put("desc_comment", w_descComment);
                map.put("date_comment", date);

                if(isImageAdded){
                    map.put("pic_comment", encodedImage);
                }

                return map;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private View addCommentChild(final View child){
        TextView textName = (TextView)child.findViewById(R.id.text_layout_child_act_show_post_name);
        final TextView textDate = (TextView)child.findViewById(R.id.text_layout_child_act_show_post_date);
        TextView textPost = (TextView)child.findViewById(R.id.text_layout_child_act_show_post_post);
        final TextView textRetry = (TextView)child.findViewById(R.id.text_layout_child_act_show_post_retry);
        CircleImageView imageSender = (CircleImageView)child.findViewById(R.id.image_layout_child_act_show_post_profile);
        ImageView imagePost = (ImageView)child.findViewById(R.id.image_layout_child_act_show_post_post);

        textName.setText(userDetails.get(SessionLoginManager.KEY_NAME));
        textDate.setText("Mengirim..");
        textPost.setText(editComment.getText().toString());

        Picasso.with(this).load(userDetails.get(SessionLoginManager.KEY_IMAGE)).into(imageSender);

        if(isImageAdded){
            imagePost.setImageBitmap(bitmapImageAdded);
            imagePost.setVisibility(View.VISIBLE);
        }else{
            imagePost.setVisibility(View.GONE);
        }

        textRetry.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textDate.setText("Mengirim..");
                        textRetry.setVisibility(View.GONE);
                        doSendComment(true, child);
                    }
                }
        );

        return child;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode == Utils.REQ_GALLERY){
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                Intent intent = new Intent(this, ActivityCropImage.class);
                intent.putExtra("path", picturePath);
                startActivityForResult(intent, Utils.REQ_CROP);

                Log.d(Utils.TAG_LINE, "This is the path : " + picturePath);
            }else if(requestCode == Utils.REQ_CROP){
                // get path from result
                String path = data.getStringExtra("path");
                bitmapImageAdded = BitmapFactory.decodeFile(path);

                // remove file from sdcard
                File file = new File(path);
                file.delete();

                // encode image to string
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapImageAdded.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                // set visibility of layout and set image to imageview
                layoutCommendVisOrGone.setVisibility(View.VISIBLE);
                imageAddedImage.setImageBitmap(bitmapImageAdded);

                // set load image to true
                isImageAdded = true;
                Log.d(Utils.TAG_LINE, "This is the path in crop : " +path);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
