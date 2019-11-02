from Json_maintain import Json_maintain

'''
	抽题器，给定参数（类型，难度，是否做过，是否做对，返回一道题目）

'''

class Question_provider():
	def __init__ (self):
		self.Json_maintainer = Json_maintain()

	#现在假定会有两种选择题目的模式，一种是通过条件找到题目，一种是查看做错的题目,返回的是一个装着题号的list
	def fetch_by_condition(self,typ,diff,doneOrnot,RightOrWrong=None):
		#
		if RightorWrong ！=None:
			l = list(self.Json_maintainer[RightOrWrong].keys())
			return l
		else:
			num = self.Json_maintainer.quesions_repo['']

		pass

	#通过题号获取题目
	def fetch_by_num(self,num):
		question =  self.Json_maintainer.questions_contents[num]
		return question

	#获取对应题号的题目的答案
	def get_answer(self):
		pass

	#返回答题结果，Json_maintainer的answer_history进行修改
	def answer_history_alter(self):
		pass


