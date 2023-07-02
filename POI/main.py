# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

import numpy as np
import pandas as pd
import requests
import csv
import json
x=104.1022
y=30.728
f = open(str(x)+", "+str(y)+'.csv', 'w', encoding='utf_8_sig', newline='')
csv_writer = csv.writer(f)
csv_writer.writerow(["名字", "类型", "POI代码", "坐标", "地址", "所属商圈"])


def search_poi(x1, y1, x2, y2, page):
    global keyi
    global api_count
    print('now location:', x1, y1, x2, y2,end=" ")

    polygon = str(x1) + "," + str(y1) + "|" + str(x2) + "," + str(y2)
    halfx = (x2 - x1)/2
    halfy = (y1 - y2)/2
    print("halfx="+str(halfx)+"  halfy="+str(halfy))

    # 构建URL
    u = "https://restapi.amap.com/v3/place/polygon?polygon=" + polygon + "&key=" + keys[
        keyi] + '&extensions=all&output=json&offset=20&page=' + str(page)
    print(u+"  api_count="+str(api_count))

    api_count += 1  # 记录一次调用

    # 单个key超出100次限额，更换key
    if api_count >= 100:
        keyi += 1
        print("change key!now key is", keys[keyi])
        api_count = 0

    # 解析数据
    data = requests.get(u)
    s = data.json()

    # 查询错误
    if s['status'] != '1':
        print('eror!')
        return

    # 如果网格太大，递归查询
    print("总条数:"+s['count'])
    if int(s['count']) >= 200:
        print("too much!count is" + s['count'])
        search_poi(x1, y1, x2 - halfx, y2 + halfy, 1)  # 左上角
        search_poi(x1 + halfx, y1, x2, y2 + halfy, 1)  # 右上角
        search_poi(x1 + halfx, y1 - halfy, x2, y2, 1)  # 左下角
        search_poi(x1, y1 - halfy, x2 - halfx, y2, 1)  # 右下角
        return

    for i in range(len(s['pois'])):
        row = []
        if 'name' in s['pois'][i]:
            row.append(s['pois'][i]['name'])
        else:
            row.append("")
        if 'type' in s['pois'][i]:
            row.append(s['pois'][i]['type'])
        else:
            row.append("")

        if 'typecode' in s['pois'][i]:
            row.append(s['pois'][i]['typecode'])
        else:
            row.append("")

        if 'location' in s['pois'][i]:
            row.append(s['pois'][i]['location'])
        else:
            row.append("")

        if 'address' in s['pois'][i]:
            row.append(s['pois'][i]['address'])
        else:
            row.append("")

        if 'business_area' in s['pois'][i]:
            row.append(s['pois'][i]['business_area'])
        else:
            row.append("")

        print(row)
        csv_writer.writerow(row)

    # 若不止一页，查询下一页
    print(len(s['pois']))
    if len(s['pois']) != 0:
        search_poi(x1, y1, x2, y2, page + 1)

# main
keys = ["4081a7b2d93ad685bdc94258a6c10b5f","d1acfb2fec9aea25b331abd5c8cc97d8","8e41837f5caf46edacc9874136c1a1af",
        "1b05cfb1055f5d9a88605c46e8e1208f","1c13e504c15beaf4652d86c6edab07af"]  # 自己申请的10个key
global keyi  # 当前使用的key编号
global api_count  # 每一个key调用api的次数
keyi = 0
api_count = 0
# 104.0422, 30.728, 104.1295, 30.653

search_poi(x, y, x+0.02, y-0.02, 1)
f.close()
