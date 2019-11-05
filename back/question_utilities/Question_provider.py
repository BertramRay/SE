from Json_maintain import Json_maintain

'''
	抽题器，给定参数（类型，难度，是否做过，是否做对，返回一道题目）

'''

class Question_provider():
	def __init__ (self):
		self.Json_maintainer = Json_maintain()

	#通过条件筛选题目，如果是没有做过的题目，不会有对错选项，所以对错选项默认置为None
	def fetch_by_condition(self,typ,dif,doneOrnot,RightOrWrong=None):
		#判断是做过的还是没做过的
		if doneOrnot =='done':
			#做过的，判断是Right还是Wrong
			if RightOrWrong == 'Right':
				l = self.Json_maintainer.questions_repo['done']['Right'][typ][dif]
			elif RightOrWrong == 'Wrong':
				l = self.Json_maintainer.questions_repo['done']['Wrong'][typ][dif]
			elif RightOrWrong == 'All':
				l = self.Json_maintainer.questions_repo['done']['Right'][typ][dif]+self.Json_maintainer.questions_repo['done']['Wrong'][typ][dif]
			else:
				l = None
		elif doneOrnot == 'undone':
			l = self.Json_maintainer.questions_repo['undone'][typ][dif]

		else:
			l = None

		return l


	#通过题号获取题目
	def fetch_by_num(self,num):
		question =  self.Json_maintainer.questions_contents[num]
		return question

	#获取对应题号的题目的答案
	def get_answer(self,num):
		answer = self.Json_maintainer.answers[num]
		return answer

	#获取对应题号的题目的解析
	def get_explanation(self,num):
		explanation = self.Json_maintainer.explanations[num]
		return explanation

	#返回答题结果，Json_maintainer的answer_history进行修改
	def answer_history_alter(self,num,answer,rightOrwrong="Wrong"):
		self.Json_maintainer.answer_history_alter(num,answer,rightOrwrong="Wrong")


if __name__ == '__main__':
	provider = Question_provider()
	#根据条件选题目
	question_nums = provider.fetch_by_condition(typ ='双空',dif = 'easy',doneOrnot='done',RightOrWrong='Wrong')
	print(question_nums)
	#根据序号返回题目
	questions = []
	answers = []
	for num in question_nums:
		question = provider.fetch_by_num(num)
		questions.append(question)
		answer = provider.get_answer(num)
		answers.append(answer)
	print(questions)
	print(answers)
	provider.answer_history_alter('2','a','Wrong')
	question_nums = provider.fetch_by_condition(typ ='双空',dif = 'easy',doneOrnot='done',RightOrWrong='Right')
	print(question_nums)