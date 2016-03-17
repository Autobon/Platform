# 订单


## 3.订单评论
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
{"result":true,"message":"comment","error":"","data":null}


## 4.创建订单
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
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 3,
        "orderNum": "16031715P9JVT3",
        "orderType": 1,
        "photo": "a/a.jpg",
        "orderTime": 1456804920000,
        "addTime": 1458198285208,
        "creatorType": 1,
        "creatorId": 1,
        "creatorName": null,
        "contactPhone": null,
        "positionLon": null,
        "positionLat": null,
        "remark": "remark is here",
        "mainTechId": 0,
        "secondTechId": 0,
        "status": "NEWLY_CREATED"
    }
}