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



