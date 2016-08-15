package id.developer.tanitionary.main.forum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.developer.tanitionary.ObjectDiscussionLoaded;
import id.developer.tanitionary.R;

/**
 * Created by Naufal on 8/13/2016.
 */
public class RecyclerAdapterForum extends RecyclerView.Adapter<ViewHolder>{

    private int TYPE_BLANK = 1, TYPE_CONTENT = 2, TYPE_PROG = 3;

    Context context;
    List<ObjectDiscussionLoaded> listData;

    private int visibleThreshold = 2;
    private int totalItemCount, lastItemShown;
    private boolean loading;
    private int lastPosition = 1;

    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerAdapterForum(Context context, List<ObjectDiscussionLoaded> list, RecyclerView recyclerView){
        this.context = context;
        this.listData = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_BLANK){
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_blank_actionbar_size, parent, false));
        }else if(viewType == TYPE_CONTENT){
            return ViewHolder.initInstance(LayoutInflater.from(context).inflate(R.layout.recycler_fragment_main_forum_content, parent, false), "content");
        }else{
            return ViewHolder.initInstance(LayoutInflater.from(context).inflate(R.layout.layout_blank_actionbar_size, parent, false), "load");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {

        switch (position){
            case 0:
                return TYPE_BLANK;

            default:
//                return listData.get(position-1) != null ? TYPE_CONTENT : TYPE_PROG;
                return TYPE_CONTENT;
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{

    String type;

    public ViewHolder(View itemView) {
        super(itemView);
        type = "";
    }

    public ViewHolder(View itemView, String type){
        super(itemView);
        this.type = type;

        if(type.equalsIgnoreCase("content")){

        }else{

        }
    }

    public static ViewHolder initInstance(View view, String type){
        return new ViewHolder(view, type);
    }
}
