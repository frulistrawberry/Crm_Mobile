package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.dialog.DialogBuilder;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.github.xubo.statusbarutils.StatusBarUtils;


/**
 * Author：xubo
 * Time：2019-09-26
 * Description：
 */
public class SortSelectDialog extends Dialog {
    public SortSelectDialog(Context context) {
        super(context, R.style.TransparentDialog);
    }

    public static class Builder {
        private Context context;
        private SelectDataAdapter selectDataAdapter;
        private OnSelectListener selectListener;
        private boolean cancelable = true;
        private View attachView;

        private RecyclerView sort_select_list_rv;
        private SortSelectAdapter bottomSelectAdapter;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setSelectDataAdapter(SelectDataAdapter selectDataAdapter) {
            this.selectDataAdapter = selectDataAdapter;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnSelectListener(OnSelectListener selectListener) {
            this.selectListener = selectListener;
            return this;
        }

        public Builder setAttachView(View attachView) {
            this.attachView = attachView;
            return this;
        }

        public SortSelectDialog build() {
            SortSelectDialog dialog = new SortSelectDialog(context);
            dialog.setContentView(R.layout.dialog_sort_select);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(cancelable);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.TOP);
            window.setWindowAnimations(R.style.DialogSortAnimation);
            if (attachView != null) {
                int[] location = new int[2];
                attachView.getLocationOnScreen(location);
                int locationX = 0;
                int locationY = location[1] + attachView.getMeasuredHeight() - StatusBarUtils.getStatusBarHeight(context);
                params.x = locationX;
                params.y = locationY;

            }
            initView(dialog);
            initData();
            listener(dialog);
            if (selectDataAdapter != null) {
                selectDataAdapter.bindDialog(dialog);
            }
            return dialog;
        }

        public SortSelectDialog show() {
            SortSelectDialog dialog = build();
            dialog.show();
            return dialog;
        }

        private void initView(Dialog dialog) {
            sort_select_list_rv = dialog.findViewById(R.id.sort_select_list_rv);
        }

        private void initData() {
            bottomSelectAdapter = new SortSelectAdapter(context, selectDataAdapter);
            sort_select_list_rv.setAdapter(bottomSelectAdapter);
            sort_select_list_rv.setLayoutManager(new MyLinearLayoutManager(context));
            final int driverMagin = CommonUtils.dp2pxForInt(context, 25.0F);
            final int driverHeight = CommonUtils.dp2pxForInt(context, 0.5F);
            final Paint driverPaint = new Paint();
            driverPaint.setColor(Color.parseColor("#ECECF0"));
            sort_select_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {

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
            bottomSelectAdapter.setOnItemClickListener(new SortSelectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (selectListener != null) {
                        selectListener.onSelect(dialog, position);
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

    public static class SortSelectAdapter extends RecyclerView.Adapter<SortSelectAdapter.Holder> {
        private Context context;
        private LayoutInflater inflater;
        private SelectDataAdapter selectDataAdapter;
        private int selectPosition;
        private OnItemClickListener listener;

        public SortSelectAdapter(Context context, SelectDataAdapter selectDataAdapter) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.selectDataAdapter = selectDataAdapter;
            this.selectPosition = selectDataAdapter != null ? selectDataAdapter.initSelectDataPosition() : -1;
            if (selectPosition < 0 || selectPosition >= getItemCount()) {
                this.selectPosition = -1;
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Holder holder = new Holder(inflater.inflate(R.layout.dialog_sort_select_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectPosition != position) {
                        selectPosition = position;
                        if (listener != null) {
                            listener.onItemClick(position);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            String text = selectDataAdapter.getText(position);
            holder.sort_select_item_name_tv.setText(text);
            holder.sort_select_item_name_tv.getPaint().setFakeBoldText(true);
            if (position == selectPosition) {
                holder.sort_select_item_checked_iv.setVisibility(View.VISIBLE);
            } else {
                holder.sort_select_item_checked_iv.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (selectDataAdapter != null) {
                return selectDataAdapter.getCount();
            }
            return 0;
        }

        public class Holder extends RecyclerView.ViewHolder {
            public TextView sort_select_item_name_tv;
            public ImageView sort_select_item_checked_iv;

            public Holder(View itemView) {
                super(itemView);
                sort_select_item_name_tv = itemView.findViewById(R.id.sort_select_item_name_tv);
                sort_select_item_checked_iv = itemView.findViewById(R.id.sort_select_item_checked_iv);
            }
        }


        public interface OnItemClickListener {
            void onItemClick(int position);
        }

    }

    public interface OnSelectListener {
        void onSelect(Dialog dialog, int position);
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
         * @param position 数据索引
         * @return
         */
        public abstract String getText(int position);

        /**
         * 初始普通数据选中索引
         *
         * @return
         */
        public int initSelectDataPosition() {
            return -1;
        }

    }
}
