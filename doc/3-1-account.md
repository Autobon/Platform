# 帐户及登录

## 1. 帐户注册
合作商户帐户注册.需要企业简称,手机号,密码,短信验证码完成注册.
### URL及请求方法
POST /api/mobile/coop/register
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| shortname | 企业简称 | tomcat |
| phone | 手机号 | 13072705335 |
| password | 密码, 至少6位 | 123456 |
| verifySms| 短信验证码 | 123456 |


### 返回数据
1.请求成功

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 1,
        "phone": "13072705335",
        "shortname": "tomcat",
        "fullname": null,
        "businessLicense": null,
        "corporationName": null,
        "corporationIdNo": null,
        "bussinessLicensePic": null,
        "corporationIdPicA": null,
        "corporationIdPicB": null,
        "longitude": null,
        "latitude": null,
        "invoiceHeader": null,
        "taxIdNo": null,
        "postcode": null,
        "province": null,
        "city": null,
        "district": null,
        "address": null,
        "contact": null,
        "contactPhone": null,
        "statusCode": 0,
        "lastLoginTime": null,
        "lastLoginIp": null,
        "createTime": 1457667862502
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

## 2. 帐户登录
### URL及请求方法
POST /api/mobile/coop/login
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| shortname | 企业简称 | tomcat |
| phone | 手机号 | 13072705335 |
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
        "phone": "13072705335",
        "shortname": "tomcat",
        "fullname": null,
        "businessLicense": null,
        "corporationName": null,
        "corporationIdNo": null,
        "bussinessLicensePic": null,
        "corporationIdPicA": null,
        "corporationIdPicB": null,
        "longitude": null,
        "latitude": null,
        "invoiceHeader": null,
        "taxIdNo": null,
        "postcode": null,
        "province": null,
        "city": null,
        "district": null,
        "address": null,
        "contact": null,
        "contactPhone": null,
        "statusCode": 0,
        "lastLoginTime": null,
        "lastLoginIp": "127.0.0.1",
        "createTime": 1457677133000
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

4.手机号与企业简称不匹配

```
{"result": false,
"message": "手机号与企业简称不匹配",
"error": "NO_SUCH_USER",
"data": null}
```

## 3. 找回密码
通过手机号和短信验证码设定新密码.
### URL及请求方法
POST /api/mobile/coop/resetPassword
### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| phone | 手机号 | 13072705335 |
| password | 新密码 | 123456 |
| verifySms| 短信验证码 | 123456 |
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

## 4. 更改密码
已登录的情况下,修改密码
### URL及请求方法
POST /api/mobile/coop/changePassword
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
{
    "result": true,
    "message": null,
    "error": null,
    "data": null
}
```

2.密码至少6位

```
{"result": false,
"message": "密码至少6位",
"error": "ILLEGAL_PARAM",
"data": null}
```
