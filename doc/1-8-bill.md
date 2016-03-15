# 帐单

## 1. 获取帐单列表

### URL及请求方法
`GET /api/mobile/technician/bill`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |


### 返回数据

```
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
                "id": 12,
                "techId": 1,
                "billMonth": 1435680000000,
                "count": 1,
                "sum": 520,
                "payed": false,
                "payAt": null
            },
            {
                "id": 10,
                "techId": 1,
                "billMonth": 1435680000000,
                "count": 1,
                "sum": 520,
                "payed": false,
                "payAt": null
            }
        ]
    }
}
```

## 2. 拉取帐单下的订单列表

### URL及请求方法
`GET /api/mobile/technician/bill/{billId}/order`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| billId | 是 | URL占位符参数, 将URL中的{billId}替换成订单ID即可 | 1 |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |

### 返回数据

#### a.请求成功

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
                "id": 5,
                "orderNum": "16031511D8T4KB",
                "orderType": 4,
                "photo": null,
                "orderTime": null,
                "addTime": 1458013671000,
                "creatorType": 0,
                "creatorId": 0,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": null,
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
                    "lastLoginAt": 1458030318036,
                    "lastLoginIp": "127.0.0.1",
                    "createAt": 1455724800000,
                    "skill": "1",
                    "pushId": null,
                    "status": "NEWLY_CREATED"
                },
                "secondTech": null,
                "mainConstruct": {
                    "id": 3,
                    "orderId": 5,
                    "techId": 1,
                    "positionLon": null,
                    "positionLat": null,
                    "startTime": 1433174400000,
                    "signinTime": null,
                    "endTime": 1433260800000,
                    "beforePhotos": null,
                    "afterPhotos": null,
                    "payment": 520,
                    "payStatus": 0,
                    "workItems": null,
                    "workPercent": 0,
                    "carSeat": 0
                },
                "secondConstruct": null,
                "comment": null,
                "status": "FINISHED"
            }
        ]
    }
}
```
#### b.你没有这个帐单


```
{
    "result": false,
    "message": "你没有这个帐单",
    "error": "ILLEGAL_BILL_ID",
    "data": null
}
```