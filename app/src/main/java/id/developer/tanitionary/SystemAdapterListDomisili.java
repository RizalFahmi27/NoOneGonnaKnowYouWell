package id.developer.tanitionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Naufal on 8/19/2016.
 */
public class SystemAdapterListDomisili extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    Context context;
    private List<ObjectDomisili> domisilis;
//    private String[] mCountries;
    private int[] mSectionIndices;
    private String[] mSectionLetters;
    private LayoutInflater mInflater;

    public SystemAdapterListDomisili(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        domisilis = new SessionListDomisili(context).getListForum();

//        mCountries = context.getResources().getStringArray(R.array.domisili);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        char lastFirstChar = domisilis.get(0).getName().charAt(0);
        sectionIndices.add(0);
        for (int i = 1; i < domisilis.size(); i++) {
            if (domisilis.get(i).getName().charAt(0) != lastFirstChar) {
                lastFirstChar = domisilis.get(i).getName().charAt(0);
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = domisilis.get(mSectionIndices[i]).getProv();
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
        CharSequence headerChar = domisilis.get(i).getName().subSequence(0, 1);
        holder.text.setText(headerChar);

        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return domisilis.get(i).getName().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getCount() {
        return domisilis.size();
    }

    @Override
    public Object getItem(int position) {
        return domisilis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return domisilis.indexOf(domisilis.get(position));
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

        holder.text.setText(domisilis.get(position).getName()+ " - " +domisilis.get(position).getProv());

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
