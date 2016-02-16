# 一、帐户及登录

## 1. 请求发送验证短信
向指定手机号发送验证码短信.
### URL及请求方法
GET /api/mobile/verifySms
### 请求参数
请求参数均为URL参数.

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| phone | 手机号 | 18812345678 |

### 用例


| URL | Header | Body | Result |
| --- | ------ | ---- | ------ |
| GET /api/mobile/verifySms?phone=18812345678 | | | {"result":true,"message":"","error":null,"data":null} |
