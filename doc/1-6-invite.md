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

2.订单已进入工作模式

```
{"result": false,
"message": "订单已进入工作模式",
"error": "ORDER_STARTED",
"data": null}
```

3.订单已有人接受合作邀请

```
{"result": false,
"message": "订单已有人接受合作邀请",
"error": "INVITATION_ACCEPTED",
"data": null}
```

3.主技师和合作技师不能为同一人

```
{"result": false,
"message": "主技师和合作技师不能为同一人",
"error": "ILLEGAL_OPERATION",
"data": null}
```

4.系统中没有邀请的技师

```
{"result": false,
"message": "系统中没有邀请的技师",
"error": "NO_SUCH_TECH",
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

6.受邀技师没有通过认证

```
{
  "result": false,
  "message": "受邀技师没有通过认证",
  "error": "NOT_VERIFIED",
  "data": null
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
"message": "无效操作: 订单已改邀他人或订单已强制开始",
"error": "NO_SUCH_INVITATION",
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
"message": "订单已结束",
"error": "ORDER_FINISHED",
"data": null}
```
