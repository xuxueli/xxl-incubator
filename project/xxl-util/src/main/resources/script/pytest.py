#!/usr/bin/python
# -*- coding: UTF-8 -*-

import logging
import time

logging.basicConfig(level=logging.DEBUG)

logging.debug('debug hello python')
logging.info('info hello python')
logging.info('warning hello python')
print 'print hello python'

for num in range(0, 3):
	time.sleep(1)
	logging.info('warning 当前序号 :' + str(num) )
	print'当前序号 :', num

logging.info('warning Good bye!')

print "Good bye!"

