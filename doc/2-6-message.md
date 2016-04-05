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

## 2. 分页查找通知
创建后台通知, 测试用后台管理员token: `staff:ssEoVBwJ3rSYnidORQUvhQ==`

### URL及请求方法
`GET /api/web/admin/message`

### 请求参数

| 参数名称 | 是否可选 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| page | 是 | 查找页数，默认查找第一页 | 1 |
| pageSize | 是 | 查找记录数，默认记录数为20条 | 10 |

### 返回数据

````
{
    "result": true,
    "message": "",
    "error": "",
     "data": {
             "page": 1,
             "totalElements": 2,
             "totalPages": 1,
             "pageSize": 20,
             "count": 2,
             "list": [
                 {
                         "id": 1,
                         "title": "测试",
                         "content": "发送给技师",
                         "sentTo": 1,
                         "updateTime": 2016-04-03 19:59:00,
                         "status": 0
                     },
                     {
                         "id": 3,
                         "title": "测试",
                         "content": "发送给合作商户",
                         "sentTo": 2,
                         "updateTime": 2016-04-03 18:59:00,
                         "status": 0
                     }
                   ]
            }
}
````

## 3. 查找单个通知
创建后台通知, 测试用后台管理员token: `staff:ssEoVBwJ3rSYnidORQUvhQ==`

### URL及请求方法
`GET /api/web/admin/message/{id}`

### 请求参数

| 参数名称 | 是否可选 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| id | 否 | 通知id | 1 |

### 返回数据

````
{
    "result": true,
    "message": "",
    "error": "",
    "data":  {
                 "id": 1,
                 "title": "测试",
                 "content": "发送给技师",
                 "sentTo": 1,
                 "updateTime": 2016-04-03 19:59:00,
                 "status": 0
             }
}
````

## 4. 删除单个通知
创建后台通知, 测试用后台管理员token: `staff:ssEoVBwJ3rSYnidORQUvhQ==`

### URL及请求方法
`DELETE /api/web/admin/message/{id}`

### 请求参数

| 参数名称 | 是否可选 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| id | 否 | 通知id | 1 |

### 返回数据

````
{
    "result": true,
    "message": "comment",
    "error": "",
    "data": null
}
````

#### a.删除成功

```
{
    "result": true,
    "message": "删除成功",
    "error": "",
    "data": null
}
```

#### b.没有找到对应的通知记录
 
 ```
 {
     "result": false,
     "message": "没有找到对应的通知记录",
     "error": "",
     "data": null
 }
 ```
 
#### c.已发布的通知不能删除
 
 ```
 {
     "result": false,
     "message": "已发布的通知不能删除",
     "error": "",
     "data": null
 }
 ```
 
## 5. 修改或发布通知
创建后台通知, 测试用后台管理员token: `staff:ssEoVBwJ3rSYnidORQUvhQ==`

### URL及请求方法
`PUT /api/web/admin/message/{id}`

### 请求参数

| 参数名称 | 是否可选 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| title | 是 | 通知标题 | 四月贴膜季 |
| content | 是 | 通知内容 | 四月期间贴膜酬劳加倍，balabalbal |
| sendTo | 是 | 发送给，1-技师 2-合作商户 | 1 |
| status | 是 | 状态，0-未发布 1-已发布（且推送给对应的客户端） | 1 |
### 返回数据

````
{
 "result": true,
 "message": "修改成功",
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