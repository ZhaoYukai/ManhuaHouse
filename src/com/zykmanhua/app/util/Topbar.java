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
	
	//��ؼ��йصı���
	private Button leftButton;
	private Button rightButton;
	private TextView tvTitle;
	
	//��Ӧatts.xml���������Ǹ�title�йص��Զ�������
	private float titleTextSize;
	private int titleTextColor;
	private String title;
	
	//��Ӧatts.xml���������Ǹ�left�йص��Զ�������
	private int leftTextColor;
	private Drawable leftBackground;
	private String leftText;
	
	//��Ӧatts.xml���������Ǹ�right�йص��Զ�������
	private int rightTextColor;
	private Drawable rightBackground;
	private String rightText;
	
	
	//��һ���ؼ��ŵ�Layout���棬���Ǿ���Ҫ����LayoutParam
	private LayoutParams leftParams;
	private LayoutParams rightParams;
	private LayoutParams titleParams;
	
	
	//�ӿڣ�����¼����ص�
	private topbarClickListener listener;
	
	public interface topbarClickListener{
		public void leftClick();
		public void rightClick();
	}
	
	public void setOnTopbarClickListener(topbarClickListener listener){
		this.listener = listener;
	}
	
	

	
	//���캯������ΪҪʹ�������Զ����atts.xml�����Ҫ�еڶ�������
	@SuppressWarnings("deprecation")
	public Topbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//(1)��ô��ô���ܻ�ȡ������֮ǰ�����õ���Щ�Զ��������أ���Android�Ѿ�Ϊ������ƺ���API����ͨ��TypedArray������һ�����ݽṹ�����洢������xml�ļ��л�ȡ�����Զ������Ե�ֵ��
		//��context�����ǵ���obtainStyledAttributes()����������������������һ���������ǹ��췽�����Ѿ������õ�attrs���ڶ�������������ͨ��R.styleable���ҵ�����֮ǰ�������Topbar
		//�����Ļ���atts.xml����һ������ԾͶ�ӳ�䵽��ta���������ȥ�ˣ����������Ǿʹ�ta�а���Щ���Էֱ�ȡ������
		TypedArray ta = context.obtainStyledAttributes(attrs , R.styleable.TopBar);
		
		//(2)�����濪ʼ��Ҫ��atts.xml�����������ԣ�һ������ӳ�䵽���涨�����Щprivate������ȥ
		//getColor()�е�һ���������ṹ��Topbar_��������name���ڶ���������Ĭ��ֵ
		leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor , 0);
		leftBackground = ta.getDrawable(R.styleable.TopBar_leftBackground);
		leftText = ta.getString(R.styleable.TopBar_leftText);
		
		rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor , 0);
		rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
		rightText = ta.getString(R.styleable.TopBar_rightText);
		
		titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize , 0);
		titleTextColor = ta.getColor(R.styleable.TopBar_titleTextColor , 0);
		title = ta.getString(R.styleable.TopBar_title);
		
		//(3)TypedArray������֮�󣬱����˶�TypedArray������Դ����
		ta.recycle();
		
		//(4)��ʼ��3���ؼ�ʵ����Ȼ�������д�õ��Զ���������ӵ��⼸���ؼ�֮��
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
		tvTitle.setGravity(Gravity.CENTER); //�ñ��������ʾ
		
		
		//���������ؼ��ı�����ɫ
		setBackgroundColor(0xFFF59563);
		
		
		//(5)����Ϊֹ��Topbar�е���Щ��ť�����Ѿ����ú�����������Ҫ�����ֺ���ɫ����С�����ǽ�������Ҫ���ľ��ǰ����Ƿŵ�ViewGroup�У������ͨ��addView()����������ӵ�
		leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);  //��һ�������൱��layout_width , �ڶ��������൱��layout_height
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT , TRUE);
		addView(leftButton , leftParams); //�����ͽ�leftButton��leftParams����ʽ���뵽�����ǵ�ViewGroup��
		
		rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);  //��һ�������൱��layout_width , �ڶ��������൱��layout_height
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , TRUE);
		addView(rightButton , rightParams); //�����ͽ�rightButton��rightParams����ʽ���뵽�����ǵ�ViewGroup��
		
		titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.MATCH_PARENT);  //��һ�������൱��layout_width , �ڶ��������൱��layout_height
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT , TRUE);
		addView(tvTitle , titleParams);
		
		
		//(6)Ϊ��ť��ӵ���¼�
		leftButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				listener.leftClick(); //�����ͳ���������ʵ��ģ����
			}
		});
		
		rightButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				listener.rightClick(); //�����ͳ���������ʵ��ģ����
			}
		});
		
	}//���캯������
	
	
	
	
	//������ߵİ�ť�Ƿ���ʾ
	public void setLeftIsVisable(boolean flag){
		if(flag){
			leftButton.setVisibility(View.VISIBLE);
		}
		else{
			leftButton.setVisibility(View.GONE);
		}
	}
	
	
	//�����ұߵİ�ť�Ƿ���ʾ
	public void setRightIsVisable(boolean flag){
		if(flag){
			rightButton.setVisibility(View.VISIBLE);
		}
		else{
			rightButton.setVisibility(View.GONE);
		}
	}
	
	
	//����TopBar�ı���
	public void setTopBarTitle(String title) {
		this.title = title;
		tvTitle.setText(title);
	}
	
	//����TopBar�ұ߰�ť������
	public void setTopBarRightTitle(String title) {
		rightButton.setText(title);
	}
	
	//����TopBar�ұ߰�ť���ֵ���ɫ
	public void setTopBarRightColor(int color) {
		rightButton.setTextColor(color);
	}
	
	
	

}