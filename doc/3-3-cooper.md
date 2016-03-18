# 合作商户

## 1. 商户资料提交认证
商户资料提交
### URL及请求方法
post("/api/mobile/coop/check")

### 请求参数
| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| fullname | 是 | 企业名称 | A汽车美容公司 |
| businessLicense | 是 | 营业执照号 | 3335555 |
| corporationName | 是 | 法人姓名 | 张三 |
| corporationIdNo | 是 | 法人身份证号 | 422365196605050001 |
| bussinessLicensePic | 是 | 营业执照副本照片 | a/a.jpg |
| corporationIdPicA | 是 | 法人身份证正面照 | a/b.jpg |
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
        "phone": "13072705000",
        "shortname": "first-coop",
        "fullname": "A汽车美容公司",
        "businessLicense": "3335555",
        "corporationName": "张三",
        "corporationIdNo": "422365196605050001",
        "bussinessLicensePic": "a/a.jpg",
        "corporationIdPicA": "a/b.jpg",
        "corporationIdPicB": null,
        "longitude": null,
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
        "statusCode": 0,
        "lastLoginTime": null,
        "lastLoginIp": null,
        "createTime": 1457677133000,
        "isMain": true,
        "pushId": null
    }
}
```


#### a.身份证号码有误

```
{
    "result": false,
    "message": "身份证号码有误",
    "error": "ILLEGAL_PARAM",
    "data": null
}
```

#### b.手机号有误

```
{
    "result": false,
    "message": "手机号格式错误",
    "error": "ILLEGAL_PARAM",
    "data": null
}
```

## 2.上传营业执照副本照片
上传营业执照副本照片

### URL及请求方法
POST /api/mobile/coop/bussinessLicensePic

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| file | 上传的图片 | |

### 返回数据

#### a.请求成功

```
{
    "result": true,
    "error": "",
    "message": "",
    "date": "/uploads/coop/bussinessLicensePic/20160304165030100001.jpg"
}
```

#### b.没有上传文件

```
{
    "result": false,
    "error": "NO_UPLOAD_FILE",
    "message": "没有上传文件",
    "date": null
}
```

## 3.上传法人身份证正面照
上传法人身份证正面照

### URL及请求方法
POST /api/mobile/coop/corporationIdPicA

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| file | 上传的图片 | |

### 返回数据

#### a.请求成功

```
{
    "result": true,
    "error": "",
    "message": "",
    "date": "/uploads/coop/corporationIdPicA/20160304165030100002.jpg"
}
```

#### b.没有上传文件

```
{
    "result": false,
    "error": "NO_UPLOAD_FILE",
    "message": "没有上传文件",
    "date": null
}
```




