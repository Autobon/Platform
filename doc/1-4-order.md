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
| status | 否 | 订单状态，默认为1 | 1 所有订单  2 未完成  3 已完成 4 合作的订单 |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |


### 返回数据

```
{
  "status": true,
  "message": {
    "content": [
      {
        "id": 31,
        "orderNum": "16111610HRS854",
        "photo": "/uploads/order/photo/20161116102323127236.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": null,
        "afterPhotos": null,
        "startTime": null,
        "endTime": null,
        "signTime": null,
        "takenTime": 1479263036000,
        "createTime": 1479263028000,
        "type": null,
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "测试开始工作界面图片",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "TAKEN_UP",
        "agreedStartTime": null
      },
      {
        "id": 32,
        "orderNum": "161116148634X2",
        "photo": "/uploads/order/photo/20161116140138696699.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": null,
        "afterPhotos": null,
        "startTime": null,
        "endTime": null,
        "signTime": null,
        "takenTime": 1479276185000,
        "createTime": 1479276165000,
        "type": null,
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "测试首页，是否有抢单展示",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "TAKEN_UP",
        "agreedStartTime": null
      },
      {
        "id": 10,
        "orderNum": "16110909BGCECB",
        "photo": "/uploads/order/photo/20161109094422624952.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": "/uploads/order/c-161109134315-UDBQAQJV.jpg",
        "afterPhotos": null,
        "startTime": 1478670197000,
        "endTime": null,
        "signTime": 1478662657000,
        "takenTime": 1478655906000,
        "createTime": 1478655901000,
        "type": null,
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "测试进入订单1",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "AT_WORK",
        "agreedStartTime": null
      },
      {
        "id": 14,
        "orderNum": "161109144R6NRN",
        "photo": "/uploads/order/photo/20161109141128914140.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": "/uploads/order/c-161109141645-HEWNMGCK.jpg",
        "afterPhotos": null,
        "startTime": 1478672207000,
        "endTime": null,
        "signTime": 1478672155000,
        "takenTime": 1478671964000,
        "createTime": 1478671915000,
        "type": null,
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "测试订单状态",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "AT_WORK",
        "agreedStartTime": null
      },
      {
        "id": 15,
        "orderNum": "161109145N4MV8",
        "photo": "/uploads/order/photo/20161109141215060918.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": "/uploads/order/c-161110094845-TUR52WAJ.jpg",
        "afterPhotos": null,
        "startTime": 1478742528000,
        "endTime": null,
        "signTime": 1478678609000,
        "takenTime": 1478671958000,
        "createTime": 1478671953000,
        "type": null,
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "测试订单状态2",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "AT_WORK",
        "agreedStartTime": null
      },
      {
        "id": 16,
        "orderNum": "16110914ULK9F8",
        "photo": "/uploads/order/photo/20161109141347673473.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": "/uploads/order/c-161109142601-CC5K8FMV.jpg",
        "afterPhotos": null,
        "startTime": 1478672784000,
        "endTime": null,
        "signTime": 1478672736000,
        "takenTime": 1478672059000,
        "createTime": 1478672052000,
        "type": null,
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "测试订单状态3",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "AT_WORK",
        "agreedStartTime": null
      },
      {
        "id": 23,
        "orderNum": "16111010RGFQV5",
        "photo": "/uploads/order/photo/20161110104756142898.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": "/uploads/order/c-161110140259-HNREX77G.jpg",
        "afterPhotos": null,
        "startTime": 1478757781000,
        "endTime": null,
        "signTime": 1478748999000,
        "takenTime": 1478746154000,
        "createTime": 1478746111000,
        "type": "1,2",
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "测试第一个开始工作jiemian",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "AT_WORK",
        "agreedStartTime": null
      },
      {
        "id": 33,
        "orderNum": "16111614RSNW52",
        "photo": "/uploads/order/photo/20161116143009457201.jpg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": "/uploads/order/c-161116151447-MQLMQDWW.jpg,/uploads/order/c-161116151450-F4YJ8F8C.jpg,/uploads/order/c-161116151453-CGMJ99QL.jpg,/uploads/order/c-161116151459-MAK4DWT9.jpg",
        "afterPhotos": null,
        "startTime": 1479280501000,
        "endTime": null,
        "signTime": 1479280469000,
        "takenTime": 1479277830000,
        "createTime": 1479277823000,
        "type": null,
        "coopId": 2,
        "coopName": "超级英卡",
        "address": "405",
        "longitude": "114.4093215959149",
        "latitude": "30.48114024418524",
        "creatorId": 2,
        "creatorName": "超级英卡",
        "contactPhone": "13396077412",
        "remark": "而你我破POP民心破破破哦",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "AT_WORK",
        "agreedStartTime": null
      },
      {
        "id": 19,
        "orderNum": "16110916XNQH4G",
        "photo": "/uploads/order/photo/20161109164617223433.jpeg",
        "creatorType": 0,
        "techId": 1,
        "techName": "tom",
        "techPhone": "18812345678",
        "beforePhotos": "/uploads/order/c-161110141320-CV9HX4ME.jpg",
        "afterPhotos": null,
        "startTime": 1478758404000,
        "endTime": null,
        "signTime": 1478758395000,
        "takenTime": 1478757334000,
        "createTime": 1478681194000,
        "type": null,
        "coopId": 3,
        "coopName": "incar",
        "address": "湖北省武汉市洪山区光谷软件园C6栋405",
        "longitude": "114.4100851555984",
        "latitude": "30.4839179299059",
        "creatorId": 3,
        "creatorName": "incar",
        "contactPhone": "18771148252",
        "remark": "得了急急急",
        "orderConstructionShow": null,
        "techLongitude": null,
        "techLatitude": null,
        "agreedEndTime": null,
        "status": "AT_WORK",
        "agreedStartTime": null
      }
    ],
    "totalElements": 9,
    "totalPages": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "first": true,
    "sort": null,
    "numberOfElements": 9
  }
}
```

data字段是一个典型的分页对象, 请参考 [帐户及登录 - 10. 查询技师](1-1-account.md)





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
    
