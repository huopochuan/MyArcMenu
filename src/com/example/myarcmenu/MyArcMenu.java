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
    //�Զ���view
	//Ĭ��λ��
	private Position mposition=Position.RIGHT_BOTTOM;
	
	private Status mstatus=Status.CLOSE;
	
	//Ĭ�ϰ뾶
	private int radius=100;
	//��ǰ״̬
	public enum Status{
		OPEN,CLOSE
	}
	public enum Position{
		LEFT_TOP,LEFT_BOTTOM,RIGHT_TOP,RIGHT_BOTTOM
	}
    public interface OnMenuItemClickListener{
	    	 void onClick(View v,int index);
	 }
	//�ص��ӿ�
	private OnMenuItemClickListener listener;

	
	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		this.listener = listener;
	}

	public MyArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//��dpת��Ϊpx
		radius=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius,
				getResources().getDisplayMetrics());
		//��ȡ�Զ�������
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
		//��뾶ȡֵ 
		radius=a.getDimensionPixelSize(R.styleable.MyArcMenu_radius,
				(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,
						getResources().getDisplayMetrics()));
		//�ͷ���Դ
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
	   //�����Ӳ��ִ�С
	   int count=getChildCount();
	   
	   for(int i=0;i<count;i++){
		   
		   getChildAt(i).measure(0, 0);
	   }
	   
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	
		 //���õ�һ������ť
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
		    //�ڸ������е�λ��
		    menu.layout(left, top, left+menu.getMeasuredWidth(), top+menu.getMeasuredHeight());
		    
		    //��������childλ��
		    if(count-2<=0){
		    	return;
		    }
		    double angle=(double) (Math.PI /2/(count-2));
		    for(int i=0;i<count-1;i++){
		    	 View child=getChildAt(i+1);
		    	 child.setVisibility(View.GONE); //����
	             int cWidth = child.getMeasuredWidth();  
	             
	             int cHeight = child.getMeasuredHeight();  
	             //ע��ĵط�
	             // д��ctop=(int)Math.sin(i*angle)*radius�Ļ�ǰ��Ľ��sin��ǿ��Ϊint;
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
		
		 //��ת����
		
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
			 translate.setFillAfter(true); //���пɶ�û��Ӱ�� �Ƿ�ͣ������λ��
			 //�ӳٲ���ʱ��
			 translate.setStartOffset((i * 100) / (count - 1));  
			rotate.setFillAfter(true);
			 set.setDuration(300);  
			 //���ü�����
			 set.setInterpolator(new OvershootInterpolator(2F));  
		     //�����Ⱥ�˳�����Ϊ����ת���ƶ�
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
					//�Ŵ󶯻�����ɫ���䶯��
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
	    		  //�Ŵ󶯻�
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
