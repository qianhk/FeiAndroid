package com.njnu.kai;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BMIActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(calcBMI);
    }

    private Button.OnClickListener calcBMI = new OnClickListener()
    {
		@Override
		public void onClick(View v) {
			DecimalFormat nf = new DecimalFormat("0.00");
			try{
			EditText fieldHeight = (EditText)findViewById(R.id.stature);
			EditText fieldWeight = (EditText)findViewById(R.id.weight);
			double height = Double.parseDouble(fieldHeight.getText().toString()) / 100;
			double weight = Double.parseDouble(fieldWeight.getText().toString());
			double BMI = weight / (height * height);

			TextView result = (TextView)findViewById(R.id.result);
			result.setText("Your BMI is " + nf.format(BMI));
			TextView fieldSuggest = (TextView)findViewById(R.id.suggest);
			if (BMI > 25){
				fieldSuggest.setText(R.string.advice_heavy);
			} else if(BMI < 20){
				fieldSuggest.setText(R.string.advice_light);
			}
			else{
				fieldSuggest.setText(R.string.advice_average);
			}
			}
			catch(Exception err)
			{
				Toast.makeText(BMIActivity.this, "error", Toast.LENGTH_SHORT).show();
			}
//			openOptionDialog();
		}
    };

    private void openOptionDialog(){
//    	Toast.makeText(BMIActivity.this, "haha", Toast.LENGTH_SHORT).show();
    	new AlertDialog.Builder(BMIActivity.this).setTitle(R.string.about_title).setMessage(R.string.about_msg)
    	.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				Uri uri = Uri.parse("http://wap.google.com");
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				startActivity(intent);
			}
		})
    	.show();
    }

    protected static final int MENU_ABOUT = Menu.FIRST;
    protected static final int MENU_Quit = Menu.FIRST + 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	menu.add(0, MENU_ABOUT, 0, "About...");
    	menu.add(0, MENU_Quit, 0, "Exit");
    	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	switch (item.getItemId()){
    	case MENU_ABOUT:
    		openOptionDialog();
    		break;

    	case MENU_Quit:
    		finish();
    		break;
    	}

    	return true;
    }
}