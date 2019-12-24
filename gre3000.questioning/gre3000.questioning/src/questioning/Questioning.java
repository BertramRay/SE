

/*
 * 需解决的问题：
 * 1.题目跳转时时间的错乱（√）
 * 2.分离初始化和执行代码（√）
 * 2.套题模式的实现
 * 	2.1不同显示
 * 	2.2套题中的信息暂存
 * 3.不同刷题模式的不同显示
 * 	3.1菜单栏
 * 	3.2提交
 * 4.单题模式下下一题与提交（√）
 * 5.多皮肤的实现
 * 6.看错题模式
 * 7.优化读取json文件的方法（√）
 * 8.收藏显示（√）
 * 9.设置写入保存（√）
 */

/*
 * 套题：
 * 1-2：单空五选一
 * 3-4：双空
 * 5-6：三空
 * 7-10：单空六选二
 */

package questioning;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.alibaba.fastjson.*;

/*
 * 答题页面的显示
 * 通过操作与后端进行联系
 */
public class Questioning {
	/*
	 * 一些常量参数
	 */
	private final int PANE_WIDTH = 1000;			//窗口的宽度
	private final int PANE_HEIGHT = 750;			//窗口的高度
	private final int FUNTIONAL_BAR_HEIGHT = 75;		//功能栏的高度
	private final int FUNCTIONAL_BTN_WIDTH = 90;		//功能按钮的宽度
	private final int FUNCTIONAL_BTN_HEIGHT = 40;		//功能按钮的高度
	private final int FUNCTIONAL_BTN_GAP = 125;		//功能区按钮之间的距离
	private final int FIRST_FUNCTIONAL_BTN_X = PANE_WIDTH - 4 * FUNCTIONAL_BTN_GAP;	//功能区第一个按钮的横坐标
	private final int FIRST_FUNCTIONAL_BTN_Y = 18;		//功能区第一个按钮的纵坐标
	private final int INFORMATIONAL_BAR_HEIGHT = 35;	//信息栏的高度
	private final int BLANK_AREA_WIDTH = 100;			//题目区两侧的留白宽度
	private final int CHOICE_WIDTH = 260;			//选项宽度
	private final int CHOICE_HEIGHT = 32;			//选项高度
	private final int CHOICE_GAP = 10;				//选项间距
	private final Color QUIT_BTN_COLOR = new Color(140,6,137);			//退出按钮的颜色
	private final Color MARK_CHECKBOX_COLOR = new Color(60,60,60);		//收藏复选框的颜色
	private final Color PAGE_BTN_COLOR = new Color(40,85,160);			//返回上一题、下一题按钮的颜色
	private final Color SUBMIT_BTN_COLOR = new Color(85,180,10);			//提交按钮的颜色
	private final Color VIEW_EXPLANATION_BTN_COLOR = new Color(220,45,45);	//查看解析按钮的颜色
	private final Color INFORMATION_BAR_COLOR = new Color(250,170,155);	//信息栏底色
	private final Color BG_COLOR = Color.WHITE;							//背景颜色
	private final Font QUESTION_FONT = new Font("Arial",Font.PLAIN,18);	//题目所用字体
	
	//与后端格式配合的参数写法
	String[] QTYPE = {"单空","六选二","双空","三空"};
	String[] DIF = {"easy","median","hard"};
	String[] DONE = {"undone","done"};
	
	/*
	 * 一些内部参数
	 */
	private int second = 0;		//计时器上的秒
	private int minute = 0;		//计时器上的分
	private JButton sinChoosen1 = null;	//单空五选一或双空、三空第一空的已选答案
	private JButton sinChoosen2 = null;	//双空或三空的第二空的已选答案
	private JButton sinChoosen3 = null;	//三空的第三空的已选答案
	
	/*
	 * 一些外部参数
	 */
	private int practiceMode1;				//刷题模式设置1，0-单题模式，1-套题模式
	private int practiceMode2;				//刷题模式设置2，0-新题，1-错题重测
	private int difficulty;					//题目难度设置，0-简单，1-适中，2-困难
	private int questionType;				//题型设置，0-单空五选一，1-单空六选二，2-双空，3-三空
	public int[] qNo;						//该条件下的所有满足条件题目的题号
	public int qNum = 0;					//满足条件的题目数
	public int[] collectionNo;				//已收藏题目的题号
	public int collectionNum;				//收藏题目的数量
	private boolean done;					//本题是否已做过
	private boolean marked;					//本题是否被收藏
	private boolean timeHidden;				//时间是否被隐藏
	private boolean explanationShowing;		//是否显示解释
	private boolean submitted = false;		//本题是否已提交
	private int section;					//本题属于第几Section
	private int innerQuestionNo;			//本题是本Section的第几题
	private int questionNo;					//本题的大题号
	private final int SECTION_COUNT = 100;	//本题库一共有多少个Section
	private final int QUESTION_COUNT = 10;	//本Section一共有多少题
	private String foreQuestionStem;		//大题干
	private String questionStem;			//题干
	private String choiceA;					//选项（最多9个）
	private String choiceB;
	private String choiceC;
	private String choiceD;
	private String choiceE;
	private String choiceF;
	private String choiceG;
	private String choiceH;
	private String choiceI;
	private String answer;					//后端读取的标准答案
	private String explanation;				//题目解析
	private int skin;						//皮肤，0-默认皮肤（ETS风格），1-
	
	/*
	 * 布局对象
	 */
	JFrame frame;							//页面
	JPanel panel;							//布局对象
	
	/*
	 * 菜单栏中的对象
	 */
	private JMenuBar menuBar;				//菜单栏
	private JMenu practiceModeMenu;			//练习模式设置
	private JMenu practiceMode1Menu;		//练习模式设置1
	private JMenu practiceMode2Menu;		//练习模式设置2
	private JMenu difficultyMenu;			//难度设置
	private JMenu questionTypeMenu;			//题型设置
	private JMenu view;						//查看错题本或收藏夹
	private JMenuItem bySectionMenuItem;	//套题模式
	private JMenuItem bySingleMenuItem;		//单题模式
	private JMenuItem byNewMenuItem;		//新题
	private JMenuItem byWrongMenuItem;		//错题重测
	private JMenuItem oneBlankOneChoiceMenuItem;	//题型-单空五选一
	private JMenuItem oneBlankTwoChoiceMenuItem;	//题型-单空六选二
	private JMenuItem twoBlankMenuItem;				//题型-双空
	private JMenuItem threeBlankMenuItem;			//题型-三空
	private JMenuItem easyMenuItem;					//难度-简单
	private JMenuItem medianMenuItem;				//难度-适中
	private JMenuItem hardMenuItem;					//难度-困难
	private JMenuItem markedCollection;				//收藏夹
	private JMenuItem wrongCollection;				//错题本
	
	/*
	 * 操作栏中的对象
	 */
	private JButton quitBtn;		//退出按钮
	private JButton backBtn;		//返回上一题按钮
	private JButton nextBtn;		//下一题按钮（并保存本题作答）
	private JCheckBox markCheckBox;	//收藏复选框
	private JLabel timeLabel;		//计时显示（不同模式显示不同）
	private JButton timeHiddingBtn;	//隐藏时间按钮
	private Timer timer;			//计时器
	
	/*
	 * 信息栏中的对象
	 */
	private JLabel sectionInformationLabel;
	private JLabel numInformationLabel;
	TimeListener timeListener;
	
	/*
	 * 答题区中的对象
	 */
	//题干
	JTextArea foreQuestionStemArea;		//大题干
	JTextArea questionStemArea;			//小题干
	JPanel questionArea;				//题目区面板
	JPanel choiceArea;					//选项区面板
	
	//单空五选一 & 双空 & 三空 的选项
	private JButton singleChoiceBtnA;
	private JButton singleChoiceBtnB;
	private JButton singleChoiceBtnC;
	private JButton singleChoiceBtnD;
	private JButton singleChoiceBtnE;
	private JButton singleChoiceBtnF;
	private JButton singleChoiceBtnG;
	private JButton singleChoiceBtnH;
	private JButton singleChoiceBtnI;
	
	//单空六选二的选项
	private JCheckBox multiChoiceBtnA;
	private JCheckBox multiChoiceBtnB;
	private JCheckBox multiChoiceBtnC;
	private JCheckBox multiChoiceBtnD;
	private JCheckBox multiChoiceBtnE;
	private JCheckBox multiChoiceBtnF;
	
	//操作按钮
	private JButton submitBtn;			//提交按钮
	private JButton viewExplanationBtn;	//查看解析按钮
	
	//解析文本框
	private JTextArea explanationArea;
	
	/*
	 * 创建并显示答题界面
	 * 提供与后端的交互界面
	 */
	public void questioningPage() {
		//好看点的风格
		/*
		 * JFrame.setDefaultLookAndFeelDecorated(true);
		 *JDialog.setDefaultLookAndFeelDecorated(true);
		 *try {
	     *    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	     *} catch (Exception evt) {}
		 */
		
		//创建JFrame并进行基本设置
		frame = new JFrame("GRE 要你命3000");
		frame.setSize(PANE_WIDTH,PANE_HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//设置菜单栏
		menuBar = new JMenuBar();
		setMenuBar(menuBar);
		
		//创建面板并添加
		panel = new JPanel();
		frame.add(panel);
		
		//调用类内方法布置面板
		placeComponent(panel);
		
		//使面板可见
		frame.setVisible(true);
	}
	
	/*
	 * 定义并布置菜单栏
	 */
	public void setMenuBar(JMenuBar menuBar) {
		//菜单栏各对象的定义
		practiceModeMenu = new JMenu("练习模式");
		practiceMode1Menu = new JMenu("模式设置1");
		practiceMode2Menu = new JMenu("模式设置2");
		difficultyMenu = new JMenu("难度设置");
		questionTypeMenu = new JMenu("题型设置");
		view = new JMenu("查看");
		bySectionMenuItem = new JMenuItem("套题模式");
		bySingleMenuItem = new JMenuItem("单题模式");
		byNewMenuItem = new JMenuItem("新题");
		byWrongMenuItem = new JMenuItem("错题重测");
		oneBlankOneChoiceMenuItem = new JMenuItem("单空五选一");
		oneBlankTwoChoiceMenuItem = new JMenuItem("单空五选二");
		twoBlankMenuItem = new JMenuItem("双空");
		threeBlankMenuItem = new JMenuItem("三空");
		easyMenuItem = new JMenuItem("简单");
		medianMenuItem = new JMenuItem("中等");
		hardMenuItem = new JMenuItem("困难");
		markedCollection = new JMenuItem("收藏夹");
		wrongCollection = new JMenuItem("错题本");

		//布置菜单栏
		menuBar.add(practiceModeMenu);
		menuBar.add(difficultyMenu);
		menuBar.add(questionTypeMenu);
		menuBar.add(view);
		practiceModeMenu.add(practiceMode1Menu);
		practiceModeMenu.add(practiceMode2Menu);
		practiceMode1Menu.add(bySectionMenuItem);
		practiceMode1Menu.add(bySingleMenuItem);
		practiceMode2Menu.add(byNewMenuItem);
		practiceMode2Menu.add(byWrongMenuItem);
		difficultyMenu.add(easyMenuItem);
		difficultyMenu.add(medianMenuItem);
		difficultyMenu.add(hardMenuItem);
		questionTypeMenu.add(oneBlankOneChoiceMenuItem);
		questionTypeMenu.add(oneBlankTwoChoiceMenuItem);
		questionTypeMenu.add(twoBlankMenuItem);
		questionTypeMenu.add(threeBlankMenuItem);
		view.add(markedCollection);
		view.add(wrongCollection);

		//关联监听器
		bySectionMenuItem.addActionListener(new BySectionMenuListener());
		bySingleMenuItem.addActionListener(new BySingleMenuListener());
		byNewMenuItem.addActionListener(new ByNewMenuListener());
		byWrongMenuItem.addActionListener(new ByWrongMenuListener());
		easyMenuItem.addActionListener(new EasyMenuListener());
		medianMenuItem.addActionListener(new MedianMenuListener());
		hardMenuItem.addActionListener(new HardMenuListener());
		oneBlankOneChoiceMenuItem.addActionListener(new OneBlankOneChoiceMenuListener());
		oneBlankTwoChoiceMenuItem.addActionListener(new OneBlankTwoChoiceMenuListener());
		twoBlankMenuItem.addActionListener(new TwoBlankMenuListener());
		threeBlankMenuItem.addActionListener(new ThreeBlankMenuListener());
		markedCollection.addActionListener(new MarkedCollectionListener());
		wrongCollection.addActionListener(new WrongCollectionListener());

		//显示菜单栏
		frame.setJMenuBar(menuBar);
	}
	
	/*
	 * 此方法用于布置面板上的元件
	 */
	private void placeComponent(JPanel p) {
		/*
		 * 整体布局，采取类似BorderLayout的布局方法，
		 * North区放功能栏和信息栏，
		 * Center区放答题区
		 * East和West放置一定宽度的留白
		 * South不用
		 */
		
		p.setLayout(null);
		p.setBackground(BG_COLOR);
		JPanel functionalAndInformationalArea = new JPanel();	//功能信息区
		questionArea = new JPanel();						//答题区
		JPanel blankAreaLeft = new JPanel();					//左侧留白区
		JPanel blankAreaRight = new JPanel();					//右侧留白区
		
		//设置各子panel的大小和位置
		functionalAndInformationalArea.setBounds(0, 0, PANE_WIDTH, FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT);
		questionArea.setBounds(BLANK_AREA_WIDTH,FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT,
				PANE_WIDTH - 2 * BLANK_AREA_WIDTH, PANE_HEIGHT - FUNTIONAL_BAR_HEIGHT - INFORMATIONAL_BAR_HEIGHT);
		blankAreaLeft.setBounds(0, FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT,
				BLANK_AREA_WIDTH, PANE_HEIGHT - FUNTIONAL_BAR_HEIGHT - INFORMATIONAL_BAR_HEIGHT);
		blankAreaRight.setBounds(PANE_WIDTH - BLANK_AREA_WIDTH, FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT,
				BLANK_AREA_WIDTH, PANE_HEIGHT - FUNTIONAL_BAR_HEIGHT - INFORMATIONAL_BAR_HEIGHT);
		
		//测试用
		/*
		functionalAndInformationalArea.setBackground(Color.RED);
		questionArea.setBackground(Color.GREEN);
		blankAreaLeft.setBackground(Color.CYAN);
		blankAreaRight.setBackground(Color.CYAN);
		*/
		
		//将各子panel添加到p中
		p.add(functionalAndInformationalArea);
		p.add(questionArea);
		p.add(blankAreaLeft);
		p.add(blankAreaRight);
		
		/*
		 * 功能信息区的布局
		 * 采取FlowLayout的办法
		 * 上下依次放置功能栏和信息栏
		 */
		functionalAndInformationalArea.setLayout(null);
		JPanel functionalBar = new JPanel();					//功能栏
		JPanel informationalBar = new JPanel();					//信息栏
		
		//设置各子panel的大小
		functionalBar.setBounds(0, 0, PANE_WIDTH, FUNTIONAL_BAR_HEIGHT);
		informationalBar.setBounds(0, FUNTIONAL_BAR_HEIGHT, PANE_WIDTH, INFORMATIONAL_BAR_HEIGHT);
		
		//将各子panel添加进去
		functionalAndInformationalArea.add(functionalBar);
		functionalAndInformationalArea.add(informationalBar);
		
		/*
		 * 功能栏的布局
		 * 左侧是产品的logo
		 * 右侧依次分布退出、收藏、返回、下一题四个按钮
		 */
		//各部件的定义
		ImageIcon logoImage = new ImageIcon("img/logo.png");
		JLabel logo = new JLabel(logoImage);	//产品logo,尺寸257*75
		quitBtn = new JButton("Quit");				//退出按钮
		markCheckBox = new JCheckBox("Mark",marked);//收藏按钮（以复选框来实现）
		backBtn = new JButton("Back");				//返回上一题按钮
		nextBtn = new JButton("Next");				//下一题按钮
		
		//设置各部件的颜色、字体
		functionalBar.setBackground(Color.black);
		functionalBar.setLayout(null);
		Font font = new Font("Arial",Font.BOLD,18);	//功能栏字体
		
		quitBtn.setBackground(QUIT_BTN_COLOR);
		quitBtn.setForeground(Color.white);
		quitBtn.setFont(font);
		quitBtn.setBorderPainted(false);
		quitBtn.setFocusPainted(false);
		
		markCheckBox.setBackground(MARK_CHECKBOX_COLOR);
		markCheckBox.setForeground(Color.white);
		markCheckBox.setFont(font);
		markCheckBox.setBorderPainted(false);
		markCheckBox.setFocusPainted(false);
		
		backBtn.setBackground(PAGE_BTN_COLOR);
		backBtn.setForeground(Color.white);
		backBtn.setFont(font);
		backBtn.setBorderPainted(false);
		backBtn.setFocusPainted(false);
		
		nextBtn.setBackground(PAGE_BTN_COLOR);
		nextBtn.setForeground(Color.white);
		nextBtn.setFont(font);
		nextBtn.setBorderPainted(false);
		nextBtn.setFocusPainted(false);
		
		//将各部件加进功能栏
		functionalBar.add(logo);
		functionalBar.add(quitBtn);
		functionalBar.add(markCheckBox);
		functionalBar.add(backBtn);
		functionalBar.add(nextBtn);
		logo.setBounds(0,0,257,75);
		quitBtn.setBounds(FIRST_FUNCTIONAL_BTN_X, FIRST_FUNCTIONAL_BTN_Y, FUNCTIONAL_BTN_WIDTH, FUNCTIONAL_BTN_HEIGHT);
		markCheckBox.setBounds(FIRST_FUNCTIONAL_BTN_X + FUNCTIONAL_BTN_GAP, FIRST_FUNCTIONAL_BTN_Y, FUNCTIONAL_BTN_WIDTH, FUNCTIONAL_BTN_HEIGHT);
		backBtn.setBounds(FIRST_FUNCTIONAL_BTN_X + 2 * FUNCTIONAL_BTN_GAP, FIRST_FUNCTIONAL_BTN_Y, FUNCTIONAL_BTN_WIDTH, FUNCTIONAL_BTN_HEIGHT);
		nextBtn.setBounds(FIRST_FUNCTIONAL_BTN_X + 3 * FUNCTIONAL_BTN_GAP, FIRST_FUNCTIONAL_BTN_Y, FUNCTIONAL_BTN_WIDTH, FUNCTIONAL_BTN_HEIGHT);
		
		//关联监听器
		quitBtn.addActionListener(new QuitBtnListener());
		markCheckBox.addChangeListener(new MarkBtnListener());
		backBtn.addActionListener(new BackBtnListener());
		nextBtn.addActionListener(new NextBtnListener());
		
		/*
		 * 信息栏的布局
		 * 左侧是答题信息Label
		 * 右侧是时间信息Label
		 */
		informationalBar.setBackground(INFORMATION_BAR_COLOR);	//设置背景颜色
		//答题信息Label定义与设置
		sectionInformationLabel = new JLabel("Section " + section + " of " + SECTION_COUNT);		//Section信息
		sectionInformationLabel.setFont(new Font("Comic Sans MS",Font.BOLD,14));
		numInformationLabel = new JLabel("| Question " + innerQuestionNo + " of " + QUESTION_COUNT);	//题号信息
		numInformationLabel.setFont(new Font("Comic Sans MS",Font.PLAIN,14));
		
		//答题信息Label加入信息栏中
		informationalBar.add(sectionInformationLabel);
		informationalBar.add(numInformationLabel);
		informationalBar.setLayout(null);
		sectionInformationLabel.setBounds(15, 0, 130, 35);
		numInformationLabel.setBounds(140, 0, 125, 35);
		
		//时间信息Label的定义与设置
		timeLabel = new JLabel(minute + ":" + second);
		timeLabel.setBounds(840,0,100,35);
		timeLabel.setFont(new Font("Comic Sans MS",Font.BOLD,20));
		
		//隐藏时间按钮的定义与设置
		timeHiddingBtn = new JButton();
		timeHiddingBtn.setFont(new Font("Comic Sans MS",Font.PLAIN,15));
		timeHiddingBtn.setContentAreaFilled(false);		//透明
		timeHiddingBtn.setBorderPainted(false);
		timeHiddingBtn.setFocusPainted(false);
		
		//Label与Button加入信息栏
		informationalBar.add(timeHiddingBtn);
		informationalBar.add(timeLabel);
		timeHiddingBtn.setBounds(870,0,130,35);
		
		//timeHidding决定时间显示与否，用户按下隐藏时间按钮时其值翻转（在timeHiddingButton的监听器中实现）
		if(!timeHidden) {
			timeLabel.setVisible(true);
			timeHiddingBtn.setText("| Hide Time");
		}else {
			timeLabel.setVisible(false);
			timeHiddingBtn.setText("| Show Time");
		}
		
		//按钮关联监听器
		timeHiddingBtn.addActionListener(new TimeHiddingBtnListener());
		
		//计时器，每秒重置一次timeLabel的显示
		timeListener = new TimeListener();
		timer = new Timer(1000,timeListener);
		timer.start();
		
		/*
		 * 答题区的布置
		 * 从上往下分别是
		 * 大题干区域-JTextArea
		 * 题干区域-JTextArea
		 * 选项区-单空五选一/双空/三空-JRadioButton;单空六选二-JCheckBox
		 * 操作按钮区，有提交按钮和查看解释按钮
		 * 解释显示区-JTextArea
		 */
		
		//答题区整体设置
		questionArea.setLayout(new FlowLayout());
		questionArea.setBackground(BG_COLOR);
		
		//大题干
		foreQuestionStemArea = new JTextArea();
		foreQuestionStemArea.setText("\n" + foreQuestionStem + "\n");
		foreQuestionStemArea.setLineWrap(true);			//自动换行
		foreQuestionStemArea.setWrapStyleWord(true);	//换行不断字
		foreQuestionStemArea.setColumns(60);
		foreQuestionStemArea.setFont(new Font("Arial",Font.PLAIN,15));
		foreQuestionStemArea.setBackground(Color.GRAY);
		foreQuestionStemArea.setEditable(false);		//不可更改
		questionArea.add(foreQuestionStemArea);
		
		//小题干
		questionStemArea = new JTextArea();
		questionStemArea.setText(questionStem);
		questionStemArea.setLineWrap(true);				//自动换行
		questionStemArea.setWrapStyleWord(true);		//换行不断字
		questionStemArea.setColumns(52);
		questionStemArea.setFont(QUESTION_FONT);
		questionStemArea.setEditable(false);			//不可更改
		questionArea.add(questionStemArea);
		
		//选项区
		choiceArea = new JPanel();
		choiceArea.setBackground(BG_COLOR);
		singleChoiceBtnA = new JButton();
		singleChoiceBtnB = new JButton();
		singleChoiceBtnC = new JButton();
		singleChoiceBtnD = new JButton();
		singleChoiceBtnE = new JButton();
		singleChoiceBtnF = new JButton();
		singleChoiceBtnG = new JButton();
		singleChoiceBtnH = new JButton();
		singleChoiceBtnI = new JButton();
		//根据题型来安排布局
		if(questionType==0) {			//单空五选一，为了确保按钮出现在下方，这里采用auxilliaryLabel占据空间
			//辅助占空间的label，一左一右，choiceArea居中
			JLabel auxilliaryLabel1 = new JLabel("");
			JLabel auxilliaryLabel2 = new JLabel("");
			auxilliaryLabel1.setPreferredSize(new Dimension(CHOICE_WIDTH,5 * CHOICE_HEIGHT));
			auxilliaryLabel2.setPreferredSize(new Dimension(CHOICE_WIDTH,5 * CHOICE_HEIGHT));
			
			singleChoiceBtnA.setText(choiceA);
			singleChoiceBtnB.setText(choiceB);
			singleChoiceBtnC.setText(choiceC);
			singleChoiceBtnD.setText(choiceD);
			singleChoiceBtnE.setText(choiceE);
			
			singleChoiceBtnA.setFocusPainted(false);
			singleChoiceBtnB.setFocusPainted(false);
			singleChoiceBtnC.setFocusPainted(false);
			singleChoiceBtnD.setFocusPainted(false);
			singleChoiceBtnE.setFocusPainted(false);
			
			singleChoiceBtnA.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnB.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnC.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnD.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnE.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			
			singleChoiceBtnA.setBackground(BG_COLOR);
			singleChoiceBtnB.setBackground(BG_COLOR);
			singleChoiceBtnC.setBackground(BG_COLOR);
			singleChoiceBtnD.setBackground(BG_COLOR);
			singleChoiceBtnE.setBackground(BG_COLOR);
			
			singleChoiceBtnA.setFont(QUESTION_FONT);
			singleChoiceBtnB.setFont(QUESTION_FONT);
			singleChoiceBtnC.setFont(QUESTION_FONT);
			singleChoiceBtnD.setFont(QUESTION_FONT);
			singleChoiceBtnE.setFont(QUESTION_FONT);
			
			choiceArea.setLayout(new GridLayout(5,1,CHOICE_GAP,0));	//采用GridLayout，保证整齐布局
			questionArea.add(auxilliaryLabel1);
			questionArea.add(choiceArea);
			choiceArea.add(singleChoiceBtnA);
			choiceArea.add(singleChoiceBtnB);
			choiceArea.add(singleChoiceBtnC);
			choiceArea.add(singleChoiceBtnD);
			choiceArea.add(singleChoiceBtnE);
			questionArea.add(auxilliaryLabel2);
		}else if(questionType==1) {			//单空六选二，为了确保按钮出现在下方，这里采用auxilliaryLabel占据空间
			//辅助占空间的label，一左一右，choiceArea居中
			JLabel auxilliaryLabel1 = new JLabel("");
			JLabel auxilliaryLabel2 = new JLabel("");
			auxilliaryLabel1.setPreferredSize(new Dimension(CHOICE_WIDTH,6 * CHOICE_HEIGHT));
			auxilliaryLabel2.setPreferredSize(new Dimension(CHOICE_WIDTH,6 * CHOICE_HEIGHT));
			
			//替换复选框图标
			ImageIcon checked = new ImageIcon("img/checked.png");
			ImageIcon unchecked = new ImageIcon("img/unchecked.png");
			
			multiChoiceBtnA = new JCheckBox(choiceA,unchecked);
			multiChoiceBtnB = new JCheckBox(choiceB,unchecked);
			multiChoiceBtnC = new JCheckBox(choiceC,unchecked);
			multiChoiceBtnD = new JCheckBox(choiceD,unchecked);
			multiChoiceBtnE = new JCheckBox(choiceE,unchecked);
			multiChoiceBtnF = new JCheckBox(choiceF,unchecked);
			
			multiChoiceBtnA.setSelectedIcon(checked);
			multiChoiceBtnB.setSelectedIcon(checked);
			multiChoiceBtnC.setSelectedIcon(checked);
			multiChoiceBtnD.setSelectedIcon(checked);
			multiChoiceBtnE.setSelectedIcon(checked);
			multiChoiceBtnF.setSelectedIcon(checked);
			
			multiChoiceBtnA.setFocusPainted(false);
			multiChoiceBtnB.setFocusPainted(false);
			multiChoiceBtnC.setFocusPainted(false);
			multiChoiceBtnD.setFocusPainted(false);
			multiChoiceBtnE.setFocusPainted(false);
			multiChoiceBtnF.setFocusPainted(false);
			
			multiChoiceBtnA.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			multiChoiceBtnB.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			multiChoiceBtnC.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			multiChoiceBtnD.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			multiChoiceBtnE.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			multiChoiceBtnF.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			
			multiChoiceBtnA.setBackground(BG_COLOR);
			multiChoiceBtnB.setBackground(BG_COLOR);
			multiChoiceBtnC.setBackground(BG_COLOR);
			multiChoiceBtnD.setBackground(BG_COLOR);
			multiChoiceBtnE.setBackground(BG_COLOR);
			multiChoiceBtnF.setBackground(BG_COLOR);
			
			multiChoiceBtnA.setFont(QUESTION_FONT);
			multiChoiceBtnB.setFont(QUESTION_FONT);
			multiChoiceBtnC.setFont(QUESTION_FONT);
			multiChoiceBtnD.setFont(QUESTION_FONT);
			multiChoiceBtnE.setFont(QUESTION_FONT);
			multiChoiceBtnF.setFont(QUESTION_FONT);
			
			choiceArea.setLayout(new GridLayout(6,1,10,0));		//采取GridLayout布局，确保整齐
			questionArea.add(auxilliaryLabel1);
			questionArea.add(choiceArea);
			choiceArea.add(multiChoiceBtnA);
			choiceArea.add(multiChoiceBtnB);
			choiceArea.add(multiChoiceBtnC);
			choiceArea.add(multiChoiceBtnD);
			choiceArea.add(multiChoiceBtnE);
			choiceArea.add(multiChoiceBtnF);
			questionArea.add(auxilliaryLabel2);
		}else if(questionType==2) {			//双空
			//辅助占空间的Label，其中Label1、Label2占据在选项上方“Blank(i)”、“Blank(ii)”标签的两侧，Label3、Label4占据选项区的两侧
			JLabel auxilliaryLabel1 = new JLabel("");
			JLabel auxilliaryLabel2 = new JLabel("");
			JLabel auxilliaryLabel3 = new JLabel("");
			JLabel auxilliaryLabel4 = new JLabel("");
			auxilliaryLabel1.setPreferredSize(new Dimension(CHOICE_WIDTH/3,CHOICE_HEIGHT));
			auxilliaryLabel2.setPreferredSize(new Dimension(CHOICE_WIDTH/3,CHOICE_HEIGHT));
			auxilliaryLabel3.setPreferredSize(new Dimension(CHOICE_WIDTH/3,3 * CHOICE_HEIGHT));
			auxilliaryLabel4.setPreferredSize(new Dimension(CHOICE_WIDTH/3,3 * CHOICE_HEIGHT));
			
			singleChoiceBtnA.setText(choiceA);
			singleChoiceBtnB.setText(choiceB);
			singleChoiceBtnC.setText(choiceC);
			singleChoiceBtnD.setText(choiceD);
			singleChoiceBtnE.setText(choiceE);
			singleChoiceBtnF.setText(choiceF);
			
			singleChoiceBtnA.setFocusPainted(false);
			singleChoiceBtnB.setFocusPainted(false);
			singleChoiceBtnC.setFocusPainted(false);
			singleChoiceBtnD.setFocusPainted(false);
			singleChoiceBtnE.setFocusPainted(false);
			singleChoiceBtnF.setFocusPainted(false);
			
			singleChoiceBtnA.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnB.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnC.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnD.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnE.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnF.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			
			singleChoiceBtnA.setBackground(BG_COLOR);
			singleChoiceBtnB.setBackground(BG_COLOR);
			singleChoiceBtnC.setBackground(BG_COLOR);
			singleChoiceBtnD.setBackground(BG_COLOR);
			singleChoiceBtnE.setBackground(BG_COLOR);
			singleChoiceBtnF.setBackground(BG_COLOR);
			
			singleChoiceBtnA.setFont(QUESTION_FONT);
			singleChoiceBtnB.setFont(QUESTION_FONT);
			singleChoiceBtnC.setFont(QUESTION_FONT);
			singleChoiceBtnD.setFont(QUESTION_FONT);
			singleChoiceBtnE.setFont(QUESTION_FONT);
			singleChoiceBtnF.setFont(QUESTION_FONT);
			
			//选项分组提示标签“Blank(i)”、“Blank(ii)”
			JLabel blank1Label = new JLabel("Blank(i)",JLabel.CENTER);
			JLabel blank2Label = new JLabel("Blank(ii)",JLabel.CENTER);
			blank1Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank2Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank1Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			blank2Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			
			choiceArea.setLayout(new GridLayout(3,2,CHOICE_GAP,0));
			questionArea.add(auxilliaryLabel1);
			questionArea.add(blank1Label);
			questionArea.add(blank2Label);
			questionArea.add(auxilliaryLabel2);
			questionArea.add(auxilliaryLabel3);
			questionArea.add(choiceArea);
			questionArea.add(auxilliaryLabel4);
			choiceArea.add(singleChoiceBtnA);
			choiceArea.add(singleChoiceBtnD);
			choiceArea.add(singleChoiceBtnB);
			choiceArea.add(singleChoiceBtnE);
			choiceArea.add(singleChoiceBtnC);
			choiceArea.add(singleChoiceBtnF);
		}else {						//三空
			singleChoiceBtnA.setText(choiceA);
			singleChoiceBtnB.setText(choiceB);
			singleChoiceBtnC.setText(choiceC);
			singleChoiceBtnD.setText(choiceD);
			singleChoiceBtnE.setText(choiceE);
			singleChoiceBtnF.setText(choiceF);
			singleChoiceBtnG.setText(choiceG);
			singleChoiceBtnH.setText(choiceH);
			singleChoiceBtnI.setText(choiceI);
			
			singleChoiceBtnA.setFocusPainted(false);
			singleChoiceBtnB.setFocusPainted(false);
			singleChoiceBtnC.setFocusPainted(false);
			singleChoiceBtnD.setFocusPainted(false);
			singleChoiceBtnE.setFocusPainted(false);
			singleChoiceBtnF.setFocusPainted(false);
			singleChoiceBtnG.setFocusPainted(false);
			singleChoiceBtnH.setFocusPainted(false);
			singleChoiceBtnI.setFocusPainted(false);
			
			singleChoiceBtnA.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnB.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnC.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnD.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnE.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnF.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnG.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnH.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			singleChoiceBtnI.setPreferredSize(new Dimension(CHOICE_WIDTH,CHOICE_HEIGHT));
			
			singleChoiceBtnA.setBackground(BG_COLOR);
			singleChoiceBtnB.setBackground(BG_COLOR);
			singleChoiceBtnC.setBackground(BG_COLOR);
			singleChoiceBtnD.setBackground(BG_COLOR);
			singleChoiceBtnE.setBackground(BG_COLOR);
			singleChoiceBtnF.setBackground(BG_COLOR);
			singleChoiceBtnG.setBackground(BG_COLOR);
			singleChoiceBtnH.setBackground(BG_COLOR);
			singleChoiceBtnI.setBackground(BG_COLOR);
			
			singleChoiceBtnA.setFont(QUESTION_FONT);
			singleChoiceBtnB.setFont(QUESTION_FONT);
			singleChoiceBtnC.setFont(QUESTION_FONT);
			singleChoiceBtnD.setFont(QUESTION_FONT);
			singleChoiceBtnE.setFont(QUESTION_FONT);
			singleChoiceBtnF.setFont(QUESTION_FONT);
			singleChoiceBtnG.setFont(QUESTION_FONT);
			singleChoiceBtnH.setFont(QUESTION_FONT);
			singleChoiceBtnI.setFont(QUESTION_FONT);
			
			//提示选项分组的标签“Blank(i)”、“Blank(ii)”“Blank(iii)”
			JLabel blank1Label = new JLabel("Blank(i)",JLabel.CENTER);
			JLabel blank2Label = new JLabel("Blank(ii)",JLabel.CENTER);
			JLabel blank3Label = new JLabel("Blank(iii)",JLabel.CENTER);
			blank1Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank2Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank3Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank1Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			blank2Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			blank3Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			
			choiceArea.setLayout(new GridLayout(3,3,CHOICE_GAP,0));		//采用GridLayout，保证布局整齐
			questionArea.add(blank1Label);
			questionArea.add(blank2Label);
			questionArea.add(blank3Label);
			questionArea.add(choiceArea);
			choiceArea.add(singleChoiceBtnA);
			choiceArea.add(singleChoiceBtnD);
			choiceArea.add(singleChoiceBtnG);
			choiceArea.add(singleChoiceBtnB);
			choiceArea.add(singleChoiceBtnE);
			choiceArea.add(singleChoiceBtnH);
			choiceArea.add(singleChoiceBtnC);
			choiceArea.add(singleChoiceBtnF);
			choiceArea.add(singleChoiceBtnI);
		}
		
		//单选选项按钮关联监听器
		singleChoiceBtnA.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnB.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnC.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnD.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnE.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnF.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnG.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnH.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnI.addActionListener(new SingleChoiceBtnListener());
		
		//操作按钮区
		JPanel operationArea = new JPanel();
		questionArea.add(operationArea);
		operationArea.setBackground(BG_COLOR);
		//提交按钮，套题模式下不显示
		submitBtn = new JButton("Submit");
		submitBtn.setFont(font);
		submitBtn.setForeground(Color.WHITE);
		submitBtn.setBackground(SUBMIT_BTN_COLOR);
		submitBtn.setBorderPainted(false);
		submitBtn.setFocusPainted(false);
		if(practiceMode1==1) {
			submitBtn.setVisible(false);
		}
		//查看解析按钮，在提交之后才出现；再次按下隐藏解析（监听器中实现）
		viewExplanationBtn = new JButton("View Explanation");
		viewExplanationBtn.setFont(font);
		viewExplanationBtn.setForeground(Color.WHITE);
		viewExplanationBtn.setBackground(VIEW_EXPLANATION_BTN_COLOR);
		viewExplanationBtn.setBorderPainted(false);
		viewExplanationBtn.setFocusPainted(false);
		viewExplanationBtn.setVisible(false);
		//按钮加入操作区
		operationArea.add(submitBtn);
		operationArea.add(viewExplanationBtn);
		//关联监听器
		submitBtn.addActionListener(new SubmitBtnListener());
		viewExplanationBtn.addActionListener(new ViewExplanationBtnListener());
		
		//解析文本区
		explanationArea = new JTextArea(explanation);
		questionArea.add(explanationArea);
		explanationArea.setLineWrap(true);		//自动换行
		explanationArea.setWrapStyleWord(true);	//换行不断字
		explanationArea.setColumns(100);
		explanationArea.setEditable(false);		//不可修改
		explanationArea.setFont(new Font("楷体",Font.PLAIN,16));
		if(!explanationShowing) {				//是否可见取决于explanationShowing，其值由viewExplanationBtn更改
			explanationArea.setVisible(false);
		}
		
		/*
		 * 答题区两侧留白的设置
		 */
		blankAreaLeft.setBackground(BG_COLOR);
		blankAreaRight.setBackground(BG_COLOR);
	}
	
	/*
	 * 从setting.txt读取设置参数、题目信息
	 * practiceMode1、practiceMode2、questionType、difficulty这四个参数
	 * 由进入答题界面之前的设置界面设置，也可以在答题界面上进行更改
	 */
	public void readSetting() {
		try {
			Scanner input = new Scanner(new File("setting.txt"));
			practiceMode1 = input.nextInt();
			practiceMode2 = input.nextInt();
			questionType = input.nextInt();
			difficulty = input.nextInt();
			timeHidden = Boolean.parseBoolean(input.next());
			skin = input.nextInt();
			input.close();
		}catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,"未找到设置文件","错误！",
					JOptionPane.ERROR_MESSAGE);	//弹窗的父窗口暂设为null
		}
	}
	
	/*
	 * 更改设置之后，将新的设置参数写入setting.txt
	 */
	private void saveSetting() {
		try {
			PrintStream out = new PrintStream(new File("setting.txt"));
			out.println(practiceMode1);
			out.println(practiceMode2);
			out.println(questionType);
			out.println(difficulty);
			out.println(timeHidden);
			out.println(skin);
			}catch(FileNotFoundException e) {
				System.out.println("无法打开文件");
			}
	}

	/*
	 * 根据题型，从questionStem.txt读取大题干
	 */
	private void getQuestionStem() {
		try {
			Scanner input = new Scanner(new File("questionStem.txt"));
			for(int i=0;i<questionType;i++) {
				input.nextLine();
			}
			foreQuestionStem = input.nextLine();
			input.close();
		}catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,"未找到大题干文件","错误！",
					JOptionPane.ERROR_MESSAGE);	//弹窗的父窗口暂设为null
		}
	}
	
	/*
	 * 根据设置参数调用后端fetch_by_condition
	 * 获取符合条件的所有题目的题号
	 * 保存在数组qNo中
	 */
	public void getQuestionNo() {
		//调用fetch_by_condition，根据参数获取所有符合要求的题号
		try {
			qNum = 0;
			String[] arg = new String[]{"python","Question_provider.py","0",
					QTYPE[questionType],DIF[difficulty],DONE[done?1:0]};
			Process proc = Runtime.getRuntime().exec(arg);
			Scanner in = new Scanner(new InputStreamReader(proc.getInputStream()));
			in.useDelimiter("'");			//采用单引号“'”作为分隔符读取
			int[] temp = new int[1000];		//临时储存该条件下的所有满足条件题目的题号
			in.next();						//先把左括号读了
			while (in.hasNextInt()) {		//将所有得到的题号依次写入temp数组中
				temp[qNum++] = in.nextInt() + 1;
				in.next();
			}
			in.close();
			//将所有题号倒序写进qNo中
			qNo = new int[qNum];
			for(int i=0;i<qNum;i++) {
				qNo[i]=temp[qNum-i-1];
			}
			if(proc.waitFor()==0) System.out.println("读取题号成功");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 调用后端provider.get_collection
	 * 读取已收藏题目的题号
	 * 储存在数组collection[]中
	 */
	public void getCollection() {
		try {
			collectionNum = 0;
			String[] arg = new String[]{"python","Question_provider.py","5","Question"};
			Process proc = Runtime.getRuntime().exec(arg);
			Scanner in = new Scanner(new InputStreamReader(proc.getInputStream()));
			in.useDelimiter("'");			//采用单引号“'”作为分隔符读取
			collectionNo = new int[1000];	//储存该条件下的所有已收藏题目的题号
			in.next();						//先把左括号读了
			while (in.hasNextInt()) {		//将所有得到的题号依次写入temp数组中
				collectionNo[collectionNum++] = in.nextInt() + 1;
				in.next();
			}
			in.close();
			if(proc.waitFor()==0) System.out.println("读取收藏题号成功");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 由题号No获取大题干、题目、答案、解析
	 */
	public void readQuestion(int No) {
		questionNo = No;			//写入题号
		getQuestionStem();			//获取大题干
		getQuestion();				//读取题目
		getAnswer();				//读取答案
		getExplanation();			//读取解析
		isMarked(No);				//判断是否已收藏
	}
	
	/*
	 * 调用后端程序获取题目
	 * 调用后端fetch_by_num按照题号获取题干、选项以及题目信息
	 * 并与设置文档中读取的题目信息进行对比
	 * 调用后端读取答案和解析
	 */
	private void getQuestion(){
		try {
			String arg[] = new String[]{"python","Question_provider.py",
					"1",Integer.toString(questionNo - 1)};	//注意到后端提供的题目题号从0开始
			Process proc = Runtime.getRuntime().exec(arg);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String json = null;
			String temp = null;
			while ((temp = in.readLine()) != null) {	//将整个Json对象的字符串读入json中
				json = temp;
			}
			in.close();
			
			//创建Json对象读取题目信息
			JSONObject object = JSONObject.parseObject(json);	//转换为Json对象
			innerQuestionNo = object.getInteger("num");			//读入Section中的题号
			questionStem = object.getString("text");			//读入小题干
			String innerQuestionType = object.getString("typ");		//读入题型
			section = Integer.parseInt(object.getString("sec"));//读入section号
			String innerDifficulty = object.getString("dif");	//读入难度
			//读入所有选项
			choiceA = object.getString("option_a");
			choiceB = object.getString("option_b");
			choiceC = object.getString("option_c");
			choiceD = object.getString("option_d");
			choiceE = object.getString("option_e");
			choiceF = object.getString("option_f");
			choiceG = object.getString("option_g");
			choiceH = object.getString("option_h");
			choiceI = object.getString("option_i");
			
			//验证题型
			if(!innerQuestionType.equals(QTYPE[questionType])) {//若文件中的题型与设置中的题型对不上
				JOptionPane.showMessageDialog(null,"题目题型验证错误","数据出错！",
						JOptionPane.ERROR_MESSAGE);				//则弹窗提示错误
				System.exit(0);									//并退出
			}
			
			//验证题号
			if(questionNo != QUESTION_COUNT * (section - 1) + innerQuestionNo) {//若文件中的题号与传入的题号对不上
				JOptionPane.showMessageDialog(null,"题目题号验证错误","数据出错！",
						JOptionPane.ERROR_MESSAGE);				//则弹窗提示错误
				System.exit(0);									//并退出
			}
			
			//验证难度
			if(!innerDifficulty.equals(DIF[difficulty])) {
				JOptionPane.showMessageDialog(null,"题目难度验证错误","数据出错！",
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			
			if(proc.waitFor()==0) System.out.println("读取题目成功");
		}catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 通过题号，调用后端读取题目答案
	 * 加入了题号验证
	 */
	private void getAnswer() {
		//调用get_answer获取题号为questionNo的题目的答案
		try {
			String[] arg = new String[]{"python","Question_provider.py","2",Integer.toString(questionNo - 1)};
			Process proc = Runtime.getRuntime().exec(arg);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String json = null;
			String temp = null;
			while ((temp = in.readLine()) != null) {	//将整个Json对象的字符串读入json中
				json = temp;
			}
			in.close();
			
			//创建Json对象读取答案
			JSONObject object = JSONObject.parseObject(json);	//转换为Json对象
			answer = object.getString("ans");
			
			//验证题号
			int num_temp = object.getInteger("n");
			if(num_temp!=questionNo) {									//若文件中的题号与设置中的题号对不上
				JOptionPane.showMessageDialog(null,"答案题号验证错误","数据出错！",
						JOptionPane.ERROR_MESSAGE);						//则弹窗提示错误
				System.exit(0);											//并退出
			}
			if(proc.waitFor()==0) System.out.println("读取答案成功");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 通过题号，调用后端读取题目的解析
	 * 加入了题号验证
	 */
	private void getExplanation() {
		//调用get_answer获取题号为questionNo的题目的答案
		try {
			String[] arg = new String[]{"python","Question_provider.py","3",Integer.toString(questionNo - 1)};
			Process proc = Runtime.getRuntime().exec(arg);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String json = null;
			String temp = null;
			while ((temp = in.readLine()) != null) {	//将整个Json对象的字符串读入json中
				json = temp;
			}
			in.close();
			
			//创建Json对象读取答案
			JSONObject object = JSONObject.parseObject(json);	//转换为Json对象
			String exp = object.getString("exp");				//解析
			String inter = object.getString("inter");			//翻译
			explanation = "解析：" + exp + "\n" + "翻译：" + inter;

			//验证题号
			int num_temp = object.getInteger("n");				//读入answer中的题号
			if(num_temp!=questionNo) {									//若文件中的题号与设置中的题号对不上
				JOptionPane.showMessageDialog(null,"解析题号验证错误","数据出错！",
						JOptionPane.ERROR_MESSAGE);						//则弹窗提示错误
				System.exit(0);											//并退出
			}
			
			if(proc.waitFor()==0) System.out.println("读取解析成功");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 读取已选答案
	 */
	private String scanAnswer() {
		String ans = "";
		if(questionType == 0){			//单空五选一
			if(sinChoosen1 == singleChoiceBtnA) ans += "A";
			else if(sinChoosen1 == singleChoiceBtnB) ans += "B";
			else if(sinChoosen1 == singleChoiceBtnC) ans += "C";
			else if(sinChoosen1 == singleChoiceBtnD) ans += "D";
			else if(sinChoosen1 == singleChoiceBtnE) ans += "E";
		}else if(questionType == 1) {	//单空六选二
			if (multiChoiceBtnA.isSelected()) ans += "A";
			if (multiChoiceBtnB.isSelected()) ans += "B";
			if (multiChoiceBtnC.isSelected()) ans += "C";
			if (multiChoiceBtnD.isSelected()) ans += "D";
			if (multiChoiceBtnE.isSelected()) ans += "E";
			if (multiChoiceBtnF.isSelected()) ans += "F";
		}else {							//双空或三空
			if(sinChoosen1 == singleChoiceBtnA) ans += "A";
			if(sinChoosen1 == singleChoiceBtnB) ans += "B";
			if(sinChoosen1 == singleChoiceBtnC) ans += "C";
			if(sinChoosen2 == singleChoiceBtnD) ans += "D";
			if(sinChoosen2 == singleChoiceBtnE) ans += "E";
			if(sinChoosen2 == singleChoiceBtnF) ans += "F";
			if(sinChoosen3 == singleChoiceBtnG) ans += "G";
			if(sinChoosen3 == singleChoiceBtnH) ans += "H";
			if(sinChoosen3 == singleChoiceBtnI) ans += "I";
		}
		return ans;
	}
	
	/*
	 * 更改题型/难度设置，或“下一题”后，读取题目数据并重新显示
	 */
	private void reDisplay() {
		timer.removeActionListener(timeListener);
		second = 0;
		minute = 0;
		explanationShowing = false;
		System.gc();
		if(practiceMode1 == 0) {	//单题模式
			if(0 < qNum) {				//有题目时
				readQuestion(qNo[--qNum]);		//读取题目
				panel.removeAll();
				placeComponent(panel);				//刷新显示
			}else {						//题目做完了，弹窗提示更改设置
				JOptionPane.showMessageDialog(frame, 
						QTYPE[questionType]+"、"+DIF[difficulty]+"、"+(done?("做过"+""):"未做过")+"的题目已经做完了，换个题型/难度吧~", 
						"没题了", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	/*
	 * 用户修改设置，后台更改并重新显示
	 * param标识用户的操作，0-修改题型，1-修改难度，2-下一题，3-退出
	 */
	private void changeSetting(int param,int param1) {
		if(submitted) {		//本题已提交
			submitted = false;
			if(param == 0) {
				questionType = param1;
				getQuestionNo();		//重新抽题
			}else if(param == 1) {
				difficulty = param1;
				getQuestionNo();		//重新抽题
			}else if(param == 2);
			else if(param == 3) {
				saveSetting();			//保存设置参数
				System.exit(0);			//退出，合并后改为退回到主页面
			}
			reDisplay();			//刷新数据并重新显示
		}else {				//未提交，则弹窗提示
			Object[] sub = {"提交","不提交","取消"};
			int sm = JOptionPane.showOptionDialog(frame,"您尚未提交，本题作答记录将不会被保存。" + "\n" + "您是否要提交答案？",
					"提示",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null,sub,"提交");
			if (sm == 0) {		//选择”提交“，则扫描已选答案，并写入后台
				String ans = scanAnswer();
				System.out.println(ans);
				System.out.println(answer);
				String rightOrWrong = ans.equals(answer)?"Right":"Wrong";
				try {
					String[] arg = new String[]{"python","Question_provider.py",
							"4",Integer.toString(questionNo - 1),ans,rightOrWrong};
					Process proc = Runtime.getRuntime().exec(arg);
					if(proc.waitFor()==0) System.out.println("更改答题历史成功");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(param == 0) {
					questionType = param1;
					getQuestionNo();		//重新抽题
				}else if(param == 1) {
					difficulty = param1;
					getQuestionNo();		//重新抽题
				}else if(param == 2);
				else if(param == 3) {
					saveSetting();			//保存设置参数
					System.exit(0);			//退出，合并后改为退回到主页面
				}
				reDisplay();			//刷新数据并重新显示
			}else if(sm == 1) {	//不提交，放弃更改
				if(param == 0) {
					questionType = param1;
					getQuestionNo();		//重新抽题
				}else if(param == 1) {
					difficulty = param1;
					getQuestionNo();		//重新抽题
				}else if(param == 2);	//下一题
				else if(param == 3) {
					saveSetting();			//保存设置参数
					System.exit(0);			//退出，合并后改为退回到主页面
				}
				reDisplay();			//刷新数据并重新显示
			}else if(sm == 2);	//取消
		}
	}
	
	/*
	 * 判断题号为No的题目是否已收藏
	 * 搜索collectionNo中是否存在该题号
	 * 最后根据结果更改q.marked的布尔值
	 */
	private void isMarked(int No) {
		marked = false;		//预设为假
		for(int i=0;i<collectionNum;i++) {
			if(No == collectionNo[i]) {
				marked = true;
				break;
			}
		}
	}
	
	/*
	 * 监听器
	 */
	private class BySectionMenuListener implements ActionListener{			//菜单中的套题练习模式
		public void actionPerformed(ActionEvent event) {
			practiceMode1 = 1;
			questionTypeMenu.setVisible(false);		//隐藏菜单栏中的题型设置菜单
			/*
			 * 调用抽题模块重新抽题，重新进入页面
			 */
		}
	}
	
	private class BySingleMenuListener implements ActionListener{			//菜单中的单题练习模式
		public void actionPerformed(ActionEvent event) {
			practiceMode1 = 0;
			questionTypeMenu.setVisible(true);		//显示菜单栏中的题型设置菜单
			/*
			 * 调用抽题模块重新抽题，重新进入页面
			 */
		}
	}
	
	private class ByNewMenuListener implements ActionListener{				//菜单中的新题练习模式
		public void actionPerformed(ActionEvent event) {
			practiceMode2 = 0;
			/*
			 * 调用抽题模块重新抽题，重新进入页面
			 */
		}
	}
	
	private class ByWrongMenuListener implements ActionListener{			//菜单中的错题重测练习模式
		public void actionPerformed(ActionEvent event) {
			practiceMode2 = 1;
			/*
			 * 调用抽题模块重新抽题，重新进入页面
			 */
		}
	}
	
	private class OneBlankOneChoiceMenuListener implements ActionListener{	//菜单中的题型设置，单空五选一
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,0);
		}
	}
	
	private class OneBlankTwoChoiceMenuListener implements ActionListener{	//菜单中的题型设置，单空六选二
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,1);
		}
	}
	
	private class TwoBlankMenuListener implements ActionListener{			//菜单中的题型设置，双空
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,2);
		}
	}
	
	private class ThreeBlankMenuListener implements ActionListener{			//菜单中的题型设置，三空
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,3);
		}
	}
	
	private class EasyMenuListener implements ActionListener{				//菜单中的难度设置，简单
		public void actionPerformed(ActionEvent event) {
			changeSetting(1,0);
		}
	}
	
	private class MedianMenuListener implements ActionListener{				//菜单中的难度设置，中等
		public void actionPerformed(ActionEvent event) {
			changeSetting(1,1);
		}
	}
	
	private class HardMenuListener implements ActionListener{				//菜单中的难度设置，困难
		public void actionPerformed(ActionEvent event) {
			changeSetting(1,2);
		}
	}
	
	private class MarkedCollectionListener implements ActionListener{				//菜单中的查看收藏夹
		public void actionPerformed(ActionEvent event) {
			
		}
	}
	
	private class WrongCollectionListener implements ActionListener{				//菜单中的查看错题本
		public void actionPerformed(ActionEvent event) {
			
		}
	}
	
	private class QuitBtnListener implements ActionListener{				//功能栏中的退出
		public void actionPerformed(ActionEvent event) {
			changeSetting(3,0);
		}
	}
	
	private class MarkBtnListener implements ChangeListener{				//功能栏中的收藏
		public void stateChanged(ChangeEvent event) {
			try {
				String arg[] = new String[]{"python","Question_provider.py",
						"6","Question",marked?"delete":"add",Integer.toString(questionNo - 1)};
				Process proc = Runtime.getRuntime().exec(arg);
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				in.close();
				if(proc.waitFor()==0) System.out.println(marked?"取消":"" + "收藏成功");
			}catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class BackBtnListener implements ActionListener{				//功能栏中的返回
		public void actionPerformed(ActionEvent event) {
			/*
			 * 暂存本题数据
			 * 返回上一题
			 */
		}
	}
	
	private class NextBtnListener implements ActionListener{				//功能栏中的下一题
		public void actionPerformed(ActionEvent event) {
			changeSetting(2,0);
		}
	}
	
	private class TimeHiddingBtnListener implements ActionListener{			//隐藏时间按钮
		public void actionPerformed(ActionEvent event) {
			if(!timeHidden) {						//如果此前时间是显示的
				timeHiddingBtn.setText("| Show Time");	//按钮改为“显示时间按钮”
				timeLabel.setVisible(false);			//时间标签设为不可见
				timeHidden = true;
			}else {									//如果此前时间是隐藏的
				timeHiddingBtn.setText("| Hide Time");	//按钮改为“隐藏时间按钮”
				timeLabel.setVisible(true);				//时间标签设为可见
				timeHidden = false;
			}
			
		}
	}
	
	private class SingleChoiceBtnListener implements ActionListener{	//单选的选项（单空五选一、双空或三空中一组选项）
		public void actionPerformed(ActionEvent event) {
			ChooseHighlight((JButton)event.getSource());	//调用方法点亮该按钮，并熄灭同组的其他按钮
		}
	}
	
	private class SubmitBtnListener implements ActionListener{			//提交按钮
		public void actionPerformed(ActionEvent event) {	//读取答案，并显示看解析的按钮
			if(practiceMode1 == 0) {		//单题模式
				String ans = scanAnswer();
				System.out.println(ans);
				System.out.println(answer);
				String rightOrWrong = ans.equals(answer)?"Right":"Wrong";
				submitted = true;
				
				/*
				 * 显示正确与否或者显示答案
				 */
				
				//调用后端answer_history_alter，更改答题历史
				try {
					String[] arg = new String[]{"python","Question_provider.py",
							"4",Integer.toString(questionNo - 1),ans,rightOrWrong};
					Process proc = Runtime.getRuntime().exec(arg);
					if(proc.waitFor()==0) System.out.println("更改答题历史成功");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				viewExplanationBtn.setVisible(true);			//显示“显示解析”按钮
			}
		}
	}
	
	private class ViewExplanationBtnListener implements ActionListener{	//显示解析按钮
		public void actionPerformed(ActionEvent event) {
			if(!explanationShowing) {							//如果此前解析未显示
				explanationShowing = true;							//更改标志位
				explanationArea.setVisible(true);					//解析设为可见
				viewExplanationBtn.setText("Retract Explanation");	//按钮改为“收回解析”
			}else {												//否则
				explanationShowing = false;							//更改标志位
				explanationArea.setVisible(false);					//解析设为不可见
				viewExplanationBtn.setText("View Explanation");		//按钮改为“查看解析”
			}
		}
	}
	
	private class TimeListener implements ActionListener{				//计时器
			public void actionPerformed(ActionEvent evt) {
				second++;
				if (second==60) {	//秒数进位处理
					minute++;
					second = 0;
				}
				timeLabel.setText(minute + ":" + second);
			}
	}
	
	/*
	 * 单选选项选中的显示效果
	 */
	
	private void ChooseHighlight(JButton JBtn) {
		if(questionType==0) {	//单空五选一	
			if(sinChoosen1!=null) {
				//将之前已选的选项改成白底黑字
				sinChoosen1.setBackground(Color.WHITE);
				sinChoosen1.setForeground(Color.BLACK);
			}
			//将本次选中的选项改成黑底白字
			JBtn.setBackground(Color.BLACK);
			JBtn.setForeground(Color.WHITE);
			//sinChoosen1指向本次选中的选项
			sinChoosen1 = JBtn;
		}else if((questionType==2) || (questionType==3)) {	//多空选择题（双空或三空），每空有三个选项
			if((JBtn==singleChoiceBtnA) || (JBtn==singleChoiceBtnB) || (JBtn==singleChoiceBtnC)) {	//所选答案为第一空的答案
				if(sinChoosen1!=null) {
					//将之前已选的该组选项改成白底黑字
					sinChoosen1.setBackground(Color.WHITE);
					sinChoosen1.setForeground(Color.BLACK);
				}
				//将本次选中的选项改成黑底白字
				JBtn.setBackground(Color.BLACK);
				JBtn.setForeground(Color.WHITE);
				//sinChoosen1指向本次选中的选项
				sinChoosen1 = JBtn;
			}else if((JBtn==singleChoiceBtnD) || (JBtn==singleChoiceBtnE) || (JBtn==singleChoiceBtnF)) {	//所选答案为第二空的答案
				if(sinChoosen2!=null) {
					//将之前已选的选项改成白底黑字
					sinChoosen2.setBackground(Color.WHITE);
					sinChoosen2.setForeground(Color.BLACK);
				}
				//将本次选中的选项改成黑底白字
				JBtn.setBackground(Color.BLACK);
				JBtn.setForeground(Color.WHITE);
				//sinChoosen1指向本次选中的选项
				sinChoosen2 = JBtn;
			}else {	//所选答案为第三空的答案（如果存在）
				if(sinChoosen3!=null) {
					//将之前已选的选项改成白底黑字
					sinChoosen3.setBackground(Color.WHITE);
					sinChoosen3.setForeground(Color.BLACK);
				}
				//将本次选中的选项改成黑底白字
				JBtn.setBackground(Color.BLACK);
				JBtn.setForeground(Color.WHITE);
				//sinChoosen1指向本次选中的选项
				sinChoosen3 = JBtn;
			}
		}else {	//单空六选二
			/*
			 * 不会调用这里，可省略
			 */
		}
	}
}
