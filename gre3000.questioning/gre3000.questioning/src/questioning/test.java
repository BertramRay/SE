package questioning;

public class test {
	/*
	 * ���ֻ࣬����ù���
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				Questioning q = new Questioning();
				q.readSetting();			//��ȡ������Ϣ
				q.getQuestionNo();			//�������û�ȡ��Ӧ�����
				q.getCollection();			//��ȡ���ղص����
				if(0 < q.qNum) {			//����Ŀʱ
					q.readQuestion(q.qNo[--q.qNum]);	//��ȡ�����С����Ŀ��������Ϣ
				}
				q.questioningPage();		//��ʾҳ��
			}
		});
	}
}
