package com.xiaoma.betweenactivityanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BetweenActivityAnimationDemoActivity3 extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main3);
        init();
    }
    
    private void init(){
    	findViewById(R.id.button3).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button3)
		{
			finish();
			//如果这个地方不想用自己的，可以直接调安卓提供的动画，如下：
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			
			//安卓自带的动画哦，看效果就知道了
			//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			
		}
		
	}
    
}