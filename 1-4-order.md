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

## 2.作为次责任人订单列表

### URL及请求方法
GET /api/mobile/technician/order/listSecond

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

## 3.订单评论
评论


### URL及请求方法
POST /api/mobile/technician/order/comment

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| orderId | 订单id | 1 |
| star | 星级 |5 |
| arriveOnTime | 准时达到 |1 |
| completeOnTime | 准时完成 |1 |
| professional | 专业技能 |1 |
| dressNeatly | 整洁着装 |1 |
| carProtect | 车辆保护 |1 |
| goodAttitude | 态度好 |1 |
| advice | 建议 |1 |

订单需要有指定主技师。


###返回结果
{"result":true,"message":"comment","error":"","data":null}

