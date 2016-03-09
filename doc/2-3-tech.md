# 技师管理
## 1. 技师认证
对技师进行信息认证. 认证成功或拒绝, 被认证的技师都将收到认证结果的推送消息. 推送消息,
参见: [技师端推送消息-技师认证](1-7-push.md)
### URL及请求方法
`POST /api/web/admin/technician/verify/{techId}`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| techId | 是 | 技师ID, URL占位符参数| 1 |
| verified | 是 | 认证通过或拒绝, true或false | true |
| verifyMsg | 否 | 认证消息, 当拒绝认证通过时, 必须填写 | 身份证照片不清晰 |

### 返回数据

#### a.请求成功

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": null
}
```

#### b.没有这个技师


```
{"result": false,
"message": "没有这个技师",
"error": "ILLEGAL_PARAM",
"data": null}
```

#### c.请填写认证失败原因

```
{"result": false,
"message": "请填写认证失败原因",
"error": "INSUFFICIENT_PARAM",
"data": null}
```

2.技师查询列表
后台查询技师列表信息，通过搜索字符查询技师列表
### URL及请求方法
`GET /api/web/admin/technician?query=18812345678&page=1&pageSize=10`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| query | 否 | 搜索关键字，可以是手机号或者技师名字| 18812345678 |
| page | 否 | 页码 | 1 |
| pageSize | 否 | 每页显示数目 | 10 |

### 返回数据
...
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "page": 1,
        "totalElements": 1,
        "totalPages": 1,
        "pageSize": 20,
        "count": 1,
        "list": [
            {
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
                "requestVerifyAt": null,
                "verifyMsg": null,
                "lastLoginAt": 1456195103000,
                "lastLoginIp": "127.0.0.1",
                "createAt": 1455724800000,
                "star": 0,
                "voteRate": 0,
                "skill": "1",
                "pushId": null,
                "status": "NEWLY_CREATED"
            }
        ]
    }
}
...


3.更新技师资料
后台更新技师资料
### URL及请求方法
`POST /api/web/admin/technician/1`

### 请求参数
| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| techId | 是 | 技师id| 1 |
| phone | 是 | 手机号码 | 13070705003 |
| name | 是 | 名字 | henry |
| gender | 是 | 性别 | man |
| idNo | 是 | 身份证号 | 422111198602020011 |
| idPhoto | 是 | 身份证图片 | a/a.jpg |
| bank | 是 | 银行 | 0003 |
| bankAddress | 是 | 银行地址 | wuhan guanggu |
| bankCardNo | 是 | 银行卡号 | 99998888 |
| skill | 是 | 技能| 1,2,3 |

### 返回数据
...
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 1,
        "phone": "13070705003",
        "name": "henry",
        "gender": "man",
        "avatar": null,
        "idNo": "422111198602020011",
        "idPhoto": "a/a.jpg",
        "bank": "0003",
        "bankAddress": "wuhan guanggu",
        "bankCardNo": "99998888",
        "verifyAt": null,
        "requestVerifyAt": null,
        "verifyMsg": null,
        "lastLoginAt": 1456195103000,
        "lastLoginIp": "127.0.0.1",
        "createAt": 1455724800000,
        "star": 0,
        "voteRate": 0,
        "skill": "1,2,3",
        "pushId": null,
        "status": "NEWLY_CREATED"
    }
}
...