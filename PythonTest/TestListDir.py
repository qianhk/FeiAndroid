#!/usr/bin/python
# coding=utf-8

import os


# 获取当前目录中所有的apk源包
src_apks = []
# python3 : os.listdir()即可，这里使用兼容Python2的os.listdir('.')
for file in os.listdir('.'):
    if os.path.isfile(file):
        extension = os.path.splitext(file)[1][1:]
        logTxt = "file={vfile} extension={exten}".format(vfile=file,exten=extension)
        print logTxt
        if extension == 'apk':
            src_apks.append(file)


for src_apk in src_apks:
    print("after=" + src_apk)
    # file name (with extension)
    src_apk_file_name = os.path.basename(src_apk)
    # 分割文件名与后缀
    temp_list = os.path.splitext(src_apk_file_name)
    # name without extension
    src_apk_name = temp_list[0]
    # 后缀名，包含.   例如: ".apk "
    src_apk_extension = temp_list[1]

    # 创建生成目录,与文件名相关
    output_dir = 'output_' + src_apk_name + '/'

