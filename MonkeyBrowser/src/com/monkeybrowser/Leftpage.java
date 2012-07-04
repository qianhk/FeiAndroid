package com.monkeybrowser;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Leftpage extends LinearLayout{
	//resource
	private String mTuangou[];
	private String mForum[];
	//tuangou
	private static GridView mTuangouGridView;
	private ImageView mTuangouAdd;
	//luntan
	private static GridView mForumGridView;
	private ImageView mForumAdd;

	public Leftpage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.leftpage_layout, this);
		
		mTuangouGridView = (GridView) findViewById(R.id.GridViewTuangou);
		mTuangouAdd = (ImageView) findViewById(R.id.tuangouAdd);
		
		mForumGridView = (GridView) findViewById(R.id.GridViewForum);
		mForumAdd = (ImageView) findViewById(R.id.forumAdd);
	    //resource
		Resources res = getResources();
		mTuangou = res.getStringArray(R.array.tuangou);
		mForum = res.getStringArray(R.array.forum);
		//tuan gou
		mTuangouGridView.setAdapter(new TextList(context, mTuangou));
		
		mTuangouAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	if(mTuangouGridView.getVisibility()!=View.VISIBLE)
            		mTuangouGridView.setVisibility(View.VISIBLE);
            	else
            		mTuangouGridView.setVisibility(View.GONE);
            }
        }); 
		
		//forum
		mForumGridView.setAdapter(new TextList(context, mForum));
		
		mForumAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	if(mForumGridView.getVisibility()!=View.VISIBLE)
            		mForumGridView.setVisibility(View.VISIBLE);
            	else
            		mForumGridView.setVisibility(View.GONE);
            }
        }); 
	}
	
	public class TextList extends BaseAdapter {  
		Context mContext;
		String mText[];
          
        //construct  
        public TextList(Context context, String []text) {  
        	mContext = context;  
        	mText = text;
        }  
          
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return mText.length;  
        }  
  
        @Override  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return mText[position];  
        }  
  
        @Override  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return position;  
        }  
  

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			TextView tv = new TextView(mContext);
			tv.setText(mText[arg0]);
			//tv.setBackgroundColor(mContext.getResources().getColor(R.color.addresscolor));
			tv.setTextColor(mContext.getResources().getColor(R.color.addresscolor));
			tv.setGravity(Gravity.CENTER); 
			return tv;
		}  
    }  
	

}
