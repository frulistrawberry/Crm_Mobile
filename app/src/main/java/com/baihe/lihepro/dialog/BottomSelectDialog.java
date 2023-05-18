package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.blankj.utilcode.util.ScreenUtils;


/**
 * Author：xubo
 * Time：2019-09-26
 * Description：底部弹起选择Dialog
 */
public class BottomSelectDialog extends Dialog {
    public BottomSelectDialog(Context context) {
        super(context, R.style.CommonDialog);
    }

    public static class Builder {
        private Context context;
        private SelectDataAdapter selectDataAdapter;
        private OnConfirmClickListener confirmListener;
        private OnCancelClickListener cancelListener;
        private String title;
        private boolean cancelable = true;

        private TextView bottom_select_cancel_tv;
        private TextView bottom_select_confirm_tv;
        private TextView bottom_select_title_tv;
        private RecyclerView bottom_select_list_rv;
        private BottomSelectAdapter bottomSelectAdapter;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setSelectDataAdapter(SelectDataAdapter selectDataAdapter) {
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

        public BottomSelectDialog build() {
            BottomSelectDialog dialog = new BottomSelectDialog(context);
            dialog.setContentView(R.layout.dialog_bottom_select);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(cancelable);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height  = (int) (ScreenUtils.getScreenHeight() * 0.5);
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

        public BottomSelectDialog show() {
            BottomSelectDialog dialog = build();
            dialog.show();
            return dialog;
        }

        private void initView(Dialog dialog) {
            bottom_select_cancel_tv = dialog.findViewById(R.id.bottom_select_cancel_tv);
            bottom_select_confirm_tv = dialog.findViewById(R.id.bottom_select_confirm_tv);
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
            bottomSelectAdapter = new BottomSelectAdapter(context, selectDataAdapter);
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
                    if (bottomSelectAdapter.selectPosition >= 0) {
                        dialog.dismiss();
                        if (confirmListener != null) {
                            confirmListener.onConfirm(dialog, bottomSelectAdapter.selectPosition);
                        }
                    }
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

    public static class BottomSelectAdapter extends RecyclerView.Adapter<BottomSelectAdapter.Holder> {
        private static final int NORMAL_TYPE = 0;
        private static final int MULTI_TYPE = 1;
        private static final int EXTRA_TYPE = 2;

        private Context context;
        private LayoutInflater inflater;
        private SelectDataAdapter selectDataAdapter;
        private int selectPosition;
        private int extraLayoutId;
        private int extraCount;

        public BottomSelectAdapter(Context context, SelectDataAdapter selectDataAdapter) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.selectDataAdapter = selectDataAdapter;
            this.extraLayoutId = selectDataAdapter != null ? selectDataAdapter.getExtraItemLayoutResourceId() : -1;
            this.extraCount = extraLayoutId == -1 ? 0 : selectDataAdapter.getExtraCount();
            this.selectPosition = selectDataAdapter != null ? selectDataAdapter.initSelectPosition() : -1;
            if (selectPosition < 0 || selectPosition >= getItemCount()) {
                this.selectPosition = -1;
            }
        }

        @Override
        public int getItemViewType(int position) {
            //存在额外数据布局
            if (extraCount > 0) {
                //额外前置和后置的索引条件
                if ((selectDataAdapter.isFirstIndexExtra() && position < extraCount) || (!selectDataAdapter.isFirstIndexExtra() && position >= getItemCount() - extraCount)) {
                    return EXTRA_TYPE;
                }
            }
            if (selectDataAdapter instanceof MultiSelectDataAdapter) {
                return MULTI_TYPE;
            } else {
                return NORMAL_TYPE;
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == EXTRA_TYPE) {
                view = inflater.inflate(extraLayoutId, parent, false);
            } else if (viewType == MULTI_TYPE) {
                MultiSelectDataAdapter multiSelectDataAdapter = (MultiSelectDataAdapter) selectDataAdapter;
                view = inflater.inflate(multiSelectDataAdapter.getItemLayoutResourceId(), parent, false);
            } else {
                view = inflater.inflate(R.layout.dialog_bottom_select_item, parent, false);
            }
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            int viewType = getItemViewType(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPosition = position;
                    notifyDataSetChanged();
                }
            });
            if (viewType == EXTRA_TYPE) {
                //索引适配额外数据，使索引使正常数据集合索引
                int dataPostion = selectDataAdapter.isFirstIndexExtra() ? position : position - (getItemCount() - extraCount);
                int dataSelectPosition = selectDataAdapter.isFirstIndexExtra() ? selectPosition : selectPosition - (getItemCount() - extraCount);
                selectDataAdapter.bindExtraViewHolder(holder, dataPostion, dataSelectPosition);
            } else if (viewType == MULTI_TYPE) {
                MultiSelectDataAdapter multiSelectDataAdapter = (MultiSelectDataAdapter) selectDataAdapter;
                //索引适配额外数据，使索引使正常数据集合索引
                int dataPostion = !selectDataAdapter.isFirstIndexExtra() ? position : position - extraCount;
                int dataSelectPosition = !selectDataAdapter.isFirstIndexExtra() ? selectPosition : selectPosition - extraCount;
                multiSelectDataAdapter.bindViewHolder(holder, dataPostion, dataSelectPosition);
            } else {
                //索引适配额外数据，使索引使正常数据集合索引
                int dataPostion = !selectDataAdapter.isFirstIndexExtra() ? position : position - extraCount;
                String text = selectDataAdapter.getText(dataPostion);
                holder.bottom_select_item_name_tv.setText(text);
                holder.bottom_select_item_name_tv.getPaint().setFakeBoldText(true);
                if (position == selectPosition) {
                    holder.bottom_select_item_checked_cb.setChecked(true);
                } else {
                    holder.bottom_select_item_checked_cb.setChecked(false);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (selectDataAdapter != null) {
                return selectDataAdapter.getCount() + extraCount;
            }
            return 0;
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
        void onConfirm(Dialog dialog, int position);
    }

    public interface OnCancelClickListener {
        void onCancel(Dialog dialog);
    }

    /**
     * 一般布局数据适配器
     */
    public abstract static class SelectDataAdapter {
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
         * 初始全局选中索引（包括额外数据的占位）
         * 推荐请用{@link #initSelectDataPosition()} 和{@link #initSelectExtraPosition()}替代
         *
         * @return
         */
        @Deprecated
        public int initSelectPosition() {
            int extraCount = getExtraItemLayoutResourceId() == -1 ? 0 : getExtraCount();
            int selectExtraPosition = initSelectExtraPosition();
            if (selectExtraPosition >= 0 && selectExtraPosition < extraCount) {
                int selectPosition = isFirstIndexExtra() ? selectExtraPosition : selectExtraPosition + getCount();
                return selectPosition;
            }
            int selectDataPosition = initSelectDataPosition();
            if (selectDataPosition >= 0 && selectDataPosition < getCount()) {
                int selectPosition = isFirstIndexExtra() ? selectExtraPosition + extraCount : selectDataPosition;
                return selectPosition;
            }
            return -1;
        }

        /**
         * 初始普通数据选中索引
         *
         * @return
         */
        public int initSelectDataPosition() {
            return -1;
        }

        /**
         * 初始额外数据选中索引
         *
         * @return
         */
        public int initSelectExtraPosition() {
            return -1;
        }


        /**
         * 额外数据数量
         *
         * @return
         */
        public int getExtraCount() {
            return 0;
        }

        /**
         * 额外数据的item的layout资源id
         *
         * @return
         */
        public @LayoutRes
        int getExtraItemLayoutResourceId() {
            return -1;
        }

        /**
         * 绑定数据到额外数据View上（选择点击无需处理）
         *
         * @param holder
         * @param dataPostion        额外数据索引
         * @param dataSelectPosition 额外数据选中索引
         */
        public void bindExtraViewHolder(RecyclerView.ViewHolder holder, int dataPostion, int dataSelectPosition) {

        }

        /**
         * 额外数据是否在列表最靠前，true最靠前，false最靠后
         *
         * @return
         */
        public boolean isFirstIndexExtra() {
            return false;
        }

    }

    /**
     * 复杂布局数据适配器
     */
    public abstract static class MultiSelectDataAdapter extends SelectDataAdapter {

        /**
         * item的layout资源id
         *
         * @return
         */
        public abstract @LayoutRes
        int getItemLayoutResourceId();

        /**
         * 绑定数据到View上（选择点击无需处理）
         *
         * @param holder
         * @param dataPostion        数据选中
         * @param dataSelectPosition 数据选中索引
         */
        public abstract void bindViewHolder(RecyclerView.ViewHolder holder, int dataPostion, int dataSelectPosition);

        @Override
        public final String getText(int position) {
            return "";
        }
    }

}
