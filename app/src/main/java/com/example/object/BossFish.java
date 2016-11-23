package com.example.object;

import com.example.constant.ConstantUtil;
import com.example.mylittlefish.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.Random;

/*BOSS敌机的类*/
public class BossFish extends EnemyFish {
    private static int currentCount = 0;     //	对象当前的数量
    private Bitmap bossFish;
    private Bitmap resizeBmp;
    private Bitmap resizeBmp2;
    public static int sumCount = 6;

    public BossFish(Resources resources) {
        super(resources);
        // TODO Auto-generated constructor stub
        this.score = 10000;
        this.size = 7;
    }

    //初始化数据
    @Override
    public void initial(int arg0, float arg1, float arg2) {
        isAlive = true;
        isVisible = true;
        Random ran = new Random();
        speed = ran.nextInt(14) + 12 * arg0;
        if (0 == currentCount % 2) {
            isFromLeft = true;
            object_x = -object_width * (currentCount + 1);
        } else {
            isFromLeft = false;
            object_x = screen_width + object_width * (currentCount + 1);
        }
        object_y = ran.nextInt((int) (screen_height - object_height));
        currentCount++;
        if (currentCount >= sumCount) {
            currentCount = 0;
        }
    }

    //初始化图片
    @Override
    public void initBitmap() {
        // TODO Auto-generated method stub
        bossFish = BitmapFactory.decodeResource(resources, R.drawable.boss);
        object_width = bossFish.getWidth() / 2;            //获得每一帧位图的宽
        object_height = bossFish.getHeight() / 2;        //获得每一帧位图的高
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        resizeBmp = Bitmap.createBitmap(bossFish, 0, 0, bossFish.getWidth(), bossFish.getHeight(), matrix, true);
        matrix.postScale(-1f, 1f);
        resizeBmp2 = Bitmap.createBitmap(bossFish, 0, 0, bossFish.getWidth(), bossFish.getHeight(), matrix, true);
    }

    //绘图函数
    @Override
    public void drawSelf(Canvas canvas) {
        // TODO Auto-generated method stub
        if (isAlive) {
            if (isVisible) {
                canvas.save();
                canvas.clipRect(object_x, object_y, object_x + object_width, object_y + object_height);
                if (isFromLeft)
                    canvas.drawBitmap(resizeBmp2, object_x, object_y, paint);
                else
                    canvas.drawBitmap(resizeBmp, object_x, object_y, paint);
                canvas.restore();
            }
            logic();
        }
    }

    //释放资源
    @Override
    public void release() {
        // TODO Auto-generated method stub
        if (!bossFish.isRecycled()) {
            bossFish.recycle();
        }
    }

    // 检测碰撞
    @Override
    public boolean isCollide(GameObject obj) {
        return super.isCollide(obj);
    }
}
