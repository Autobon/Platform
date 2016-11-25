# 订单

## 1.订单评论
评论

### URL及请求方法
POST /api/mobile/coop/merchant/order/comment

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
    "status": true,
    "message": "comment"
}

````

#### a.订单未完成或已评论

```
{
    "status": false,
    "message": "订单未完成或已评论"
}

```
#### b.此订单未指定技师

```
{
    "status": false,
    "message": "此订单未指定技师"
}

```

## 2.创建订单
合作商户创建订单并推送

### URL及请求方法
POST /api/mobile/coop/merchant/order

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| -------- | -------- | ---- | --- |
| photo | 是 | 订单图片地址 | a/a.jpg |
| remark | 是 | 备注 | remark is here |
| agreedStartTime | 是 | 预约开工时间 |2016-03-01 12:02 |
| agreedEndTime | 是 | 最晚交车时间 | 2016-03-01 16:02 |
| type | 是 | 施工项目 | 1,2 项目ID拼接 |
| pushToAll | 否 | 是否推送给技师, 不指定时, 向技师群推 | false |

### 返回结果

### a.请求成功

````
{
  "status": true,
  "message": {
    "id": 34,
    "orderNum": "16111616YKDP3E",
    "orderType": 0,
    "photo": "1.jpg",
    "orderTime": null,
    "addTime": 1479284748525,
    "finishTime": null,
    "takenTime": null,
    "creatorId": 1,
    "coopId": 1,
    "creatorName": "Tom",
    "contactPhone": "13072726003",
    "positionLon": "114.287685",
    "positionLat": "30.639203",
    "remark": "12",
    "mainTechId": 0,
    "secondTechId": 0,
    "beforePhotos": null,
    "afterPhotos": null,
    "signTime": null,
    "startTime": null,
    "endTime": null,
    "type": "1,2",
    "status": "NEWLY_CREATED",
    "agreedStartTime": 1456804920000,
    "agreedEndTime": 1456804920000
  }
}

````

#### b.商户未通过验证

```
{
    "status": false,
    "message": "商户未通过验证"
}

```

#### c.订单时间格式不对

```
{
    "status": false,
    "message": "订单时间格式不对,正确格式: 2016-02-10 09:23"
}

```

## 3.商户端订单列表
商户端订单列表

### URL及请求方法
POST /api/mobile/coop/merchant/order

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| status | 状态 | 1未完成 2已完成 |
| page | 页码(默认值为1) | 1 |
| pageSize | 每页显示数目（默认值为20） | 20 |

### 返回结果

````
{
  "status": true,
  "message": {
    "content": [
      {
        "id": 34,
        "orderNum": "16111616YKDP3E",
        "photo": "1.jpg",
        "creatorType": 0,
        "techId": 0,
        "techName": null,
        "techPhone": null,
        "beforePhotos": null,
        "afterPhotos": null,
        "startTime": null,
        "endTime": null,
        "signTime": null,
        "takenTime": null,
        "createTime": 1479284749000,
        "type": "1,2",
        "coopId": 1,
        "coopName": "Tom",
        "address": "软件园中路",
        "longitude": "114.287685",
        "latitude": "30.639203",
        "creatorId": 1,
        "creatorName": "Tom",
        "contactPhone": "13072726003",
        "remark": "12",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedStartTime": 1456804920000,
        "agreedEndTime": 1456804920000,
        "status": "NEWLY_CREATED"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": null,
    "first": true,
    "numberOfElements": 1
  }
}

````




## 5.上传订单照片
上传订单照片

### URL及请求方法
POST /api/mobile/coop//merchant/order/uploadPhoto

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| file | 上传的图片 | |

### 返回数据

#### a.请求成功

```
{
    "status": true,
    "message": "/uploads/order/photo/20160304165030100001.jpg"
}
```

#### b.没有上传文件

```
{
    "status": false,
    "message": "没有上传文件"
}
```

## 6. 给订单指定技师
商户下单时如果选择不推送给所有员工,可以在下单后指定技师。指定技师后, 技师将收到派单推送消息。

### URL及请求方法
`POST /api/mobile/coop/merchant/order/appoint`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| -------- | -------- | ---- | ---- |
| orderId | 是 | 订单ID | 1 |
| techId | 是 | 技师ID | 2 |

### 返回数据

#### a. 请求成功

```
{
    "status": true,
    "message": null
}
```

 

### b. 订单不可指定技师

```
{
    "status": false,
    "message": "订单不可指定技师"
}
```


### c. 技师不存在

```
{
    "status": false,
    "message": "技师不存在"
}
```

## 7. 撤销订单
商户在发布订单后30分钟内或预约的时间2小时前可以放弃订单

### URL及请求方法
`PUT /api/mobile/coop//merchant/order/{orderId}/cancel`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | URL占位符参数, 订单编号  | 1 |

### 返回数据
1. 请求成功

    ```
    {
        "status": true,
        "message": null
    }
    ```
    
2. 没有此订单

    ```
    {
        "status": false,
        "message": "没有此订单"
    }
    ```
    
3. 只允许未接订单或下单后半小时内或订单约定时间前2小时撤单

    ```
    {
        "status": false,
        "message": "已开始施工订单, 不能撤销"
    }
    ```
    
## 8. 查询订单详情

### URL及请求方法
`POST /api/mobile/coop/merchant/order/{orderId:\\d+}`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | URL占位符参数, 订单编号  | 1 |

### 返回数据

#### a. 请求成功

 ```
{
  "status": true,
  "message": {
    "id": 1,
    "orderNum": "16022313fsd123",
    "photo": "",
    "creatorType": 1,
    "techId": 1,
    "techName": "tom",
    "techPhone": "18812345678",
    "beforePhotos": null,
    "afterPhotos": null,
    "startTime": null,
    "endTime": null,
    "signTime": null,
    "takenTime": null,
    "createTime": 1456196963000,
    "type": null,
    "coopId": 1,
    "coopName": "Tom",
    "address": "软件园中路",
    "longitude": "114.287685",
    "latitude": "30.639203",
    "creatorId": 1,
    "creatorName": "超级管理员",
    "contactPhone": "13072726003",
    "remark": "bababala",
    "orderConstructionShow": [],
    "techLongitude": null,
    "techLatitude": null,
    "status": "EXPIRED",
    "agreedEndTime": null,
    "agreedStartTime": null
  }
}

 ```


## 9.商户查询技师列表（距离优先）

### URL及请求方法
`GET /api/mobile/coop/merchant/technician/distance`

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| longitude | 经度 | 123.3232 |
| latitude | 纬度 | 34.2323 |
| page | 页码(默认值为1) | 1 |
| pageSize | 每页显示数目（默认值为20） | 20 |
### 返回数据

#### a. 请求成功

 ```
{
  "status": true,
  "message": {
    "content": [
      {
        "id": 1,
        "name": "tom",
        "phone": "18812345678",
        "filmLevel": 0,
        "carCoverLevel": 0,
        "colorModifyLevel": 0,
        "beautyLevel": 0,
        "distance": 39.62,
        "status": 1
      },
      {
        "id": 2,
        "name": "tom2",
        "phone": "18812345670",
        "filmLevel": 0,
        "carCoverLevel": 0,
        "colorModifyLevel": 0,
        "beautyLevel": 0,
        "distance": 42.95,
        "status": 1
      },
      {
        "id": 4,
        "name": "英卡",
        "phone": "18771148252",
        "filmLevel": 0,
        "carCoverLevel": 0,
        "colorModifyLevel": 0,
        "beautyLevel": 0,
        "distance": 9382.49,
        "status": 1
      }
    ],
    "totalPages": 1,
    "totalElements": 3,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": null,
    "numberOfElements": 3,
    "first": true
  }
}

 ```


