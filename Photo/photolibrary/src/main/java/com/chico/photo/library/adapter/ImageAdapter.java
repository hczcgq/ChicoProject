package com.chico.photo.library.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.chico.photo.library.ImageActivity;
import com.chico.photo.library.R;
import com.chico.photo.library.entity.MediaEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/12/15.
 * Author Chico Chen
 */
public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;

    private List<MediaEntity> images = new ArrayList<>();
    private List<MediaEntity> selectImages = new ArrayList<>();

    private Context mContext;
    private int selectType;
    private boolean showCamera = true;
    private boolean enablePreview = true;
    private int maxSelectNum;
    private int selectMode = ImageActivity.MODE_MULTIPLE;

    private OnImageSelectChangedListener listener;

    public ImageAdapter(Context context,int selectType, int maxSelectNum, int selectMode, boolean showCamera, boolean enablePreview) {
        this.mContext = context;
        this.selectType=selectType;
        this.selectMode = selectMode;
        this.maxSelectNum = maxSelectNum;
        this.showCamera = showCamera;
        this.enablePreview = enablePreview;
    }

    /**
     * 设置数据
     * @param datas
     */
    public void setDatas(List<MediaEntity> datas) {
        this.images = datas;
        notifyDataSetChanged();
    }


    /**
     * 设置选择图片
     * @param images
     */
    public void setSelectDatas(List<MediaEntity> images) {
        this.selectImages = images;
        notifyDataSetChanged();
        if (listener != null) {
            listener.onChange(selectImages);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.nameText.setText(selectType == ImageActivity.TYPE_IMAGE?"拍摄照片":"录制视频");
            headerHolder.mediaIamge.setImageResource(selectType == ImageActivity.TYPE_IMAGE?R.drawable.ic_camera:R.drawable.ic_videocam);
            headerHolder.headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTakePhoto();
                    }
                }
            });
        } else {
            final ViewHolder contentHolder = (ViewHolder) holder;
            contentHolder.videoImage.setVisibility(selectType == ImageActivity.TYPE_IMAGE?View.GONE:View.VISIBLE);

            final MediaEntity image = images.get(showCamera ? position - 1 : position);
            if (image.getPath().endsWith(".gif")) {
                contentHolder.gif.setVisibility(View.VISIBLE);
            } else {
                contentHolder.gif.setVisibility(View.GONE);
            }

            Glide.with(mContext)
                    .load(new File(image.getPath()))
                    .asBitmap()
                    .centerCrop()
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.default_square_image)
                    .error(R.drawable.default_square_image)
                    .into(contentHolder.picture);

            if (selectMode == ImageActivity.MODE_SINGLE) {
                contentHolder.check.setVisibility(View.GONE);
            }

            selectImage(contentHolder, isSelected(image));

            if (enablePreview) {
                contentHolder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeCheckboxState(contentHolder, image);
                    }
                });
            }

            contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((selectMode == ImageActivity.MODE_SINGLE || enablePreview) && listener != null) {
                        listener.onPictureClick(image, showCamera ? position - 1 : position);
                    } else {
                        changeCheckboxState(contentHolder, image);
                    }
                }
            });
        }
    }

    private void changeCheckboxState(ViewHolder contentHolder, MediaEntity image) {
        boolean isChecked = contentHolder.check.isSelected();
        if (selectImages.size() >= maxSelectNum && !isChecked) {
            Toast.makeText(mContext, mContext.getString(R.string.message_max_num, maxSelectNum), Toast.LENGTH_LONG).show();
            return;
        }
        if (isChecked) {
            for (MediaEntity media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    break;
                }
            }
        } else {
            selectImages.add(image);
        }
        selectImage(contentHolder, !isChecked);
        if (listener != null) {
            listener.onChange(selectImages);
        }
    }

    public boolean isSelected(MediaEntity image) {
        for (MediaEntity media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    public void selectImage(ViewHolder holder, boolean isChecked) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            holder.picture.setColorFilter(mContext.getResources().getColor(R.color.image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.picture.setColorFilter(mContext.getResources().getColor(R.color.image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public List<MediaEntity> getImages() {
        return images;
    }

    @Override
    public int getItemCount() {
        return showCamera ? images.size() + 1 : images.size();
    }

    public List<MediaEntity> getSelectedImages() {
        return selectImages;
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        View headerView;
        TextView nameText;
        ImageView mediaIamge;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = itemView;
            nameText= (TextView) itemView.findViewById(R.id.tv_name);
            mediaIamge= (ImageView) itemView.findViewById(R.id.camera);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        ImageView picture;
        ImageView check;
        ImageView gif;
        ImageView videoImage;
        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            picture = (ImageView) itemView.findViewById(R.id.picture);
            gif = (ImageView) itemView.findViewById(R.id.iv_gif);
            check = (ImageView) itemView.findViewById(R.id.check);
            videoImage= (ImageView) itemView.findViewById(R.id.iv_video);
        }
    }

    public interface OnImageSelectChangedListener {
        void onChange(List<MediaEntity> selectImages);

        void onTakePhoto();

        void onPictureClick(MediaEntity media, int position);
    }

    public void setOnImageSelectChangedListener(OnImageSelectChangedListener listener) {
        this.listener = listener;
    }
}
