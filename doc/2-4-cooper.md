# 后台对合作商户管理

## 1.合作商户查询列表
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
```
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
```

## 2. 后台合作商户认证
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

## 3.更新合作商户资料
后台更新合作商户资料
### URL及请求方法
`POST /api/web/admin/cooperator/update/1`

### 请求参数
| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| coopId | 是 | 合作商户id| 1 |
| phone | 是 | 手机号码 | 13085856332 |
| shortname | 是 | 企业简称 | A公司 |
| fullname | 是 | 企业名称 | A汽车美容公司 |
| businessLicense | 是 | 营业执照号 | 3335555 |
| corporationName | 是 | 法人姓名 | 张三 |
| corporationIdNo | 是 | 法人身份证号 | 422365196605050001 |
| bussinessLicensePic | 是 | 营业执照副本照片 | a/a.jpg |
| corporationIdPicA | 是 | 法人身份证正面照 | a/b.jpg |
| corporationIdPicB | 是 | 法人身份证背面照| a/c.jpg |
| longitude | 是 | 商户位置经度| 120.34 |
| latitude | 是 | 商户位置纬度| 35.55 |
| invoiceHeader | 是 | 发票抬头| 武汉市A科技公司 |
| taxIdNo | 是 | 纳税识别号| 362362236 |
| postcode | 是 | 邮政编码| 430000 |
| province | 是 | 省| 湖北省 |
| city | 是 | 市| 武汉市 |
| district | 是 | 区| 光谷 |
| address | 是 | 详细地址| 中山路3号 |
| contact | 是 | 联系人姓名|李四 |
| contactPhone | 是 | 联系人电话| 13025523002 |

### 返回数据
```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 1,
        "phone": "13085856332",
        "shortname": "A公司",
        "fullname": "A汽车美容公司",
        "businessLicense": "3335555",
        "corporationName": "张三",
        "corporationIdNo": "422365196605050001",
        "bussinessLicensePic": "a/a.jpg",
        "corporationIdPicA": "a/b.jpg",
        "corporationIdPicB": "a/c.jpg",
        "longitude": "120.34",
        "latitude": "35.55",
        "invoiceHeader": "武汉市A科技公司",
        "taxIdNo": "362362236",
        "postcode": "430000",
        "province": "湖北省",
        "city": "武汉市",
        "district": "光谷",
        "address": "中山路3号",
        "contact": "李四",
        "contactPhone": "13025523002",
        "statusCode": 1,
        "lastLoginTime": null,
        "lastLoginIp": null,
        "createTime": 1457677133000
    }
}
```