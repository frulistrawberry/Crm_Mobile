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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.dialog.BaseDialog;
import com.baihe.common.dialog.DialogBuilder;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.KeyValueEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-12
 * Description：
 */
public class AdjustDialog extends BaseDialog {
    public AdjustDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private boolean cancelable = true;
        private AdjustDialog.OnConfirmClickListener confirmListener;
        private AdjustDialog.OnCancelClickListener cancelListener;
        private String title;
        private Context context;
        private List<KeyValueEntity> list;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setData(List<KeyValueEntity> list) {
            this.list = list;
            return this;
        }

        public Builder setOnConfirmClickListener(AdjustDialog.OnConfirmClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public Builder setOnCancelClickListener(AdjustDialog.OnCancelClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public AdjustDialog build() {
            DialogBuilder<AdjustDialog> dialogBuilder = new DialogBuilder<AdjustDialog>(context, R.layout.dialog_adjust, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT) {
                @Override
                public void createView(final Dialog dialog, View view) {
                    TextView adjust_cancel_tv = view.findViewById(R.id.adjust_cancel_tv);
                    TextView adjust_title_tv = view.findViewById(R.id.adjust_title_tv);
                    TextView adjust_confirm_tv = view.findViewById(R.id.adjust_confirm_tv);
                    RecyclerView adjust_list_rv = view.findViewById(R.id.adjust_list_rv);
                    if (TextUtils.isEmpty(title)) {
                        adjust_title_tv.setVisibility(View.GONE);
                    } else {
                        adjust_title_tv.setVisibility(View.VISIBLE);
                        adjust_title_tv.getPaint().setFakeBoldText(true);
                        adjust_title_tv.setText(title);
                    }
                    adjust_cancel_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (cancelListener != null) {
                                cancelListener.onCancel(dialog);
                            }
                        }
                    });
                    adjust_confirm_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            if (confirmListener != null) {
                                confirmListener.onConfirm(dialog);
                            }
                        }
                    });
                    AdjustAdapter adjustAdapter = new AdjustAdapter(context);
                    adjust_list_rv.setAdapter(adjustAdapter);
                    adjust_list_rv.setLayoutManager(new BottomSelectDialog.MyLinearLayoutManager(context));
                    final int driverMagin = CommonUtils.dp2pxForInt(context, 15.0F);
                    final int driverHeight = CommonUtils.dp2pxForInt(context, 0.5F);
                    final Paint driverPaint = new Paint();
                    driverPaint.setColor(Color.parseColor("#ECECF0"));
                    adjust_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {

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
                    adjustAdapter.setData(list);
                }

                @Override
                public AdjustDialog createDialog(Context context, int themeResId) {
                    return new AdjustDialog(context, themeResId);
                }
            }.setAnimationRes(R.style.dialog_style).setGravity(Gravity.BOTTOM).setCancelable(cancelable);
            return dialogBuilder.create();
        }
    }

    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog);
    }

    public interface OnCancelClickListener {
        void onCancel(Dialog dialog);
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

    public static class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.Holder> {
        private static final int MAX_NUM = 999;
        private static final int MIN_NUM = 0;

        private List<KeyValueEntity> list;
        private LayoutInflater inflater;

        public AdjustAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.list = new ArrayList<>();
        }

        public void setData(List<KeyValueEntity> list) {
            this.list.clear();
            if (list != null) {
                this.list.addAll(list);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(inflater.inflate(R.layout.dialog_adjust_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            final KeyValueEntity keyValueEntity = list.get(position);
            holder.adjust_item_name_tv.setText(keyValueEntity.getKey());
            holder.adjust_item_num_tv.setText(getNum(keyValueEntity) + "");
            holder.adjust_item_less_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = getNum(keyValueEntity);
                    if (num > MIN_NUM) {
                        num--;
                    } else {
                        num = MIN_NUM;
                    }
                    setNum(keyValueEntity, num);
                    notifyDataSetChanged();
                }
            });
            holder.adjust_item_plus_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = getNum(keyValueEntity);
                    if (num < MAX_NUM) {
                        num++;
                    } else {
                        num = MAX_NUM;
                    }
                    setNum(keyValueEntity, num);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private int getNum(KeyValueEntity keyValueEntity) {
            int num = 0;
            try {
                num = TextUtils.isEmpty(keyValueEntity.getTempValue()) ? 0 : Integer.parseInt(keyValueEntity.getTempValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return num;
        }

        private void setNum(KeyValueEntity keyValueEntity, int num) {
            keyValueEntity.setTempValue(num + "");
        }

        public class Holder extends RecyclerView.ViewHolder {
            public TextView adjust_item_name_tv;
            public ImageView adjust_item_less_iv;
            public TextView adjust_item_num_tv;
            public ImageView adjust_item_plus_iv;

            public Holder(@NonNull View itemView) {
                super(itemView);
                adjust_item_name_tv = itemView.findViewById(R.id.adjust_item_name_tv);
                adjust_item_less_iv = itemView.findViewById(R.id.adjust_item_less_iv);
                adjust_item_num_tv = itemView.findViewById(R.id.adjust_item_num_tv);
                adjust_item_plus_iv = itemView.findViewById(R.id.adjust_item_plus_iv);
            }
        }
    }
}
