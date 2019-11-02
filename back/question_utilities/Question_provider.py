from Json_maintain import Json_maintain

'''
	抽题器，给定参数（类型，难度，是否做过，是否做对，返回一道题目）

'''

class Question_provider():
	def __init__ (self):
		self.Json_maintainer = Json_maintain()

	#通过条件筛选题目，如果是没有做过的题目，不会有对错选项，所以对错选项默认置为None
	def fetch_by_condition(self,typ,diff,doneOrnot,RightOrWrong=None):
		#判断是做过的还是没做过的
		if doneOrnot =='done':
			#做过的，判断是Right还是Wrong
			if RightOrWrong == 'Right':
				l = self.Json_maintainer.questions_repo['done']['Right'][typ][diff]
			elif RightOrWrong == 'Wrong':
				l = self.Json_maintainer.questions_repo['done']['Wrong'][typ][diff]
			else:
				l = None
		elif doneOrnot == 'Undone':
			l = self.Json_maintainer.questions_repo['Undone'][typ][diff]

		else:
			l = None

		return l


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


if __name__ == '__main__':
	pass