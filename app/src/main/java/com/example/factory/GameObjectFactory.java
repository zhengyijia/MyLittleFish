package com.example.factory;

import android.content.res.Resources;

import com.example.object.BigFish;
import com.example.object.BombGoods;
import com.example.object.BossFish;
import com.example.object.GameObject;
import com.example.object.MiddleFish;
import com.example.object.MyFish;
import com.example.object.SmallFish;
/*游戏对象的工厂类*/
public class GameObjectFactory {
	//创建小型敌机的方法
	public GameObject createSmallPlane(Resources resources){
		return new SmallFish(resources);
	}
	//创建中型敌机的方法
	public GameObject createMiddlePlane(Resources resources){
		return new MiddleFish(resources);
	}
	//创建大型敌机的方法
	public GameObject createBigPlane(Resources resources){
		return new BigFish(resources);
	}
	//创建BOSS敌机的方法
	public GameObject createBossPlane(Resources resources){
		return new BossFish(resources);
	}
	//创建玩家飞机的方法
	public GameObject createMyPlane(Resources resources){
		return new MyFish(resources);
	}
	//创建导弹物品
	public GameObject createMissileGoods(Resources resources){
		return new BombGoods(resources);
	}
}
