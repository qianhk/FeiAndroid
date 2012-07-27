package me.longerian.demo.singlechoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }
	
	public void clickEventHandler(View v) {
		int id = v.getId();
		Intent intent = new Intent();
		switch(id) {
		case R.id.btn_default_ui_check_mark:
			intent.setClass(getApplicationContext(), DefaultUICheckMarkDemo.class);
			startActivity(intent);
			break;
		case R.id.btn_custom_ui_check_mark:
			intent.setClass(getApplicationContext(), CustomUICheckMarkDemo.class);
			startActivity(intent);
			break;
		case R.id.btn_custom_ui_check_mark2:
			intent.setClass(getApplicationContext(), CustomUICheckMarkDemo2.class);
			startActivity(intent);
			break;
		case R.id.btn_custom_ui_check_mark3:
			intent.setClass(getApplicationContext(), CustomUICheckMarkDemo3.class);
			startActivity(intent);
			break;
		case R.id.btn_checkable_layout1:
			intent.setClass(getApplicationContext(), CheckableLinearLayoutDemo1.class);
			startActivity(intent);
			break;
		case R.id.btn_checkable_layout2:
			intent.setClass(getApplicationContext(), CheckableLinearLayoutDemo2.class);
			startActivity(intent);
			break;
		case R.id.btn_checkable_layout3:
			intent.setClass(getApplicationContext(), CheckableRelativeLayoutDemo.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
