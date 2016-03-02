# 一、订单列表及签到

## 1.作为主责任人订单列表

### URL及请求方法
GET /api/mobile/technician/order/listMain

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |


### 返回数据

```
{
    "result": true,
    "message": "orderList",
    "error": null,
    "data": [
        {
            "id": 1,
            "orderNum": "1",
            "orderType": 1,
            "photo": "1",
            "orderTime": 1420041600000,
            "addTime": 1420041600000,
            "statusCode": 1,
            "creatorType": 1,
            "creatorId": 1,
            "creatorName": "1",
            "positionLon": "1",
            "positionLat": "1",
            "remark": "1",
            "mainTechId": 1,
            "secondTechId": 1
        }
    ]
}
```

## 1.作为主责任人订单列表

### URL及请求方法
GET /api/mobile/technician/order/listMain

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |


### 返回数据

```
{
    "result": true,
    "message": "orderList",
    "error": null,
    "data": [
        {
            "id": 1,
            "orderNum": "1",
            "orderType": 1,
            "photo": "1",
            "orderTime": 1420041600000,
            "addTime": 1420041600000,
            "statusCode": 1,
            "creatorType": 1,
            "creatorId": 1,
            "creatorName": "1",
            "positionLon": "1",
            "positionLat": "1",
            "remark": "1",
            "mainTechId": 1,
            "secondTechId": 1
        }
    ]
}
```