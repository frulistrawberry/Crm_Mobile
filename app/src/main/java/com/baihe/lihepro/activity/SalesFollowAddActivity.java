package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.UploadFileWrap;
import com.baihe.http.callback.CallBack;
import com.baihe.http.callback.UploadCallBack;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.UploadResultEntity;
import com.baihe.lihepro.glide.transformation.RoundedCornersTransformation;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Author：xubo
 * Time：2020-08-08
 * Description：
 */
public class SalesFollowAddActivity extends BaseActivity {
    private static final String INTENT_FOLLOW_CONFIG = "INTENT_FOLLOW_CONFIG";
    private static final String INTENT_CUSTOMERID = "INTENT_CUSTOMERID";
    private static final String INTENT_ORDERID = "INTENT_ORDERID";
    private static final String INTENT_RESERVEID = "INTENT_RESERVEID";
    private static final String INTENT_RESERVE_CONFIRM_COUNT = "INTENT_RESERVE_CONFIRM_COUNT";

    private static final int REQUEST_CODE_SELECT_PHOTO = 100;

    private static final int MAX_ATTACHMENT_NUM = 50;
    private static final int MAX_ATTACHMENT_SELECT_NUM = 9;

    public static void start(Activity activity, String orderId, String customerId, List<KeyValueEntity> followConfigData, String reserveId, int reserveConfirmCount, int requestCode) {
        Intent intent = new Intent(activity, SalesFollowAddActivity.class);
        intent.putParcelableArrayListExtra(INTENT_FOLLOW_CONFIG, (ArrayList<? extends Parcelable>) followConfigData);
        intent.putExtra(INTENT_ORDERID, orderId);
        intent.putExtra(INTENT_CUSTOMERID, customerId);
        intent.putExtra(INTENT_RESERVEID, reserveId);
        intent.putExtra(INTENT_RESERVE_CONFIRM_COUNT, reserveConfirmCount);
        activity.startActivityForResult(intent, requestCode);
    }

    private KeyValueEditLayout follow_add_follow_kvel;
    private RecyclerView follow_detail_attachment_rv;
    private TextView follow_detail_ok_tv;

    private List<KeyValueEntity> followConfigData;
    private String reserveId;
    private int reserveConfirmCount;
    private AttachmentAddAdapter attachmentAddAdapter;
    private String customerId;
    private String orderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("录入跟进");
        setContentView(R.layout.activity_follow_add);
        followConfigData = getIntent().getParcelableArrayListExtra(INTENT_FOLLOW_CONFIG);
        reserveId = getIntent().getStringExtra(INTENT_RESERVEID);
        reserveConfirmCount = getIntent().getIntExtra(INTENT_RESERVE_CONFIRM_COUNT, -1);
        customerId = getIntent().getStringExtra(INTENT_CUSTOMERID);
        orderId = getIntent().getStringExtra(INTENT_ORDERID);
        init();
        initData();
        listener();
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
        follow_add_follow_kvel = findViewById(R.id.follow_add_follow_kvel);
        follow_detail_attachment_rv = findViewById(R.id.follow_detail_attachment_rv);
        follow_detail_ok_tv = findViewById(R.id.follow_detail_ok_tv);
    }

    private void initData() {
        follow_add_follow_kvel.setData(followConfigData);

        attachmentAddAdapter = new AttachmentAddAdapter(context);
        follow_detail_attachment_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        final int offset = CommonUtils.dp2pxForInt(context, 15);
        follow_detail_attachment_rv.setAdapter(attachmentAddAdapter);
        follow_detail_attachment_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
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

    private int findNumCount(String str){
        int count = 0;
        char[] array = str.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i]>='0' && array[i]<='9'){
                count++;
            }
        }
        return count;
    }

    private void listener() {
        follow_detail_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = follow_add_follow_kvel.commit();
                if (data != null) {
                    if (data.containsKey("comment")){
                        String comment = (String) data.get("comment");
                        if (!TextUtils.isEmpty(comment)){
                            if (findNumCount(comment)>3){
                                ToastUtils.toast("跟进记录输入数字累计不允许超过3个！");
                                return;
                            }
                        }
                    }
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
        attachmentAddAdapter.setOnItemClickListener(new AttachmentAddAdapter.OnItemClickListener() {
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

                PhotoListActivity.start(SalesFollowAddActivity.this, selectMaxSize, REQUEST_CODE_SELECT_PHOTO);
            }
        });
        follow_add_follow_kvel.setOnItemActionListener(new KeyValueEditLayout.OnItemActionListener() {
            @Override
            public void onEvent(KeyValueEntity keyValueEntity, KeyValueEditLayout.ItemAction itemAction) {
                //到店状态
                KeyValueEntity findEntity1 = follow_add_follow_kvel.findEntityByParamKey("orderArrivalStatus");
                //到店时间
                KeyValueEntity findEntity2 = follow_add_follow_kvel.findEntityByParamKey("orderArrivalTime");
                //到店地点
                KeyValueEntity findEntity3 = follow_add_follow_kvel.findEntityByParamKey("ArrivalPlace");
                //到店目的
                KeyValueEntity findEntity4 = follow_add_follow_kvel.findEntityByParamKey("ArrivalObjective");
                if (keyValueEntity.getEvent().getParamKey().equals("status")) {  //销售状态
                    // 【销售状态】如果选择【销售待定】或【销售有效维护】则出现到店状态
                    if ("310".equals(keyValueEntity.getDefaultVal()) || "330".equals(keyValueEntity.getDefaultVal())) {
                        if (findEntity1 != null) {
                            findEntity1.setShowStatus("1");
                            findEntity1.setDefaultVal("");
                            follow_add_follow_kvel.refreshItem(findEntity1);
                        }
                    } else {
                        if (findEntity1 != null) {
                            findEntity1.setShowStatus("0");
                            follow_add_follow_kvel.refreshItem(findEntity1);
                        }
                        if (findEntity2 != null) {
                            findEntity2.setShowStatus("0");
                            findEntity2.setDefaultVal("");
                            follow_add_follow_kvel.refreshItem(findEntity2);
                        }
                        if (findEntity3 != null) {
                            findEntity3.setShowStatus("0");
                            findEntity3.setDefaultVal("");
                            follow_add_follow_kvel.refreshItem(findEntity3);
                        }
                        if (findEntity4 != null) {
                            findEntity4.setShowStatus("0");
                            findEntity4.setDefaultVal("");
                            follow_add_follow_kvel.refreshItem(findEntity4);
                        }
                    }
                } else if (keyValueEntity.getEvent().getParamKey().equals("orderArrivalStatus")) {  //到店状态
                    //到店状态选择有效到店，则显示到店时间、到店地点、到店目的
                    if ("1".equals(keyValueEntity.getDefaultVal())) {
                        if (findEntity2 != null) {
                            findEntity2.setShowStatus("1");
                            follow_add_follow_kvel.refreshItem(findEntity2);
                        }
                        if (findEntity3 != null) {
                            findEntity3.setShowStatus("1");
                            follow_add_follow_kvel.refreshItem(findEntity3);
                        }
                        if (findEntity4 != null) {
                            findEntity4.setShowStatus("1");
                            follow_add_follow_kvel.refreshItem(findEntity4);
                        }
                    } else {
                        if (findEntity2 != null) {
                            findEntity2.setShowStatus("0");
                            follow_add_follow_kvel.refreshItem(findEntity2);
                        }
                        if (findEntity3 != null) {
                            findEntity3.setShowStatus("0");
                            follow_add_follow_kvel.refreshItem(findEntity3);
                        }
                        if (findEntity4 != null) {
                            findEntity4.setShowStatus("0");
                            follow_add_follow_kvel.refreshItem(findEntity4);
                        }
                    }
                }
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
                .putParamValue("type", 3)
                .putParamValue("customerId", customerId)
                .putParamValue("relationId", orderId)
                .putParamValue("reserveId", reserveId)
                .putParamValue("attachment", buffer.toString());
        HttpRequest.create(UrlConstant.CREATE_FOLLOW_URL).connectTimeout(10000).readTimeout(10000).putParam(jsonParam).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String result) {
                ToastUtils.toast("提交成功");
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

    public static class AttachmentAddAdapter extends RecyclerView.Adapter<SalesFollowAddActivity.AttachmentAddAdapter.Holder> {
        public static final int ITEM_NORMAL_TYPE = 0;
        public static final int ITEM_ADD_TYPE = 1;

        private Context context;
        private List<String> attachments;
        private LayoutInflater inflater;

        private SalesFollowAddActivity.AttachmentAddAdapter.OnItemClickListener listener;

        public AttachmentAddAdapter(Context context) {
            this.context = context;
            this.attachments = new ArrayList<>();
            this.inflater = LayoutInflater.from(context);
        }

        public void add(String attachment) {
            this.attachments.add(attachment);
            notifyDataSetChanged();
        }

        public void add(List<String> attachments) {
            if (attachments != null) {
                this.attachments.addAll(attachments);
                notifyDataSetChanged();
            }
        }

        public void remove(int position) {
            this.attachments.remove(position);
            notifyDataSetChanged();
        }

        public int getDataSize() {
            return attachments.size();
        }

        public List<String> getData() {
            return attachments;
        }

        public void setOnItemClickListener(SalesFollowAddActivity.AttachmentAddAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public SalesFollowAddActivity.AttachmentAddAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_ADD_TYPE) {
                return new SalesFollowAddActivity.AttachmentAddAdapter.Holder(inflater.inflate(R.layout.activity_follow_detail_attachment_add_item, parent, false), viewType);
            }
            return new SalesFollowAddActivity.AttachmentAddAdapter.Holder(inflater.inflate(R.layout.activity_follow_detail_attachment_edit_item, parent, false), viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1 && attachments.size() < MAX_ATTACHMENT_NUM) {
                return ITEM_ADD_TYPE;
            }
            return ITEM_NORMAL_TYPE;
        }

        @Override
        public void onBindViewHolder(@NonNull SalesFollowAddActivity.AttachmentAddAdapter.Holder holder, final int position) {
            final int viewType = getItemViewType(position);
            if (viewType == ITEM_NORMAL_TYPE) {
                String attachment = attachments.get(position);
                GlideApp.with(context).load(attachment).transform(new RoundedCornersTransformation(CommonUtils.dp2px(context, 12), 1)).placeholder(R.drawable.image_load_round_default).into(holder.follow_detail_attachment_item_iv);
                holder.follow_detail_attachment_item_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        listener.onItemClick(attachments, position);
                    }
                });
                holder.follow_detail_attachment_item_delete_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        listener.onItemDelete(position);
                    }
                });
            } else if (viewType == ITEM_ADD_TYPE) {
                holder.follow_detail_attachment_item_add_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener == null) {
                            return;
                        }
                        listener.onItemAdd();
                    }
                });
            }
        }

        public class Holder extends RecyclerView.ViewHolder {
            private ImageView follow_detail_attachment_item_add_iv;

            private ImageView follow_detail_attachment_item_iv;
            private ImageView follow_detail_attachment_item_delete_iv;

            public Holder(@NonNull View itemView, int type) {
                super(itemView);
                if (type == ITEM_ADD_TYPE) {
                    follow_detail_attachment_item_add_iv = itemView.findViewById(R.id.follow_detail_attachment_item_add_iv);
                } else {
                    follow_detail_attachment_item_iv = itemView.findViewById(R.id.follow_detail_attachment_item_iv);
                    follow_detail_attachment_item_delete_iv = itemView.findViewById(R.id.follow_detail_attachment_item_delete_iv);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (attachments.size() < MAX_ATTACHMENT_NUM) {
                return attachments.size() + 1;
            }
            return attachments.size();
        }

        public interface OnItemClickListener {
            void onItemClick(List<String> pics, int postion);

            void onItemDelete(int postion);

            void onItemAdd();
        }
    }
}
