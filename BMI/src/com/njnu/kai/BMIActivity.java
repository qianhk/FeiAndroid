package com.njnu.kai;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BMIActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(calcBMI);
    }

    private OnClickListener calcBMI = new OnClickListener()
    {
		@Override
		public void onClick(View v) {
			DecimalFormat nf = new DecimalFormat("0.00");
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
    };
}