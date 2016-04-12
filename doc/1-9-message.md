# 通知消息

# 拉取通知列表

### URL及请求方法
`GET /api/mobile/technician/message`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| page | 否 | 分页页码, 从1开始，默认为1 | 1 |
| pageSize | 否 | 每页条数, 默认20 | 20 |

### 返回数据

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "page": 1,
        "totalElements": 1,
        "totalPages": 1,
        "pageSize": 20,
        "count": 1,
        "list": [
            {
                "id": 3,
                "title": "Hello11",
                "content": "Haha",
                "sendTo": 1,
                "createTime": 1460371749000,
                "publishTime": 1460375231000,
                "status": 1
            }
        ]
    }
}
```
