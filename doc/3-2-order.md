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