package com.baihe.lihepro.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.BottomSelectDialog;
import com.baihe.lihepro.dialog.PersonSearchDialog2;
import com.baihe.lihepro.entity.CategoryEntity;
import com.baihe.lihepro.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class AddTeamActivity extends BaseActivity {
    private String customerId;
    private String teamUsers;
    private String teamUsersText;
    private String category;
    private String categoryText;
    private boolean isEdit;
    private TextView categoryTv;
    private TextView teamUserTv;
    private TextView categoryTitle;
    private TextView optionTv;
    private ImageView arrowIv;
    private TextView commitTv;
    private int type;
    private int action;

    public static void start(Fragment context,String customerId,int type){
        Intent intent = new Intent(context.getContext(), AddTeamActivity.class);
        intent.putExtra("customerId",customerId);
        intent.putExtra("type",type);
        context.startActivityForResult(intent,1);
    }

    public static void start(Fragment context,int type,String customerId,String category,String categoryText){
        Intent intent = new Intent(context.getContext(), AddTeamActivity.class);
        intent.putExtra("customerId",customerId);
        intent.putExtra("category",category);
        intent.putExtra("categoryText",categoryText);
        intent.putExtra("type",type);
        context.startActivityForResult(intent,1);
    }

    public static void start(Fragment context, int type, String customerId, String category, String categoryText, String teamUsers, String teamUsersText){
        Intent intent = new Intent(context.getContext(), AddTeamActivity.class);
        intent.putExtra("customerId",customerId);
        intent.putExtra("category",category);
        intent.putExtra("categoryText",categoryText);
        intent.putExtra("teamUsers",teamUsers);
        intent.putExtra("teamUsersText",teamUsersText);
        intent.putExtra("type",type);
        intent.putExtra("isEdit",true);
        context.startActivityForResult(intent,1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        customerId = getIntent().getStringExtra("customerId");
        category = getIntent().getStringExtra("category");
        categoryText = getIntent().getStringExtra("categoryText");
        teamUsers = getIntent().getStringExtra("teamUsers");
        teamUsersText = getIntent().getStringExtra("teamUsersText");
        type = getIntent().getIntExtra("type",1);
        if (isEdit){
            setTitleText("编辑项目成员");
        }else {
            setTitleText("添加项目成员");
        }
        setContentView(R.layout.activity_add_team);
        customerId = getIntent().getStringExtra("customerId");
        teamUsers = getIntent().getStringExtra("teamUsers");
        teamUsersText = getIntent().getStringExtra("teamUsersText");
        category = getIntent().getStringExtra("category");
        categoryText = getIntent().getStringExtra("categoryText");
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        categoryTv = findViewById(R.id.categoryTv);
        categoryTitle = findViewById(R.id.categoryTitle);
        teamUserTv = findViewById(R.id.usersValueTv);
        optionTv = findViewById(R.id.optionTv);
        arrowIv = findViewById(R.id.arrowIv);
        commitTv = findViewById(R.id.tv_build);
        if (teamUsersText != null){
            teamUserTv.setText(teamUsersText);
        }
        if (categoryText!=null){
            categoryTv.setText(categoryText);
            categoryTitle.setTextColor(Color.parseColor("#C5C5CE"));
            categoryTv.setTextColor(Color.parseColor("#C5C5CE"));
            arrowIv.setVisibility(View.GONE);
            optionTv.setVisibility(View.GONE);
        }
        teamUserTv.setOnClickListener(view -> {
            List<UserEntity> selectedData = null;
            if (teamUsers!=null && teamUsersText!=null){
                String[] teamUserArr = teamUsers.split(",");
                String[] teamUserTextArr = teamUsersText.split(",");
                selectedData = new ArrayList<>();
                for (int i = 0; i < teamUserArr.length; i++) {
                    UserEntity entity = new UserEntity();
                    entity.setId(teamUserArr[i]);
                    entity.setName(teamUserTextArr[i]);
                    entity.setSelect(true);
                    selectedData.add(entity);
                }
            }
            new PersonSearchDialog2.Builder(this)
                    .setTitle("选择成员")
                    .setMulti(true)
                    .setMulti(selectedData)
                    .setOnConfirmClickListener(new PersonSearchDialog2.OnConfirmClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog, String customerName, String customerId) {
                            teamUsers = customerId;
                            teamUsersText = customerName;
                            teamUserTv.setText(teamUsersText.replaceAll(","," "));
                            teamUserTv.setTextColor(Color.parseColor("#4A4C5C"));
                            dialog.dismiss();
                        }
                    }).build().show();

        });

        categoryTv.setOnClickListener(view -> {
            if (isEdit || type == 3){
                return;
            }
            getCategory();

        });
        commitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit){
                    action = 2;
                }else {
                    action = 1;
                }
                operateTeam(customerId,category,teamUsers,action+"",type+"");
            }
        });
    }

    private void getCategory(){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("customerId", customerId);
        HttpRequest.create(UrlConstant.TEAM_GET_CATEGORY).putParam(jsonParam).get(new CallBack<List<CategoryEntity>>() {
            @Override
            public List<CategoryEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, CategoryEntity.class);
            }

            @Override
            public void success(List<CategoryEntity> entities) {
                if (entities != null) {
                    new BottomSelectDialog.Builder(AddTeamActivity.this)
                            .setCancelable(true)
                            .setTitle("选择品类")
                            .setOnCancelClickListener(new BottomSelectDialog.OnCancelClickListener() {
                                @Override
                                public void onCancel(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .setOnConfirmClickListener(new BottomSelectDialog.OnConfirmClickListener() {
                                @Override
                                public void onConfirm(Dialog dialog, int position) {
                                    category = entities.get(position).getId();
                                    categoryText = entities.get(position).getName();
                                    categoryTv.setText(categoryText);
                                    categoryTv.setTextColor(Color.parseColor("#4A4C5C"));
                                    dialog.dismiss();


                                }
                            })
                            .setSelectDataAdapter(new BottomSelectDialog.SelectDataAdapter() {
                                @Override
                                public int getCount() {
                                    return entities.size();
                                }

                                @Override
                                public String getText(int dataPostion) {
                                    return entities.get(dataPostion).getName();
                                }
                            })
                            .build().show();
                }


            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }

            @Override
            public void before() {
                super.before();

            }

            @Override
            public void after() {
                super.after();

            }
        });
    }


    private void operateTeam(String customerId,String category,String teamUserIds,final String action,String type){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("customerId", customerId)
                .putParamValue("teamUserIds", teamUserIds)
                .putParamValue("action", action)
                .putParamValue("type", type)
                .putParamValue("category",category);
        HttpRequest.create(UrlConstant.OPREATE_TEAM).putParam(jsonParam).get(new CallBack<Integer>() {
            @Override
            public Integer doInBackground(String response) {
                return JsonUtils.parse(response, Integer.class);
            }

            @Override
            public void success(Integer entity) {
                ToastUtils.toast("1".equals(action)?"添加成功":"编辑成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }

            @Override
            public void before() {
                super.before();
                showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }
        });
    }


}
