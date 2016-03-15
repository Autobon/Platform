# 后台对合作商户管理

## 1.合作商户查询列表
后台查询合作商户列表信息，通过企业名称，营业执照号，企业审核状态进行查询
### URL及请求方法
post("/api/web/admin/cooperator/coopList"）

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| fullname | 否 | 企业名称 | tom |
| businessLicense | 否 | 营业执照号 | 0001 |
| statusCode | 否 | 状态 0-未审核 1-审核成功 2-审核失败 3-账号禁用 | 1 |
| page | 是 | 页数 | 1 |
| pageSize | 是 | 每页显示条数 | 10 |

### 返回数据
```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "content": [],
        "last": true,
        "totalPages": 0,
        "totalElements": 0,
        "number": 0,
        "size": 10,
        "sort": null,
        "numberOfElements": 0,
        "first": true
    }
}

```

## 2. 后台合作商户认证
对合作商户进行资质审核
### URL及请求方法
post("/api/web/admin/cooperator/checkCoop/coopId"

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| coopId | 是 | 审核商户的ID | 1 |
| statusCode | 是 | 状态 0-未审核 1-审核成功 2-审核失败 3-账号禁用 | 2 |
| resultDesc | 是 | 审核失败原因描述 | 照片不清楚 |

### 返回数据

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 1,
        "cooperatorsId": 1,
        "reviewTime": 1457943134584,
        "checkedBy": "超级管理员",
        "resultDesc": "照片不清楚"
    }
}
```

#### a.请求成功

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": null
}
```

#### b.照片不清楚

```
{"result": false,
"message": "照片不清楚",
"error": "ILLEGAL_PARAM",
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