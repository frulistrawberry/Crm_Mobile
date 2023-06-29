package com.baihe.lib_common.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lib_common.R;
import com.baihe.lib_framework.utils.DpToPx;
import com.baihe.lib_framework.utils.ResUtils;

/**
 * Create By MR.D
 * 我珍惜一眼而过的青春，才如此疯狂的面对未来。
 * 2019/5/30
 * USE: Recyclerview时间轴(2)
 **/
public class AxisItemDecoration extends RecyclerView.ItemDecoration {


    private Paint paint;
    private int itemView_leftinterval;
    private int icon_width;
    private Bitmap bitmap;
    private Context context;
    public AxisItemDecoration(Context context) {
        this.context = context;
        paint = new Paint();
        //画笔颜色：#FFD7D7D7
        paint.setColor(ResUtils.getColorFromResource(R.color.color_spline));
        //设置画笔宽度，可用于修改轴的宽度
        paint.setStrokeWidth(DpToPx.dpToPx(0.5f));
        //获取图片
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_node);
        //矩形左侧偏移量px
        itemView_leftinterval = DpToPx.dpToPx(16);
        //图标所在正方形宽高(一半)
        icon_width = DpToPx.dpToPx(8);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //重中之重，时间轴实现的核心，可以理解为，在某一个方向将子布局推移
        //参数含义：相对于父布局左偏移，上偏移，右偏移，下偏移 （个人理解：变相margin值）
        outRect.set(itemView_leftinterval,0,0,0);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        c.drawColor(context.getResources().getColor(R.color.white));
        int childCount = parent.getChildCount();
        //遍历子布局，为每一个子布局进行绘制
        for(int i = 0;i<childCount;i++){
            View child = parent.getChildAt(i);
            //下面两个参数是计算我们的矩形相对于父布局所在位置
            int centerx = child.getLeft() - itemView_leftinterval / 2;
            int centery = child.getTop() + icon_width;
            //计算图标展示的正方形大小（相对于父布局位置）
            Rect rect = new Rect(centerx-icon_width, centery-icon_width
                    , centerx+icon_width, centery+icon_width);

            /**
             * 上半轴绘制
             */
            //上端点坐标（X，Y）
            float upLine_up_x = centerx;
            float upLine_up_y = child.getTop();
            //下端点坐标（X，Y）
            float upLine_bottom_x = centerx;
            float upLine_bottom_y = centery - icon_width;

            /**
             * 如果是最后一个item绘制上半轴需要加长
             */
            //上端点坐标（X，Y）
            float upLine_last_up_x = centerx;
            float upLine_last_up_y = child.getTop();
            //下端点坐标（X，Y）
            float upLine_last_bottom_x = centerx;
            float upLine_last_bottom_y = centery;

            /**
             * 下半轴绘制
             */
            //上端点（X,Y）
            float bottomLine_up_x = centerx;
            float bottom_up_y = centery + icon_width;
            //下端点（X,Y）
            float bottomLine_bottom_x = centerx;
            float bottomLine_bottom_y = child.getBottom();

            //顶部不绘制上端线，底部不绘制下端线且改变图标为实心圆
            int index = parent.getChildAdapterPosition(child);
            if(index == 0){
                c.drawBitmap(bitmap,null,rect,paint);
                c.drawLine(bottomLine_up_x,bottom_up_y,bottomLine_bottom_x,bottomLine_bottom_y,paint);
            }else if(index + 1 == childCount){
                c.drawBitmap(bitmap,null,rect,paint);
                c.drawLine(upLine_last_up_x,upLine_last_up_y,upLine_last_bottom_x,upLine_last_bottom_y,paint);
            }else{
                c.drawBitmap(bitmap,null,rect,paint);
                c.drawLine(upLine_up_x,upLine_up_y,upLine_bottom_x,upLine_bottom_y,paint);
                c.drawLine(bottomLine_up_x,bottom_up_y,bottomLine_bottom_x,bottomLine_bottom_y,paint);
            }
        }
    }



}
