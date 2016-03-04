# 技师管理
## 1. 技师认证
对技师进行信息认证. 认证成功或拒绝, 被认证的技师都将收到认证结果的推送消息. 推送消息,
参见: [技师端推送消息-技师认证](1-7-push.md)
### URL及请求方法
`GET /api/web/admin/technician/verify/{techId}`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| techId | 是 | 技师ID, URL占位符参数| 1 |
| verified | 是 | 认证通过或拒绝, true或false | true0 |

### 返回数据

#### a.请求成功

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": null
}
```

#### b.没有这个技师


```
{"result": false,
"message": "没有这个技师",
"error": "ILLEGAL_PARAM",
"data": null}
```