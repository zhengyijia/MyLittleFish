package com.example.view;

import java.util.ArrayList;
import java.util.List;

import com.example.constant.ConstantUtil;
import com.example.factory.GameObjectFactory;
import com.example.mylittlefish.R;
import com.example.object.BigFish;
import com.example.object.BombGoods;
import com.example.object.BossFish;
import com.example.object.EnemyFish;
import com.example.object.GameObject;
import com.example.object.MiddleFish;
import com.example.object.MyFish;
import com.example.object.SmallFish;
import com.example.sounds.GameSoundPool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
/*游戏进行的主界面*/
public class MainView extends BaseView{
    private int missileCount; 		// 导弹的数量
	private int middlePlaneScore;	// 中型敌机的积分	//根据分数确定什么时候进行敌机初始化，初始化后分数归零，每次得分该分数会跟着增加
	private int bigPlaneScore;		// 大型敌机的积分
	private int bossPlaneScore;		// boss型敌机的积分
    private int missileScore;		// 导弹的积分
	private int sumScore;			// 游戏总得分
	private int speedTime;			// 游戏速度的倍数
	private float bg_y;				// 图片的坐标
	private float bg_y2;
	private float play_bt_w;
	private float play_bt_h;
	private boolean isPlay;			// 标记游戏运行状态
	private boolean isTouchPlane;	// 判断玩家是否按下屏幕
	private Bitmap background; 		// 背景图片
	private Bitmap background2; 	// 背景图片
	private Bitmap playButton; 		// 开始/暂停游戏的按钮图片
	private MyFish myFish;		// 玩家的飞机
	private BossFish bossFish;	// boss飞机
	private List<EnemyFish> enemyFishes;
	private GameObjectFactory factory;
	private int goalScore = 3000;
    private float touch_x;
    private float touch_y;
    private float missile_bt_y;
    private Bitmap missile_bt;		// 导弹按钮图标
    private Bitmap ori_missile_bt;
    private BombGoods missileGoods;
    public MainView(Context context,GameSoundPool sounds) {
		super(context,sounds);
		// TODO Auto-generated constructor stub
		isPlay = true;
		speedTime = 1;
		factory = new GameObjectFactory();						  //工厂类
		enemyFishes = new ArrayList<EnemyFish>();
		myFish = (MyFish) factory.createMyPlane(getResources());//生产玩家的飞机
		myFish.setMainView(this);
		for(int i = 0; i < SmallFish.sumCount; i++){
			//生产小型敌机
			SmallFish smallPlane = (SmallFish) factory.createSmallPlane(getResources());
			enemyFishes.add(smallPlane);
		}
		for(int i = 0; i < MiddleFish.sumCount; i++){
			//生产中型敌机
			MiddleFish middlePlane = (MiddleFish) factory.createMiddlePlane(getResources());
			enemyFishes.add(middlePlane);
		}
		for(int i = 0; i < BigFish.sumCount; i++){
			//生产大型敌机
			BigFish bigPlane = (BigFish) factory.createBigPlane(getResources());
			enemyFishes.add(bigPlane);
		}
		//生产BOSS敌机
        for(int i = 0; i < BossFish.sumCount; i++) {
            bossFish = (BossFish) factory.createBossPlane(getResources());
            enemyFishes.add(bossFish);
        }
        //生产导弹物品
        missileGoods = (BombGoods) factory.createMissileGoods(getResources());
		thread = new Thread(this);
        // 设置常亮
        this.setKeepScreenOn(true);
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
		initBitmap(); // 初始化图片资源
		for(GameObject obj: enemyFishes){
			obj.setScreenWH(screen_width,screen_height);
		}
        missileGoods.setScreenWH(screen_width, screen_height);
		myFish.setScreenWH(screen_width,screen_height);
		myFish.setAlive(true);
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
		if(event.getAction() == MotionEvent.ACTION_UP){
			isTouchPlane = false;
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN){
			float x = event.getX();
			float y = event.getY();
			if(x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h){
				if(isPlay){
					isPlay = false;
				}		
				else{
					isPlay = true;	
					synchronized(thread){
						thread.notify();
					}
				}
				return true;
			}
            //判断导弹按钮是否被按下
            else if(x > 10 && x < 10 + missile_bt.getWidth()
                    && y > missile_bt_y && y < missile_bt_y + missile_bt.getHeight()){
                if(missileCount > 0){
                    missileCount--;
                    sounds.playSound(5, 0);
                    for(EnemyFish fobj:enemyFishes){
                        if(fobj.isCanCollide()){
                            fobj.setAlive(false);
                        }
                    }
                }
                return true;
            }
			//判断玩家飞机是否被按下
			/*else if(x > myFish.getObject_x() && x < myFish.getObject_x() + myFish.getObject_width()
					&& y > myFish.getObject_y() && y < myFish.getObject_y() + myFish.getObject_height()){
				if(isPlay){
					isTouchPlane = true;
				}
				return true;
			}*/
            else{
                touch_x = myFish.getMiddle_x() - x;
                touch_y = myFish.getMiddle_y() - y;
                isTouchPlane = true;
                return true;
            }
		}
		//响应手指在屏幕移动的事件
		else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
			//判断触摸点是否为玩家的飞机
			if(isTouchPlane){
				float x = event.getX();
				float y = event.getY();
				if(x + touch_x > myFish.getMiddle_x() + 20){
					if(myFish.getMiddle_x() + myFish.getSpeed() <= screen_width){
						myFish.setMiddle_x(myFish.getMiddle_x() + myFish.getSpeed());
					}					
				}
				else if(x + touch_x < myFish.getMiddle_x() - 20){
					if(myFish.getMiddle_x() - myFish.getSpeed() >= 0){
						myFish.setMiddle_x(myFish.getMiddle_x() - myFish.getSpeed());
					}				
				}
				if(y + touch_y > myFish.getMiddle_y() + 20){
					if(myFish.getMiddle_y() + myFish.getSpeed() <= screen_height){
						myFish.setMiddle_y(myFish.getMiddle_y() + myFish.getSpeed());
					}		
				}
				else if(y + touch_y < myFish.getMiddle_y() - 20){
					if(myFish.getMiddle_y() - myFish.getSpeed() >= 0){
						myFish.setMiddle_y(myFish.getMiddle_y() - myFish.getSpeed());
					}
				}
				return true;
			}	
		}
		return false;
	}
	// 初始化图片资源方法
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		playButton = BitmapFactory.decodeResource(getResources(),R.drawable.play);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_01);
		background2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_02);
        ori_missile_bt = BitmapFactory.decodeResource(getResources(), R.drawable.missile_bt);
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        missile_bt = Bitmap.createBitmap(ori_missile_bt, 0, 0, ori_missile_bt.getWidth(), ori_missile_bt.getHeight(), matrix, true);
		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();
		play_bt_w = playButton.getWidth();
		play_bt_h = playButton.getHeight()/2;
		bg_y = 0;
		bg_y2 = screen_width + bg_y;
        missile_bt_y = screen_height - 10 - missile_bt.getHeight();
	}
	//初始化游戏对象
	public void initObject(){
		for(EnemyFish obj: enemyFishes){
			//初始化小型敌机	
			if(obj instanceof SmallFish){
				if(!obj.isAlive()){
					obj.initial(speedTime,0,0);
					break;
				}	
			}
			//初始化中型敌机
			else if(obj instanceof MiddleFish){
				//if(middlePlaneScore >= 0){
					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	
				//}
			}
			//初始化大型敌机
			else if(obj instanceof BigFish){
				if(bigPlaneScore >= 1000){
					if(!obj.isAlive()){
						obj.initial(speedTime,0,0);
						break;
					}	
				}
			}
			//初始化BOSS敌机
			else{
				if(bossPlaneScore >= 10000){
					if(!obj.isAlive()){
						obj.initial(0,0,0);
						break;
					}
				}
			}
            //初始化导弹物品
            if(missileScore >= 3000){
                if(!missileGoods.isAlive()){
                    missileGoods.initial(0,0,0);
                    missileScore = 0;
                }
            }
		}
		//提升等级
		if(sumScore >= speedTime*100000 && speedTime < 6){
			speedTime++;	
		}
	}
	// 释放图片资源的方法
	@Override
	public void release() {
		// TODO Auto-generated method stub
		for(GameObject obj: enemyFishes){
			obj.release();
		}
		myFish.release();
        missileGoods.release();
		if(!playButton.isRecycled()){
			playButton.recycle();
		}
		if(!background.isRecycled()){
			background.recycle();
		}
		if(!background2.isRecycled()){
			background2.recycle();
		}
        if(!ori_missile_bt.isRecycled()){
            ori_missile_bt.recycle();
        }
	}
	// 绘图方法
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); // 绘制背景色
			canvas.save();
			// 计算背景图片与屏幕的比例
			canvas.scale(scalex, scaley, 0, 0);
			canvas.drawBitmap(background, bg_y, 0, paint);   // 绘制背景图
			canvas.drawBitmap(background2, bg_y2, 0, paint); // 绘制背景图
			canvas.restore();
            //绘制导弹按钮
            if(missileCount > 0){
                paint.setTextSize(40);
                paint.setColor(Color.BLACK);
                canvas.drawBitmap(missile_bt, 10,missile_bt_y, paint);
                canvas.drawText("X "+String.valueOf(missileCount), 20 + missile_bt.getWidth(), screen_height - 25, paint);//绘制文字
            }
            //绘制导弹物品
            if(missileGoods.isAlive()){
                if(missileGoods.isCollide(myFish)){
                    missileGoods.setAlive(false);
                    missileCount++;
                    sounds.playSound(4, 0);
                }
                else
                    missileGoods.drawSelf(canvas);
            }
			//绘制按钮
			canvas.save();
			canvas.clipRect(10, 10, 10 + play_bt_w,10 + play_bt_h);
			if(isPlay){
				canvas.drawBitmap(playButton, 10, 10, paint);			 
			}
			else{
				canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
			}
			canvas.restore();
			//绘制敌机
			for(EnemyFish obj: enemyFishes){
				if(obj.isAlive()){
					obj.drawSelf(canvas);					
					//检测敌机是否与玩家的飞机碰撞					
					if(obj.isCanCollide() && myFish.isAlive()){
						if(obj.isCollide(myFish)){
							if(obj.getSize() >= myFish.getSize())
							    myFish.setAlive(false);
                            else{
								sounds.playSound(2, 0);
                                obj.setAlive(false);
                                addGameScore(obj.getScore());
                                if(myFish.getSize() < 8) {
                                    if (sumScore >= goalScore) {
                                        myFish.setSize(myFish.getSize() + 2);
                                        goalScore *= 8;
                                    }
                                }

                            }
						}
					}
				}	
			}
			if(!myFish.isAlive()){
				threadFlag = false;
				sounds.playSound(3, 0);			//飞机炸毁的音效
			}
			myFish.drawSelf(canvas);	//绘制玩家的飞机
			//sounds.playSound(1, 0);	  //子弹音效
            //绘制导弹按钮
            if(missileCount > 0){
                paint.setTextSize(40);
                paint.setColor(Color.BLACK);
                canvas.drawBitmap(missile_bt, 10,missile_bt_y, paint);
                canvas.drawText("X "+String.valueOf(missileCount), 20 + missile_bt.getWidth(), screen_height - 25, paint);//绘制文字
            }
			//绘制积分文字
			paint.setTextSize(30);
			paint.setColor(Color.BLACK);
			canvas.drawText("积分:"+String.valueOf(sumScore), 30 + play_bt_w, 40, paint);		//绘制文字
			canvas.drawText("等级 X "+String.valueOf(speedTime), screen_width - 150, 40, paint); //绘制文字
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	// 背景移动的逻辑函数
	public void viewLogic(){
		if(bg_y < bg_y2){
			bg_y -= 10;
			bg_y2 = bg_y + background.getWidth();
		}
		else{
			bg_y2 -= 10;
			bg_y = bg_y2 + background.getWidth();
		}
		if(bg_y <= -background.getWidth()){
			bg_y = bg_y2 + background.getWidth();
		}
		else if(bg_y2 <= -background.getWidth()){
			bg_y2 = bg_y + background.getWidth();
		}
	}
	// 增加游戏分数的方法 
	public void addGameScore(int score){
		middlePlaneScore += score;	// 中型敌机的积分
		bigPlaneScore += score;		// 大型敌机的积分
		bossPlaneScore += score;	// boss型敌机的积分
        if(missileScore > 3000){
            missileScore = 0;
        }
        missileScore += 100;		// 导弹的积分
		sumScore += score;			// 游戏总得分
	}
	// 播放音效
	public void playSound(int key){
		sounds.playSound(key, 0);
	}
	// 线程运行的方法
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (threadFlag) {	
			long startTime = System.currentTimeMillis();
			initObject();
			drawSelf();	
			viewLogic();		//背景移动的逻辑	
			long endTime = System.currentTimeMillis();	
			if(!isPlay){
				synchronized (thread) {  
				    try {  
				    	thread.wait();  
				    } catch (InterruptedException e) {  
				        e.printStackTrace();  
				    }  
				}  		
			}	
			try {
				if (endTime - startTime < 100)
					Thread.sleep(100 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message message = new Message();   
		message.what = 	ConstantUtil.TO_END_VIEW;
		message.arg1 = Integer.valueOf(sumScore);
		mainActivity.getHandler().sendMessage(message);
	}
}
