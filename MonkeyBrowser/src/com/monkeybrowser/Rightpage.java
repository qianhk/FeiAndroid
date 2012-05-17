package com.monkeybrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Rightpage extends LinearLayout{

	private ListView mListViewComment;
	private EditText mEditTextComment;

	public Rightpage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.rightpage_layout, this);
		
		mListViewComment = (ListView) findViewById(R.id.comment);
		mEditTextComment = (EditText) findViewById(R.id.commentET);
	}
}