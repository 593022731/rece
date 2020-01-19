# rece
大数据（多类型）内外网数据包 接收/分发平台
仅通过改配置文件，即可实现 任意数据类型/数据包，接受/分发功能

## 通用配置demo
```properties
################内外网通用配置start######################
#所在城市编号
areaCode=310100
#RECE应用标示是外网还是内网  rece-net为外网,rece-inner为内网
rece.appname=rece-net

#ftp发送队列最大数量
#ftp.sender.maxQueue=10000
#外网发送ftp目录配置或内网扫描接收数据目录配置
#凭证文件夹名(默认不更改)
#ftp.receipt=RECEIPT
#数据文件夹名(默认不更改)
#ftp.fileinfo=FILEINFO
#数据备份文件夹名(默认不更改)
#ftp.bak=BAK
################内外网通用配置end######################
```

## 内网配置demo
```properties
#内网同时分发数默认5
#working.core=5
#以下为FTP连接配置为默认如果inner.ftp配置选项未配置下列项会使用默认配置
#内网ftp连接池大小
#ftp.pool=5
#ftp连接超时时间
#ftp.timeout=60000
#ftp.remote=
#ftp.directory=
#ftp.user=
#ftp.password=

#外网传内网FTP上的凭证文件夹名
#ftp.receipt=RECEIPT
#数据文件夹名
#ftp.fileinfo=FILEINFO
#数据备份文件夹名
#ftp.bak=BAK

#文件发送配置
#内网发送ftp
sendftp.fileinfo=FILE/xuhui
sendftp.receipt=RECEIPT/xuhui
#内网发送文件夹
sendfs.fileinfo=FILE
sendfs.receipt=RECEIPT

#backup.fail=FAILED
#backup.success=SUCCESS
#scan.folder=ZIP

#扫描根目录
scan.basic=/home/ftpuser/FILE/ZIP
#是否需要扫描备份文件如果有bak目录有数据则表示需要扫描
scan.bak=yes
#文件发送后的备份目录
backup.basic=/opt/receSender

#需要扫描的数据
rece-inner=trace,jpg

#该类型数据扫描文件夹名
trace.scan.folder=ZIP
trace.scan.prefix=NETBAR_Track,120
trace.transmit=ftp_server1,/opt/FILE/ZIP/NETBAR_TRACK

jpg.scan.folder=ZIP
jpg.scan.prefix=NETBAR_Media
jpg.transmit=ftp_server2,/opt/FILE/ZIP/NETBAR_MEDIA

inner.ftp=server1,server2

server1.ftp.directory=/rece/MEDIA
server2.ftp.directory=/rece/TRACK

ftp.remote=127.0.0.1
ftp.user=ftpuser
ftp.password=123456

#是否递归扫描子目录
#scan.recursive=false

#扫描类
#scan.className=InnerScanPack
```

## 外网配置demo
```properties
#3.4发送文件主目录(默认不更改)
scan.basic=/home/ftpuser

#ftp待发送目录
scan.sender=/opt/xfirereceservice/thirdpart/ZIP

#ftp配置选项
ftp.remote=192.168.1.32
ftp.port=21
ftp.user=jetory
ftp.password=jetory@paibo!@#
ftp.directory=/rece/ZIP

###########################自定义配置项start#####################################

#外网需要发送ftp的数据配置：trace(轨迹数据),jpg(照片数据),media(音视频数据),loc(非经场所数据),av0002(MAC音视频轨迹AV0002数据)
#rece-net=trace,jpg,media,loc,av0002
rece-net=trace,jpg,media,loc,av0002

#3.4上传目录配置

loc.scan.folder=LOCFILEINFO

av0002.scan.folder=MACFILEINFO

#trace.scan.basic=/opt/ftpuser
trace.scan.folder=TRACKFILEINFO
trace.scan.hasfileinfo=true

#jpg.scan.basic=/opt/ftpuser
jpg.scan.folder=COMMONFILEINFO
jpg.scan.hasfileinfo=true

#wma.scan.basic=/opt/ftpuser
media.scan.folder=COMMONFILEINFO
media.scan.hasfileinfo=true

#ftp配置选项
#trace.ftp.remote=192.168.1.32
#trace.ftp.port=21
#trace.ftp.user=jetory
#trace.ftp.password=jetory@paibo!@#
#trace.ftp.directory=/rece/trace

#ftp发送待目录配置
#trace.scan.sender=/opt/xfirereceservice/thirdpart/TRACK
#jpg.scan.sender=/opt/xfirereceservice/thirdpart/JPG


###########################自定义配置项end#####################################
###########################通用配置项start#####################################

#线程池数量
working.core=1

#是否递归扫描子目录(默认不更改)
scan.recursive=false

#扫描目录中是否需要FILEINFO目录(默认为false不需要FILEINFO和RECEIPT目录,直接在指定扫描目录下扫描数据)(默认不更改)
scan.hasfileinfo=false

#扫描的凭证文件夹名(默认不更改)
scan.receipt=RECEIPT

#扫描的实体文件夹名(默认不更改)
scan.fileinfo=FILEINFO

#待发送凭证文件夹名(默认不更改)
sender.receipt=RECEIPT

#待发送实体文件夹名(默认不更改)
sender.fileinfo=FILEINFO

#BAK文件夹名(默认不更改)
scan.bak=BAK

#是否发送BAK文件(默认不更改)
issendbak=true

#是否备份(默认值true代表备份，其它值代表不备份直接删除)(默认不更改)
isbackup=true

#打包图片最大数量(默认30个图片一个包)(默认不更改)
scan.piccount=30

#备份成功文件夹名(默认不更改)
backup.success=SUCCESS

#备份错误文件夹名(默认不更改)
backup.fail=FAILED

###########################通用配置项end#####################################

###########################默认不能更改的配置项start#####################################
#系统默认提供的数据扫描工具类

#音视频通用扫描类
media.scan.className=ScanMediaPack
#图片通用扫描类
jpg.scan.className=ScanPicturePack
#NETBAR数据流文件通用扫描类
trace.scan.className=ScanNetBarPack
#非经网吧场所文件通用扫描类
loc.scan.className=ScanISBSListPack
#Mac(音视频轨迹AV0002)文件通用扫描类
av0002.scan.className=ScanAV0002Pack
#通用扫描类，把源数据直接发送到内网RECE
scan.className=CommonScanPack
###########################默认不能更改的配置项end#####################################
```
