from Question_provider import Question_provider
from Examiner import Examiner

'''
主函数，根据所给参数调用对应方法


'''

#初始化各个类,初始化一个examiner对象，将会用到的函数装入list

examiner = Examiner()

examiner_function_list=[examiner.room_change,examiner.test,examiner.judge,examiner.review,examiner.paper_make,examiner.answer_history_alter]
provider_function_list=[examiner.provider.fetch_by_condition,examiner.provider.fetch_by_num,examiner.provider.get_answer,examiner.provider.get_explanation]

mod_list = [examiner_function_list,provider_function_list]


#主函数根据给定的参数,调用对应方法
def main(mod_num = 0,function_num=0,*params):

	res = mod_list[mod_num][function_num](*params)
	return res
if __name__ == '__main__':
	main(0,0,1)
	res = main(0,1)
	print(res)