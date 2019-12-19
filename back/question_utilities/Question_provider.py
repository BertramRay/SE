from Json_maintain import Json_maintain
import sys


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
		self.Json_maintainer.answer_history_alter(num,answer,rightOrwrong=rightOrwrong)


	#将题目进行收藏
	def collection_alter(self,typ,fun,num):
		self.Json_maintainer.collection_alter(typ,fun,num)

	#获取收藏的题目
	def get_collection(self,typ):
		if typ in self.Json_maintainer.collection.keys():
			return self.Json_maintainer.collection[typ]

def main(function_no,params):
	# print(function_no)
	provider = Question_provider()
	#六种功能
	funcion_list = [provider.fetch_by_condition,
	provider.fetch_by_num,
	provider.get_answer,
	provider.get_explanation,
	provider.answer_history_alter,
	provider.get_collection,
	provider.collection_alter
	]

	#通过params的参数个数
	length = len(params)
	if length ==1:
		param_1 = params[0]
		res = funcion_list[function_no](param_1)
		return res

	if length ==2:
		param_1 = params[0]
		param_2 = params[1]
		res = funcion_list[function_no](param_1,param_2)
		return res
	
	if length ==3:
		param_1 = params[0]
		param_2 = params[1]
		param_3 = params[2]
		res = funcion_list[function_no](param_1,param_2,param_3)
		return res
	
	if length ==4:
		param_1 = params[0]
		param_2 = params[1]
		param_3 = params[2]
		param_4 = params[3]
		res = funcion_list[function_no](param_1,param_2,param_3,param_4)
		return res
	
	


if __name__ == '__main__':
	# provider = Question_provider()
	# print(provider.get_collection('Question'))
	# provider.collection_alter('Question','add','2')
	# print(provider.get_collection('Question'))
	# #根据条件选题目
	# question_nums = provider.fetch_by_condition(typ ='双空',dif = 'easy',doneOrnot='done',RightOrWrong='Wrong')
	# print(question_nums)
	# #根据序号返回题目
	# questions = []
	# answers = []
	# for num in question_nums:
	# 	question = provider.fetch_by_num(num)
	# 	questions.append(question)
	# 	answer = provider.get_answer(num)
	# 	answers.append(answer)
	# print(questions)
	# print(answers)
	# provider.answer_history_alter('2','a','Wrong')
	# question_nums = provider.fetch_by_condition(typ ='双空',dif = 'easy',doneOrnot='done',RightOrWrong='Right')
	# print(question_nums)

	args = sys.argv
	#功能参数
	fun_no = int(args[1])
	#函数参数
	fun_param = args[2:]
	print(fun_no)
	res = main(fun_no,fun_param)
	print(res)
