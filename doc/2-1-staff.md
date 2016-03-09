# 帐户及登录

## 1.登录
车邻邦后台登录

### URL及请求方法
POST /api/staff/login

### 请求参数
| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| username | 用户名 | admin |
| password | 密码 | admin |

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| POST | /api/staff/login | | | {"result":true,"message":"登录成功","error":null,"data":{1,"admin",2016-02-18 13:00}} |

## 2.注销
车邻邦后台注销

### URL及请求方法
DELETE /api/staff/logout

### 请求参数
无

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| POST | /api/staff/logout | | | {"result":true,"message":"注销成功","error":null,"data":null} |
