# 一、订单列表及签到

## 1.订单列表
获取状态为1(已接单), 2(工作中)的订单
### URL及请求方法
GET /api/mobile/order/orderList

### 请求参数
无

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| GET | /api/mobile/order/orderList | | | {"result":true,"message":"orderList","error":null,"data":[{"id":1,"orderNum":"1","orderType":1,"photo":"1","orderTime":1420041600000,"addTime":1420041600000,"status":1,"customerType":1,"customerId":1,"customerName":"1","customerLon":"1","customerLat":"1","remark":"1","mainTechId":1,"secondTechId":1}]} |

## 2.获取位置
获取商户位置坐标

### URL及请求方法
GET /api/mobile/order/getLocation?orderId=1

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| orderId | 订单id | 1 |

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| GET | /api/mobile/order/getLocation | | | {"result":true,"message":"location","error":null,"data":{"id":1,"orderNum":"1","orderType":1,"photo":"1","orderTime":1420041600000,"addTime":1420041600000,"status":1,"customerType":1,"customerId":1,"customerName":"1","customerLon":"1","customerLat":"1","remark":"1","mainTechId":1,"secondTechId":1}} |


## 3.添加合伙人
添加合伙人id


### URL及请求方法
POST /api/mobile/order/addSecondTechId

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| orderId | 订单id | 1 |
| technicianId | 合伙人id |2 |

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| POST | /api/mobile/order/addSecondTechId | | | {"result":true,"message":"addSecondTechId","error":null,"data":null} |