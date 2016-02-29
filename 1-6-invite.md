# 邀请合作人

## 1. 发送合作邀请
向其它技师发出合作邀请
### URL及请求方法
POST /api/mobile/technician/order/{orderId}/invite/{partnerId}

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------- | ---- | ---- |
| orderId | URL点位符参数, 订单ID | 1 |
| partnerId | URL点位符参数, 将要邀请的技师ID | 2 |

举例:

```
POST /api/mobile/technician/order/1/invite/2
```

### 返回数据
1. 请求成功

```
{"result": true,
"message": "",
"error": null,
"data": null}
```

2. 订单已进入工作模式或已有人接单

```
{"result": false,
"message": "订单已进入工作模式或已有人接单",
"error": "ILLEGAL_PARAM",
"data": null}
```

3. 主技师和合作技师不能为同一人

```
{"result": false,
"message": "主技师和合作技师不能为同一人",
"error": "ILLEGAL_PARAM",
"data": null}
```

4. 系统中没有邀请的技师

```
{"result": false,
"message": "系统中没有邀请的技师",
"error": "ILLEGAL_PARAM",
"data": null}
```

当向其它技师发起合作邀请时,系统将向邀请的技师发送**合作邀请**推送消息.
**合作邀请** `action=INVITE_PARTNER` 推送消息样例:

```
{
"invitation": {
    "id": 4,
    "order": {
        "id": 13,
        "orderNum": "test-order-num",
        "orderType": 1,
        "photo": null,
        "orderTime": 1456817833035,
        "addTime": null,
        "status": 0,
        "customerType": 0,
        "customerId": 0,
        "customerName": null,
        "customerLon": null,
        "customerLat": null,
        "remark": null,
        "mainTechId": 1,
        "secondTechId": 0
    },
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
        "lastLoginAt": 1456195103000,
        "lastLoginIp": "127.0.0.1",
        "createAt": 1455724800000,
        "star": 0,
        "voteRate": 0,
        "skill": "1",
        "pushId": null,
        "status": "NOTVERIFIED"
    },
    "invitedTech": {
        "id": 12,
        "phone": "tempPhoneNo",
        "name": null,
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1456731432961,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": "",
        "status": "NOTVERIFIED"
    },
    "createAt": 1456731433129,
    "status": "NOT_ACCEPTED"
},
"action": "INVITE_PARTNER"
}
```

## 2. 接受或拒绝合作邀请
### URL及请求方法
POST /api/mobile/technician/order/invitation/{invitationId}

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------- | ---- | ---- |
| invitationId | URL点位符参数, 邀请ID | 1 |

举例:

```
POST /api/mobile/technician/order/invitation/1
```

### 返回数据
1. 请求成功

```
{"result": true,
"message": "",
"error": null,
"data": null}
```

2. 你没有这个邀请

```
{"result": false,
"message": "你没有这个邀请",
"error": "ILLEGAL_PARAM",
"data": null}
```

3. 你已接受邀请

```
{"result": false,
"message": "你已接受邀请",
"error": "ILLEGAL_PARAM",
"data": null}
```

4. 你已拒绝邀请

```
{"result": false,
"message": "你已拒绝邀请",
"error": "ILLEGAL_PARAM",
"data": null}
```

5. 已有其它人接单或邀请已失效

```
{"result": false,
"message": "已有其它人接单或邀请已失效",
"error": "ILLEGAL_PARAM",
"data": null}
```

当技师端APP收到合作邀请时,应显示一个可供选择的界面,让用户选择接受或拒绝的操作.

在用户收到合作邀请推送消息时, 在透传消息JSON中可获取邀请ID.

当合作邀请有人接单时, 发起邀请的技师将收到**邀请已接受** `action=INVITATION_ACCEPT` 推送消息, 如下:

```
{
"invitation": {
   "id": 4,
   "order": {
       "id": 13,
       "orderNum": "test-order-num",
       "orderType": 1,
       "photo": null,
       "orderTime": 1456817833035,
       "addTime": null,
       "status": 0,
       "customerType": 0,
       "customerId": 0,
       "customerName": null,
       "customerLon": null,
       "customerLat": null,
       "remark": null,
       "mainTechId": 1,
       "secondTechId": 0
   },
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
       "lastLoginAt": 1456195103000,
       "lastLoginIp": "127.0.0.1",
       "createAt": 1455724800000,
       "star": 0,
       "voteRate": 0,
       "skill": "1",
       "pushId": null,
       "status": "NOTVERIFIED"
   },
   "invitedTech": {
       "id": 12,
       "phone": "tempPhoneNo",
       "name": null,
       "gender": null,
       "avatar": null,
       "idNo": null,
       "idPhoto": null,
       "bank": null,
       "bankAddress": null,
       "bankCardNo": null,
       "verifyAt": null,
       "lastLoginAt": null,
       "lastLoginIp": null,
       "createAt": 1456731432961,
       "star": 0,
       "voteRate": 0,
       "skill": null,
       "pushId": "",
       "status": "NOTVERIFIED"
   },
   "createAt": 1456731433129,
   "status": "ACCEPTED"
},
"action": "INVITATION_ACCEPT"
}
```