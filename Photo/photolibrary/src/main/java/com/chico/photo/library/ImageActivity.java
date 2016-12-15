package com.chico.photo.library;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chico.photo.library.adapter.ImageAdapter;
import com.chico.photo.library.entity.FoldEntity;
import com.chico.photo.library.entity.MediaEntity;
import com.chico.photo.library.util.FileUtils;
import com.chico.photo.library.util.GridSpacingItemDecoration;
import com.chico.photo.library.util.ImageUtil;
import com.chico.photo.library.util.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class ImageActivity extends AppCompatActivity {

    private ImageView backImage;
    private TextView titleText, cancelText, doneText;
    private RecyclerView mRecyclerView;

    public final static String BUNDLE_CAMERA_PATH = "CameraPath";
    public final static String REQUEST_OUTPUT = "outputList";
    public final static int REQUEST_IMAGE = 66;
    public final static int REQUEST_CAMERA = 67;
    public final static int REQUEST_FOLD = 68;
    public final static int REQUEST_RECORD = 69; //录制视频
    public final static int MODE_MULTIPLE = 1;
    public final static int MODE_SINGLE = 2;
    public final static int TYPE_VIDEO = 1;
    public final static int TYPE_IMAGE = 2;
    public final static String EXTRA_SELECT_TYPE = "SelectType";
    public final static String EXTRA_SELECT_MODE = "SelectMode";
    public final static String EXTRA_SHOW_CAMERA = "ShowCamera";
    public final static String EXTRA_ENABLE_PREVIEW = "EnablePreview";
    public final static String EXTRA_ENABLE_CROP = "EnableCrop";
    public final static String EXTRA_MAX_SELECT_NUM = "MaxSelectNum";

    private int spanCount = 3;
    private int selectType = TYPE_IMAGE;
    private int maxSelectNum = 9;           //最大选择图片
    private int selectMode = MODE_MULTIPLE; //选择模式-单选，多选
    private boolean showCamera = true;      //是否显示拍摄item
    private boolean enablePreview = true;   //是否能预览
    private boolean enableCrop = false;     //是否能裁剪
    private ImageAdapter mAdapter;

    private List<FoldEntity> foldArray; //目录集合
    private String cameraPath;


    public static void start(Activity activity, int type) {
        if (type == TYPE_IMAGE) {
            start(activity, TYPE_IMAGE, 1, MODE_SINGLE, true, false, true);
        } else if (type == TYPE_VIDEO) {
            start(activity, TYPE_VIDEO, 1, MODE_SINGLE, true, false, false);
        }
    }

    public static void start(Activity activity, int type, int maxSelectNum, int mode, boolean isShow, boolean enablePreview, boolean enableCrop) {
        Intent intent = new Intent(activity, ImageActivity.class);
        intent.putExtra(EXTRA_SELECT_TYPE, type);
        intent.putExtra(EXTRA_MAX_SELECT_NUM, maxSelectNum);
        intent.putExtra(EXTRA_SELECT_MODE, mode);
        intent.putExtra(EXTRA_SHOW_CAMERA, isShow);
        intent.putExtra(EXTRA_ENABLE_PREVIEW, enablePreview);
        intent.putExtra(EXTRA_ENABLE_CROP, enableCrop);
        activity.startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void getIntendData(Bundle savedInstanceState) {
        selectType = getIntent().getIntExtra(EXTRA_SELECT_TYPE, TYPE_IMAGE);
        maxSelectNum = getIntent().getIntExtra(EXTRA_MAX_SELECT_NUM, 9);
        selectMode = getIntent().getIntExtra(EXTRA_SELECT_MODE, MODE_MULTIPLE);
        showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        enablePreview = getIntent().getBooleanExtra(EXTRA_ENABLE_PREVIEW, true);
        enableCrop = getIntent().getBooleanExtra(EXTRA_ENABLE_CROP, false);

        if (selectType == TYPE_IMAGE) {
            if (selectMode == MODE_MULTIPLE) {
                enableCrop = false;
            } else {
                enablePreview = false;
            }
        } else {
            enableCrop = false;
            enablePreview = false;
        }

        if (savedInstanceState != null) {
            cameraPath = savedInstanceState.getString(BUNDLE_CAMERA_PATH);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getIntendData(savedInstanceState);

        backImage = (ImageView) findViewById(R.id.iv_back);
        titleText = (TextView) findViewById(R.id.tv_title);
        cancelText = (TextView) findViewById(R.id.tv_cancle);
        doneText = (TextView) findViewById(R.id.tv_done);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_view);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, ScreenUtils.dip2px(this, 5), false));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        mAdapter = new ImageAdapter(this,selectType, maxSelectNum, selectMode, showCamera, enablePreview);
        mRecyclerView.setAdapter(mAdapter);

        registerListener();
        selectMedia();

    }

    /**
     * 注册控件监听事件
     */
    private void registerListener() {
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foldArray != null && foldArray.size() > 0) {
                    FoldListActivity.startFold(ImageActivity.this, foldArray);
                }

            }
        });
        doneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectDone(mAdapter.getSelectedImages());
            }
        });

        mAdapter.setOnImageSelectChangedListener(new ImageAdapter.OnImageSelectChangedListener() {
            @Override
            public void onChange(List<MediaEntity> selectImages) {
                boolean enable = selectImages.size() != 0;
                doneText.setEnabled(enable ? true : false);
                if (enable) {
                    doneText.setText(getString(R.string.done_num, selectImages.size(), maxSelectNum));
                } else {
                    doneText.setText(R.string.done);
                }
            }

            @Override
            public void onTakePhoto() {
                if (selectType == TYPE_IMAGE) {
                    startCamera();
                } else if (selectType == TYPE_VIDEO) {
                    startRecord();
                }
            }

            @Override
            public void onPictureClick(MediaEntity media, int position) {
                if (enablePreview) {
                    startPreview(mAdapter.getImages(), position);
                } else if (enableCrop) {
                    startCrop(media.getPath());
                } else {
                    onSelectDone(media.getPath());
                }
            }
        });
    }


    /**
     * 跳转到预览
     *
     * @param previewImages
     * @param position
     */
    private void startPreview(List<MediaEntity> previewImages, int position) {
        PreviewActivity.startPreview(this, previewImages, mAdapter.getSelectedImages(), maxSelectNum, position);
    }

    /**
     * 跳转到裁剪
     *
     * @param path
     */
    private void startCrop(String path) {
        CropActivity.startCrop(this, path);
    }


    /**
     * 获取所有图片数据
     */
    private void selectMedia() {
        ImageUtil.getAllMediaFold(this, selectType)
                .subscribe(new Subscriber<List<FoldEntity>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<FoldEntity> array) {
                        if (array != null && array.size() > 0) {
                            foldArray = array;
                            titleText.setText(array.get(0).getName());
                            mAdapter.setDatas(array.get(0).getMedias());
                        }
                    }
                });
    }

    /**
     * 调用系统相机
     */
    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File cameraFile = FileUtils.createCameraFile(this);
            cameraPath = cameraFile.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    /**
     * 调用系统录制
     */
    private void startRecord() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_RECORD);
    }

    /**
     * 单张图片完成
     *
     * @param path
     */
    public void onSelectDone(String path) {
        ArrayList<String> images = new ArrayList<>();
        images.add(path);
        onResult(images);
    }

    /**
     * 多张图片完成
     *
     * @param medias
     */
    public void onSelectDone(List<MediaEntity> medias) {
        ArrayList<String> images = new ArrayList<>();
        for (MediaEntity media : medias) {
            images.add(media.getPath());
        }
        onResult(images);
    }

    /**
     * 返回选择图片
     *
     * @param images
     */
    public void onResult(ArrayList<String> images) {
        if (images != null && images.size() > 0) {
            setResult(RESULT_OK, new Intent().putStringArrayListExtra(REQUEST_OUTPUT, images));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FOLD) {
                if (data == null) {
                    finish();
                } else {
                    List<MediaEntity> array = (List<MediaEntity>) data.getExtras().getSerializable("array");
                    String name = data.getExtras().getString("name");
                    mAdapter.setDatas(array);
                    titleText.setText(name);
                }
            } else if (requestCode == REQUEST_CAMERA) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(cameraPath)));
                if (enableCrop) {
                    startCrop(cameraPath);
                } else {
                    onSelectDone(cameraPath);
                }
            } else if (requestCode == PreviewActivity.REQUEST_PREVIEW) { //on preview select change
                boolean isDone = data.getBooleanExtra(PreviewActivity.OUTPUT_ISDONE, false);
                List<MediaEntity> images = (List<MediaEntity>) data.getSerializableExtra(PreviewActivity.OUTPUT_LIST);
                if (images != null && images.size() > 0) {
                    if (isDone) {
                        onSelectDone(images);
                    } else {
                        mAdapter.setSelectDatas(images);
                    }
                }
            } else if (requestCode == CropActivity.REQUEST_CROP) {   // on crop success
                String path = data.getStringExtra(CropActivity.OUTPUT_PATH);
                onSelectDone(path);
            }
        }
    }
}
