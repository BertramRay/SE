package questioning;

public class test {
	/*
	 * 主类，只起调用功能
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				Questioning q = new Questioning();
				q.readSetting();			//获取设置信息
				q.getQuestionNo();			//根据设置获取相应的题号
				q.getCollection();			//读取已收藏的题号
				if(0 < q.qNum) {			//有题目时
					q.readQuestion(q.qNo[--q.qNum]);	//读取题号最小的题目的所有信息
				}
				q.questioningPage();		//显示页面
			}
		});
	}
}
