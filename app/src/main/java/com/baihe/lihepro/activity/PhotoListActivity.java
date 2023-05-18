package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.manager.Task;
import com.baihe.common.manager.TaskManager;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.PhotoListAdapter;
import com.baihe.lihepro.entity.photo.LocalPhotoEntity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author：xubo
 * Time：2020-08-08
 * Description：
 */
public class PhotoListActivity extends BaseActivity implements View.OnClickListener {
    private static final int MAX_SIZE = 9;
    private static final String INTENT_MAX_SIZE = "INTENT_MAX_SIZE";

    public static final String RESULT_INTENT_SELECT_PHOTO_PATHS = "RESULT_INTENT_SELECT_PHOTO_PATHS";

    public static void start(final Activity activity, final int maxSize, final int requestCode) {
        AndPermission.with(activity)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Intent intent = new Intent(activity, PhotoListActivity.class);
                        intent.putExtra(INTENT_MAX_SIZE, maxSize);
                        activity.startActivityForResult(intent, requestCode);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        ToastUtils.toast("请打开读取权限");
                    }
                })
                .start();
    }

    private RecyclerView photo_list_rv;
    private TextView photo_list_preview_tv;
    private Button photo_list_select_btn;

    private PhotoListAdapter photoListAdapter;
    private AlbumParser albumParser;
    private int maxSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("选择照片");
        setContentView(R.layout.activity_photo_list);
        maxSize = getIntent().getIntExtra(INTENT_MAX_SIZE, MAX_SIZE);
        init();
        initData();
        listener();
    }

    private void init() {
        photo_list_rv = findViewById(R.id.photo_list_rv);
        photo_list_preview_tv = findViewById(R.id.photo_list_preview_tv);
        photo_list_select_btn = findViewById(R.id.photo_list_select_btn);
    }

    private void initData() {
        photoListAdapter = new PhotoListAdapter(context, maxSize);
        photo_list_rv.setAdapter(photoListAdapter);
        photo_list_rv.setLayoutManager(new GridLayoutManager(context, 4));
        final int space = CommonUtils.dp2pxForInt(context, 4);
        final int spaceHalf = CommonUtils.dp2pxForInt(context, 2);
        photo_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childIndex = parent.getChildLayoutPosition(view);
                int count = photoListAdapter.getListSize() / 4 + (photoListAdapter.getListSize() % 4 > 0 ? 1 : 0);
                if (childIndex % 4 == 0) {
                    outRect.left = space;
                } else {
                    outRect.left = spaceHalf;
                }
                if (childIndex % 4 == 3) {
                    outRect.right = space;
                } else {
                    outRect.right = spaceHalf;
                }
                if (childIndex / 4 == 0) {
                    outRect.top = space;
                } else {
                    outRect.top = spaceHalf;
                }
                if (childIndex / 4 == count - 1) {
                    outRect.bottom = space;
                } else {
                    outRect.bottom = spaceHalf;
                }
            }
        });

        TaskManager.newInstance().runOnBackground(new Task() {
            @Override
            public void onRun() {
                albumParser = new AlbumParser(context);
                albumParser.parseAlbum();
                TaskManager.newInstance().runOnUi(new Runnable() {
                    @Override
                    public void run() {
                        List<LocalPhotoEntity> photoInfos = albumParser.photoInfos;
                        photoListAdapter.addList(photoInfos);
                    }
                });
            }
        });
    }

    private void listener() {
        photo_list_preview_tv.setOnClickListener(this);
        photo_list_select_btn.setOnClickListener(this);
        photoListAdapter.setOnItemClickListener(new PhotoListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                ArrayList<String> urls = new ArrayList<>();
                for (LocalPhotoEntity localPhotoEntity : photoListAdapter.getList()) {
                    urls.add(localPhotoEntity.getPhotoPath());
                }
                PhotoBrowserActivity.start(context, urls, position);
            }

            @Override
            public void select(boolean haveSelect) {
                if (haveSelect) {
                    photo_list_select_btn.setText("选择" + "(" + photoListAdapter.getSelectPhotoInfos().size() + ")");
                    photo_list_select_btn.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    photo_list_select_btn.setText("选择");
                    photo_list_select_btn.setTextColor(Color.parseColor("#80FFFFFF"));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_list_preview_tv:
                break;
            case R.id.photo_list_select_btn:
                if (photoListAdapter.haveSelect()) {
                    Map<Integer, Integer> selectPhotoMap = photoListAdapter.getSelectPhotoInfos();
                    Set<Map.Entry<Integer, Integer>> entrys = selectPhotoMap.entrySet();
                    ArrayList<String> sendPhotoPaths = new ArrayList<>();
                    int size = selectPhotoMap.size();
                    for (int i = 0; i < size; i++) {
                        for (Map.Entry<Integer, Integer> entry : entrys) {
                            int key = entry.getKey();
                            int value = entry.getValue();
                            if (i == value - 1) {
                                sendPhotoPaths.add(photoListAdapter.getList().get(key).getPhotoPath());
                                selectPhotoMap.remove(key);
                                break;
                            }
                        }
                    }
                    Intent intentData = new Intent();
                    intentData.putStringArrayListExtra(RESULT_INTENT_SELECT_PHOTO_PATHS, sendPhotoPaths);
                    setResult(RESULT_OK, intentData);
                    finish();
                }
                break;
        }
    }

    public static class AlbumParser {
        /**
         * 所有相片
         */
        public List<LocalPhotoEntity> photoInfos = new ArrayList<LocalPhotoEntity>();
        private ContentResolver resolver;

        public AlbumParser(Context context) {
            resolver = context.getContentResolver();
        }

        /***
         * 解析相册
         */
        public void parseAlbum() {
            Map<String, String> mapForPhotoThumbnail = parseThumbnail();
            String columns[] = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                    MediaStore.Images.Media.DATE_MODIFIED + " desc");
            if (cursor.moveToFirst()) {
                int photoIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int photoPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                int albumNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                do {
                    String photoId = cursor.getString(photoIdIndex);
                    String photoPath = cursor.getString(photoPathIndex);
                    LocalPhotoEntity photoInfo = new LocalPhotoEntity(photoId, photoPath);
                    String photoThumbnailPath = mapForPhotoThumbnail.get(photoId);
                    photoInfo.setPhotoThumbnailPath(photoThumbnailPath);

                    photoInfos.add(photoInfo); //添加到所有相片中
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        /**
         * 解析缩略图
         */
        private Map<String, String> parseThumbnail() {
            String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                    MediaStore.Images.Thumbnails.DATA};
            Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(resolver, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    MediaStore.Images.Thumbnails.MINI_KIND, projection);
            Map<String, String> map = new HashMap<String, String>();
            if (cursor.moveToFirst()) {
                int photoIdIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int photoThumbnailIndex = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                do {
                    String phototId = String.valueOf(cursor.getInt(photoIdIndex));
                    String photoThumbnailPath = cursor.getString(photoThumbnailIndex);
                    map.put(phototId, photoThumbnailPath);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return map;
        }

    }

}
