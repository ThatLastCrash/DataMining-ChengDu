# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

import numpy as np
import pandas as pd
import requests
import csv
import json

def getAddress(rows):
    global keyi
    global api_count
    location = ''
    for row in rows:
        x1=row[0]
        y1=row[1]
        print('now location:', x1, y1)
        if location != '':
            location += "|"
        location += str(x1) + "," + str(y1)
    # 构建URL
    u = "https://restapi.amap.com/v3/geocode/regeo?location="+location+"&key=" + keys[
        keyi] + '&batch=true&radius=1000&extensions=all&output=json'
    print(u+"  api_count="+str(api_count))

    api_count += 1  # 记录一次调用
    # 解析数据
    data = requests.get(u)
    s = data.json()

    # 查询错误
    if s['status'] != '1':
        print('eror!')
        return
    count = 0
    for row in rows:
        row.append(s['regeocodes'][count]['formatted_address'])
        csv_writer.writerow(row)
        count = count + 1

# main
keys = ["4081a7b2d93ad685bdc94258a6c10b5f","d1acfb2fec9aea25b331abd5c8cc97d8","8e41837f5caf46edacc9874136c1a1af",
        "1b05cfb1055f5d9a88605c46e8e1208f","1c13e504c15beaf4652d86c6edab07af"]  # 自己申请的10个key
global keyi  # 当前使用的key编号
global api_count  # 每一个key调用api的次数
keyi = 0
api_count = 0

f = open(r'D:\NNU\exercise\Java\DataMining\MapData\14\data14.csv', 'r', encoding='utf_8_sig', newline='')
wf = open(r'D:\NNU\exercise\Java\DataMining\Driver\result_14.csv', 'a+', encoding='utf_8_sig', newline='')
csv_reader=csv.reader(f)
csv_writer = csv.writer(wf)
csv_writer.writerow(["经度", "纬度", "聚类编号","地址"])
data = []

header = next(csv_reader)        # 读取第一行每一列的标题
cnt=0
cntrow=0
each=[]
for row in csv_reader:  # 将csv 文件中的数据保存到data中
    each.append(row)
    cnt = cnt+1
    if cnt == 10:
        cntrow+=10
        cnt = 0
        if cntrow>5540:
            getAddress(each)
        each=[]
    # data.append(row)  # 选择某一列加入到data数组中
getAddress(each)
#
f.close()
wf.close()
