import json
from prettyprinter import cpprint

from json_transfer import structure_create
from tools import *


'''
用以维护题目的源文件以及提供存储题目的数据结构（存着所有题目的dic，存着题目类型/难度/题号的structure，存着回答历史的answer_history）

'''


class Json_maintain():
	def __init__(self,questions_contents_name=questions_contents_name,questions_structure_name=questions_structure_name,answer_history_name=answer_history_name,answers_name=answers_name,explanations_name =explanations_name):
		self.questions_contents_name = questions_contents_name
		self.questions_structure_name = questions_structure_name
		self.answer_history_name =answer_history_name
		self.answers_name = answers_name
		self.explanations_name = explanations_name
		#读入json
		self.questions_contents = None
		self.questions_structure = None
		self.answer_history = None
		self.answers = None
		self.explanations = None

		self.load()
		#构建题库数据结构
		self.questions_repo = self.repo_build()

	#退出程序时候的操作
	def exit(self):
		#调用refresh方法
		refresh()

	#修改answer_history的数据结构，目前只能回答，也就是没有把回答记录删去的功能，只能覆盖回答记录
	def answer_history_alter(self,num,answer,rightOrwrong="Wrong"):
		#修改类内部的数据结构
		#首先判断这道题目有没有答过，答了，就要删除原先的答题记录
		if num in self.answer_history['Right'].keys(): 
			del self.answer_history['Right'][num]
		if num in self.answer_history['Wrong'].keys():
			del self.answer_history['Wrong'][num]

		self.answer_history[rightOrwrong][num] = answer
		#利用refresh将新内容冲回文件,同时也刷新了repo和structure，一举两得
		self.refresh()

	#修改question_contents的内容
	def questions_contents_alter(self):
		pass


	#保存题库文件和answer_history文件
	def save(self):
		#保存question_contents
		self.json_save(self.questions_contents,self.questions_contents_name)
		#保存structure
		self.json_save(self.questions_structure,self.questions_structure_name)
		#保存answer_history?
		self.json_save(self.answer_history,self.answer_history_name)
		#保存answers
		self.json_save(self.answers,self.answers_name)
		#保存explanations
		self.json_save(self.explanations,self.explanations_name)

	#读入所有题库文件和answer_history文件
	def load(self):
		self.questions_contents = self.json_load(self.questions_contents_name)
		self.questions_structure = self.json_load(self.questions_structure_name)
		self.answer_history = self.json_load(self.answer_history_name)
		self.explanations = self.json_load(self.explanations_name)
		self.answers = self.json_load(self.answers_name)

	#刷新repo，由于answer_history在使用的过程中可能会变，日后可能会添加自己增加题目的功能,本质是刷新所有文件和类内数据结构
	def refresh(self):
		#第一步，利用save保存contents
		self.save()
		#第二步，利用从json_transfer引入的构建structure的方法创建新的structure然后引入新的structure
		structure_create(self.questions_contents_name,self.questions_structure_name)
		#第三步,利用load读入新的文件
		self.load()

		#第三步，结合answer_history，用repo_build，构建新的repo
		self.questions_repo = self.repo_build()


	#给定json文件名称，读入json
	def json_load(self,src):
		file = open(src,"r+",encoding="utf8")
		contents = json.load(file)
		file.close()
		return contents

	#给定json文件名称，写入json
	def json_save(self,dic,dest):
		dest_json = json.dumps(dic)
		file = open(dest,"w",encoding="utf8")
		file.write(dest_json)
		file.close()

	#结合回答历史，做一个新的repo,repo的一级结构是这道题目是否回答过，二级结构是这道题目是否回答正确，三级结构之后就和questions_structure一样了
	def repo_build(self):
		dic = {'done':{'Right':{},'Wrong':{}},'undone':{}}
		for typ_k,typ_v in self.questions_structure.items():
			#判断题型是否加入字典
			if typ_k not in dic['done']['Right'].keys():
				dic['done']['Right'][typ_k] ={}
			if typ_k not in dic['done']['Wrong'].keys():
				dic['done']['Wrong'][typ_k] ={}
			if typ_k not in dic['undone'].keys():
				dic['undone'][typ_k] ={}
			for dif_k,dif_v in typ_v.items():
				#判断难度
				if dif_k not in dic['done']['Right'][typ_k].keys():
					dic['done']['Right'][typ_k][dif_k] = []
				if dif_k not in dic['done']['Wrong'][typ_k].keys():
					dic['done']['Wrong'][typ_k][dif_k] = []
				if dif_k not in dic['undone'][typ_k].keys():
					dic['undone'][typ_k][dif_k] = []
				
				#接下来判断每个题号是否在answer_history中
				for num in dif_v:
					if num in self.answer_history['Right'].keys():
						dic['done']['Right'][typ_k][dif_k].append(num)
					if num in self.answer_history['Wrong'].keys():
						dic['done']['Wrong'][typ_k][dif_k].append(num)
					else:
						dic['undone'][typ_k][dif_k].append(num)

		return dic


if __name__ == '__main__':
	json_maintainer = Json_maintain()


	# cpprint(json_maintainer.questions_repo)
	# # cpprint(json_maintainer.questions_structure)
	cpprint(json_maintainer.answer_history)
	json_maintainer.answer_history_alter('2','a','Right')
	cpprint(json_maintainer.answer_history)
	# cpprint(json_maintainer.explanations)


