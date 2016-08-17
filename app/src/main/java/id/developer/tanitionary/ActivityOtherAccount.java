package id.developer.tanitionary;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ActivityOtherAccount extends AppCompatActivity {

    ExpandableStickyListHeadersListView mExpandableListView;
    WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
    RelativeLayout layoutAvatar, layoutName, layoutDomisili, layoutPhone, layoutEmail, layoutLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView(){
        layoutAvatar = (RelativeLayout)findViewById(R.id.relative_act_other_account_layout_avatar);
        layoutName = (RelativeLayout)findViewById(R.id.relative_act_other_account_layout_name);
        layoutDomisili = (RelativeLayout)findViewById(R.id.relative_act_other_account_layout_location);
        layoutPhone = (RelativeLayout)findViewById(R.id.relative_act_other_account_layout_phone_number);
        layoutEmail = (RelativeLayout)findViewById(R.id.relative_act_other_account_layout_email);
        layoutLogOut = (RelativeLayout)findViewById(R.id.relative_act_other_account_layout_logout);

        layoutLogOut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AlertDialog.Builder(ActivityOtherAccount.this)
                                .setTitle(getString(R.string.app_name))
                                .setMessage(getString(R.string.act_other_account_sure_to_log_out))
                                .setPositiveButton(R.string.dialog_yes_button,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SessionLoginManager mSessionLoginManager = new SessionLoginManager(getApplicationContext());
                                                mSessionLoginManager.userLogout();

                                                try{
                                                    ActivityMain.mActivityMain.finish();
                                                }catch (Exception e){

                                                }finally {
                                                    startActivity(new Intent(ActivityOtherAccount.this, ActivityFirst.class));
                                                    finish();
                                                }
                                            }
                                        })
                                .setNegativeButton(R.string.dialog_no_button, null)
                                .show();
                    }
                }
        );
    }

    private void initListView(){
//        mExpandableListView = (ExpandableStickyListHeadersListView)findViewById(R.id.list_act_other_account);
//        //custom expand/collapse animation
//        mExpandableListView.setAnimExecutor(new AnimationExecutor());
//        mExpandableListView.setAdapter(new ExpandableListAdapter(this));
//        mExpandableListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
//            @Override
//            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
//                if(mExpandableListView.isHeaderCollapsed(headerId)){
//                    mExpandableListView.expand(headerId);
//                }else {
//                    mExpandableListView.collapse(headerId);
//                }
//            }
//        });
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

    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if(ExpandableStickyListHeadersListView.ANIMATION_EXPAND==animType&&target.getVisibility()==View.VISIBLE){
                return;
            }
            if(ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE==animType&&target.getVisibility()!=View.VISIBLE){
                return;
            }
            if(mOriginalViewHeightPool.get(target)==null){
                mOriginalViewHeightPool.put(target,target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();

        }
    }
}

class ExpandableListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer{

    Context context;
    private String[] mCountries;
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;

    public ExpandableListAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mCountries = context.getResources().getStringArray(R.array.countries);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        char lastFirstChar = mCountries[0].charAt(0);
        sectionIndices.add(0);
        for (int i = 1; i < mCountries.length; i++) {
            if (mCountries[i].charAt(0) != lastFirstChar) {
                lastFirstChar = mCountries[i].charAt(0);
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = mCountries[mSectionIndices[i]].charAt(0);
        }
        return letters;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        HeaderViewHolder holder;

        if (view == null) {
            holder = new HeaderViewHolder();
            view = mInflater.inflate(R.layout.layout_act_other_account_header, viewGroup, false);
            holder.text = (TextView)view.findViewById(R.id.text_layout_act_other_account_header);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }

        // set header text as first char in name
        CharSequence headerChar = mCountries[i].subSequence(0, 1);
        holder.text.setText(headerChar);

        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return mCountries[i].subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return mCountries.length;
    }

    @Override
    public Object getItem(int position) {
        return mCountries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_act_other_account_item, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text_layout_act_other_account_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(mCountries[position]);

        return convertView;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }
}