# 帐户及登录

技师的帐户状态

| statusCode | 帐户状态 |
| ------ | ------ |
| NEWLY_CREATED | 未认证 |
| IN_VERIFICATION | 等待审核中 |
| VERIFIED | 已认证通过 |
| REJECTED | 认证未通过 |
| BANNED | 帐户已被禁用 |

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
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 3,
        "phone": "18827075300",
        "name": null,
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "requestVerifyAt": null,
        "verifyMsg": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1457277685096,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": null,
        "status": "NEWLY_CREATED"
    }
}
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
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 1,
        "phone": "18812345678",
        "name": "tom",
        "gender": null,
        "avatar": null,
        "idNo": "422302198608266313",
        "idPhoto": "/etc/a.jpg",
        "bank": "工商银行",
        "bankAddress": "光谷",
        "bankCardNo": "88888888888",
        "verifyAt": null,
        "requestVerifyAt": 1457277682000,
        "verifyMsg": null,
        "lastLoginAt": 1457277685376,
        "lastLoginIp": "127.0.0.1",
        "createAt": 1455724800000,
        "star": 0,
        "voteRate": 0,
        "skill": "1,2",
        "pushId": null,
        "status": "IN_VERIFICATION"
    }
}
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
| oldPassword| 旧密码 | 123456 |
| newPassword| 新密码 | 111222 |

### 请求Cookie
请求Cookie中必须有有效的autoken.

### 返回数据
1.请求成功

```
{"result": true,
"message": null,
"error": null,
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
返回状态码: 406

```
{"result": false,
"message": "上传文件大小不能超过2MB",
"error": "UPLOAD_SIZE_EXCEED",
"data": null}
```

## 7. 上传身份证照片
### URL及请求方法
POST /api/mobile/technician/idPhoto
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
"data": "/uploads/technician/idPhoto/20160219162940293084.png"}
```
返回数据中的data字段加上服务器域名及端口就是身份证照片网址.

2.没有上传文件

```
{"result": false,
"message": "没有上传文件",
"error": "NO_UPLOAD_FILE",
"data": null}
```

3.上传文件大小超过2MB
返回状态码: 406

```
{"result": false,
"message": "上传文件大小不能超过2MB",
"error": "UPLOAD_SIZE_EXCEED",
"data": null}
```

## 8. 更新个推ID
### URL及请求方法
POST /api/mobile/technician/pushId
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| pushId | 个推ID | 9a05f0154913d57ef537fdf51ffc14bd |

### 返回数据

```
{"result": true,
"message": null,
"error": null,
"data": null}
```

## 9. 获取技师信息
### URL及请求方法
GET /api/mobile/technician

### 请求参数
无

### 返回数据

```
{"result": true,
"message": "",
"error": null,
"data": {
    "id": 1,
    "phone": "18812345678",
    "name": "tom",
    "gender": null,
    "avatar": null,
    "idNo": "422302198608266313",
    "idPhoto": "/etc/a.jpg",
    "bank": "工商银行",
    "bankAddress": "光谷",
    "bankCardNo": "88888888888",
    "verifyAt": null,
    "requestVerifyAt": 1457277682000,
    "verifyMsg": null,
    "lastLoginAt": 1457277685376,
    "lastLoginIp": "127.0.0.1",
    "createAt": 1455724800000,
    "star": 0,
    "voteRate": 0,
    "skill": "1,2",
    "pushId": null,
    "status": "IN_VERIFICATION"
}}
```

`statusCode` 字段表示技师的帐户状态

| statusCode | 帐户状态 |
| ------ | ------ |
| NEWLY_CREATED | 未认证 |
| IN_VERIFICATION | 等待审核中 |
| VERIFIED | 已认证通过 |
| REJECTED | 认证未通过 |
| BANNED | 帐户已被禁用 |

## 10. 查询技师
### URL及请求方法
GET /api/mobile/technician/search

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| query| 是 | 手机号或姓名 | 张三 |
| page | 否 | 分页页码, 从1开始，默认为1, 仅当query参数为姓名时有效 | 1 |
| pageSize | 否 | 每页条数, 默认20, 仅当query参数为姓名时有效 | 20 |

### 返回数据

```
{"result": true,
"message": "",
"error": "",
"data":{
    "page": 1,
    "totalElements": 1,
    "totalPages": 1,
    "pageSize": 20,
    "count": 1,
    "list":[{
        "id": 1,
        "phone": "18812345678",
        "name": "tom",
        "gender": null,
        "avatar": null,
        "idNo": "422302198608266313",
        "idPhoto": "/etc/a.jpg",
        "bank": "027",
        "bankAddress": "光谷",
        "bankCardNo": "88888888888",
        "verifyAt": null,
        "lastLoginAt": 1456195103000,
        "lastLoginIp": "127.0.0.1",
        "createAt": 1455724800000,
        "star": 0,
        "voteRate": 0,
        "skill": "1,6",
        "pushId": null,
        "statusCode": "NOTVERIFIED"
        }]
    }
}
```

返回值中的data属性是一个分布对象:

| 字段 | 说明 |
| ---- | ---- |
| page | 当前页序号 |
| pageSize | 每页条数 |
| count | 本次返回条数 |
| totalElements | 总条数 |
| totalPages | 总页数 |
| list | 本次返回的记录数组 |



## 11. 报告实时位置
### URL及请求方法
POST /api/mobile/technician/reportLocation

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| rtpostionLon| 是 | 实时位置经度 | 144.4 |
| positionLat | 是 | 实时位置维度 | 34.4 |

### 返回数据

```
{
    "result": true,
    "message": "setLocation",
    "error": "",
    "data": null
}
```

当前报告位置时间与最近报告的时间间隔不能小于1分钟, 1分钟内多次提交以第一次提交为准.