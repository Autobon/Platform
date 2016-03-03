# 订单
## 1. 创建订单
创建后台订单, 测试用后台管理员token: `staff:ssEoVBwJ3rSYnidORQUvhQ==`

### URL及请求方法
POST /api/web/admin/order

### 请求参数

| 参数名称 | 是否可选 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderType | 否 | 订单类型, 1-隔热膜 2-隐形车衣 3-车身改色 4-美容清洁 | 1 |
| orderTime | 否 | 预约时间, 格式: yyyy-MM-dd HH:mm | 2016-01-02 09:01 |
| positionLon | 否 | 订单位置经度 | 20.214411 |
| positionLat | 否 | 订单位置维度 | 35.123521 |
| contactPhone | 否 | 订单联系方式 | 18812345678 |
| photo | 是 | 订单图片网址, 上传时图片时返回的地址 | /uploads/order/o-122.jpg |
| remark | 是 | 订单备注内容 |  |

### 返回数据

````
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 51,
        "orderNum": "20160303141432Q4NA7P",
        "orderType": 1,
        "photo": "",
        "orderTime": 1456804920000,
        "addTime": 1456985672438,
        "creatorType": 2,
        "creatorId": 1,
        "creatorName": "超级管理员",
        "contactPhone": "18812345678",
        "positionLon": "25.22342",
        "positionLat": "36.45485",
        "remark": "remark is here",
        "mainTechId": 0,
        "secondTechId": 0,
        "status": "NEWLY_CREATED"
    }
}
````

