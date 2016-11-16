# 合作商户

## 1. 商户资料提交认证
商户资料提交
### URL及请求方法
POST /api/mobile/coop/merchant/certificate

### 请求参数
| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| enterpriseName | 是 | 企业名称 | A汽车美容公司 |
| bussinessLicensePic | 是 | 营业执照副本照片 | a/a.jpg |
| longitude | 是 | 商户位置经度| 120.34 |
| latitude | 是 | 商户位置纬度| 35.55 |
### 返回数据

```
{
    "status": true,
    "message": {
        "id": 1,
        "fullname": "A汽车美容公司",
        "businessLicense": "3335555",
        "corporationName": "张三",
        "corporationIdNo": "422365196605050001",
        "bussinessLicensePic": "a/a.jpg",
        "corporationIdPicA": "a/b.jpg",
        "corporationIdPicB": null,
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
        "createTime": 1457677133000,
        "statusCode": 0,
        "orderNum": 2
    }
}

```



#### a.没有此关联商户

```
{
    "status": false,
    "message": "没有此关联商户"
}

```

#### b.你已经认证成功

```
{
    "status": true,
    "message": "你已经认证成功"
}

```

#### c.等待审核

```
{
    "status": false,
    "message": "等待审核"
}

```
#### e.商户状态码不正确

```
{
    "status": false,
    "message": "商户状态码不正确"
}

```

## 2.上传营业执照副本照片
上传营业执照副本照片

### URL及请求方法
POST /api/mobile/coop/merchant/bussinessLicensePic

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| file | 上传的图片 | |

### 返回数据

#### a.请求成功

```
{
    "status": true,
    "message": "/uploads/coop/bussinessLicensePic/20160304165030100001.jpg"
}
```

#### b.没有上传文件

```
{
    "status": false,
    "message": "没有上传文件"
}
```



## 4. 获取商户信息
获取商户信息
### URL及请求方法
get("/api/mobile/coop/merchant")


### 返回数据

```
{
    "status": true,
    "message": {
        "id": 1,
        "phone": "13072705000",
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
        "contactPhone": "13072705000",
        "lastLoginTime": null,
        "lastLoginIp": null,
        "createTime": 1457677133000,
        "statusCode": 1
    }
}

```


## 5. 商户审核信息
商户审核信息
### URL及请求方法
get("/api/mobile/coop/merchant/coopCheckResult")

商户审核信息可能有多条，取审核时间最新的一条数据。

### 返回数据

```
{
    "status": true,
    "message": {
        "reviewCooper": null,
        "cooperator": {
            "id": 1,
            "phone": "13072705000",
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
            "contactPhone": "13072705000",
            "lastLoginTime": null,
            "lastLoginIp": null,
            "createTime": 1457677133000,
            "statusCode": 1
        }
    }
}

```

## 6. 合作商户的订单统计
商户的订单统计
### URL及请求方法
get("/api/mobile/coop/merchant/order/orderCount")

### 返回数据
当前账户为管理员时，查询合作商户所有订单数目。
```
{
    "status": true,
    "message": 2
}

```
当前账户为业务员时，查询该业务员下订单数目。
```
{
    "status": true,
    "message": 1
}

```
