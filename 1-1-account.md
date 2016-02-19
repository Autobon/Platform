# 一、帐户及登录

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

### 用例

* **Request**

    `POST /api/mobile/technician/register`
* **POST参数**

    `phone=18827075338&password=123456&verifySms=123456`
* 返回结果

    ```
    {"result":true,"message":"","error":null,"data":{"id":1,"phone":"18827075338","password":"7c4a8d09ca3762af61e59520943dc26494f8941b","name":null,"gender":null,"avatar":null,"idNo":null,"idPhoto":null,"bank":null,"bankAddress":null,"bankCardNo":null,"verifyAt":null,"lastLoginAt":null,"lastLoginIp":null,"createAt":1455592936660,"star":0,"voteRate":0.0,"status":"NOTVERIFIED","activated":false,"banned":false,"available":false}}
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
1. 登录成功


## 4. 找回密码
通过手机号和短信验证码接收新密码短信.
### URL及请求方法
POST /api/mobile/technician/resetPassword
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| phone | 手机号 | 18812345678 |
| password | 新密码 | 123456 |
| verifySms| 短信验证码 | 111222 |

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

