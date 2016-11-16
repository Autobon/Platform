# 订单

订单状态列表:

* `CREATED_TO_APPOINT` 新建待指定技师
* `NEWLY_CREATED` 新建并已推送
* `TAKEN_UP` 已接单
* `SEND_INVITATION` 已发送合作邀请并等待结果
* `INVITATION_ACCEPTED` 合作邀请已接受
* `INVITATION_REJECTED` 合作邀请已拒绝
* `IN_PROGRESS` 订单开始工作中
* `SIGNED_IN` 已签到
* `FINISHED` 订单已结束
* `COMMENTED` 订单已评论
* `CANCELED` 订单已撤销(商户撤销)
* `GIVEN_UP` 订单已被放弃(技师放弃)
* `EXPIRED` 订单已超时, 超过预约3个小时还没有签到的订单自动进入超时状态, 超过3天未完成施工的订单自动进入超时

当技师抢到订单或接受了别人的合作邀请时,订单进入技师的未完成订单列表.此时订单详情返回数据没有施工信息(construction字段为NULL).
当技师已点击“开始工作”后,系统为技师创建一个施工记录，此时订单详情返回数据中的施工信息字段不再为NULL,且startTime为开始工作时间.
当技师提交签到后,技师的施工记录中的signinTime将由null变为签到时间. 当技师提交工作前照片地址后, 施工记录中的beforePhotos字段不再为null,
而是上传照片网址数组用逗号拼接而成的字符串.

施工单支付状态 `payStatus` :

1. 0 - 未出账
2. 1 - 已出账,未支付
3. 2 - 已完成支付

## 1. 查询本人订单

### URL及请求方法
`GET /api/mobile/technician/v2/order`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| status | 否 | 订单状态，默认为1 | 1 所有订单  2 未完成  3 已完成 |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |


### 返回数据

```
{
    "status": true,
    "message": {
        "page": 1,
        "totalElements": 1,
        "totalPages": 1,
        "pageSize": 20,
        "count": 1,
        "list": [
            {
                "id": 5,
                "orderNum": "16031109BN39AJ",
                "orderType": 4,
                "photo": null,
                "orderTime": null,
                "addTime": 1457659791000,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": null,
                "mainTech":1,
                "status": "IN_PROGRESS"
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
数据结构与主责任人订单列表相同。



## 3. 获取订单详细信息
获取订单信息及主技师,次技师,施工信息(仅发起请求技师的施工信息),评论信息等内容.
### URL及请求方法
`GET /api/mobile/technician/v2/order/{orderId:\\d+}`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单编号, URL点位符参数 | 1 |

示例: `GET /api/mobile/technician/v2/order/1`


### 返回数据

```
{
  "status": true,
  "message": {
    "id": 1,
    "orderNum": "16022313fsd123",
    "photo": "",
    "creatorType": 2,
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
    "type": "隔热膜,隐形车衣",
    "coopId": 1,
    "coopName": "Tom",
    "address": "软件园中路",
    "longitude": "114.287685",
    "latitude": "30.639203",
    "creatorId": 1,
    "creatorName": "超级管理员",
    "contactPhone": "13072726003",
    "remark": "bababala",
    "orderConstructionShow": [
      {
        "techId": 1,
        "techName": "tom",
        "isMainTech": 1,
        "payStatus": 0,
        "payment": 0,
        "projectPosition": [
          {
            "project": "隔热膜",
            "position": "前风挡,左前门"
          },
          {
            "project": "隐形车衣",
            "position": "右前门,左后门+角"
          }
        ]
      },
      {
        "techId": 2,
        "techName": "tom2",
        "isMainTech": 0,
        "payStatus": 0,
        "payment": 0,
        "projectPosition": [
          {
            "project": "隐形车衣",
            "position": "前风挡"
          },
          {
            "project": "车身改色",
            "position": "前风挡"
          }
        ]
      }
    ],
    "techLongitude": null,
    "techLatitude": null,
    "status": "FINISHED",
    "agreedStartTime": 1478495394000,
    "agreedEndTime": 1479877797000
  }
}
```

## 4. 抢单
技师收到订单推送信息后, 抢取订单
### URL及请求方法
`POST /api/mobile/technician/v2/order/take`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单编号 | 1 |


### 返回数据

#### a.抢单成功
```
{
    "status": true,
    "message": {
        "id": 46,
        "orderNum": "20160303120243KFX8W3",
        "orderType": 0,
        "photo": null,
        "orderTime": null,
        "addTime": 1456977763720,
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
{"status": false,
"message": "已有人接单"
}
```

#### c.订单已取消

```
{"status": false,
"message": "订单已取消"
}
```

#### d.你当前的认证技能没有....

```
{
    "status": false,
    "message": "你当前的认证技能没有车身改色"
}
```

#### e.没有这个订单

```
{
  "status": false,
  "message": "没有这个订单"
}
```

#### f.你没有通过认证, 不能抢单

```
{
  "status": false,
  "message": "你没有通过认证, 不能抢单"
}
```

## 6. 拉取可抢订单列表
### URL及请求方法
`GET /api/mobile/technician/v2/order/listNew`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |


### 返回数据

```
{
    "status": true,
    "message": {
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
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": null,
                "mainTechId": 0,
                "secondTechId": 0,
                "status": "NEWLY_CREATED"
            }, {
                "id": 48,
                "orderNum": "20160303120243AS5WWW",
                "orderType": 0,
                "photo": null,
                "orderTime": null,
                "addTime": 1456977763758,
                "creatorName": null,
                "contactPhone": null,
                "positionLon": null,
                "positionLat": null,
                "remark": null,
                "mainTechId": 0,
                "secondTechId": 0,
                "status": "NEWLY_CREATED"
            }
        ]
    }
}
```

## 7. 放弃订单
技师在接到订单后30分钟内或预约的时间5小时前可以放弃订单

### URL及请求方法
`PUT /api/mobile/technician/v2/order/{orderId:\d+}/cancel`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | URL占位符参数, 订单编号  | 1 |

### 返回数据
1. 请求成功

    ```
    {
        "status": true,
        "message": "订单已放弃，已重新释放"
    }
    ```
    
2. 没有此订单

    ```
    {
        "status": false,
        "message": "没有此订单"
    }
    ```
    
