package com.jinkun_innovation.claim.weight.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jinkun_innovation.claim.utilcode.util.SizeUtils;


/**
 * Created by yangxing on 2018/2/5.
 */

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
    private static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;//水平方向
    private static final int VERTICAL = LinearLayoutManager.VERTICAL;//垂直方向
    private int orientation;//方向
    private final int decoration;//边距大小 px

    public GalleryItemDecoration(@LinearLayoutCompat.OrientationMode int orientation, int decoration) {
        this.orientation = orientation;
        this.decoration = decoration;
    }


    @Override
    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        final int lastPosition = state.getItemCount() - 1;//整个RecyclerView最后一个item的position
        final int firstPosition = 0;//整个RecyclerView第一个item的position
        final int current = parent.getChildLayoutPosition(view);//获取当前要进行布局的item的position
        final int firstDecoration = SizeUtils.dp2px(decoration);//获取当前要进行布局的item的position

        if (current == -1) return;//holder出现异常时，可能为-1
        if (layoutManager instanceof LinearLayoutManager && !(layoutManager instanceof GridLayoutManager)) {//LinearLayoutManager
            if (orientation == LinearLayoutManager.VERTICAL) {//垂直
                outRect.set(0, 0, 0, firstDecoration);
                if (current == lastPosition) {//判断是否为最后一个item
                    outRect.set(0, 0, 0, 0);
                } else {
                    outRect.set(0, 0, 0, firstDecoration);
                }
            } else {//水平
                if (current == firstPosition) {//判断是否为最后一个item
                    outRect.set(firstDecoration, 0, firstDecoration / 4, 0);
                } else if (current == lastPosition) {
                    outRect.set(firstDecoration / 4, 0, firstDecoration, 0);
                } else {
                    outRect.set(firstDecoration / 4, 0, firstDecoration / 4, 0);
                }
            }
        }

    }


}
