import read_db as db
import csv
import random
import  pymysql
app_num = len(db.appinfo)
user_num = len(db.users)
benchmark = user_num * 0.15
install_num = {}
category_dic ={}
weight_dic={}
minutes_dic={}#app平均使用时间
at_dic={}#用户平均at
time_dic={}#用户平均使用时间
record_dic={}#用户每个app每天平均使用时间
name_dic ={} #packagename->appaname
i=0
with open('data.csv') as f:
    f_csv = csv.reader(f)
    for row in f_csv:
        i = i + 1
        if(i%2 == 1):
            category_dic[row[0]] = row[1]
i=0
with open('name.csv',encoding="utf-8") as f:
    f_csv = csv.reader(f)
    for row in f_csv:
        i = i + 1
        if(i%2 == 1):
            name_dic[row[0]] = row[1]

pack_array = category_dic.keys()


for i in range(len(db.weight)):
    packagename = db.weight[i][1]
    if(install_num.__contains__(packagename)):
        install_num[packagename] = install_num[packagename] + 1
    else:
        install_num[packagename] = 1
    if(weight_dic.__contains__(packagename)):
        weight_dic[packagename][0] += db.weight[i][2]
        weight_dic[packagename][1] += 1
    else:
        weight_dic[packagename]=[db.weight[i][2],1,0]

for i in weight_dic.keys():
    weight_dic[i][2] = int(weight_dic[i][0] / weight_dic[i][1])

for i in range(len(db.record)):#统计minute_dic
    pack = db.record[i][1]
    email = db.record[i][0]
    if(record_dic.__contains__((email,pack))):
        record_dic[(email,pack)][0] += db.record[i][4]
        record_dic[(email,pack)][1] += 1
    else:
        record_dic[(email,pack)] = [db.record[i][4],1,0]

    if(minutes_dic.__contains__(pack)):
        minutes_dic[pack][0] += db.record[i][4]
        minutes_dic[pack][1] += 1
    else:
        minutes_dic[pack] = [db.record[i][4],1,0]

for i in range(len(db.at)):
    email = db.at[i][0]
    if(at_dic.__contains__(email)):
        at_dic[email][0] += db.at[i][2]
        at_dic[email][1] += 1
    else:
        at_dic[email] = [db.at[i][2],1,0]
for i in range(len(db.min)):
    email = db.min[0]
    if(time_dic.__contains__(email)):
        time_dic[email][0] += db.min[i][3]
        time_dic[email][1] += 1
    else:
        time_dic[email] = [db.min[i][3],1,0]
for i in minutes_dic.keys():
    minutes_dic[i][2] = int(minutes_dic[i][0] / minutes_dic[i][1])
for i in record_dic.keys():
    record_dic[i][2] = int(record_dic[i][0] / record_dic[i][1])
for i in time_dic.keys():
    time_dic[i][2] = int(time_dic[i][0] / time_dic[i][1])
for i in at_dic.keys():
    at_dic[i][2] = int(at_dic[i][0] / at_dic[i][1])

#数据库数据读入+处理结束

db.cursor.execute("delete from feature")
for j in install_num.keys():
    sql = ''
    if(install_num[j] < benchmark):
        sql = "insert feature(packagename) values('%s')" % j
    try:
        db.cursor.execute(sql)
        db.db.commit()
    except:
        print(sql)
        db.db.rollback()
#feature的更新结束


def update_appinfo():
    for i in install_num.keys():#更新数据库appinfo的信息
        sql = ''
        if(category_dic.__contains__(i) and weight_dic.__contains__(i) and install_num.__contains__(i) and minutes_dic.__contains__(i)):
            sql = 'update appinfo set weight = {}, installnum = {},category = {},minutes={} where packagename = \'{}\''.format(weight_dic[i][2],install_num[i],category_dic[i],minutes_dic[i][2],i)
        else:
            sql = 'update appinfo set weight = {}, installnum = {},category = {},minutes={} where packagename = \'{}\''.format(random.randint(0,100), random.randint(1,100), random.randint(0,10), random.randint(0,100),i)#如果不存在就随机给他分配category
            #sql = 'insert appinfo(packagename,appname,weight,installnum,category,minutes,image) values(\'{}\',\'{}\',{},{},{},{},{})'.format(i,i,weight_dic[i][2],install_num[i],random.randint(0,10),0,'null')
        try:
            db.cursor.execute(sql)
            db.db.commit()
        except:
            print(sql)
            db.db.rollback()

def update_stat():
    for i in at_dic.keys():
        sql = ""
        if(minutes_dic.__contains__(i)):
            sql = "update stat set avg_at ={},avg_min={} where email = \'{}\'".format(at_dic[i][2],minutes_dic[i][2],i)
        else:
            sql = "update stat set avg_at ={},avg_min={} where email = \'{}\'".format(at_dic[i][2],random.randint(0,50),i)
            try:
                db.cursor.execute(sql)
                db.db.commit()
            except:
                print(sql)
                db.db.rollback()

db = pymysql.connect('localhost','root','justbemyself1998','time')

cursor = db.cursor()
def update_time_record():
    for i,j in record_dic.keys():
        sql1="update time_record set minutes = {} where email={} and packagename=\'{}\'".format(record_dic[(i,j)],i,j)
        sql2='insert into time_record(email,packagename,appname,minutes) values(\'{}\',\'{}\',\'{}\',{})'.format(i,j,name_dic[j],record_dic[(i,j)][2])
        try:
            db.cursor.execute(sql1)
            db.cursor.execute(sql2)
            db.db.commit()
        except:
            print(sql2)
            db.db.rollback()

            cursor.execute("select  * from appinfo")

            appinfo = cursor.fetchall()
            cursor.execute("delete from rank_install")
            cursor.execute("delete from rank_minutes")
            apps = []
            l = len(appinfo)
            for i in range(l):
                tmp = appinfo[i]
                apps.append([tmp[0], tmp[1], tmp[2], tmp[3], tmp[4], tmp[5], tmp[6]])

            def update_rank():
                apps.sort(key=lambda x: x[3], reverse=True)
                for i in range(50):
                    tmp = apps[i]
                    packagename = tmp[0]
                    appname = tmp[1]
                    weight = tmp[2]
                    installnum = tmp[3]
                    category = tmp[4]
                    minutes = tmp[5]
                    image = tmp[6]
                    sql = "insert into rank_install(packagename,appname,weight,installnum,category,image,minutes) values('%s','%s',%i,%i,%i,'%s',%i)" % (
                    packagename, appname, weight, installnum, category, image, minutes)
                    try:
                        cursor.execute(sql)
                        db.commit()
                    except:
                        print(sql)
                        db.rollback()
                apps.sort(key=lambda x: x[5], reverse=True)
                for i in range(50):
                    tmp = apps[i]
                    packagename = tmp[0]
                    appname = tmp[1]
                    weight = tmp[2]
                    installnum = tmp[3]
                    category = tmp[4]
                    minutes = tmp[5]
                    image = tmp[6]
                    sql = "insert into rank_minutes(packagename,appname,weight,installnum,category,image,minutes) values('%s','%s',%i,%i,%i,'%s',%i)" % (
                    packagename, appname, weight, installnum, category, image, minutes)
                    try:
                        cursor.execute(sql)
                        db.commit()
                    except:
                        print(sql)
                        db.rollback()

            update_rank()

#update_appinfo()
#update_time_record()
#update_stat()