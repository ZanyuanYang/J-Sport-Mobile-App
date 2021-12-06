package edu.neu.madcourse.prev_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.neu.madcourse.team_j_sport.R;

public class StickerGridViewAdapter extends BaseAdapter {
    private List<StickerGridViewCell> cellList;
    private LayoutInflater layoutInflater;
    private final  Context mContext;
    public StickerGridViewAdapter(Context context, List<StickerGridViewCell> cellList){
        this.cellList = cellList;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return cellList.size();
    }

    @Override
    public Object getItem(int i) {
        return cellList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {



        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.sticker_grid_view_cell, null);
            view.setTag(viewHolder);
            viewHolder.stickerImageView = view.findViewById(R.id.Sticker_ImageView);

        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        StickerGridViewCell cell = cellList.get(i);
        if (cell != null){
            ImageView imageView = viewHolder.stickerImageView;
            Glide.with(mContext /* context */)
                    .load(cell.imageReference)
                    .into(imageView);
        }

        return view;
    }

    private class ViewHolder{
        public ImageView stickerImageView;
    }
}
