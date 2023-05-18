package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.UploadFileWrap;
import com.baihe.http.callback.CallBack;
import com.baihe.http.callback.UploadCallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.UploadResultEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Author：xubo
 * Time：2020-09-06
 * Description：
 */
public class ProductAddActivity extends BaseActivity {
    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ProductAddActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    private static final int REQUEST_CODE_SELECT_PHOTO = 100;
    private static final int MAX_ATTACHMENT_NUM = 50;
    private static final int MAX_ATTACHMENT_SELECT_NUM = 9;

    private KeyValueEditLayout product_add_data_kvel;
    private RecyclerView product_add_pics_rv;
    private TextView product_add_ok_tv;

    private FollowAddActivity.AttachmentAddAdapter attachmentAddAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("新建产品");
        setContentView(R.layout.activity_product_add);
        init();
        initData();
        listener();
        loadData();
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("type", "product");
        HttpRequest.create(UrlConstant.BUILD_PARAMS_URL).putParam(jsonParam).get(new CallBack<List<KeyValueEntity>>() {
            @Override
            public List<KeyValueEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, KeyValueEntity.class);
            }

            @Override
            public void success(List<KeyValueEntity> list) {
                statusLayout.normalStatus();
                product_add_data_kvel.setData(list);
            }

            @Override
            public void error() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void fail() {
                statusLayout.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                statusLayout.loadingStatus();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
                List<String> selectPhotoPaths = data.getStringArrayListExtra(PhotoListActivity.RESULT_INTENT_SELECT_PHOTO_PATHS);
                attachmentAddAdapter.add(selectPhotoPaths);
            }
        }
    }

    private void init() {
        product_add_data_kvel = findViewById(R.id.product_add_data_kvel);
        product_add_pics_rv = findViewById(R.id.product_add_pics_rv);
        product_add_ok_tv = findViewById(R.id.product_add_ok_tv);
    }

    private void initData() {
        attachmentAddAdapter = new FollowAddActivity.AttachmentAddAdapter(context);
        product_add_pics_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        final int offset = CommonUtils.dp2pxForInt(context, 15);
        product_add_pics_rv.setAdapter(attachmentAddAdapter);
        product_add_pics_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == attachmentAddAdapter.getItemCount() - 1) {
                    outRect.set(offset, 0, offset, 0);
                } else {
                    outRect.set(offset, 0, 0, 0);
                }
            }
        });
    }

    private void listener() {
        product_add_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = product_add_data_kvel.commit();
                if (data != null) {
                    List<String> uploadUrls = new ArrayList<>();
                    List<String> attachments = attachmentAddAdapter.getData();
                    if (attachments != null && attachments.size() > 0) {
                        uploadAttachments(attachments, data, uploadUrls);
                    } else {
                        commit(data, uploadUrls);
                    }
                    showOptionLoading();
                }
            }
        });
        attachmentAddAdapter.setOnItemClickListener(new FollowAddActivity.AttachmentAddAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<String> pics, int postion) {
                PhotoBrowserActivity.start(context, (ArrayList<String>) pics, postion);
            }

            @Override
            public void onItemDelete(int postion) {
                attachmentAddAdapter.remove(postion);
            }

            @Override
            public void onItemAdd() {
                int dataSize = attachmentAddAdapter.getDataSize();
                int selectMaxSize = MAX_ATTACHMENT_NUM - dataSize;
                selectMaxSize = selectMaxSize > MAX_ATTACHMENT_SELECT_NUM ? MAX_ATTACHMENT_SELECT_NUM : selectMaxSize;

                PhotoListActivity.start(ProductAddActivity.this, selectMaxSize, REQUEST_CODE_SELECT_PHOTO);
            }
        });
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                loadData();
            }

            @Override
            public void onNetFailClick() {
                loadData();
            }

            @Override
            public void onExpandClick() {

            }
        });
    }

    private void uploadAttachments(List<String> attachments, Map<String, Object> data, List<String> uploadUrls) {
        Queue<String> queue = new LinkedList<>();
        for (String attachment : attachments) {
            queue.offer(attachment);
        }
        uploadAttachment(queue, data, uploadUrls);
    }

    private void uploadAttachment(final Queue<String> queue, final Map<String, Object> data, final List<String> uploadUrls) {
        String path = queue.poll();
        if (path == null) {
            commit(data, uploadUrls);
            return;
        }
        HttpRequest.create(UrlConstant.UPLOAD_URL).upload(new UploadFileWrap(path, "file", "image/jpg"), new UploadCallBack<UploadResultEntity>() {
            @Override
            public void uploadProgress(long uploadSize, long totalSize, double progress) {

            }

            @Override
            public UploadResultEntity doInBackground(String response) {
                return JsonUtils.parse(response, UploadResultEntity.class);
            }

            @Override
            public void success(UploadResultEntity entity) {
                uploadUrls.add(entity.getPath());
                if (queue.peek() == null) {
                    commit(data, uploadUrls);
                } else {
                    uploadAttachment(queue, data, uploadUrls);
                }
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void after(UploadResultEntity entity) {
                super.after(entity);
                if (entity == null) {
                    dismissOptionLoading();
                }
            }
        });
    }

    private void commit(Map<String, Object> data, List<String> uploadUrls) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < uploadUrls.size(); i++) {
            buffer.append(uploadUrls.get(i));
            if (i < data.size() - 1) {
                buffer.append(",");
            }
        }
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue(data)
                .putParamValue("attachment", buffer.toString());
        HttpRequest.create(UrlConstant.ADD_PRODUCT_URL).connectTimeout(10000).readTimeout(10000).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String result) {
                ToastUtils.toast("新增成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }
        });
    }

}
