package com.example.view;

import com.example.constant.ConstantUtil;
import com.example.mylittlefish.R;
import com.example.sounds.GameSoundPool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
/*游戏开始前的界面类*/
public class ReadyView extends BaseView{
	private float fly_x;					// 图片的坐标
	private float fly_y;
	private float fly_height;
	private float text_x;
	private float text_y;
	private float button_x;
    private float button_x2;
	private float button_y;
	private float strwid;
	private float strhei;
	private boolean isBtChange;				// 按钮图片改变的标记
	private boolean isBtChange2;
	//private String startGame = "开始游戏";	// 按钮的文字
	//private String exitGame = "退出游戏";
	private Bitmap text;					// 文字图片
    private Bitmap resizeText;
	private Bitmap startButton;				// 开始按钮图片
	private Bitmap startButtonPress;		// 开始按钮按下图片
	private Bitmap exitButton;				// 退出按钮图片
	private Bitmap exitButtonPress;			// 退出按钮按下图片
	private Bitmap resizeStart;
	private Bitmap resizeStartPress;
	private Bitmap resizeExit;
	private Bitmap resizeExitPress;
	//private Bitmap planefly;				// 飞机飞行的图片
	private Bitmap background;				// 背景图片
	private Rect rect;						// 绘制文字的区域
	public ReadyView(Context context,GameSoundPool sounds) {
		super(context,sounds);
		// TODO Auto-generated constructor stub
		paint.setTextSize(40);
		rect = new Rect();
		thread = new Thread(this);
	}
	// 视图改变的方法
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}
	// 视图创建的方法
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		initBitmap(); 
		if(thread.isAlive()){
			thread.start();
		}
		else{
			thread = new Thread(this);
			thread.start();
		}
	}
	// 视图销毁的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		release();
	}
	// 响应触屏事件的方法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& event.getPointerCount() == 1) {
			float x = event.getX();
			float y = event.getY();
			//判断第一个按钮是否被按下
			if (x > button_x && x < button_x + resizeStart.getWidth()
					&& y > button_y && y < button_y + resizeStart.getHeight()) {
				sounds.playSound(1, 0);
				isBtChange = true;
				drawSelf();
				mainActivity.getHandler().sendEmptyMessage(ConstantUtil.TO_MAIN_VIEW);
			}
			//判断第二个按钮是否被按下
			else if (x > button_x2 && x < button_x2 + resizeExit.getWidth()
					&& y > button_y && y < button_y + resizeExit.getHeight()) {
				sounds.playSound(1, 0);
				isBtChange2 = true;
				drawSelf();
				mainActivity.getHandler().sendEmptyMessage(ConstantUtil.END_GAME);
			}
			return true;
		} 
		//响应屏幕单点移动的消息
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float x = event.getX();
			float y = event.getY();
			if (x > button_x && x < button_x + resizeStart.getWidth()
					&& y > button_y && y < button_y + resizeStart.getHeight()) {
				isBtChange = true;
			} else {
				isBtChange = false;
			}
			if (x > button_x2 && x < button_x2 + resizeExit.getWidth()
					&& y > button_y && y < button_y + resizeExit.getHeight()) {
				isBtChange2 = true;
			} else {
				isBtChange2 = false;
			}
			return true;
		} 
		//响应手指离开屏幕的消息
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			isBtChange = false;
			isBtChange2 = false;
			return true;
		}
		return false;
	}
	// 初始化图片资源方法
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		background = BitmapFactory.decodeResource(getResources(),R.drawable.bg_01);
		text = BitmapFactory.decodeResource(getResources(), R.drawable.text);
		//planefly = BitmapFactory.decodeResource(getResources(), R.drawable.fly);
		startButton = BitmapFactory.decodeResource(getResources(),R.drawable.start_bt);
		startButtonPress = BitmapFactory.decodeResource(getResources(),R.drawable.start_press_bt);
		exitButton = BitmapFactory.decodeResource(getResources(),R.drawable.exit_bt);
		exitButtonPress = BitmapFactory.decodeResource(getResources(),R.drawable.exit_press_bt);
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f,0.5f); //长和宽放大缩小的比例
        resizeText = Bitmap.createBitmap(text,0,0,text.getWidth(),text.getHeight(),matrix,true);
        resizeStart = Bitmap.createBitmap(startButton,0,0,startButton.getWidth(),startButton.getHeight(),matrix,true);
        resizeStartPress = Bitmap.createBitmap(startButtonPress,0,0,startButtonPress.getWidth(),startButtonPress.getHeight(),matrix,true);
        resizeExit = Bitmap.createBitmap(exitButton,0,0,exitButton.getWidth(),exitButton.getHeight(),matrix,true);
        resizeExitPress = Bitmap.createBitmap(exitButtonPress,0,0,exitButtonPress.getWidth(),exitButtonPress.getHeight(),matrix,true);
		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();
		text_x = screen_width / 2 - resizeText.getWidth() / 2;
		text_y = screen_height / 2 - resizeText.getHeight();
		//fly_x = screen_width / 2 - planefly.getWidth() / 2;
		//fly_height = planefly.getHeight() / 3;
		fly_y = text_y - fly_height - 20;
		button_x = screen_width / 2 - resizeStart.getWidth()*3/2 ;
        button_x2 = button_x +resizeStart.getWidth()*2;
		button_y = screen_height / 2 + resizeStart.getHeight();

		// 返回包围整个字符串的最小的一个Rect区域
		/*paint.getTextBounds(startGame, 0, startGame.length(), rect);
		strwid = rect.width();
		strhei = rect.height();*/
	}
	// 释放图片资源的方法
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if (!text.isRecycled()) {
			text.recycle();
		}
		if (!startButton.isRecycled()) {
			startButton.recycle();
		}
		if (!startButtonPress.isRecycled()) {
			startButtonPress.recycle();
		}
        if(!exitButton.isRecycled()){
            exitButton.recycle();
        }
        if(!exitButtonPress.isRecycled()){
            exitButtonPress.recycle();
        }
		/*if (!planefly.isRecycled()) {
			planefly.recycle();
		}*/
		if (!background.isRecycled()) {
			background.recycle();
		}
	}
	// 绘图方法
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); 						// 绘制背景色
			canvas.save();
			canvas.scale(scalex, scaley, 0, 0);					// 计算背景图片与屏幕的比例
			canvas.drawBitmap(background, 0, 0, paint); 		// 绘制背景图
			canvas.restore();
			canvas.drawBitmap(resizeText, text_x, text_y, paint);		// 绘制文字图片
			//当手指滑过按钮时变换图片
			if (isBtChange) {
				canvas.drawBitmap(resizeStartPress, button_x, button_y, paint);
			} 
			else {
				canvas.drawBitmap(resizeStart, button_x, button_y, paint);
			}
			if (isBtChange2) {
				canvas.drawBitmap(resizeExitPress, button_x2, button_y, paint);
			}
			else {
				canvas.drawBitmap(resizeExit, button_x2, button_y, paint);
			}		
			//开始游戏的按钮
			/*canvas.drawText(startGame, screen_width / 2 - strwid / 2, button_y
					+ button.getHeight() / 2 + strhei / 2, paint);*/
			//退出游戏的按钮
			/*canvas.drawText(exitGame, screen_width / 2 - strwid / 2, button_y2
					+ button.getHeight() / 2 + strhei / 2, paint);*/
			//飞机飞行的动画
			/*canvas.save();
			canvas.clipRect(fly_x, fly_y, fly_x + planefly.getWidth(), fly_y + fly_height);
			canvas.drawBitmap(planefly, fly_x, fly_y - currentFrame * fly_height,paint);
			currentFrame++;
			if (currentFrame >= 3) {
				currentFrame = 0;
			}
			canvas.restore();*/
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	// 线程运行的方法
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (threadFlag) {
			long startTime = System.currentTimeMillis();
			drawSelf();
			long endTime = System.currentTimeMillis();
			try {
				if (endTime - startTime < 400)
					Thread.sleep(400 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
	}
}
