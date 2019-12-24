

/*
 * ���������⣺
 * 1.��Ŀ��תʱʱ��Ĵ��ң��̣�
 * 2.�����ʼ����ִ�д��루�̣�
 * 2.����ģʽ��ʵ��
 * 	2.1��ͬ��ʾ
 * 	2.2�����е���Ϣ�ݴ�
 * 3.��ͬˢ��ģʽ�Ĳ�ͬ��ʾ
 * 	3.1�˵���
 * 	3.2�ύ
 * 4.����ģʽ����һ�����ύ���̣�
 * 5.��Ƥ����ʵ��
 * 6.������ģʽ
 * 7.�Ż���ȡjson�ļ��ķ������̣�
 * 8.�ղ���ʾ���̣�
 * 9.����д�뱣�棨�̣�
 */

/*
 * ���⣺
 * 1-2��������ѡһ
 * 3-4��˫��
 * 5-6������
 * 7-10��������ѡ��
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
 * ����ҳ�����ʾ
 * ͨ���������˽�����ϵ
 */
public class Questioning {
	/*
	 * һЩ��������
	 */
	private final int PANE_WIDTH = 1000;			//���ڵĿ��
	private final int PANE_HEIGHT = 750;			//���ڵĸ߶�
	private final int FUNTIONAL_BAR_HEIGHT = 75;		//�������ĸ߶�
	private final int FUNCTIONAL_BTN_WIDTH = 90;		//���ܰ�ť�Ŀ��
	private final int FUNCTIONAL_BTN_HEIGHT = 40;		//���ܰ�ť�ĸ߶�
	private final int FUNCTIONAL_BTN_GAP = 125;		//��������ť֮��ľ���
	private final int FIRST_FUNCTIONAL_BTN_X = PANE_WIDTH - 4 * FUNCTIONAL_BTN_GAP;	//��������һ����ť�ĺ�����
	private final int FIRST_FUNCTIONAL_BTN_Y = 18;		//��������һ����ť��������
	private final int INFORMATIONAL_BAR_HEIGHT = 35;	//��Ϣ���ĸ߶�
	private final int BLANK_AREA_WIDTH = 100;			//��Ŀ����������׿��
	private final int CHOICE_WIDTH = 260;			//ѡ����
	private final int CHOICE_HEIGHT = 32;			//ѡ��߶�
	private final int CHOICE_GAP = 10;				//ѡ����
	private final Color QUIT_BTN_COLOR = new Color(140,6,137);			//�˳���ť����ɫ
	private final Color MARK_CHECKBOX_COLOR = new Color(60,60,60);		//�ղظ�ѡ�����ɫ
	private final Color PAGE_BTN_COLOR = new Color(40,85,160);			//������һ�⡢��һ�ⰴť����ɫ
	private final Color SUBMIT_BTN_COLOR = new Color(85,180,10);			//�ύ��ť����ɫ
	private final Color VIEW_EXPLANATION_BTN_COLOR = new Color(220,45,45);	//�鿴������ť����ɫ
	private final Color INFORMATION_BAR_COLOR = new Color(250,170,155);	//��Ϣ����ɫ
	private final Color BG_COLOR = Color.WHITE;							//������ɫ
	private final Font QUESTION_FONT = new Font("Arial",Font.PLAIN,18);	//��Ŀ��������
	
	//���˸�ʽ��ϵĲ���д��
	String[] QTYPE = {"����","��ѡ��","˫��","����"};
	String[] DIF = {"easy","median","hard"};
	String[] DONE = {"undone","done"};
	
	/*
	 * һЩ�ڲ�����
	 */
	private int second = 0;		//��ʱ���ϵ���
	private int minute = 0;		//��ʱ���ϵķ�
	private JButton sinChoosen1 = null;	//������ѡһ��˫�ա����յ�һ�յ���ѡ��
	private JButton sinChoosen2 = null;	//˫�ջ����յĵڶ��յ���ѡ��
	private JButton sinChoosen3 = null;	//���յĵ����յ���ѡ��
	
	/*
	 * һЩ�ⲿ����
	 */
	private int practiceMode1;				//ˢ��ģʽ����1��0-����ģʽ��1-����ģʽ
	private int practiceMode2;				//ˢ��ģʽ����2��0-���⣬1-�����ز�
	private int difficulty;					//��Ŀ�Ѷ����ã�0-�򵥣�1-���У�2-����
	private int questionType;				//�������ã�0-������ѡһ��1-������ѡ����2-˫�գ�3-����
	public int[] qNo;						//�������µ���������������Ŀ�����
	public int qNum = 0;					//������������Ŀ��
	public int[] collectionNo;				//���ղ���Ŀ�����
	public int collectionNum;				//�ղ���Ŀ������
	private boolean done;					//�����Ƿ�������
	private boolean marked;					//�����Ƿ��ղ�
	private boolean timeHidden;				//ʱ���Ƿ�����
	private boolean explanationShowing;		//�Ƿ���ʾ����
	private boolean submitted = false;		//�����Ƿ����ύ
	private int section;					//�������ڵڼ�Section
	private int innerQuestionNo;			//�����Ǳ�Section�ĵڼ���
	private int questionNo;					//����Ĵ����
	private final int SECTION_COUNT = 100;	//�����һ���ж��ٸ�Section
	private final int QUESTION_COUNT = 10;	//��Sectionһ���ж�����
	private String foreQuestionStem;		//�����
	private String questionStem;			//���
	private String choiceA;					//ѡ����9����
	private String choiceB;
	private String choiceC;
	private String choiceD;
	private String choiceE;
	private String choiceF;
	private String choiceG;
	private String choiceH;
	private String choiceI;
	private String answer;					//��˶�ȡ�ı�׼��
	private String explanation;				//��Ŀ����
	private int skin;						//Ƥ����0-Ĭ��Ƥ����ETS��񣩣�1-
	
	/*
	 * ���ֶ���
	 */
	JFrame frame;							//ҳ��
	JPanel panel;							//���ֶ���
	
	/*
	 * �˵����еĶ���
	 */
	private JMenuBar menuBar;				//�˵���
	private JMenu practiceModeMenu;			//��ϰģʽ����
	private JMenu practiceMode1Menu;		//��ϰģʽ����1
	private JMenu practiceMode2Menu;		//��ϰģʽ����2
	private JMenu difficultyMenu;			//�Ѷ�����
	private JMenu questionTypeMenu;			//��������
	private JMenu view;						//�鿴���Ȿ���ղؼ�
	private JMenuItem bySectionMenuItem;	//����ģʽ
	private JMenuItem bySingleMenuItem;		//����ģʽ
	private JMenuItem byNewMenuItem;		//����
	private JMenuItem byWrongMenuItem;		//�����ز�
	private JMenuItem oneBlankOneChoiceMenuItem;	//����-������ѡһ
	private JMenuItem oneBlankTwoChoiceMenuItem;	//����-������ѡ��
	private JMenuItem twoBlankMenuItem;				//����-˫��
	private JMenuItem threeBlankMenuItem;			//����-����
	private JMenuItem easyMenuItem;					//�Ѷ�-��
	private JMenuItem medianMenuItem;				//�Ѷ�-����
	private JMenuItem hardMenuItem;					//�Ѷ�-����
	private JMenuItem markedCollection;				//�ղؼ�
	private JMenuItem wrongCollection;				//���Ȿ
	
	/*
	 * �������еĶ���
	 */
	private JButton quitBtn;		//�˳���ť
	private JButton backBtn;		//������һ�ⰴť
	private JButton nextBtn;		//��һ�ⰴť�������汾������
	private JCheckBox markCheckBox;	//�ղظ�ѡ��
	private JLabel timeLabel;		//��ʱ��ʾ����ͬģʽ��ʾ��ͬ��
	private JButton timeHiddingBtn;	//����ʱ�䰴ť
	private Timer timer;			//��ʱ��
	
	/*
	 * ��Ϣ���еĶ���
	 */
	private JLabel sectionInformationLabel;
	private JLabel numInformationLabel;
	TimeListener timeListener;
	
	/*
	 * �������еĶ���
	 */
	//���
	JTextArea foreQuestionStemArea;		//�����
	JTextArea questionStemArea;			//С���
	JPanel questionArea;				//��Ŀ�����
	JPanel choiceArea;					//ѡ�������
	
	//������ѡһ & ˫�� & ���� ��ѡ��
	private JButton singleChoiceBtnA;
	private JButton singleChoiceBtnB;
	private JButton singleChoiceBtnC;
	private JButton singleChoiceBtnD;
	private JButton singleChoiceBtnE;
	private JButton singleChoiceBtnF;
	private JButton singleChoiceBtnG;
	private JButton singleChoiceBtnH;
	private JButton singleChoiceBtnI;
	
	//������ѡ����ѡ��
	private JCheckBox multiChoiceBtnA;
	private JCheckBox multiChoiceBtnB;
	private JCheckBox multiChoiceBtnC;
	private JCheckBox multiChoiceBtnD;
	private JCheckBox multiChoiceBtnE;
	private JCheckBox multiChoiceBtnF;
	
	//������ť
	private JButton submitBtn;			//�ύ��ť
	private JButton viewExplanationBtn;	//�鿴������ť
	
	//�����ı���
	private JTextArea explanationArea;
	
	/*
	 * ��������ʾ�������
	 * �ṩ���˵Ľ�������
	 */
	public void questioningPage() {
		//�ÿ���ķ��
		/*
		 * JFrame.setDefaultLookAndFeelDecorated(true);
		 *JDialog.setDefaultLookAndFeelDecorated(true);
		 *try {
	     *    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	     *} catch (Exception evt) {}
		 */
		
		//����JFrame�����л�������
		frame = new JFrame("GRE Ҫ����3000");
		frame.setSize(PANE_WIDTH,PANE_HEIGHT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//���ò˵���
		menuBar = new JMenuBar();
		setMenuBar(menuBar);
		
		//������岢���
		panel = new JPanel();
		frame.add(panel);
		
		//�������ڷ����������
		placeComponent(panel);
		
		//ʹ���ɼ�
		frame.setVisible(true);
	}
	
	/*
	 * ���岢���ò˵���
	 */
	public void setMenuBar(JMenuBar menuBar) {
		//�˵���������Ķ���
		practiceModeMenu = new JMenu("��ϰģʽ");
		practiceMode1Menu = new JMenu("ģʽ����1");
		practiceMode2Menu = new JMenu("ģʽ����2");
		difficultyMenu = new JMenu("�Ѷ�����");
		questionTypeMenu = new JMenu("��������");
		view = new JMenu("�鿴");
		bySectionMenuItem = new JMenuItem("����ģʽ");
		bySingleMenuItem = new JMenuItem("����ģʽ");
		byNewMenuItem = new JMenuItem("����");
		byWrongMenuItem = new JMenuItem("�����ز�");
		oneBlankOneChoiceMenuItem = new JMenuItem("������ѡһ");
		oneBlankTwoChoiceMenuItem = new JMenuItem("������ѡ��");
		twoBlankMenuItem = new JMenuItem("˫��");
		threeBlankMenuItem = new JMenuItem("����");
		easyMenuItem = new JMenuItem("��");
		medianMenuItem = new JMenuItem("�е�");
		hardMenuItem = new JMenuItem("����");
		markedCollection = new JMenuItem("�ղؼ�");
		wrongCollection = new JMenuItem("���Ȿ");

		//���ò˵���
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

		//����������
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

		//��ʾ�˵���
		frame.setJMenuBar(menuBar);
	}
	
	/*
	 * �˷������ڲ�������ϵ�Ԫ��
	 */
	private void placeComponent(JPanel p) {
		/*
		 * ���岼�֣���ȡ����BorderLayout�Ĳ��ַ�����
		 * North���Ź���������Ϣ����
		 * Center���Ŵ�����
		 * East��West����һ����ȵ�����
		 * South����
		 */
		
		p.setLayout(null);
		p.setBackground(BG_COLOR);
		JPanel functionalAndInformationalArea = new JPanel();	//������Ϣ��
		questionArea = new JPanel();						//������
		JPanel blankAreaLeft = new JPanel();					//���������
		JPanel blankAreaRight = new JPanel();					//�Ҳ�������
		
		//���ø���panel�Ĵ�С��λ��
		functionalAndInformationalArea.setBounds(0, 0, PANE_WIDTH, FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT);
		questionArea.setBounds(BLANK_AREA_WIDTH,FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT,
				PANE_WIDTH - 2 * BLANK_AREA_WIDTH, PANE_HEIGHT - FUNTIONAL_BAR_HEIGHT - INFORMATIONAL_BAR_HEIGHT);
		blankAreaLeft.setBounds(0, FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT,
				BLANK_AREA_WIDTH, PANE_HEIGHT - FUNTIONAL_BAR_HEIGHT - INFORMATIONAL_BAR_HEIGHT);
		blankAreaRight.setBounds(PANE_WIDTH - BLANK_AREA_WIDTH, FUNTIONAL_BAR_HEIGHT + INFORMATIONAL_BAR_HEIGHT,
				BLANK_AREA_WIDTH, PANE_HEIGHT - FUNTIONAL_BAR_HEIGHT - INFORMATIONAL_BAR_HEIGHT);
		
		//������
		/*
		functionalAndInformationalArea.setBackground(Color.RED);
		questionArea.setBackground(Color.GREEN);
		blankAreaLeft.setBackground(Color.CYAN);
		blankAreaRight.setBackground(Color.CYAN);
		*/
		
		//������panel��ӵ�p��
		p.add(functionalAndInformationalArea);
		p.add(questionArea);
		p.add(blankAreaLeft);
		p.add(blankAreaRight);
		
		/*
		 * ������Ϣ���Ĳ���
		 * ��ȡFlowLayout�İ취
		 * �������η��ù���������Ϣ��
		 */
		functionalAndInformationalArea.setLayout(null);
		JPanel functionalBar = new JPanel();					//������
		JPanel informationalBar = new JPanel();					//��Ϣ��
		
		//���ø���panel�Ĵ�С
		functionalBar.setBounds(0, 0, PANE_WIDTH, FUNTIONAL_BAR_HEIGHT);
		informationalBar.setBounds(0, FUNTIONAL_BAR_HEIGHT, PANE_WIDTH, INFORMATIONAL_BAR_HEIGHT);
		
		//������panel��ӽ�ȥ
		functionalAndInformationalArea.add(functionalBar);
		functionalAndInformationalArea.add(informationalBar);
		
		/*
		 * �������Ĳ���
		 * ����ǲ�Ʒ��logo
		 * �Ҳ����ηֲ��˳����ղء����ء���һ���ĸ���ť
		 */
		//�������Ķ���
		ImageIcon logoImage = new ImageIcon("img/logo.png");
		JLabel logo = new JLabel(logoImage);	//��Ʒlogo,�ߴ�257*75
		quitBtn = new JButton("Quit");				//�˳���ť
		markCheckBox = new JCheckBox("Mark",marked);//�ղذ�ť���Ը�ѡ����ʵ�֣�
		backBtn = new JButton("Back");				//������һ�ⰴť
		nextBtn = new JButton("Next");				//��һ�ⰴť
		
		//���ø���������ɫ������
		functionalBar.setBackground(Color.black);
		functionalBar.setLayout(null);
		Font font = new Font("Arial",Font.BOLD,18);	//����������
		
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
		
		//���������ӽ�������
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
		
		//����������
		quitBtn.addActionListener(new QuitBtnListener());
		markCheckBox.addChangeListener(new MarkBtnListener());
		backBtn.addActionListener(new BackBtnListener());
		nextBtn.addActionListener(new NextBtnListener());
		
		/*
		 * ��Ϣ���Ĳ���
		 * ����Ǵ�����ϢLabel
		 * �Ҳ���ʱ����ϢLabel
		 */
		informationalBar.setBackground(INFORMATION_BAR_COLOR);	//���ñ�����ɫ
		//������ϢLabel����������
		sectionInformationLabel = new JLabel("Section " + section + " of " + SECTION_COUNT);		//Section��Ϣ
		sectionInformationLabel.setFont(new Font("Comic Sans MS",Font.BOLD,14));
		numInformationLabel = new JLabel("| Question " + innerQuestionNo + " of " + QUESTION_COUNT);	//�����Ϣ
		numInformationLabel.setFont(new Font("Comic Sans MS",Font.PLAIN,14));
		
		//������ϢLabel������Ϣ����
		informationalBar.add(sectionInformationLabel);
		informationalBar.add(numInformationLabel);
		informationalBar.setLayout(null);
		sectionInformationLabel.setBounds(15, 0, 130, 35);
		numInformationLabel.setBounds(140, 0, 125, 35);
		
		//ʱ����ϢLabel�Ķ���������
		timeLabel = new JLabel(minute + ":" + second);
		timeLabel.setBounds(840,0,100,35);
		timeLabel.setFont(new Font("Comic Sans MS",Font.BOLD,20));
		
		//����ʱ�䰴ť�Ķ���������
		timeHiddingBtn = new JButton();
		timeHiddingBtn.setFont(new Font("Comic Sans MS",Font.PLAIN,15));
		timeHiddingBtn.setContentAreaFilled(false);		//͸��
		timeHiddingBtn.setBorderPainted(false);
		timeHiddingBtn.setFocusPainted(false);
		
		//Label��Button������Ϣ��
		informationalBar.add(timeHiddingBtn);
		informationalBar.add(timeLabel);
		timeHiddingBtn.setBounds(870,0,130,35);
		
		//timeHidding����ʱ����ʾ����û���������ʱ�䰴ťʱ��ֵ��ת����timeHiddingButton�ļ�������ʵ�֣�
		if(!timeHidden) {
			timeLabel.setVisible(true);
			timeHiddingBtn.setText("| Hide Time");
		}else {
			timeLabel.setVisible(false);
			timeHiddingBtn.setText("| Show Time");
		}
		
		//��ť����������
		timeHiddingBtn.addActionListener(new TimeHiddingBtnListener());
		
		//��ʱ����ÿ������һ��timeLabel����ʾ
		timeListener = new TimeListener();
		timer = new Timer(1000,timeListener);
		timer.start();
		
		/*
		 * �������Ĳ���
		 * �������·ֱ���
		 * ���������-JTextArea
		 * �������-JTextArea
		 * ѡ����-������ѡһ/˫��/����-JRadioButton;������ѡ��-JCheckBox
		 * ������ť�������ύ��ť�Ͳ鿴���Ͱ�ť
		 * ������ʾ��-JTextArea
		 */
		
		//��������������
		questionArea.setLayout(new FlowLayout());
		questionArea.setBackground(BG_COLOR);
		
		//�����
		foreQuestionStemArea = new JTextArea();
		foreQuestionStemArea.setText("\n" + foreQuestionStem + "\n");
		foreQuestionStemArea.setLineWrap(true);			//�Զ�����
		foreQuestionStemArea.setWrapStyleWord(true);	//���в�����
		foreQuestionStemArea.setColumns(60);
		foreQuestionStemArea.setFont(new Font("Arial",Font.PLAIN,15));
		foreQuestionStemArea.setBackground(Color.GRAY);
		foreQuestionStemArea.setEditable(false);		//���ɸ���
		questionArea.add(foreQuestionStemArea);
		
		//С���
		questionStemArea = new JTextArea();
		questionStemArea.setText(questionStem);
		questionStemArea.setLineWrap(true);				//�Զ�����
		questionStemArea.setWrapStyleWord(true);		//���в�����
		questionStemArea.setColumns(52);
		questionStemArea.setFont(QUESTION_FONT);
		questionStemArea.setEditable(false);			//���ɸ���
		questionArea.add(questionStemArea);
		
		//ѡ����
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
		//�������������Ų���
		if(questionType==0) {			//������ѡһ��Ϊ��ȷ����ť�������·����������auxilliaryLabelռ�ݿռ�
			//����ռ�ռ��label��һ��һ�ң�choiceArea����
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
			
			choiceArea.setLayout(new GridLayout(5,1,CHOICE_GAP,0));	//����GridLayout����֤���벼��
			questionArea.add(auxilliaryLabel1);
			questionArea.add(choiceArea);
			choiceArea.add(singleChoiceBtnA);
			choiceArea.add(singleChoiceBtnB);
			choiceArea.add(singleChoiceBtnC);
			choiceArea.add(singleChoiceBtnD);
			choiceArea.add(singleChoiceBtnE);
			questionArea.add(auxilliaryLabel2);
		}else if(questionType==1) {			//������ѡ����Ϊ��ȷ����ť�������·����������auxilliaryLabelռ�ݿռ�
			//����ռ�ռ��label��һ��һ�ң�choiceArea����
			JLabel auxilliaryLabel1 = new JLabel("");
			JLabel auxilliaryLabel2 = new JLabel("");
			auxilliaryLabel1.setPreferredSize(new Dimension(CHOICE_WIDTH,6 * CHOICE_HEIGHT));
			auxilliaryLabel2.setPreferredSize(new Dimension(CHOICE_WIDTH,6 * CHOICE_HEIGHT));
			
			//�滻��ѡ��ͼ��
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
			
			choiceArea.setLayout(new GridLayout(6,1,10,0));		//��ȡGridLayout���֣�ȷ������
			questionArea.add(auxilliaryLabel1);
			questionArea.add(choiceArea);
			choiceArea.add(multiChoiceBtnA);
			choiceArea.add(multiChoiceBtnB);
			choiceArea.add(multiChoiceBtnC);
			choiceArea.add(multiChoiceBtnD);
			choiceArea.add(multiChoiceBtnE);
			choiceArea.add(multiChoiceBtnF);
			questionArea.add(auxilliaryLabel2);
		}else if(questionType==2) {			//˫��
			//����ռ�ռ��Label������Label1��Label2ռ����ѡ���Ϸ���Blank(i)������Blank(ii)����ǩ�����࣬Label3��Label4ռ��ѡ����������
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
			
			//ѡ�������ʾ��ǩ��Blank(i)������Blank(ii)��
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
		}else {						//����
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
			
			//��ʾѡ�����ı�ǩ��Blank(i)������Blank(ii)����Blank(iii)��
			JLabel blank1Label = new JLabel("Blank(i)",JLabel.CENTER);
			JLabel blank2Label = new JLabel("Blank(ii)",JLabel.CENTER);
			JLabel blank3Label = new JLabel("Blank(iii)",JLabel.CENTER);
			blank1Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank2Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank3Label.setFont(new Font("Arial",Font.PLAIN,20));
			blank1Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			blank2Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			blank3Label.setPreferredSize(new Dimension(CHOICE_WIDTH,52));
			
			choiceArea.setLayout(new GridLayout(3,3,CHOICE_GAP,0));		//����GridLayout����֤��������
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
		
		//��ѡѡ�ť����������
		singleChoiceBtnA.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnB.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnC.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnD.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnE.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnF.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnG.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnH.addActionListener(new SingleChoiceBtnListener());
		singleChoiceBtnI.addActionListener(new SingleChoiceBtnListener());
		
		//������ť��
		JPanel operationArea = new JPanel();
		questionArea.add(operationArea);
		operationArea.setBackground(BG_COLOR);
		//�ύ��ť������ģʽ�²���ʾ
		submitBtn = new JButton("Submit");
		submitBtn.setFont(font);
		submitBtn.setForeground(Color.WHITE);
		submitBtn.setBackground(SUBMIT_BTN_COLOR);
		submitBtn.setBorderPainted(false);
		submitBtn.setFocusPainted(false);
		if(practiceMode1==1) {
			submitBtn.setVisible(false);
		}
		//�鿴������ť�����ύ֮��ų��֣��ٴΰ������ؽ�������������ʵ�֣�
		viewExplanationBtn = new JButton("View Explanation");
		viewExplanationBtn.setFont(font);
		viewExplanationBtn.setForeground(Color.WHITE);
		viewExplanationBtn.setBackground(VIEW_EXPLANATION_BTN_COLOR);
		viewExplanationBtn.setBorderPainted(false);
		viewExplanationBtn.setFocusPainted(false);
		viewExplanationBtn.setVisible(false);
		//��ť���������
		operationArea.add(submitBtn);
		operationArea.add(viewExplanationBtn);
		//����������
		submitBtn.addActionListener(new SubmitBtnListener());
		viewExplanationBtn.addActionListener(new ViewExplanationBtnListener());
		
		//�����ı���
		explanationArea = new JTextArea(explanation);
		questionArea.add(explanationArea);
		explanationArea.setLineWrap(true);		//�Զ�����
		explanationArea.setWrapStyleWord(true);	//���в�����
		explanationArea.setColumns(100);
		explanationArea.setEditable(false);		//�����޸�
		explanationArea.setFont(new Font("����",Font.PLAIN,16));
		if(!explanationShowing) {				//�Ƿ�ɼ�ȡ����explanationShowing����ֵ��viewExplanationBtn����
			explanationArea.setVisible(false);
		}
		
		/*
		 * �������������׵�����
		 */
		blankAreaLeft.setBackground(BG_COLOR);
		blankAreaRight.setBackground(BG_COLOR);
	}
	
	/*
	 * ��setting.txt��ȡ���ò�������Ŀ��Ϣ
	 * practiceMode1��practiceMode2��questionType��difficulty���ĸ�����
	 * �ɽ���������֮ǰ�����ý������ã�Ҳ�����ڴ�������Ͻ��и���
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
			JOptionPane.showMessageDialog(null,"δ�ҵ������ļ�","����",
					JOptionPane.ERROR_MESSAGE);	//�����ĸ���������Ϊnull
		}
	}
	
	/*
	 * ��������֮�󣬽��µ����ò���д��setting.txt
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
				System.out.println("�޷����ļ�");
			}
	}

	/*
	 * �������ͣ���questionStem.txt��ȡ�����
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
			JOptionPane.showMessageDialog(null,"δ�ҵ�������ļ�","����",
					JOptionPane.ERROR_MESSAGE);	//�����ĸ���������Ϊnull
		}
	}
	
	/*
	 * �������ò������ú��fetch_by_condition
	 * ��ȡ����������������Ŀ�����
	 * ����������qNo��
	 */
	public void getQuestionNo() {
		//����fetch_by_condition�����ݲ�����ȡ���з���Ҫ������
		try {
			qNum = 0;
			String[] arg = new String[]{"python","Question_provider.py","0",
					QTYPE[questionType],DIF[difficulty],DONE[done?1:0]};
			Process proc = Runtime.getRuntime().exec(arg);
			Scanner in = new Scanner(new InputStreamReader(proc.getInputStream()));
			in.useDelimiter("'");			//���õ����š�'����Ϊ�ָ�����ȡ
			int[] temp = new int[1000];		//��ʱ����������µ���������������Ŀ�����
			in.next();						//�Ȱ������Ŷ���
			while (in.hasNextInt()) {		//�����еõ����������д��temp������
				temp[qNum++] = in.nextInt() + 1;
				in.next();
			}
			in.close();
			//��������ŵ���д��qNo��
			qNo = new int[qNum];
			for(int i=0;i<qNum;i++) {
				qNo[i]=temp[qNum-i-1];
			}
			if(proc.waitFor()==0) System.out.println("��ȡ��ųɹ�");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ���ú��provider.get_collection
	 * ��ȡ���ղ���Ŀ�����
	 * ����������collection[]��
	 */
	public void getCollection() {
		try {
			collectionNum = 0;
			String[] arg = new String[]{"python","Question_provider.py","5","Question"};
			Process proc = Runtime.getRuntime().exec(arg);
			Scanner in = new Scanner(new InputStreamReader(proc.getInputStream()));
			in.useDelimiter("'");			//���õ����š�'����Ϊ�ָ�����ȡ
			collectionNo = new int[1000];	//����������µ��������ղ���Ŀ�����
			in.next();						//�Ȱ������Ŷ���
			while (in.hasNextInt()) {		//�����еõ����������д��temp������
				collectionNo[collectionNum++] = in.nextInt() + 1;
				in.next();
			}
			in.close();
			if(proc.waitFor()==0) System.out.println("��ȡ�ղ���ųɹ�");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * �����No��ȡ����ɡ���Ŀ���𰸡�����
	 */
	public void readQuestion(int No) {
		questionNo = No;			//д�����
		getQuestionStem();			//��ȡ�����
		getQuestion();				//��ȡ��Ŀ
		getAnswer();				//��ȡ��
		getExplanation();			//��ȡ����
		isMarked(No);				//�ж��Ƿ����ղ�
	}
	
	/*
	 * ���ú�˳����ȡ��Ŀ
	 * ���ú��fetch_by_num������Ż�ȡ��ɡ�ѡ���Լ���Ŀ��Ϣ
	 * ���������ĵ��ж�ȡ����Ŀ��Ϣ���жԱ�
	 * ���ú�˶�ȡ�𰸺ͽ���
	 */
	private void getQuestion(){
		try {
			String arg[] = new String[]{"python","Question_provider.py",
					"1",Integer.toString(questionNo - 1)};	//ע�⵽����ṩ����Ŀ��Ŵ�0��ʼ
			Process proc = Runtime.getRuntime().exec(arg);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String json = null;
			String temp = null;
			while ((temp = in.readLine()) != null) {	//������Json������ַ�������json��
				json = temp;
			}
			in.close();
			
			//����Json�����ȡ��Ŀ��Ϣ
			JSONObject object = JSONObject.parseObject(json);	//ת��ΪJson����
			innerQuestionNo = object.getInteger("num");			//����Section�е����
			questionStem = object.getString("text");			//����С���
			String innerQuestionType = object.getString("typ");		//��������
			section = Integer.parseInt(object.getString("sec"));//����section��
			String innerDifficulty = object.getString("dif");	//�����Ѷ�
			//��������ѡ��
			choiceA = object.getString("option_a");
			choiceB = object.getString("option_b");
			choiceC = object.getString("option_c");
			choiceD = object.getString("option_d");
			choiceE = object.getString("option_e");
			choiceF = object.getString("option_f");
			choiceG = object.getString("option_g");
			choiceH = object.getString("option_h");
			choiceI = object.getString("option_i");
			
			//��֤����
			if(!innerQuestionType.equals(QTYPE[questionType])) {//���ļ��е������������е����ͶԲ���
				JOptionPane.showMessageDialog(null,"��Ŀ������֤����","���ݳ���",
						JOptionPane.ERROR_MESSAGE);				//�򵯴���ʾ����
				System.exit(0);									//���˳�
			}
			
			//��֤���
			if(questionNo != QUESTION_COUNT * (section - 1) + innerQuestionNo) {//���ļ��е�����봫�����ŶԲ���
				JOptionPane.showMessageDialog(null,"��Ŀ�����֤����","���ݳ���",
						JOptionPane.ERROR_MESSAGE);				//�򵯴���ʾ����
				System.exit(0);									//���˳�
			}
			
			//��֤�Ѷ�
			if(!innerDifficulty.equals(DIF[difficulty])) {
				JOptionPane.showMessageDialog(null,"��Ŀ�Ѷ���֤����","���ݳ���",
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			
			if(proc.waitFor()==0) System.out.println("��ȡ��Ŀ�ɹ�");
		}catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ͨ����ţ����ú�˶�ȡ��Ŀ��
	 * �����������֤
	 */
	private void getAnswer() {
		//����get_answer��ȡ���ΪquestionNo����Ŀ�Ĵ�
		try {
			String[] arg = new String[]{"python","Question_provider.py","2",Integer.toString(questionNo - 1)};
			Process proc = Runtime.getRuntime().exec(arg);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String json = null;
			String temp = null;
			while ((temp = in.readLine()) != null) {	//������Json������ַ�������json��
				json = temp;
			}
			in.close();
			
			//����Json�����ȡ��
			JSONObject object = JSONObject.parseObject(json);	//ת��ΪJson����
			answer = object.getString("ans");
			
			//��֤���
			int num_temp = object.getInteger("n");
			if(num_temp!=questionNo) {									//���ļ��е�����������е���ŶԲ���
				JOptionPane.showMessageDialog(null,"�������֤����","���ݳ���",
						JOptionPane.ERROR_MESSAGE);						//�򵯴���ʾ����
				System.exit(0);											//���˳�
			}
			if(proc.waitFor()==0) System.out.println("��ȡ�𰸳ɹ�");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ͨ����ţ����ú�˶�ȡ��Ŀ�Ľ���
	 * �����������֤
	 */
	private void getExplanation() {
		//����get_answer��ȡ���ΪquestionNo����Ŀ�Ĵ�
		try {
			String[] arg = new String[]{"python","Question_provider.py","3",Integer.toString(questionNo - 1)};
			Process proc = Runtime.getRuntime().exec(arg);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String json = null;
			String temp = null;
			while ((temp = in.readLine()) != null) {	//������Json������ַ�������json��
				json = temp;
			}
			in.close();
			
			//����Json�����ȡ��
			JSONObject object = JSONObject.parseObject(json);	//ת��ΪJson����
			String exp = object.getString("exp");				//����
			String inter = object.getString("inter");			//����
			explanation = "������" + exp + "\n" + "���룺" + inter;

			//��֤���
			int num_temp = object.getInteger("n");				//����answer�е����
			if(num_temp!=questionNo) {									//���ļ��е�����������е���ŶԲ���
				JOptionPane.showMessageDialog(null,"���������֤����","���ݳ���",
						JOptionPane.ERROR_MESSAGE);						//�򵯴���ʾ����
				System.exit(0);											//���˳�
			}
			
			if(proc.waitFor()==0) System.out.println("��ȡ�����ɹ�");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ��ȡ��ѡ��
	 */
	private String scanAnswer() {
		String ans = "";
		if(questionType == 0){			//������ѡһ
			if(sinChoosen1 == singleChoiceBtnA) ans += "A";
			else if(sinChoosen1 == singleChoiceBtnB) ans += "B";
			else if(sinChoosen1 == singleChoiceBtnC) ans += "C";
			else if(sinChoosen1 == singleChoiceBtnD) ans += "D";
			else if(sinChoosen1 == singleChoiceBtnE) ans += "E";
		}else if(questionType == 1) {	//������ѡ��
			if (multiChoiceBtnA.isSelected()) ans += "A";
			if (multiChoiceBtnB.isSelected()) ans += "B";
			if (multiChoiceBtnC.isSelected()) ans += "C";
			if (multiChoiceBtnD.isSelected()) ans += "D";
			if (multiChoiceBtnE.isSelected()) ans += "E";
			if (multiChoiceBtnF.isSelected()) ans += "F";
		}else {							//˫�ջ�����
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
	 * ��������/�Ѷ����ã�����һ�⡱�󣬶�ȡ��Ŀ���ݲ�������ʾ
	 */
	private void reDisplay() {
		timer.removeActionListener(timeListener);
		second = 0;
		minute = 0;
		explanationShowing = false;
		System.gc();
		if(practiceMode1 == 0) {	//����ģʽ
			if(0 < qNum) {				//����Ŀʱ
				readQuestion(qNo[--qNum]);		//��ȡ��Ŀ
				panel.removeAll();
				placeComponent(panel);				//ˢ����ʾ
			}else {						//��Ŀ�����ˣ�������ʾ��������
				JOptionPane.showMessageDialog(frame, 
						QTYPE[questionType]+"��"+DIF[difficulty]+"��"+(done?("����"+""):"δ����")+"����Ŀ�Ѿ������ˣ���������/�ѶȰ�~", 
						"û����", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	/*
	 * �û��޸����ã���̨���Ĳ�������ʾ
	 * param��ʶ�û��Ĳ�����0-�޸����ͣ�1-�޸��Ѷȣ�2-��һ�⣬3-�˳�
	 */
	private void changeSetting(int param,int param1) {
		if(submitted) {		//�������ύ
			submitted = false;
			if(param == 0) {
				questionType = param1;
				getQuestionNo();		//���³���
			}else if(param == 1) {
				difficulty = param1;
				getQuestionNo();		//���³���
			}else if(param == 2);
			else if(param == 3) {
				saveSetting();			//�������ò���
				System.exit(0);			//�˳����ϲ����Ϊ�˻ص���ҳ��
			}
			reDisplay();			//ˢ�����ݲ�������ʾ
		}else {				//δ�ύ���򵯴���ʾ
			Object[] sub = {"�ύ","���ύ","ȡ��"};
			int sm = JOptionPane.showOptionDialog(frame,"����δ�ύ�����������¼�����ᱻ���档" + "\n" + "���Ƿ�Ҫ�ύ�𰸣�",
					"��ʾ",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null,sub,"�ύ");
			if (sm == 0) {		//ѡ���ύ������ɨ����ѡ�𰸣���д���̨
				String ans = scanAnswer();
				System.out.println(ans);
				System.out.println(answer);
				String rightOrWrong = ans.equals(answer)?"Right":"Wrong";
				try {
					String[] arg = new String[]{"python","Question_provider.py",
							"4",Integer.toString(questionNo - 1),ans,rightOrWrong};
					Process proc = Runtime.getRuntime().exec(arg);
					if(proc.waitFor()==0) System.out.println("���Ĵ�����ʷ�ɹ�");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(param == 0) {
					questionType = param1;
					getQuestionNo();		//���³���
				}else if(param == 1) {
					difficulty = param1;
					getQuestionNo();		//���³���
				}else if(param == 2);
				else if(param == 3) {
					saveSetting();			//�������ò���
					System.exit(0);			//�˳����ϲ����Ϊ�˻ص���ҳ��
				}
				reDisplay();			//ˢ�����ݲ�������ʾ
			}else if(sm == 1) {	//���ύ����������
				if(param == 0) {
					questionType = param1;
					getQuestionNo();		//���³���
				}else if(param == 1) {
					difficulty = param1;
					getQuestionNo();		//���³���
				}else if(param == 2);	//��һ��
				else if(param == 3) {
					saveSetting();			//�������ò���
					System.exit(0);			//�˳����ϲ����Ϊ�˻ص���ҳ��
				}
				reDisplay();			//ˢ�����ݲ�������ʾ
			}else if(sm == 2);	//ȡ��
		}
	}
	
	/*
	 * �ж����ΪNo����Ŀ�Ƿ����ղ�
	 * ����collectionNo���Ƿ���ڸ����
	 * �����ݽ������q.marked�Ĳ���ֵ
	 */
	private void isMarked(int No) {
		marked = false;		//Ԥ��Ϊ��
		for(int i=0;i<collectionNum;i++) {
			if(No == collectionNo[i]) {
				marked = true;
				break;
			}
		}
	}
	
	/*
	 * ������
	 */
	private class BySectionMenuListener implements ActionListener{			//�˵��е�������ϰģʽ
		public void actionPerformed(ActionEvent event) {
			practiceMode1 = 1;
			questionTypeMenu.setVisible(false);		//���ز˵����е��������ò˵�
			/*
			 * ���ó���ģ�����³��⣬���½���ҳ��
			 */
		}
	}
	
	private class BySingleMenuListener implements ActionListener{			//�˵��еĵ�����ϰģʽ
		public void actionPerformed(ActionEvent event) {
			practiceMode1 = 0;
			questionTypeMenu.setVisible(true);		//��ʾ�˵����е��������ò˵�
			/*
			 * ���ó���ģ�����³��⣬���½���ҳ��
			 */
		}
	}
	
	private class ByNewMenuListener implements ActionListener{				//�˵��е�������ϰģʽ
		public void actionPerformed(ActionEvent event) {
			practiceMode2 = 0;
			/*
			 * ���ó���ģ�����³��⣬���½���ҳ��
			 */
		}
	}
	
	private class ByWrongMenuListener implements ActionListener{			//�˵��еĴ����ز���ϰģʽ
		public void actionPerformed(ActionEvent event) {
			practiceMode2 = 1;
			/*
			 * ���ó���ģ�����³��⣬���½���ҳ��
			 */
		}
	}
	
	private class OneBlankOneChoiceMenuListener implements ActionListener{	//�˵��е��������ã�������ѡһ
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,0);
		}
	}
	
	private class OneBlankTwoChoiceMenuListener implements ActionListener{	//�˵��е��������ã�������ѡ��
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,1);
		}
	}
	
	private class TwoBlankMenuListener implements ActionListener{			//�˵��е��������ã�˫��
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,2);
		}
	}
	
	private class ThreeBlankMenuListener implements ActionListener{			//�˵��е��������ã�����
		public void actionPerformed(ActionEvent event) {
			changeSetting(0,3);
		}
	}
	
	private class EasyMenuListener implements ActionListener{				//�˵��е��Ѷ����ã���
		public void actionPerformed(ActionEvent event) {
			changeSetting(1,0);
		}
	}
	
	private class MedianMenuListener implements ActionListener{				//�˵��е��Ѷ����ã��е�
		public void actionPerformed(ActionEvent event) {
			changeSetting(1,1);
		}
	}
	
	private class HardMenuListener implements ActionListener{				//�˵��е��Ѷ����ã�����
		public void actionPerformed(ActionEvent event) {
			changeSetting(1,2);
		}
	}
	
	private class MarkedCollectionListener implements ActionListener{				//�˵��еĲ鿴�ղؼ�
		public void actionPerformed(ActionEvent event) {
			
		}
	}
	
	private class WrongCollectionListener implements ActionListener{				//�˵��еĲ鿴���Ȿ
		public void actionPerformed(ActionEvent event) {
			
		}
	}
	
	private class QuitBtnListener implements ActionListener{				//�������е��˳�
		public void actionPerformed(ActionEvent event) {
			changeSetting(3,0);
		}
	}
	
	private class MarkBtnListener implements ChangeListener{				//�������е��ղ�
		public void stateChanged(ChangeEvent event) {
			try {
				String arg[] = new String[]{"python","Question_provider.py",
						"6","Question",marked?"delete":"add",Integer.toString(questionNo - 1)};
				Process proc = Runtime.getRuntime().exec(arg);
				BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				in.close();
				if(proc.waitFor()==0) System.out.println(marked?"ȡ��":"" + "�ղسɹ�");
			}catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class BackBtnListener implements ActionListener{				//�������еķ���
		public void actionPerformed(ActionEvent event) {
			/*
			 * �ݴ汾������
			 * ������һ��
			 */
		}
	}
	
	private class NextBtnListener implements ActionListener{				//�������е���һ��
		public void actionPerformed(ActionEvent event) {
			changeSetting(2,0);
		}
	}
	
	private class TimeHiddingBtnListener implements ActionListener{			//����ʱ�䰴ť
		public void actionPerformed(ActionEvent event) {
			if(!timeHidden) {						//�����ǰʱ������ʾ��
				timeHiddingBtn.setText("| Show Time");	//��ť��Ϊ����ʾʱ�䰴ť��
				timeLabel.setVisible(false);			//ʱ���ǩ��Ϊ���ɼ�
				timeHidden = true;
			}else {									//�����ǰʱ�������ص�
				timeHiddingBtn.setText("| Hide Time");	//��ť��Ϊ������ʱ�䰴ť��
				timeLabel.setVisible(true);				//ʱ���ǩ��Ϊ�ɼ�
				timeHidden = false;
			}
			
		}
	}
	
	private class SingleChoiceBtnListener implements ActionListener{	//��ѡ��ѡ�������ѡһ��˫�ջ�������һ��ѡ�
		public void actionPerformed(ActionEvent event) {
			ChooseHighlight((JButton)event.getSource());	//���÷��������ð�ť����Ϩ��ͬ���������ť
		}
	}
	
	private class SubmitBtnListener implements ActionListener{			//�ύ��ť
		public void actionPerformed(ActionEvent event) {	//��ȡ�𰸣�����ʾ�������İ�ť
			if(practiceMode1 == 0) {		//����ģʽ
				String ans = scanAnswer();
				System.out.println(ans);
				System.out.println(answer);
				String rightOrWrong = ans.equals(answer)?"Right":"Wrong";
				submitted = true;
				
				/*
				 * ��ʾ��ȷ��������ʾ��
				 */
				
				//���ú��answer_history_alter�����Ĵ�����ʷ
				try {
					String[] arg = new String[]{"python","Question_provider.py",
							"4",Integer.toString(questionNo - 1),ans,rightOrWrong};
					Process proc = Runtime.getRuntime().exec(arg);
					if(proc.waitFor()==0) System.out.println("���Ĵ�����ʷ�ɹ�");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				viewExplanationBtn.setVisible(true);			//��ʾ����ʾ��������ť
			}
		}
	}
	
	private class ViewExplanationBtnListener implements ActionListener{	//��ʾ������ť
		public void actionPerformed(ActionEvent event) {
			if(!explanationShowing) {							//�����ǰ����δ��ʾ
				explanationShowing = true;							//���ı�־λ
				explanationArea.setVisible(true);					//������Ϊ�ɼ�
				viewExplanationBtn.setText("Retract Explanation");	//��ť��Ϊ���ջؽ�����
			}else {												//����
				explanationShowing = false;							//���ı�־λ
				explanationArea.setVisible(false);					//������Ϊ���ɼ�
				viewExplanationBtn.setText("View Explanation");		//��ť��Ϊ���鿴������
			}
		}
	}
	
	private class TimeListener implements ActionListener{				//��ʱ��
			public void actionPerformed(ActionEvent evt) {
				second++;
				if (second==60) {	//������λ����
					minute++;
					second = 0;
				}
				timeLabel.setText(minute + ":" + second);
			}
	}
	
	/*
	 * ��ѡѡ��ѡ�е���ʾЧ��
	 */
	
	private void ChooseHighlight(JButton JBtn) {
		if(questionType==0) {	//������ѡһ	
			if(sinChoosen1!=null) {
				//��֮ǰ��ѡ��ѡ��ĳɰ׵׺���
				sinChoosen1.setBackground(Color.WHITE);
				sinChoosen1.setForeground(Color.BLACK);
			}
			//������ѡ�е�ѡ��ĳɺڵװ���
			JBtn.setBackground(Color.BLACK);
			JBtn.setForeground(Color.WHITE);
			//sinChoosen1ָ�򱾴�ѡ�е�ѡ��
			sinChoosen1 = JBtn;
		}else if((questionType==2) || (questionType==3)) {	//���ѡ���⣨˫�ջ����գ���ÿ��������ѡ��
			if((JBtn==singleChoiceBtnA) || (JBtn==singleChoiceBtnB) || (JBtn==singleChoiceBtnC)) {	//��ѡ��Ϊ��һ�յĴ�
				if(sinChoosen1!=null) {
					//��֮ǰ��ѡ�ĸ���ѡ��ĳɰ׵׺���
					sinChoosen1.setBackground(Color.WHITE);
					sinChoosen1.setForeground(Color.BLACK);
				}
				//������ѡ�е�ѡ��ĳɺڵװ���
				JBtn.setBackground(Color.BLACK);
				JBtn.setForeground(Color.WHITE);
				//sinChoosen1ָ�򱾴�ѡ�е�ѡ��
				sinChoosen1 = JBtn;
			}else if((JBtn==singleChoiceBtnD) || (JBtn==singleChoiceBtnE) || (JBtn==singleChoiceBtnF)) {	//��ѡ��Ϊ�ڶ��յĴ�
				if(sinChoosen2!=null) {
					//��֮ǰ��ѡ��ѡ��ĳɰ׵׺���
					sinChoosen2.setBackground(Color.WHITE);
					sinChoosen2.setForeground(Color.BLACK);
				}
				//������ѡ�е�ѡ��ĳɺڵװ���
				JBtn.setBackground(Color.BLACK);
				JBtn.setForeground(Color.WHITE);
				//sinChoosen1ָ�򱾴�ѡ�е�ѡ��
				sinChoosen2 = JBtn;
			}else {	//��ѡ��Ϊ�����յĴ𰸣�������ڣ�
				if(sinChoosen3!=null) {
					//��֮ǰ��ѡ��ѡ��ĳɰ׵׺���
					sinChoosen3.setBackground(Color.WHITE);
					sinChoosen3.setForeground(Color.BLACK);
				}
				//������ѡ�е�ѡ��ĳɺڵװ���
				JBtn.setBackground(Color.BLACK);
				JBtn.setForeground(Color.WHITE);
				//sinChoosen1ָ�򱾴�ѡ�е�ѡ��
				sinChoosen3 = JBtn;
			}
		}else {	//������ѡ��
			/*
			 * ������������ʡ��
			 */
		}
	}
}
