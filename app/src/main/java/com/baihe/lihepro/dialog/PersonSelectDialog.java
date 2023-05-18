package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.blankj.utilcode.util.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author：xubo
 * Time：2019-09-26
 * Description：底部弹起选择Dialog
 */
public class PersonSelectDialog extends Dialog {
    public PersonSelectDialog(Context context) {
        super(context, R.style.CommonDialog);
    }

    public static class Builder {
        private Context context;
        private PersonDataAdapter selectDataAdapter;
        private OnConfirmClickListener confirmListener;
        private OnCancelClickListener cancelListener;
        private String title;
        private boolean cancelable = true;

        private TextView bottom_select_cancel_tv;
        private TextView bottom_select_confirm_tv;
        private EditText bottom_select_search_et;
        private ImageView bottom_select_search_delete_iv;
        private TextView bottom_select_title_tv;
        private RecyclerView bottom_select_list_rv;
        private PersonSelectAdapter bottomSelectAdapter;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setPersonDataAdapter(PersonDataAdapter selectDataAdapter) {
            this.selectDataAdapter = selectDataAdapter;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnConfirmClickListener(OnConfirmClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public Builder setOnCancelClickListener(OnCancelClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public PersonSelectDialog build() {
            PersonSelectDialog dialog = new PersonSelectDialog(context);
            dialog.setContentView(R.layout.dialog_person_select);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(cancelable);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height  = (int) (ScreenUtils.getScreenHeight() * 0.7);
            window.setWindowAnimations(R.style.dialog_style);
            window.setGravity(Gravity.BOTTOM);
            initView(dialog);
            initData();
            listener(dialog);
            if (selectDataAdapter != null) {
                selectDataAdapter.bindDialog(dialog);
            }
            return dialog;
        }

        public PersonSelectDialog show() {
            PersonSelectDialog dialog = build();
            dialog.show();
            return dialog;
        }

        private void initView(Dialog dialog) {
            bottom_select_cancel_tv = dialog.findViewById(R.id.bottom_select_cancel_tv);
            bottom_select_confirm_tv = dialog.findViewById(R.id.bottom_select_confirm_tv);
            bottom_select_search_et = dialog.findViewById(R.id.bottom_select_search_et);
            bottom_select_search_delete_iv = dialog.findViewById(R.id.bottom_select_search_delete_iv);
            bottom_select_title_tv = dialog.findViewById(R.id.bottom_select_title_tv);
            bottom_select_list_rv = dialog.findViewById(R.id.bottom_select_list_rv);
            bottom_select_confirm_tv.getPaint().setFakeBoldText(true);
        }

        private void initData() {
            if (TextUtils.isEmpty(title)) {
                bottom_select_title_tv.setVisibility(View.GONE);
            } else {
                bottom_select_title_tv.setVisibility(View.VISIBLE);
                bottom_select_title_tv.getPaint().setFakeBoldText(true);
                bottom_select_title_tv.setText(title);
            }
            bottomSelectAdapter = new PersonSelectAdapter(context, selectDataAdapter);
            bottom_select_list_rv.setAdapter(bottomSelectAdapter);
            bottom_select_list_rv.setLayoutManager(new MyLinearLayoutManager(context));
            final int driverMagin = CommonUtils.dp2pxForInt(context, 25.0F);
            final int driverHeight = CommonUtils.dp2pxForInt(context, 0.5F);
            final Paint driverPaint = new Paint();
            driverPaint.setColor(Color.parseColor("#ECECF0"));
            bottom_select_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {

                @Override
                public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                    int childSize = parent.getChildCount();
                    for (int i = 0; i < childSize - 1; i++) {
                        View child = parent.getChildAt(i);
                        float top = child.getBottom();
                        float bottom = top + driverHeight;
                        float right = child.getRight();
                        c.drawRect(driverMagin, top, right - driverMagin, bottom, driverPaint);
                    }
                }

            });
        }

        private void listener(final Dialog dialog) {
            bottom_select_cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (cancelListener != null) {
                        cancelListener.onCancel(dialog);
                    }
                }
            });

            bottom_select_confirm_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(bottomSelectAdapter.getSelectId())) {
                        dialog.dismiss();
                        if (confirmListener != null) {
                            confirmListener.onConfirm(dialog, bottomSelectAdapter.getSelectText(), bottomSelectAdapter.getSelectId());
                        }
                    }
                }
            });
            bottom_select_search_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String keyword = s.toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        bottom_select_search_delete_iv.setVisibility(View.GONE);
                    } else {
                        bottom_select_search_delete_iv.setVisibility(View.VISIBLE);
                    }
                    bottomSelectAdapter.updateKeyword(keyword);
                }
            });
            bottom_select_search_delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottom_select_search_et.setText("");
                }
            });
        }

    }

    public static class MyLinearLayoutManager extends LinearLayoutManager {
        public Context context;

        public MyLinearLayoutManager(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
            super.setMeasuredDimension(childrenBounds, wSpec, View.MeasureSpec.makeMeasureSpec(CommonUtils.dp2pxForInt(context, 480.0f), View.MeasureSpec.AT_MOST));
        }
    }

    public static class PersonSelectAdapter extends RecyclerView.Adapter<PersonSelectAdapter.Holder> {
        private Context context;
        private LayoutInflater inflater;
        private String selectId;
        private String keyWord;

        private Map<String, String> data = new HashMap<>();
        private Map<String, String> showData = new HashMap<>();
        private List<String> ids = new ArrayList<>();

        public PersonSelectAdapter(Context context, PersonDataAdapter selectDataAdapter) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.selectId = selectDataAdapter.initSelectId();

            int length = selectDataAdapter.getCount();
            for (int i = 0; i < length; i++) {
                data.put(selectDataAdapter.getId(i), selectDataAdapter.getText(i));
                showData.put(selectDataAdapter.getId(i), selectDataAdapter.getText(i));
                ids.add(selectDataAdapter.getId(i));
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.dialog_bottom_select_item, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PersonSelectAdapter.this.selectId = ids.get(position);
                    notifyDataSetChanged();
                }
            });
            String text = showData.get(ids.get(position));
            holder.bottom_select_item_name_tv.setText(text);
            holder.bottom_select_item_name_tv.getPaint().setFakeBoldText(true);
            if (!TextUtils.isEmpty(selectId) && position == ids.indexOf(selectId)) {
                holder.bottom_select_item_checked_cb.setChecked(true);
            } else {
                holder.bottom_select_item_checked_cb.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            return ids.size();
        }

        public void updateKeyword(String keyWord) {
            this.keyWord = keyWord;
            updateShowData();
            notifyDataSetChanged();
        }

        public String getSelectId() {
            return selectId;
        }

        public String getSelectText() {
            if (!TextUtils.isEmpty(selectId) && data.containsKey(selectId)) {
                return data.get(selectId);
            }
            return "";
        }

        private void updateShowData() {
            showData.clear();
            ids.clear();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (TextUtils.isEmpty(keyWord)) {
                    showData.put(entry.getKey(), entry.getValue());
                    ids.add(entry.getKey());
                }else if(entry.getValue().contains(keyWord)){
                    showData.put(entry.getKey(), entry.getValue());
                    ids.add(entry.getKey());
                }
            }
        }

        public class Holder extends RecyclerView.ViewHolder {
            public TextView bottom_select_item_name_tv;
            public CheckBox bottom_select_item_checked_cb;

            public Holder(View itemView) {
                super(itemView);
                bottom_select_item_name_tv = itemView.findViewById(R.id.bottom_select_item_name_tv);
                bottom_select_item_checked_cb = itemView.findViewById(R.id.bottom_select_item_checked_cb);
            }
        }
    }

    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, String selectText, String selectId);
    }

    public interface OnCancelClickListener {
        void onCancel(Dialog dialog);
    }

    /**
     * 一般布局数据适配器
     */
    public abstract static class PersonDataAdapter {
        protected Dialog bindDialog;

        /**
         * 绑定dialog
         *
         * @param dialog
         */
        private void bindDialog(Dialog dialog) {
            this.bindDialog = dialog;
        }

        /**
         * 数据数量
         *
         * @return
         */
        public abstract int getCount();

        /**
         * 每条数据对应的文本描述
         *
         * @param dataPostion 数据索引
         * @return
         */
        public abstract String getText(int dataPostion);

        /**
         * 每条数据对应的id
         *
         * @param dataPostion 数据索引
         * @return
         */
        public abstract String getId(int dataPostion);

        /**
         * 初始选中id
         *
         * @return
         */
        public String initSelectId() {
            return "";
        }

    }

}
