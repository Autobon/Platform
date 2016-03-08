# 订单

订单状态列表:

1. `NEWLY_CREATED` 新建
2. `TAKEN_UP` 已有人抢单
3. `SEND_INVITATION` 已发送合作邀请并等待结果
4. `INVITATION_ACCEPTED` 合作邀请已接受
5. `INVITATION_REJECTED` 合作邀请已拒绝
6. `IN_PROGRESS` 订单开始工作中
7. `FINISHED` 订单已结束
8. `COMMENTED` 订单已评论
9. `CANCELED` 订单已取消

当技师抢到订单或接受了别人的合作邀请时,订单进入技师的未完成订单列表.此时订单详情返回数据没有施工信息(construction字段为NULL).
当技师已点击“开始工作”后,系统为技师创建一个施工记录，此时订单详情返回数据中的施工信息字段不再为NULL,且startTime为开始工作时间.
当技师提交签到后,技师的施工记录中的signinTime将由null变为签到时间. 当技师提交工作前照片地址后, 施工记录中的beforePhotos字段不再为null,
而是上传照片网址数组用逗号拼接而成的字符串.

## 1. 已完成且作为主责任人订单列表

### URL及请求方法
`GET /api/mobile/technician/order/listMain`

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
                "id": 47,
                "orderNum": "20160303120243AS54GE",
                "orderType": 0,
                "photo": null,
                "orderTime": null,
                "addTime": 1456977763758,
                "creatorType": 0,
                "creatorId": 0,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": null,
                "mainTechId": 1,
                "secondTechId": 0,
                "status": "NEWLY_CREATED"
            },
            {
                "id": 1,
                "orderNum": "20160223134200014567",
                "orderType": 1,
                "photo": "",
                "orderTime": 1456293600000,
                "addTime": 1456196963000,
                "creatorType": 1,
                "creatorId": 1,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": "bababala",
                "mainTechId": 1,
                "secondTechId": 2,
                "status": null
            }
        ]
    }
}
```

data字段是一个典型的分页对象, 请参考 [帐户及登录 - 10. 查询技师](1-1-account.md)

## 2. 已完成且作为次责任人订单列表

### URL及请求方法
`GET /api/mobile/technician/order/listSecond`

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
        "totalElements": 1,
        "totalPages": 1,
        "pageSize": 20,
        "count": 1,
        "list": [
            {
                "id": 2,
                "orderNum": "20160223135200016789",
                "orderType": 2,
                "photo": null,
                "orderTime": 1456293600000,
                "addTime": 1456196963000,
                "creatorType": 1,
                "creatorId": 2,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": "somewords",
                "mainTechId": 2,
                "secondTechId": 1,
                "status": null
            }
        ]
    }
}
```

## 3. 获取未完成订单列表（包括主,次责任人订单）

### URL及请求方法
`GET /api/mobile/technician/order/listUnfinished`

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
        "totalElements": 3,
        "totalPages": 1,
        "pageSize": 20,
        "count": 3,
        "list": [
            {
                "id": 3,
                "orderNum": "20160303145128PU4XYS",
                "orderType": 0,
                "photo": null,
                "orderTime": null,
                "addTime": 1456987888607,
                "creatorType": 0,
                "creatorId": 0,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": null,
                "mainTechId": 1,
                "secondTechId": 0,
                "status": "NEWLY_CREATED"
            },
            {
                "id": 2,
                "orderNum": "20160223135200016789",
                "orderType": 2,
                "photo": null,
                "orderTime": 1456293600000,
                "addTime": 1456196963000,
                "creatorType": 1,
                "creatorId": 2,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": "somewords",
                "mainTechId": 2,
                "secondTechId": 1,
                "status": "TAKEN_UP"
            },
            {
                "id": 1,
                "orderNum": "20160223134200014567",
                "orderType": 1,
                "photo": "",
                "orderTime": 1456293600000,
                "addTime": 1456196963000,
                "creatorType": 1,
                "creatorId": 1,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": "bababala",
                "mainTechId": 1,
                "secondTechId": 2,
                "status": "TAKEN_UP"
            }
        ]
    }
}
```


## 4. 获取订单详细信息
获取订单信息及主技师,次技师,施工信息(仅发起请求技师的施工信息),评论信息等内容.
### URL及请求方法
`GET /api/mobile/technician/order/${orderId}`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单编号, URL点位符参数 | 1 |

示例: `GET /api/mobile/technician/order/1`


### 返回数据

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "mainTech": {
            "id": 1,
            "phone": "18812345678",
            "name": "tom",
            "gender": null,
            "avatar": null,
            "idNo": "422302198608266313",
            "idPhoto": "/etc/a.jpg",
            "bank": "工商银行",
            "bankAddress": "光谷",
            "bankCardNo": "88888888888",
            "verifyAt": null,
            "requestVerifyAt": 1457277682000,
            "verifyMsg": null,
            "lastLoginAt": 1456195103000,
            "lastLoginIp": "127.0.0.1",
            "createAt": 1455724800000,
            "star": 0,
            "voteRate": 0,
            "skill": "1,2",
            "pushId": null,
            "status": "VERIFIED"
        },
        "secondTech": null,
        "construction": null,
        "comment": null,
        "order": {
            "id": 5,
            "orderNum": "20160307125948NTCKSM",
            "orderType": 0,
            "photo": null,
            "orderTime": null,
            "addTime": 1457326788046,
            "creatorType": 0,
            "creatorId": 0,
            "creatorName": null,
            "contactPhone": null,
            "positionLon": null,
            "positionLat": null,
            "remark": null,
            "mainTechId": 1,
            "secondTechId": 0,
            "status": "NEWLY_CREATED"
        }
    }
}
```

## 5. 抢单
技师收到订单推送信息后, 抢取订单
### URL及请求方法
`POST /api/mobile/technician/order/takeup`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单编号 | 1 |


### 返回数据

#### a.抢单成功
```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 46,
        "orderNum": "20160303120243KFX8W3",
        "orderType": 0,
        "photo": null,
        "orderTime": null,
        "addTime": 1456977763720,
        "creatorType": 0,
        "creatorId": 0,
        "creatorName": null,
        "contactPhone": null,
        "positionLon": null,
        "positionLat": null,
        "remark": null,
        "mainTechId": 1,
        "secondTechId": 0,
        "status": "TAKEN_UP"
    }
}
```

#### b.已有人接单

```
{"result": false,
"message": "已有人接单",
"error": "ILLEGAL_OPERATION",
"data": null}
```

#### c.订单已取消

```
{"result": false,
"message": "订单已取消",
"error": "ILLEGAL_OPERATION",
"data": null}
```

#### d.你当前的认证技能没有....

```
{
    "result": false,
    "message": "你当前的认证技能没有车身改色",
    "error": "TECH_SKILL_NOT_SUFFICIANT",
    "data": null
}
```

#### e.没有这个订单

```
{
    "result": false,
    "message": "没有这个订单",
    "error": "NO_SUCH_ORDER",
    "data": null
}
```

#### f.你没有通过认证, 不能抢单

```
{
    "result": false,
    "message": "你没有通过认证, 不能抢单",
    "error": "NOT_VERIFIED",
    "data": null
}
```

