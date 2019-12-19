import json

'''
存储一些工具方法和工具参数

'''

#文件名称
questions_contents_name='questions_contents.json'
questions_structure_name = 'questions_structure.json'
answer_history_name = 'answer_history.json'
answers_name = "answers.json"
explanations_name = "explanations.json"
collection_name = 'collection.json'




#存储所有题目的类型、难度
contents_file = open(questions_contents_name,'r',encoding='utf8')
contents_json = json.load(contents_file)
contents_file.close()

# print(json)
typs = set()
difs = set()
for num,value in contents_json.items():
	typs.add(value['typ'])
	difs.add(value['dif'])


typs = list(typs)
difs = list(difs)
