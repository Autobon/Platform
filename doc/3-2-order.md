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
| advice | 建议 |贴膜技术不错 |

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

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| photo | 订单图片地址 | a/a.jpg |
| remark | 备注 |remark is here |
| orderTime | 订单时间 |2016-03-01 12:02 |
| orderType | 订单类型 |1 |



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
        "creatorType": 1,
        "creatorId": 1,
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
                "creatorType": 1,
                "creatorId": 1,
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
                "status": "NEWLY_CREATED"
            },
            {
                "id": 1,
                "orderNum": "16022313fsd123",
                "orderType": 1,
                "photo": "",
                "orderTime": 1456293600000,
                "addTime": 1456196963000,
                "finishTime": null,
                "creatorType": 1,
                "creatorId": 1,
                "creatorName": "超级管理员",
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": "bababala",
                "mainTech": {
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
                },
                "secondTech": null,
                "mainConstruct": null,
                "secondConstruct": null,
                "comment": null,
                "status": "TAKEN_UP"
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


