package com.xiaoma.betweenactivityanimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BetweenActivityAnimationDemoActivity2 extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        init();
    }

    private void init(){
    	findViewById(R.id.button2).setOnClickListener(this);
    }
    
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button2){
			Intent i = new Intent(getApplicationContext(),BetweenActivityAnimationDemoActivity3.class);
			startActivity(i);
			overridePendingTransition(R.anim.hyperspace_in, R.anim.hyperspace_out);
		}
		
	}
}