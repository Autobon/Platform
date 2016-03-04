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
1.请求成功

```
{"result": true,
"message": "",
"error": null,
"data": null}
```

2.订单已进入工作模式或已有人接单

```
{"result": false,
"message": "订单已进入工作模式或已有人接单",
"error": "ILLEGAL_PARAM",
"data": null}
```

3.主技师和合作技师不能为同一人

```
{"result": false,
"message": "主技师和合作技师不能为同一人",
"error": "ILLEGAL_PARAM",
"data": null}
```

4.系统中没有邀请的技师

```
{"result": false,
"message": "系统中没有邀请的技师",
"error": "ILLEGAL_PARAM",
"data": null}
```

5.邀请的技师的认证技能没有....

```
{
  "result": false,
  "message": "张三的认证技能没有车身改色",
  "error": "TECH_SKILL_NOT_SUFFICIANT",
  "data": null
}
```

当向其它技师发起合作邀请时,系统将向邀请的技师发送**合作邀请**推送消息.
**合作邀请** `action=INVITE_PARTNER` 推送消息样例:

```
{
"owner": {
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
    "statusCode": "NOTVERIFIED"
},
"partner": {
    "id": 14,
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
    "createAt": 1456740362383,
    "star": 0,
    "voteRate": 0,
    "skill": null,
    "pushId": "",
    "statusCode": "NOTVERIFIED"
},
"action": "INVITE_PARTNER",
"order": {
    "id": 15,
    "orderNum": "test-order-num",
    "orderType": 1,
    "photo": null,
    "orderTime": 1456826762442,
    "addTime": null,
    "statusCode": 0,
    "creatorType": 0,
    "creatorId": 0,
    "creatorName": null,
    "positionLon": null,
    "positionLat": null,
    "remark": null,
    "mainTechId": 1,
    "secondTechId": 14,
    "enumStatus": "INVITATION_NOT_ACCEPTED"
}
}
```

## 2. 接受或拒绝合作邀请
### URL及请求方法
POST /api/mobile/technician/order/{orderId}/invitation

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------- | ---- | ---- |
| orderId | URL点位符参数, 订单ID | 1 |
| accepted | POST参数, 布尔类型true或false, 是否接受邀请 | true |

举例:

```
POST /api/mobile/technician/order/1/invitation
```

### 返回数据
1.请求成功

```
{"result": true,
"message": "",
"error": null,
"data": null}
```

2.你没有这个邀请, 或订单已改邀他人

```
{"result": false,
"message": "你没有这个邀请, 或订单已改邀他人",
"error": "ILLEGAL_OPERATION",
"data": null}
```

3.你已接受邀请

```
{"result": false,
"message": "你已接受邀请",
"error": "REPEATED_OPERATION",
"data": null}
```

4.你已拒绝邀请

```
{"result": false,
"message": "你已拒绝邀请",
"error": "REPEATED_OPERATION",
"data": null}
```

5.订单已开始或已结束

```
{"result": false,
"message": "订单已开始或已结束",
"error": "ILLEGAL_OPERATION",
"data": null}
```

当技师端APP收到合作邀请时,应显示一个可供选择的界面,让用户选择接受或拒绝的操作.

在用户收到合作邀请推送消息时, 在透传消息JSON中可获取邀请订单ID.

当合作邀请有人接单时, 发起邀请的技师将收到**邀请已接受** `action=INVITATION_ACCEPT` 推送消息, 如下:

```
{
"owner": {
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
    "statusCode": "NOTVERIFIED"
},
"partner": {
    "id": 14,
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
    "createAt": 1456740362383,
    "star": 0,
    "voteRate": 0,
    "skill": null,
    "pushId": "",
    "statusCode": "NOTVERIFIED"
},
"action": "INVITATION_ACCEPT",
"order": {
    "id": 15,
    "orderNum": "test-order-num",
    "orderType": 1,
    "photo": null,
    "orderTime": 1456826762442,
    "addTime": null,
    "statusCode": 1,
    "creatorType": 0,
    "creatorId": 0,
    "creatorName": null,
    "positionLon": null,
    "positionLat": null,
    "remark": null,
    "mainTechId": 1,
    "secondTechId": 14,
    "enumStatus": "INVITATION_ACCEPTED"
}
}
```