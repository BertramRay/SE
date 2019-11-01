import json
from prettyprinter import cpprint


class Json_Maintain():
	def __init__(self,jsonname="jsonFile.json"):
		#读入json文件
		self.jsonfile = open(jsonname,"r+",encoding="utf8")
		self.questions = json.load(self.jsonfile)
		self.jsonfile.close()
		#读入答题历史
		self.answer_history = None
		#构建题库数据结构
		self.questions_repo = None

	#退出程序时候的操作
	def exit(self):
		pass

	#修改answer_history的数据结构
	def answer_history_alter(self):
		pass

	#保存题库文件和answer_history文件
	def refresh(self):
		pass




if __name__ == '__main__':
	json_maintainer = Json_Maintain()
	# cpprint(json_maintainer.questions)
	s = set()
	for key,value in json_maintainer.questions.items():
		s.add(value['dif'])


	print(s)