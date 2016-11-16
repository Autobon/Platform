# 提交认证及获取认证信息

## 1. 提交认证
技师提交认证.需要名字,身份证号,技能项数组，身份证照，银行，银行地址，银行卡号
### URL及请求方法
POST /api/mobile/technician/v2/certificate

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| name | 名字 | tom |
| idNo | 身份证号 | 422302198608266313 |
| skills| 技能项数组, 用逗号分隔 | 1,2 |
| idPhoto| 身份证照 | /a/a.jpg |
| bank| 银行名称 | 工商银行 |
| bankCardNo| 银行卡号 | 88888888888 |
| bankAddress | 开户行地址 | 南湖路支行 |
| reference | 推荐人号码 | 13233312121 |
| filmLevel| 贴膜星级 | 5 |
| filmWorkingSeniority| 贴膜年限 | 5 |
| carCoverLevel| 隐形车衣星级 | 4 |
| carCoverWorkingSeniority| 隐形车衣年限 | 3 |
| colorModifyLevel| 车色改色星级 | 5 |
| colorModifyWorkingSeniority| 贴膜年限 | 5 |
| beautyLevel| 美容清洁星级 | 4 |
| beautyWorkingSeniority| 美容清洁年限 | 3 |
| resume| 个人简历 | 从事工作N年 |

### 返回数据


* 返回结果
```
{
    "status": true,
    "message": {
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
        "requestVerifyAt": 1457277682096,
        "verifyMsg": null,
        "lastLoginAt": 1456195103000,
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


* 返回结果

```
{"status":true,"message":"身份证号码有误"
}
```


## 2. 修改银行卡
修改银行卡信息
### URL及请求方法
PUT /api/mobile/technician/v2/changeBankCard

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| bank | 银行 | 026 |
| bankCardNo | 银行卡号 | 999999999 |
| bankAddress | 银行地址 | 南湖支行 |

* 返回结果
    ```
    {"status":true,"message":null}
    ```

### 请求Cookie
请求Cookie中必须有有效的autoken.
