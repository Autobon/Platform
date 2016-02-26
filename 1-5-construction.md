# 一、施工签到及上传工作前后照片

## 1.签到
在施工信息中记录签到坐标


### URL及请求方法
POST /api/mobile/construction/signIn

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| rtpositionLon | 经度 | 111.11111111 |
| rtpositionLat | 维度 | 55.555555 |
| technicianId | 技师id | 1 |
| orderId | 订单id | 1 |

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| POST | /api/mobile/construction/signIn | | | {"result":true,"message":"signIn","error":null,"data":{"id":3,"orderId":1,"technicianId":1,"rtpositionLon":"111.11111111","rtpositionLat":"55.555555","startTime":null,"signinTime":1456457820198,"endTime":null,"beforePicA":null,"beforePicB":null,"beforePicC":null,"afterPicA":null,"afterPicB":null,"afterPicC":null,"afterPicD":null,"afterPicE":null,"afterPicF":null,"payfor":null,"workload":null}} |

## 2.保存工作前图片
保存在工作前图片地址，数量限制1到3


### URL及请求方法
POST /api/mobile/construction/saveBeforePic

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| constructionId | 施工id | 3 |
| filePaths | 文件路径数组 |"a/a.jpg","a/b.jpg","a/c.jpg" |

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| POST | /api/mobile/construction/saveBeforePic | | | {"result":true,"message":"saveBeforePic","error":null,"data":null} |

## 3.保存工作后图片
保存在工作后图片地址，数量限制3到6


### URL及请求方法
POST /api/mobile/construction/saveAfterPic

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| constructionId | 施工id | 3 |
| filePaths | 文件路径数组 |"a/a.jpg","a/b.jpg","a/c.jpg","a/e.jpg","a/f.jpg" |

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| POST | /api/mobile/construction/saveAfterPic | | | {"result":true,"message":"saveAfterPic","error":null,"data":null} |
