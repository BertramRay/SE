import json
from prettyprinter import cpprint

'''
用于转换队友的json为我们所需要的格式
一级以题目类型进行分类，二级以题目难度区分，仅存储题号，题目文件另外存储
一共会有三个文件
questions_structure
questions_contents
answer_history


'''

#创建题目结构
def structure_create(src_name = "questions_contents.json",dest_name = "questions_structure.json"):

	src_file = open(src_name,'r+',encoding="utf8")
	src_json = json.load(src_file)

	dest_dic = {}


	#循环每一道题目
	for key,value in src_json.items():
		typ = value['typ']
		#判断该题的题型是否在dic中，如果不在，加入
		if not typ in dest_dic.keys():
			dest_dic[typ] ={}

		#判断该题型内部是否存在对应难度的题目
		dif = value['dif']
		if not dif in dest_dic[typ].keys():
			dest_dic[typ][dif]=[]
		num = value['num']
		dest_dic[typ][dif].append(key)

	# print(type(dest_dic))
	dest_dic = json.dumps(dest_dic)
	# print(type(dest_dic))
	file = open(dest_name,'w+',encoding="utf8")
	file.write(dest_dic)
	file.close()


#创建回答历史，结构为正确/错误  '题号':回答
def answer_history_create(dest_name = "answer_history.json"):

	answer_history = {'Right':{},'Wrong':{}}

	answer_history_json = json.dumps(answer_history)
	# print(answer_history_json)
	file = open('answer_history.json','w',encoding="utf8")
	file.write(answer_history_json)
	file.close


if __name__ == '__main__':
	structure_create()
	answer_history_create()

	file = open('explanations.json','r',encoding ="utf8")
	json = json.load(file)
	file.close()
	cpprint(json)


