package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.FollowDetailEntity;
import com.baihe.lihepro.glide.transformation.RoundedCornersTransformation;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-04
 * Description：
 */
public class FollowDetailActivity extends BaseActivity {
    private static final String INTENT_FOLLOW_ID = "INTENT_FOLLOW_ID";
    private static final String INTENT_FOLLOW_NAME = "INTENT_FOLLOW_NAME";

    public static void start(Context context, String followId, String followName) {
        Intent intent = new Intent(context, FollowDetailActivity.class);
        intent.putExtra(INTENT_FOLLOW_ID, followId);
        intent.putExtra(INTENT_FOLLOW_NAME, followName);
        context.startActivity(intent);
    }

    private String followId;
    private String followName;

    private TextView follow_list_name_tv;
    private TextView follow_list_wedding_time_tv;
    private KeyValueLayout follow_list_data_kvl;
    private TextView follow_list_attachment_tv;
    private RecyclerView follow_detail_attachment_rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        followId = getIntent().getStringExtra(INTENT_FOLLOW_ID);
        followName = getIntent().getStringExtra(INTENT_FOLLOW_NAME);
        setTitleText(followName);
        setContentView(R.layout.activity_follow_detail);
        init();
        initData();
        listener();
        loadData();
    }

    private void init() {
        follow_list_name_tv = findViewById(R.id.follow_list_name_tv);
        follow_list_wedding_time_tv = findViewById(R.id.follow_list_wedding_time_tv);
        follow_list_data_kvl = findViewById(R.id.follow_list_data_kvl);
        follow_list_attachment_tv = findViewById(R.id.follow_list_attachment_tv);
        follow_detail_attachment_rv = findViewById(R.id.follow_detail_attachment_rv);
    }

    private void initData() {
    }

    private void listener() {
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("followId", followId);
        HttpRequest.create(UrlConstant.FOLLOW_DETAIL_URL).putParam(jsonParam).get(new CallBack<FollowDetailEntity>() {
            @Override
            public FollowDetailEntity doInBackground(String response) {
                return JsonUtils.parse(response, FollowDetailEntity.class);
            }

            @Override
            public void success(FollowDetailEntity entity) {
                statusLayout.normalStatus();
                follow_list_name_tv.setText(entity.getCustomer_name());
                follow_list_wedding_time_tv.setText("婚期：" + (TextUtils.isEmpty(entity.getWedding_date()) ? "未填写" : entity.getWedding_date()));
                follow_list_data_kvl.setData(entity.getShow_array());
                if (entity.getAttachment() == null || entity.getAttachment().size() == 0) {
                    follow_list_attachment_tv.setVisibility(View.GONE);
                    follow_detail_attachment_rv.setVisibility(View.GONE);
                } else {
                    final AttachmentAdapter attachmentAdapter = new AttachmentAdapter(context, entity.getAttachment());
                    follow_detail_attachment_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    final int offset = CommonUtils.dp2pxForInt(context, 15);
                    follow_detail_attachment_rv.setAdapter(attachmentAdapter);
                    follow_detail_attachment_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                            super.getItemOffsets(outRect, view, parent, state);
                            int position = parent.getChildAdapterPosition(view);
                            if (position == attachmentAdapter.getItemCount() - 1) {
                                outRect.set(offset, 0, offset, 0);
                            } else {
                                outRect.set(offset, 0, 0, 0);
                            }
                        }
                    });
                    attachmentAdapter.setOnItemClickListener(new AttachmentAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(List<String> pics, int postion) {
                            PhotoBrowserActivity.start(context, (ArrayList<String>) pics, postion);
                        }
                    });
                }
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

    public static class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.Holder> {
        private Context context;
        private List<String> attachments;
        private LayoutInflater inflater;

        private OnItemClickListener listener;

        public AttachmentAdapter(Context context, List<String> attachments) {
            this.context = context;
            this.attachments = attachments;
            this.inflater = LayoutInflater.from(context);
        }

        public void update(List<String> attachments) {
            this.attachments.clear();
            if (attachments != null) {
                this.attachments.addAll(attachments);
            }
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(inflater.inflate(R.layout.activity_follow_detail_attachment_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            String attachment = attachments.get(position);
            GlideApp.with(context).load(attachment).transform(new RoundedCornersTransformation(CommonUtils.dp2px(context, 12), 1)).placeholder(R.drawable.image_load_round_default).into(holder.follow_detail_attachment_item_iv);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(attachments, position);
                }
            });
        }

        public class Holder extends RecyclerView.ViewHolder {
            private ImageView follow_detail_attachment_item_iv;

            public Holder(@NonNull View itemView) {
                super(itemView);
                follow_detail_attachment_item_iv = itemView.findViewById(R.id.follow_detail_attachment_item_iv);
            }
        }

        @Override
        public int getItemCount() {
            return attachments.size();
        }

        public interface OnItemClickListener {
            void OnItemClick(List<String> pics, int postion);
        }
    }
}
