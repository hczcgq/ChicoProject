package com.chico.photo.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.chico.photo.library.R;
import com.chico.photo.library.entity.FoldEntity;
import com.chico.photo.library.entity.MediaEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dee on 15/11/20.
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder>{
    private Context mContext;
    private List<FoldEntity> folders = new ArrayList<>();
    private int checkedIndex = 0;
    private OnItemClickListener listener;

    public FolderAdapter(Context context) {
        this.mContext = context;
    }

    public void setDatas(List<FoldEntity> folders){
        this.folders = folders;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_folder,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FoldEntity folder = folders.get(position);
        Glide.with(mContext)
                .load(new File(folder.getCover()))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .centerCrop()
                .into(holder.firstImage);
        holder.folderName.setText(folder.getName());
        holder.imageNum.setText(mContext.getString(R.string.num_postfix,folder.getNumber()));

        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    checkedIndex = position;
                    notifyDataSetChanged();
                    listener.onItemClick(folder.getName(),folder.getMedias());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView firstImage;
        TextView folderName;
        TextView imageNum;
        View contentView;
        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            firstImage = (ImageView) itemView.findViewById(R.id.first_image);
            folderName = (TextView) itemView.findViewById(R.id.folder_name);
            imageNum = (TextView) itemView.findViewById(R.id.image_num);
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void onItemClick(String folderName, List<MediaEntity> images);
    }
}
