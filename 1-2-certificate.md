# 一、提交认证及获取认证信息

## 1. 提交认证
技师提交认证.需要名字,身份证号,技能项数组，身份证照，银行，银行地址，银行卡号
### URL及请求方法
POST /api/mobile/technician/commitCertificate

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| name | 名字 | tom |
| idNo | 身份证号 | 422302198608266313 |
| skillArray| 技能项数组 | {"1","6"} |
| idPhoto| 身份证照 | /a/a.jpg |
| bank| 银行字典 | 027 |
| bankCardNo| 银行卡号 | 88888888888 |


### 用例

* **Request**

    `POST /api/mobile/technician/commitCertificate`
* **POST参数**

    `Parameters = {name=[tom], idNo=[422302198608266313], skillArray=[1, 6], idPhoto=[/a/a.jpg], bank=[027], bankCardNo=[88888888888]}`
* 返回结果

    ```
    {"result":true,"message":"commitCertificate","error":null,"data":{"id":1,"phone":"18827075338","password":"7c4a8d09ca3762af61e59520943dc26494f8941b","name":"tom","gender":null,"avatar":null,"idNo":"422302198608266313","idPhoto":"/a/a.jpg","bank":"027","bankAddress":null,"bankCardNo":"88888888888","verifyAt":null,"lastLoginAt":null,"lastLoginIp":null,"createAt":1455779204000,"star":0,"voteRate":0.0,"skill":"1,6","available":false,"enabled":false,"statusCode":"NOTVERIFIED","username":"18827075338","authorities":[{"authority":"TECHNICIAN"}],"activated":false,"banned":false,"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true}}
    ```



## 2. 获取认证信息
### URL及请求方法
POST /api/mobile/technician/getCertificate

### 请求参数
无

* 返回结果
    ```
    {"result":true,"message":"getCertificate","error":null,"data":{"id":1,"phone":"18812345678","name":null,"gender":null,"avatar":null,"idNo":null,"idPhoto":null,"bank":null,"bankAddress":null,"bankCardNo":null,"verifyAt":null,"lastLoginAt":1455865153000,"lastLoginIp":"0:0:0:0:0:0:0:1","createAt":1455724800000,"star":0,"voteRate":0.0,"skill":null,"available":false,"statusCode":"NOTVERIFIED","activated":false,"banned":false}}
    ```
### 请求Cookie
请求Cookie中必须有有效的autoken.

## 3. 修改银行卡
修改银行卡信息
### URL及请求方法
POST /api/mobile/technician/changeBankCard

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| bank | 银行 | 026 |
| bankCardNo | 银行卡号 | 999999999 |

* 返回结果
    ```
    {"result":true,"message":"changeBankCard","error":null,"data":null}
    ```

### 请求Cookie
请求Cookie中必须有有效的autoken.