# 订单

## 1.订单评论
评论

### URL及请求方法
POST /api/mobile/coop/order/comment

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| orderId | 订单id | 1 |
| star | 星级 |5 |
| arriveOnTime | 准时达到 |true |
| completeOnTime | 准时完成 |true |
| professional | 专业技能 |true |
| dressNeatly | 整洁着装 |true |
| carProtect | 车辆保护 |true |
| goodAttitude | 态度好 |true |
| advice | 建议 | 贴膜技术不错 |

订单需要有指定主技师。

### 返回结果

````
{
    "result": true,
    "message": "comment",
    "error": "",
    "data": null
}

````

#### a.订单未完成或已评论

```
{
    "result": false,
    "message": "订单未完成或已评论",
    "error": "",
    "data": null
}

```
#### b.此订单未指定技师

```
{
    "result": false,
    "message": "此订单未指定技师",
    "error": "",
    "data": null
}

```

## 2.创建订单
合作商户创建订单并推送

### URL及请求方法
POST /api/mobile/coop/order/createOrder

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| -------- | -------- | ---- | --- |
| photo | 是 | 订单图片地址 | a/a.jpg |
| remark | 是 | 备注 | remark is here |
| orderTime | 是 | 订单时间 |2016-03-01 12:02 |
| orderType | 是 | 订单类型 | 1 |
| pushToAll | 否 | 是否推送给技师, 不指定时, 向技师群推 | false |

### 返回结果

````
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 3,
        "orderNum": "16032813BCU8SL",
        "orderType": 1,
        "photo": "a/a.jpg",
        "orderTime": 1456804920000,
        "addTime": 1459144745063,
        "finishTime": null,
        "creatorName": "Tom",
        "contactPhone": null,
        "positionLon": null,
        "positionLat": null,
        "remark": "remark is here",
        "mainTechId": 0,
        "secondTechId": 0,
        "status": "NEWLY_CREATED"
    }
}

````
#### a.商户未通过验证

```
{
    "result": false,
    "message": "商户未通过验证",
    "error": "ILLEGAL_PARAM",
    "data": null
}

```
#### b.订单时间格式不对

```
{
    "result": false,
    "message": "订单时间格式不对,正确格式: 2016-02-10 09:23",
    "error": "ILLEGAL_PARAM",
    "data": null
}

```

## 3.商户端未完成订单列表
商户端未完成订单列表

### URL及请求方法
POST /api/mobile/coop/order/listUnfinished

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| page | 页码(默认值为1) | 1 |
| pageSize | 每页显示数目（默认值为20） | 20 |

### 返回结果

````
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "page": 1,
        "totalElements": 2,
        "totalPages": 1,
        "pageSize": 20,
        "count": 2,
        "list": [
            {
                "id": 3,
                "orderNum": "16032813BCU8SL",
                "orderType": 1,
                "photo": "a/a.jpg",
                "orderTime": 1456804920000,
                "addTime": 1459144745000,
                "finishTime": null,
                "creatorName": "Tom",
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": "remark is here",
                "mainTech": null,
                "secondTech": null,
                "mainConstruct": null,
                "secondConstruct": null,
                "comment": null,
                "cooperator": {
                    "id": 1,
                    "fullname": "非常历害的公司",
                    "businessLicense": null,
                    "corporationName": "王大拿",
                    "corporationIdNo": null,
                    "bussinessLicensePic": null,
                    "corporationIdPicA": null,
                    "corporationIdPicB": null,
                    "longitude": "114.287685",
                    "latitude": "30.639203",
                    "invoiceHeader": null,
                    "taxIdNo": null,
                    "postcode": null,
                    "province": "湖北省",
                    "city": "武汉市",
                    "district": "洪山区",
                    "address": "软件园中路",
                    "contact": "王大拿",
                    "contactPhone": "13072705000",
                    "createTime": 1457677133000,
                    "statusCode": 1,
                    "orderNum": 2
                },
                "creator": {
                    "id": 1,
                    "cooperatorId": 1,
                    "fired": false,
                    "shortname": "Tom",
                    "phone": "13072726003",
                    "name": "Tom",
                    "gender": true,
                    "lastLoginTime": 1425525071000,
                    "lastLoginIp": "192.168.1.1",
                    "createTime": 1425265871000,
                    "pushId": null,
                    "main": true
                },
                "status": "NEWLY_CREATED"
            }
        ]
    }
}

````

## 4.商户端已完成订单列表
商户端已完成订单列表

### URL及请求方法
POST /api/mobile/coop/order/listFinished

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| page | 页码(默认值为1) | 1 |
| pageSize | 每页显示数目（默认值为20） | 20 |

### 返回结果

````
 {
     "result": true,
     "message": "",
     "error": "",
     "data": {
         "page": 1,
         "totalElements": 0,
         "totalPages": 0,
         "pageSize": 20,
         "count": 0,
         "list": []
     }
 }

````

## 5.商户端未评论订单列表
商户端未评论订单列表

### URL及请求方法
POST /api/mobile/coop/order/listUncomment

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| page | 页码(默认值为1) | 1 |
| pageSize | 每页显示数目（默认值为20） | 20 |

### 返回结果

````
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "page": 1,
        "totalElements": 0,
        "totalPages": 0,
        "pageSize": 20,
        "count": 0,
        "list": []
    }
}

````

## 6.查看技师详情
查看技师详情

### URL及请求方法
GET /api/mobile/coop/technician/getTechnician?orderId=1

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| orderId | 订单id | 1 |

订单需要有指定主技师。

### 返回结果

````
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
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
        "skill": "1",
        "pushId": null,
        "status": "NEWLY_CREATED"
    }
}

````

#### a.订单id错误

```
{
    "result": false,
    "message": "没有此订单",
    "error": "ILLEGAL_PARAM",
    "data": null
}
```
#### b.订单没有关联主技师

```
{
    "result": false,
    "message": "订单没有关联主技师",
    "error": "ILLEGAL_PARAM",
    "data": null
}
```

## 7.上传订单照片
上传订单照片

### URL及请求方法
POST /api/mobile/coop/order/uploadPhoto

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
    "date": "/uploads/order/photo/20160304165030100001.jpg"
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

## 8. 给订单指定技师
商户下单时如果选择不推送给所有员工,可以在下单后指定技师

### URL及请求方法
`POST /api/mobile/coop/order/assign`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| -------- | -------- | ---- | ---- |
| orderId | 是 | 订单ID | 1 |
| techId | 是 | 技师ID | 2 |

### 返回数据

#### a. 请求成功

```
{
    "result": true,
    "error": "",
    "message": "",
    "date": null
}
```

### b. 技师技能不支持订单类型

```
{
    "result": false,
    "error": "TECH_SKILL_NOT_SUFFICIANT",
    "message": "技师技能不支持订单类型",
    "date": null
}
```

### c. 订单不可指定技师

```
{
    "result": false,
    "error": "NOT_ASSIGNABLE_ORDER",
    "message": "订单不可指定技师",
    "date": null
}
```
