package com.zykmanhua.app.util;

import com.zykmanhua.app.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Topbar extends RelativeLayout{
	
	//与控件有关的变量
	private Button leftButton;
	private Button rightButton;
	private TextView tvTitle;
	
	//对应atts.xml中声明的那个title有关的自定义属性
	private float titleTextSize;
	private int titleTextColor;
	private String title;
	
	//对应atts.xml中声明的那个left有关的自定义属性
	private int leftTextColor;
	private Drawable leftBackground;
	private String leftText;
	
	//对应atts.xml中声明的那个right有关的自定义属性
	private int rightTextColor;
	private Drawable rightBackground;
	private String rightText;
	
	
	//把一个控件放到Layout里面，我们就需要借助LayoutParam
	private LayoutParams leftParams;
	private LayoutParams rightParams;
	private LayoutParams titleParams;
	
	
	//接口，点击事件，回调
	private topbarClickListener listener;
	
	public interface topbarClickListener{
		public void leftClick();
		public void rightClick();
	}
	
	public void setOnTopbarClickListener(topbarClickListener listener){
		this.listener = listener;
	}
	
	

	
	//构造函数，因为要使用我们自定义的atts.xml，因此要有第二个参数
	@SuppressWarnings("deprecation")
	public Topbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//(1)那么怎么才能获取到我们之前声明好的那些自定义属性呢？？Android已经为我们设计好了API，它通过TypedArray这样的一个数据结构，来存储我们在xml文件中获取到的自定义属性的值。
		//从context中我们调用obtainStyledAttributes()方法，传入两个参数。第一个参数就是构造方法中已经声明好的attrs；第二个参数，我们通过R.styleable来找到我们之前所定义的Topbar
		//这样的话，atts.xml中那一大堆属性就都映射到了ta这个变量中去了，接下来我们就从ta中把这些属性分别取出来。
		TypedArray ta = context.obtainStyledAttributes(attrs , R.styleable.TopBar);
		
		//(2)从下面开始就要把atts.xml中声明的属性，一个个的映射到上面定义的那些private变量中去
		//getColor()中第一个参数，结构是Topbar_属性名的name；第二个参数是默认值
		leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor , 0);
		leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
		leftText = ta.getString(R.styleable.TopBar_leftText);
		
		rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor , 0);
		rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
		rightText = ta.getString(R.styleable.TopBar_rightText);
		
		titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize , 0);
		titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor , 0);
		title = ta.getString(R.styleable.TopBar_title);
		
		//(3)TypedArray用完了之后，别忘了对TypedArray进行资源回收
		ta.recycle();
		
		//(4)初始化3个控件实例，然后把上面写好的自定义属性添加到这几个控件之中
		leftButton = new Button(context);
		rightButton = new Button(context);
		tvTitle = new TextView(context);
		
		leftButton.setTextColor(leftTextColor);
		leftButton.setBackgroundDrawable(leftBackground);
		leftButton.setText(leftText);
		
		rightButton.setTextColor(rightTextColor);
		rightButton.setBackgroundDrawable(rightBackground);
		rightButton.setText(rightText);
		
		tvTitle.setTextSize(titleTextSize);
		tvTitle.setTextColor(titleTextColor);
		tvTitle.setText(title);
		tvTitle.setGravity(Gravity.CENTER); //让标题居中显示
		
		
		//设置整个控件的背景颜色
		setBackgroundColor(0xFFF59563);
		
		
		//(5)到此为止，Topbar中的那些按钮标题已经设置好了我们所需要的文字和颜色、大小。我们接下来需要做的就是把它们放到ViewGroup中，这个是通过addView()方法进行添加的
		leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);  //第一个参数相当于layout_width , 第二个参数相当于layout_height
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT , TRUE);
		addView(leftButton , leftParams); //这样就将leftButton以leftParams的形式加入到了我们的ViewGroup中
		
		rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);  //第一个参数相当于layout_width , 第二个参数相当于layout_height
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , TRUE);
		addView(rightButton , rightParams); //这样就将rightButton以rightParams的形式加入到了我们的ViewGroup中
		
		titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.MATCH_PARENT);  //第一个参数相当于layout_width , 第二个参数相当于layout_height
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT , TRUE);
		addView(tvTitle , titleParams);
		
		
		//(6)为按钮添加点击事件
		leftButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				listener.leftClick(); //这样就成了名副其实的模板了
			}
		});
		
		rightButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				listener.rightClick(); //这样就成了名副其实的模板了
			}
		});
		
	}//构造函数结束
	
	
	
	
	//控制左边的按钮是否显示
	public void setLeftIsVisable(boolean flag){
		if(flag){
			leftButton.setVisibility(View.VISIBLE);
		}
		else{
			leftButton.setVisibility(View.GONE);
		}
	}
	
	
	//控制右边的按钮是否显示
	public void setRightIsVisable(boolean flag){
		if(flag){
			rightButton.setVisibility(View.VISIBLE);
		}
		else{
			rightButton.setVisibility(View.GONE);
		}
	}
	
	
	//设置TopBar的标题
	public void setTopBarTitle(String title) {
		this.title = title;
		tvTitle.setText(title);
	}
	
	//设置TopBar右边按钮的文字
	public void setTopBarRightTitle(String title) {
		rightButton.setText(title);
	}
	
	//设置TopBar右边按钮文字的颜色
	public void setTopBarRightColor(int color) {
		rightButton.setTextColor(color);
	}
	
	
	

}