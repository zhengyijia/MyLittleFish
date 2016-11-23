package com.example.object;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.mylittlefish.R;

/**
 * Created by Oneplus on 2016/11/21.
 */

public class BombGoods extends GameGoods{

    private Bitmap oriBmp;

    public BombGoods(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
    }
    // 初始化图片资源的
    @Override
    protected void initBitmap() {
        // TODO Auto-generated method stub
        oriBmp = BitmapFactory.decodeResource(resources, R.drawable.missile_goods);
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        bmp = Bitmap.createBitmap(oriBmp, 0, 0, oriBmp.getWidth(), oriBmp.getHeight(), matrix, true);
        object_width = bmp.getWidth();
        object_height = bmp.getHeight();
    }

    @Override
    public void release() {
        if(!oriBmp.isRecycled())
            oriBmp.recycle();
        if(!bmp.isRecycled())
            bmp.recycle();
    }

}
