package com.example.myarcmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class MyArcMenu extends ViewGroup implements OnClickListener{
    //自定义view
	//默认位置
	private Position mposition=Position.RIGHT_BOTTOM;
	
	private Status mstatus=Status.CLOSE;
	
	//默认半径
	private int radius=100;
	//当前状态
	public enum Status{
		OPEN,CLOSE
	}
	public enum Position{
		LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
	}
    public interface OnMenuItemClickListener{
	    	 void onClick(View v,int index);
	 }
	//回调接口
	private OnMenuItemClickListener listener;

	
	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		this.listener = listener;
	}

	public MyArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//将dp转换为px
		radius=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius,
				getResources().getDisplayMetrics());
		//读取自定义属性
		TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.MyArcMenu,
				defStyleAttr, 0);
		int postion=a.getInt(R.styleable.MyArcMenu_position, 1);
		switch(postion){
		case 0:
		   mposition=Position.LEFT_TOP;
			break;
		case 1:
			 mposition=Position.LEFT_BOTTOM;
			break;
		case 2:
			 mposition=Position.RIGHT_TOP;
			break;
		case 3:
			  mposition=Position.RIGHT_BOTTOM;
			break;
		}
		//获半径取值 
		radius=a.getDimensionPixelSize(R.styleable.MyArcMenu_radius,
				(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,
						getResources().getDisplayMetrics()));
		//释放资源
		a.recycle();
		
	}

	public MyArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		
	}
   
	public MyArcMenu(Context context) {
		this(context,null);
		
	}
   @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	   //计算子布局大小
	   int count=getChildCount();
	   
	   for(int i=0;i<count;i++){
		   
		   getChildAt(i).measure(0, 0);
	   }
	   
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	
		 //放置第一个主按钮
		if(changed){
		    int count=getChildCount();
		    View menu=getChildAt(0);
		    menu.setOnClickListener(this);
		    int left=0;
		    int top=0;
		    switch(mposition){
		       case LEFT_TOP:
		    	  break;
		       case LEFT_BOTTOM:
		    	   left=0;
		    	   top=getMeasuredHeight()-menu.getMeasuredHeight();
		    	   break;
		       case RIGHT_TOP:
		    	   left=getMeasuredWidth()-menu.getMeasuredWidth();
		    	   top=0;
		    	   break;
		       case RIGHT_BOTTOM:
		          left=getMeasuredWidth()-menu.getMeasuredWidth();
		          top=getMeasuredHeight()-menu.getMeasuredHeight();
		          break;
		    }
		    //在父布局中的位置
		    menu.layout(left, top, left+menu.getMeasuredWidth(), top+menu.getMeasuredHeight());
		    
		    //放置其他child位置
		    if(count-2<=0){
		    	return;
		    }
		    double angle=(double) (Math.PI /2/(count-2));
		    for(int i=0;i<count-1;i++){
		    	 View child=getChildAt(i+1);
		    	 child.setVisibility(View.GONE); //隐藏
	             int cWidth = child.getMeasuredWidth();  
	             
	             int cHeight = child.getMeasuredHeight();  
	             //注意的地方
	             // 写成ctop=(int)Math.sin(i*angle)*radius的话前面的结果sin会强制为int;
		    	 int ctop=(int)(Math.sin(i*angle)*radius);
		    	 int cleft=(int)(Math.cos(i*angle)*radius);
		    	 if(mposition==Position.RIGHT_BOTTOM||
		    			 mposition==Position.RIGHT_TOP){
		    		 cleft=getMeasuredWidth()-cleft-cWidth;
		    	 
		    	 }
		    	 if(mposition==Position.RIGHT_BOTTOM||
		    			 mposition==Position.LEFT_BOTTOM){
		    		 ctop=getMeasuredHeight()-ctop-cHeight;
		    	 
		    	 }
		    	 child.layout(cleft, ctop, cleft+child.getMeasuredWidth(), ctop+child.getMeasuredHeight());
		    }
		    
		}
	}

	@Override
	public void onClick(View view) {
		
		 //旋转动画
		
		RotateAnimation animation=new RotateAnimation(0, 270,
				Animation.RELATIVE_TO_SELF, 0.5f, 	Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(300);
		view.startAnimation(animation);
		
		int count=getChildCount();
		for(int i=0;i<count-1;i++){
			final View child=getChildAt(i+1);
			child.setVisibility(View.VISIBLE);
			AnimationSet set=new AnimationSet(true);
			RotateAnimation rotate= new RotateAnimation(0, 270,
					Animation.RELATIVE_TO_SELF, 0.5f, 	Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setDuration(300);
			
			 TranslateAnimation translate=null;
			 if(mstatus==Status.CLOSE){
				 translate= new TranslateAnimation(view.getLeft()-child.getLeft(),	0,
					view.getTop()-child.getTop(),0 );
				 child.setClickable(true);
			 }
			 else{
				 child.setClickable(false);
				 translate= new TranslateAnimation(
							0,view.getLeft()-child.getLeft(),0,view.getTop()-child.getTop());
				 translate.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						
						child.setVisibility(View.GONE);
					}
				});
			 }
			 translate.setFillAfter(true); //可有可恶没有影响 是否停留最终位置
			 //延迟播放时间
			 translate.setStartOffset((i * 100) / (count - 1));  
			rotate.setFillAfter(true);
			 set.setDuration(300);  
			 //设置加速器
			 set.setInterpolator(new OvershootInterpolator(2F));  
		     //调用先后顺序必须为先旋转后移动
			 set.addAnimation(rotate);
			 set.addAnimation(translate);
			 child.startAnimation(set);
			 final int index=i;
			 child.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(listener!=null){
						listener.onClick(v, index);
					}
					//放大动画与颜色渐变动画
					 childrenAnimation(index);
				}
			});
			
		}
		 changeState();
	}
   public void childrenAnimation(int index){
	    int count=getChildCount();
	    for(int i=0;i<count-1;i++){
	    	  View child=getChildAt(i+1);
	    	  if(index==i){
	    		  //放大动画
	    		  ScaleAnimation scale=new ScaleAnimation
	    				  (1.0f, 4.0f, 1.0f, 4.0f, Animation.RELATIVE_TO_SELF,
	    						  0.5f,Animation.RELATIVE_TO_SELF,
	    						  0.5f);
	    		  AlphaAnimation alpha=
	    				  new AlphaAnimation(1, 0);
	    		  AnimationSet set=new AnimationSet(true);
	    	
	    		  set.addAnimation(alpha);
	    		  set.addAnimation(scale);
	    		  set.setDuration(300);
	    		  set.setFillAfter(true);
	    		  child.startAnimation(set);
	    		  
	    	  }
	    	  else{
	    	
	    		  ScaleAnimation scale=new ScaleAnimation
	    				  (1.0f,0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF,
	    						  0.5f,Animation.RELATIVE_TO_SELF,
	    						  0.5f);
	    		  scale.setDuration(300);
	    		  scale.setFillAfter(true);
	    		  child.startAnimation(scale);
	    	  }
	    	  child.setClickable(false);
	    }
	    changeState() ;
   }
   
	private void changeState() {
		if(mstatus==Status.OPEN){
			 mstatus=Status.CLOSE;
		 }
		 else{
			 mstatus=Status.OPEN;
		 }
	}
 
}
