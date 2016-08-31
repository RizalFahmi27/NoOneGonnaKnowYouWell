package id.developer.tanitionary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ActivityRegister extends AppCompatActivity {

    private static final Integer REQ_GALLERY_PHOTO = 202;
    private final static Integer REQ_CROP = 232;
    private final static String TAG_LINE = "Act Other Account";
    private static String ROOT_URL = "http://www.tanitionary.pe.hu";

    // Parameter parsing to server
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_PASS = "pass";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_PHONE = "nomor";
    private static final String PARAM_DOMISILI = "domisili";
    private static final String PARAM_PIC = "pic";
    private static final String PARAM_DATE = "date";

    RequestQueue queue;

    TextView textGreeting, textGreetingStep1, textGreetingStep2, textGreetingStep3;
    LinearLayout linearStep1, linearStep2, linearStep3;
    EditText editEmail, editPass, editPassConf, editName, editDomisili, editPhone;
    ExpandableStickyListHeadersListView listViewDomisili;
    ScrollView scrollItem;
    RelativeLayout relativeImagePP;
    CircleImageView imagePP;
    TextView textNextStep1, textNextStep2, textNextStep3;

    LinearLayout.LayoutParams paramClose, paramOpen;
    ResizeAnimation animateOpen, animateClose;

    private int inStep = 1;
    private boolean isListViewShowed = false;
    private boolean isPhotoLoad = false;
    private Bitmap bitmapPP;
    private String encodedImage;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        queue = Volley.newRequestQueue(this);

        initPD();
        initListView();
        initView();
    }

    private void initPD(){
        PD = new ProgressDialog(this);
        PD.setMessage("Cek Data");
        PD.setCancelable(false);
    }

    private void initListView(){
        listViewDomisili = (ExpandableStickyListHeadersListView)findViewById(R.id.list_act_register_domisili);
        scrollItem = (ScrollView)findViewById(R.id.scroll_act_register);

        //custom expand/collapse animation
        listViewDomisili.setAnimExecutor(new SystemAnimationExpandableListView(new WeakHashMap<View, Integer>()));
        listViewDomisili.setAdapter(new SystemAdapterListDomisili(this));
        listViewDomisili.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (listViewDomisili.isHeaderCollapsed(headerId)) {
                    listViewDomisili.expand(headerId);
                } else {
                    listViewDomisili.collapse(headerId);
                }
            }
        });
        listViewDomisili.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        isListViewShowed = false;
                        listViewDomisili.setVisibility(View.GONE);
                        scrollItem.setVisibility(View.VISIBLE);
                        editDomisili.setText(((ObjectDomisili)listViewDomisili.getItemAtPosition(position)).getName());
                    }
                }
        );
    }

    private void initView(){
        textGreeting = (TextView)findViewById(R.id.text_act_register_greeting);
        textGreetingStep1 = (TextView)findViewById(R.id.text_act_register_step_1);
        textGreetingStep2 = (TextView)findViewById(R.id.text_act_register_step_2);
        textGreetingStep3 = (TextView)findViewById(R.id.text_act_register_step_3);
        textNextStep1 = (TextView)findViewById(R.id.text_act_register_step_1_next);
        textNextStep2 = (TextView)findViewById(R.id.text_act_register_step_2_next);
        textNextStep3 = (TextView)findViewById(R.id.text_act_register_step_3_next);
        editEmail = (EditText)findViewById(R.id.edit_act_register_email);
        editDomisili = (EditText)findViewById(R.id.edit_act_register_domisili);
        editName = (EditText)findViewById(R.id.edit_act_register_name);
        editPass = (EditText)findViewById(R.id.edit_act_register_pass);
        editPassConf = (EditText)findViewById(R.id.edit_act_register_pass_confirm);
        editPhone = (EditText)findViewById(R.id.edit_act_register_phone);
        relativeImagePP = (RelativeLayout)findViewById(R.id.relative_act_register_avatar);
        imagePP = (CircleImageView)findViewById(R.id.image_act_register_avatar);
        linearStep1 = (LinearLayout)findViewById(R.id.linear_act_register_child_1);
        linearStep2 = (LinearLayout)findViewById(R.id.linear_act_register_child_2);
        linearStep3 = (LinearLayout)findViewById(R.id.linear_act_register_child_3);

        textGreeting.setText(Html.fromHtml(getString(R.string.act_register_greeting)));
        textGreeting.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoeui.ttf"));

        editDomisili.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrollItem.setVisibility(View.GONE);
                        listViewDomisili.setVisibility(View.VISIBLE);
                        isListViewShowed = true;
                    }
                }
        );

        relativeImagePP.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, REQ_GALLERY_PHOTO);
                    }
                }
        );

        textNextStep1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = editEmail.getText().toString();
                        String pass = editPass.getText().toString();
                        String passConf = editPassConf.getText().toString();

                        if (email.equals("") || pass.equals("") || passConf.equals("")) {
                            Toast.makeText(ActivityRegister.this, "Ups, anda melupakan beberapa data.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!pass.equals(passConf)) {
                            Toast.makeText(ActivityRegister.this, "Ups, password anda tidak sama.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        PD.show();
                        cekAvailabilityOf(PARAM_EMAIL, email);
                    }
                }
        );

        textNextStep2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editName.getText().toString();
                        String domisili = editDomisili.getText().toString();
                        String phoneNumber = editPhone.getText().toString();

                        if(name.equals("") || domisili.equals("") || phoneNumber.equals("")){
                            Toast.makeText(ActivityRegister.this, "Ups, anda melupakan beberapa data.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(name.length() < 2){
                            Toast.makeText(ActivityRegister.this, "Ups, nama anda terlalu pendek.", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(name.length() > 20){
                            Toast.makeText(ActivityRegister.this, "Ups, nama anda terlalu panjang.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        PD.show();
                        cekAvailabilityOf(PARAM_PHONE, phoneNumber);
                    }
                }
        );

        textNextStep3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PD.show();
                        parseAllData();
                    }
                }
        );
    }

    private void nextButtonClicked(Integer step, LinearLayout layoutClosing, LinearLayout layoutOpening){
        inStep += step;

        // Gets linearlayout
        // Gets the layout params that will allow you to resize the layout
        paramClose = (LinearLayout.LayoutParams)layoutClosing.getLayoutParams();
        paramOpen = (LinearLayout.LayoutParams)layoutOpening.getLayoutParams();

        animateClose = new ResizeAnimation(layoutClosing);
        animateClose.setDuration(500);

        animateOpen = new ResizeAnimation(layoutOpening);
        animateOpen.setDuration(500);

        animateClose.setParams(layoutClosing.getHeight(), 0);
        layoutClosing.startAnimation(animateClose);

        layoutOpening.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        animateOpen.setParams(0, paramOpen.WRAP_CONTENT);
//        layoutOpening.startAnimation(animateOpen);

        switch (inStep){
            case 1:
                textGreetingStep1.setTextColor(getResources().getColor(R.color.colorBlueLight));
                textGreetingStep2.setTextColor(getResources().getColor(R.color.colorGrayAbout));
                textGreetingStep3.setTextColor(getResources().getColor(R.color.colorGrayAbout));
                break;

            case 2:
                textGreetingStep1.setTextColor(getResources().getColor(R.color.colorGrayAbout));
                textGreetingStep2.setTextColor(getResources().getColor(R.color.colorBlueLight));
                textGreetingStep3.setTextColor(getResources().getColor(R.color.colorGrayAbout));
                break;

            case 3:
                textGreetingStep1.setTextColor(getResources().getColor(R.color.colorGrayAbout));
                textGreetingStep2.setTextColor(getResources().getColor(R.color.colorGrayAbout));
                textGreetingStep3.setTextColor(getResources().getColor(R.color.colorBlueLight));
                break;
        }
    }

    private void cekAvailabilityOf(final String param, String value){

        String url = ROOT_URL+ "/cekAvailability.php?" +param+ "=" +value;
        Log.d(TAG_LINE, "This is the url : " +url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("ok")){

                            if(param.equalsIgnoreCase(PARAM_EMAIL)){
                                nextButtonClicked(1, linearStep1, linearStep2);
                            }else if(param.equalsIgnoreCase(PARAM_PHONE)){
                                nextButtonClicked(1, linearStep2, linearStep3);
                            }
                        }else{
                            Toast.makeText(ActivityRegister.this, "Ups, " +param+ " sudah digunakan.", Toast.LENGTH_SHORT).show();
                        }

                        PD.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG_LINE, "This is the error from volley : " +error.getMessage());
                        PD.hide();
                        Toast.makeText(ActivityRegister.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    private void parseAllData(){

        String url = ROOT_URL+ "/setNewUser.php";

        if(!isPhotoLoad){
            bitmapPP = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_avatar);
            isPhotoLoad = true;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapPP.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG_LINE, "This is the response from uploading image : " + response);
                        PD.hide();

                        if(response.equalsIgnoreCase("success")){
                            goToConfirmation();
                        }else{
                            Toast.makeText(ActivityRegister.this, getResources().getString(R.string.error_server_down), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                ,new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG_LINE, "This is the error from volley : " +error.getMessage());
                        PD.hide();
                        Toast.makeText(ActivityRegister.this, getResources().getString(R.string.error_no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                Calendar c = Calendar.getInstance();

                String email = editEmail.getText().toString();
                String pass = editPass.getText().toString();
                String name = editName.getText().toString();
                String domisili = editDomisili.getText().toString();
                String number = editPhone.getText().toString();
                String date = c.get(Calendar.DATE)+ "/" +c.get(Calendar.MONTH)+ "/" +c.get(Calendar.YEAR);

                map.put(PARAM_EMAIL, email);
                map.put(PARAM_PASS, pass);
                map.put(PARAM_NAME, name);
                map.put(PARAM_DOMISILI, domisili);
                map.put(PARAM_PHONE, number);
                map.put(PARAM_PIC, encodedImage);
                map.put(PARAM_DATE, date);

                return map;
            }
        };

        queue.add(request);
    }

    private void goToConfirmation(){
        Intent intent = new Intent(ActivityRegister.this, ActivityRegistrationConfirmation.class);
        intent.putExtra("email", editEmail.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {

        if(isListViewShowed){
            listViewDomisili.setVisibility(View.GONE);
            scrollItem.setVisibility(View.VISIBLE);
            isListViewShowed = false;
        }else if(inStep == 3){
            nextButtonClicked(-1, linearStep3, linearStep2);
        }else if(inStep == 2){
            nextButtonClicked(-1, linearStep2, linearStep1);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                intent.putExtra("rectangle", true);
                startActivityForResult(intent, REQ_CROP);

                Log.d(TAG_LINE, "This is the path : " + picturePath);
            }else if(requestCode == REQ_CROP){
                String imagePath = data.getStringExtra("path");

                bitmapPP = BitmapFactory.decodeFile(imagePath);
                isPhotoLoad = true;

                Picasso
                        .with(this)
                        .load(new File(imagePath))
                        .into(imagePP);

                Log.d(TAG_LINE, "This is the path in crop : " +imagePath);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

class ResizeAnimation extends Animation {

    private int startHeight;
    private int deltaHeight; // distance between start and end height
    private View view;

    /**
     * constructor, do not forget to use the setParams(int, int) method before
     * starting the animation
     * @param v
     */
    public ResizeAnimation (View v) {
        this.view = v;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        view.getLayoutParams().height = (int) (startHeight + deltaHeight * interpolatedTime);
        view.requestLayout();
    }

    /**
     * set the starting and ending height for the resize animation
     * starting height is usually the views current height, the end height is the height
     * we want to reach after the animation is completed
     * @param start height in pixels
     * @param end height in pixels
     */
    public void setParams(int start, int end) {

        this.startHeight = start;
        deltaHeight = end - startHeight;
    }

    /**
     * set the duration for the hideshowanimation
     */
    @Override
    public void setDuration(long durationMillis) {
        super.setDuration(durationMillis);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}