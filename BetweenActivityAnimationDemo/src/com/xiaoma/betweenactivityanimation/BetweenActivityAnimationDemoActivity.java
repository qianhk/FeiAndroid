package com.xiaoma.betweenactivityanimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**  
* @Title: BetweenActivityAnimationDemoActivity.java
* @Package com.xiaoma.betweenactivityanimation
* @Description: Activity之间跳转动画学习  如果怕动画太短看不清楚的，
* 可以把动画XML文件里面标签属性 during值设置的长些，吼吼
* @author XiaoMa
*/
public class BetweenActivityAnimationDemoActivity extends Activity implements
		OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init() {
		findViewById(R.id.button1).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			Intent intent = new Intent(getApplicationContext(),
					BetweenActivityAnimationDemoActivity2.class);
			startActivity(intent);
			
			/**
			 * 对下面这个方法的官方解释如下，版本从2.0后开始哦
			 * Call immediately after one of the flavors of startActivity(Intent) or finish() 
			 * to specify an explicit transition animation to perform next.
			 * 用工具查到解释为：
			 *   在startActivity(Intent)或finish()之法之后调用后，会立即用一个指定的描述动画的XML文件来执行
			 *   下一个Activity 
			 * 
			 * 下面两句是对这个方法两个参数的解释,在此之前小马也看了下别人讲的，
			 * 其实是错的，看官方的解释肯定没错，不懂英语的用工具查下
			 * 小马一直都说的，我英语很烂，我能查的你一定也能查得到
			 * 1.enterAnim	A resource ID of the animation resource 
			 *              to use for the incoming activity. Use 0 for no animation.
			 * 2.exitAnim	A resource ID of the animation resource 
			 *              to use for the outgoing activity. Use 0 for no animation.
			 * 一：进入动画  一个动画资源，用于目标Activity 进入屏幕时的动画，此处写0代表无动画
			 * 二：退出动画  一个动画资源，用于当前Activity 退出屏幕时的动画，此处写0代表无动画
			 * 
			 * 这个目标、当前怎么理解？比如：startActivity( A（当前）--> B（目标）) 《finish()一样》
			 * 下面参数中有一个为0，就表示A退出时无动画...一定把参数搞清楚，不然动画就搞晕了
			 * overridePendingTransition(R.anim.zoom_enter, 0);  
			 * 方法两个参数与目标、当前Activity对应关系如效果下方绿色图所示
			 */
			overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		}
	}
	
	/** 列几个安卓自带的动画效果，大家可以把上面 overridePendingTransition参数改下看看效果
	 *  实现淡入淡出的效果
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);    
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		 
		由左向右滑入的效果
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);    
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	 */
}