# 帐户及登录

## 1. 发送验证短信
向指定手机号发送验证码短信.
### URL及请求方法
GET /api/mobile/verifySms
### 请求参数
请求参数均为URL参数.

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| phone | 手机号 | 18812345678 |

### 用例


| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| GET | /api/mobile/verifySms?phone=18812345678 | | | {"result":true,"message":"","error":null,"data":null} |

### 完成时间
完成于: **2016年2月15日**
更新于: **2016年2月15日**


## 2. 帐户注册
技师帐户注册.需要手机号,密码,短信验证码完成注册.
### URL及请求方法
POST /api/mobile/technician/register
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| phone | 手机号 | 18812345678 |
| password | 密码, 至少6位 | 123456 |
| verifySms| 短信验证码 | 111222 |


### 返回数据
1.请求成功

```
{"result": true,
"message": "",
"error": null,
"data":{
    "id": 3,
    "phone": "18827075330",
    "name": null,
    "gender": null,
    "avatar": null,
    "idNo": null,
    "idPhoto": null,
    "bank": null,
    "bankAddress": null,
    "bankCardNo": null,
    "verifyAt": null,
    "lastLoginAt": null,
    "lastLoginIp": null,
    "createAt": 1455868034249,
    "star": 0,
    "voteRate": 0,
    "skill": null,
    "status": "NOTVERIFIED"
}}
```

2.验证码错误

```
{"result": false,
"message": "验证码错误",
"error": "ILLEGAL_PARAM",
"data": null}
```

3.手机号格式错误

```
{"result": false,
"message": "手机号格式错误,验证码错误",
"error": "ILLEGAL_PARAM",
"data": null}
```

4.密码至少6位

```
{"result": false,
"message": "密码至少6位",
"error": "ILLEGAL_PARAM",
"data": null}
```

## 3. 帐户登录
### URL及请求方法
POST /api/mobile/technician/login
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| phone | 手机号 | 18812345678 |
| password| 密码 | 123456 |

### 返回数据
1.登录成功

```
{"result": true,
"message": "",
"error": null,
"data":{
    "id": 1,
    "phone": "18812345678",
    "name": null,
    "gender": null,
    "avatar": null,
    "idNo": null,
    "idPhoto": null,
    "bank": null,
    "bankAddress": null,
    "bankCardNo": null,
    "verifyAt": null,
    "lastLoginAt": 1455867550725,
    "lastLoginIp": "0:0:0:0:0:0:0:1",
    "createAt": 1455724800000,
    "star": 0,
    "voteRate": 0,
    "skill": null,
    "status": "NOTVERIFIED"
}}
```
2.未注册手机

```
{"result": false,
"message": "手机号未注册",
"error": "NO_SUCH_USER",
"data": null}
```

3.密码错误

```
{"result": false,
"message": "密码错误",
"error": "PASSWORD_MISMATCH",
"data": null}
```
## 4. 找回密码
通过手机号和短信验证码设定新密码.
### URL及请求方法
POST /api/mobile/technician/resetPassword
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| phone | 手机号 | 18812345678 |
| password | 新密码 | 123456 |
| verifySms| 短信验证码 | 111222 |
### 返回数据
1.请求成功

```
{"result": true,
"message": "",
"error": null,
"data": null}
```
2.验证码错误

```
{"result": false,
"message": "验证码错误",
"error": "ILLEGAL_PARAM",
"data": null}
```
3.密码至少6位

```
{"result": false,
"message": "密码至少6位",
"error": "ILLEGAL_PARAM",
"data": null}
```
4.手机号未注册

```
{"result": false,
"message": "手机号未注册",
"error": "NO_SUCH_USER",
"data": null}
```
## 5. 更改密码
已登录的情况下,修改密码
### URL及请求方法
POST /api/mobile/technician/changePassword
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| password| 新密码 | 111222 |

### 请求Cookie
请求Cookie中必须有有效的autoken.

### 返回数据
1.请求成功

```
{"result": false,
"message": "密码至少6位",
"error": "ILLEGAL_PARAM",
"data": null}
```

2.密码至少6位

```
{"result": false,
"message": "密码至少6位",
"error": "ILLEGAL_PARAM",
"data": null}
```

## 6. 上传头像
### URL及请求方法
POST /api/mobile/technician/avatar
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| file| 图片文件 |  |

### 返回数据
1.请求成功

```
{"result": true,
"message": "",
"error": null,
"data": "/uploads/technician/avatar/20160219162940293084.png"}
```
返回数据中的data字段加上服务器域名及端口就是头像网址.

2.没有上传文件

```
{"result": false,
"message": "没有上传文件",
"error": "NO_UPLOAD_FILE",
"data": null}
```

3.上传文件大小超过2MB

