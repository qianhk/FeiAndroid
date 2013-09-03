package com.example.android.interactivechart;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup 
{
	private static final String TAG = "ScrollLayout";
    private Scroller mScroller;  //生成滑动条对象
    private VelocityTracker mVelocityTracker;  
    private int mCurScreen;  
    private int mDefaultScreen = 1;  
    private static final int TOUCH_STATE_REST = 0;  
    private static final int TOUCH_STATE_SCROLLING = 1;   
    private static final int SNAP_VELOCITY = 600;   
    private int mTouchState = TOUCH_STATE_REST;  
    private int mTouchSlop;  
    private float mLastMotionX;  
    
    //构造函数
    public ScrollLayout(Context context, AttributeSet attrs) 
    {  
        this(context, attrs, 0);  
        // TODO Auto-generated constructor stub  
    }
    
    //构造函数
    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) 
    {  
        super(context, attrs, defStyle);  
        // TODO Auto-generated constructor stub  
        mScroller = new Scroller(context);   
        mCurScreen = mDefaultScreen;  
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();  //计算子视图能滑动的距离
    }  
    
    @Override  
    protected void onLayout(boolean changed, int l, int t, int r, int b) 
    {  
        if (changed)
        {  
            int childLeft = 0;  
            final int childCount = getChildCount();//记录滚动视图组有多少个子视图  
              
            for (int i=0; i<childCount; i++)
            {  
                final View childView = getChildAt(i);//获取子视图  
                if (childView.getVisibility() != View.GONE)//判断当前子视图是否可视
                {  
                    final int childWidth = childView.getMeasuredWidth(); //获取母体宽度 
                    childView.layout(childLeft, 0,   
                            childLeft+childWidth, childView.getMeasuredHeight()); //设置子体位置
                    childLeft += childWidth;//记录字体左边边界位置  
                }  
            }
        }  
    }  
    
    
    //view.View的子函数,需要重写
    //执行初始的判断测量，滑到初始位置，其他时候不产生作用
    @Override    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {     
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);     
        final int width = MeasureSpec.getSize(widthMeasureSpec);//MeasureSpec是全局静态变量View的成员变量，封装传递从父到子的布局要求。 
        
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);   
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        
        if (widthMode != MeasureSpec.EXACTLY) //如果父母没有为子窗体提供合适的宽度，则抛出异常
        {     
            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");   
        }     
    
            
        if (heightMode != MeasureSpec.EXACTLY) //如果父母没有为子窗体提供合适的高度，则抛出异常
        {     
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");  
        }     
    
        // The children are given the same width and height as the scrollLayout  
        
        final int count = getChildCount();//记录子视图个数     
        for (int i = 0; i < count; i++) 
        {     
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);     
        }     
        
       
        scrollTo(mCurScreen * width, 0);  
    }    
      
    /** 
     * According to the position of current layout 
     * scroll to the destination page. 
     */ 
    //判断当前滚动坐标是否超过屏幕一半
    public void snapToDestination() 
    {  
        final int screenWidth = getWidth();  
        final int destScreen = (getScrollX()+ screenWidth/2)/screenWidth;  
        snapToScreen(destScreen);  
        
    }  
      
    public void snapToScreen(int whichScreen) 
    {  
        // get the valid layout page  
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));  //计算当前应该显示的是第几个子窗体（0、1、2……）
        if (getScrollX() != (whichScreen*getWidth()))//判断当前子窗体是否停在准确位置，不准确则运行 
        {  
        	int X=320;
        	if(whichScreen==2)X=getScrollX()-320;
        	if(whichScreen==0)X=getScrollX()+320;     
            final int delta = getWidth()-X;//记录当前距离标准位置坐标的距离
            mScroller.startScroll(X, 0,   
                    delta, 0, Math.abs(delta)*2);  //设置子窗体滑动的横向起点、纵向起点、横向位移、纵向位移、移动时间（微秒）      
            mCurScreen = 1;
            Log.e(TAG, "moving to screen "+mCurScreen);
            invalidate();       // Redraw the layout
            System.out.println("重画结束");
            
        }  
    } 
    
    //没搞懂这段代码是干嘛用的，注释运行之后发现没有异样。
    public void setToScreen(int whichScreen) 
    {  
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));  
        mCurScreen = whichScreen;
        scrollTo(whichScreen*getWidth(), 0);  
    }
      
    public int getCurScreen() 
    {  
        return mCurScreen;  
    }  
      
    
    @Override  
    public void computeScroll() 
    {  
        // TODO Auto-generated method stub  
        if (mScroller.computeScrollOffset()) 
        {  
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  
            postInvalidate();  
        }  
    }  
    
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) 
    {  
        // TODO Auto-generated method stub  
          
        if (mVelocityTracker == null) //如果获取触屏没有移动，则重新获取
        {  
            mVelocityTracker = VelocityTracker.obtain();  
        }  
        mVelocityTracker.addMovement(event);  
          
        final int action = event.getAction();  
        final float x = event.getX();  
          
        switch (action) 
        {  
        case MotionEvent.ACTION_DOWN:
        	
            if (!mScroller.isFinished())//如果中途结束了进程。则中断动作
            {  
                mScroller.abortAnimation();  
            }  
            mLastMotionX = x;  //获取第当前横坐标
            break;  
              
        case MotionEvent.ACTION_MOVE:
        	
            int deltaX = (int)(mLastMotionX - x);//记录移动的距离  
            mLastMotionX = x;  //获取当前横坐标
              
            scrollBy(deltaX, 0); //滑动当前子视图 
            break;  
              
        case MotionEvent.ACTION_UP:
        	//下面这个语句如果不注释的话，滚动视图会停留在各种位置，而不是和屏幕边界对齐
            // if (mTouchState == TOUCH_STATE_SCROLLING) {     
            final VelocityTracker velocityTracker = mVelocityTracker;     
            velocityTracker.computeCurrentVelocity(1000);  //设置当前滚动条的滚动速度   
            int velocityX = (int) velocityTracker.getXVelocity();  //获得当前横坐标滚动素速度     
             //如果滚动速度大于600像素/秒，而且当前子视图不是最左端视图，则向左偏移 
            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) 
            {     
                // Fling enough to move left 
                snapToScreen(mCurScreen - 1);     
            } 
            else 
            	//如果滚动速度大于600像素/秒，而且当前子视图不是最右端视图，则向右偏移 
            	if (velocityX < -SNAP_VELOCITY     
                    && mCurScreen < getChildCount() - 1) 
            	{     
                // Fling enough to move right 
            		snapToScreen(mCurScreen + 1);     
            	} 
            	//其他的则，按照移动坐标位置移动子视图
            	else 
            	{     
            		snapToDestination();     
            	}     
            //设置完子视图的滑动之后，应该释放掉mVelocityTracker对象
            if (mVelocityTracker != null) 
            {     
                mVelocityTracker.recycle();     
                mVelocityTracker = null;     
            }     
            // }     
            mTouchState = TOUCH_STATE_REST;     
            break;  
            
        case MotionEvent.ACTION_CANCEL:  
            mTouchState = TOUCH_STATE_REST;  
            break;  
        }  
          
        return true;  
    }  
    
    
    @Override  
    public boolean onInterceptTouchEvent(MotionEvent ev) {  
        // TODO Auto-generated method stub   
        final int action = ev.getAction(); 
        //如果当前动作是在移动且触屏状态显示不是初始值则返回true结束该函数
        if ((action == MotionEvent.ACTION_MOVE) &&   
                (mTouchState != TOUCH_STATE_REST)) 
        {  
            return true;  
        }  
          
        final float x = ev.getX();   
          
        switch (action) 
        {  
        case MotionEvent.ACTION_MOVE:  
            final int xDiff = (int)Math.abs(mLastMotionX-x);
            //如果按下时的坐标和当前移动到的位置的坐标差的绝对值大于子视图可移动的距离则修改为正在滑动
            if (xDiff>mTouchSlop) 
            {  
                mTouchState = TOUCH_STATE_SCROLLING;  
            }  
            break;  
              
        case MotionEvent.ACTION_DOWN:  
            mLastMotionX = x;   
            //如果滚动完成，则返回初始值，否则返回滚动中的状态
            mTouchState = mScroller.isFinished()? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;  
            break;  
              
        case MotionEvent.ACTION_CANCEL:  
        case MotionEvent.ACTION_UP:  
            mTouchState = TOUCH_STATE_REST;  
            break;  
        }  
          
        return mTouchState != TOUCH_STATE_REST;  
    }
      
}
