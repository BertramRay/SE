from Question_provider import Question_provider



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
	#现暂时给定两种room_params
	def test(self):
		pass

	def judge(self):
		pass

	def review(self):
		pass


if __name__ == '__main__':
	pass