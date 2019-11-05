from prettyprinter import cpprint

from Question_provider import Question_provider
from tools import *


'''
这个类的作用是根据考场参数反复调用抽题模块，并且设计paper这么一个东西，还要提供review和judege的功能

'''

class Examiner():
	#初始化进入的时候paper应该是None,room_params也是None,外界有一个room_change，会改变room_params
	def __init__(self):
		self.provider = Question_provider()
		self.paper = None
		self.room_params = None
		self.result = None




	#调用方法的时候检查room_params是否为None进行判断，如果非空，根据参数构建paper,然后返回paper
	#现暂时给定两种room_params,一种是错题重测(1)，会返回所有错题，一种是随机未做过的题,各类题型(0)
	def test(self):
		if self.room_params == 0:
			nums = []
			for typ in typs:
				for dif in difs: 
					nums += self.provider.fetch_by_condition(typ=typ,dif=dif,doneOrnot='undone',RightOrWrong='Right')

		elif self.room_params == 1:
			nums = []
			for typ in typs:
				for dif in difs:
					nums+=self.provider.fetch_by_condition(typ=typ,dif=dif,doneOrnot='done',RightOrWrong='Wrong')

		else:
			nums=[]

		paper = self.paper_make(nums)
		self.paper = paper
		# print(paper)
		return paper 

	#对paper进行评估，注意对于paper内没有回答过的题目，一律不批卷，最后修改paper为一个做过（本次test）的题目的paper
	def judge(self):
		if self.paper == None:
			print('请先选择试卷')
			return None
		else:
			self.result ={'Right':{},'Wrong':{}}
			#仅仅批改done中的
			for num,question in self.paper['done'].items():
				if question['correct_answer']==question['write']:
					self.result['Right'][num]=question
					#调用方法修改回答历史
					self.answer_history_alter(num,question['write'],'Right')
				else:
					self.result['Wrong'][num]=question
					self.answer_history_alter(num,question['write'],'Wrong')
		return self.result
	
	#review要呈现所有错题和explanation
	def review(self):
		reviewer = {}
		for num,value in self.result['Wrong'].items():
			explanation = self.provider.get_explanation(num)
			value['explanation']=explanation['exp']
			reviewer[num]=value
		return reviewer

	#工具函数，给定questions list返回paper数据结构
	def paper_make(self,nums):
		#paper的结构，一级是done or undone，二级是题号，对应一个list，里面存放了答案和解答
		paper = {'done':{},'undone':{}}
		for num in nums:
			question = self.provider.fetch_by_num(num)
			#调用make的时候题目都是没做（这里的没做指的是这次test里没做，和回答历史内的概念不一样）
			#在paper上插入这一题
			paper['undone'][num]={'correct_answer':None,'write':None}
			#把答案插入
			answer = self.provider.get_answer(num)
			paper['undone'][num]['correct_answer']=answer['ans']
		return paper

	#工具方法，被judge，修改回答历史
	def answer_history_alter(self,num,answer,rightOrwrong="Wrong"):
		self.provider.answer_history_alter(num,answer,rightOrwrong="Wrong")


if __name__ == '__main__':
	examiner = Examiner()
	examiner.room_params = 0
	examiner.test()
	# print(examiner.paper)
	examiner.judge()
	print(examiner.review())