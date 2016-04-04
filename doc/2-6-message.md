# 通知消息
## 1. 创建通知
创建后台通知, 测试用后台管理员token: `staff:ssEoVBwJ3rSYnidORQUvhQ==`

### URL及请求方法
`POST /api/web/admin/message`

### 请求参数

| 参数名称 | 是否可选 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| title | 否 | 通知标题 | 四月贴膜季 |
| content | 否 | 通知内容 | 四月期间贴膜酬劳加倍，balabalbal |
| sendTo | 否 | 发送给 | 1-技师 2-合作商户 |

### 返回数据

````
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 3,
        "title": "四月贴膜季",
        "content": "四月期间贴膜酬劳加倍，balabalbal",
        "sentTo": 1,
        "updateTime": 1456804920000,
        "status": 0
    }
}
````

