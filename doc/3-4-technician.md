# 技师

## 1.根据订单ID获取技师相关信息

### URL及请求方法
get("api/mobile/coop/technician/getTechnician")

### 请求参数
| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单ID | 1 |

### 返回数据

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "technician": {
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
            "lastLoginAt": 1456195103000,
            "lastLoginIp": "127.0.0.1",
            "createAt": 1455724800000,
            "skill": "1",
            "pushId": null,
            "status": "NEWLY_CREATED"
        },
        "totalOrders": 10,
        "starRate": 5.0
    }
}

```


#### a.订单没有关联主技师

```
{
    "result": false,
    "message": "订单没有关联主技师",
    "error": "ILLEGAL_PARAM",
    "data": null
}
```

#### b.没有此订单

```
{
    "result": false,
    "message": "没有此订单",
    "error": "ILLEGAL_PARAM",
    "data": null
}

```





