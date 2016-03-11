# 公共数据接口

## 1. 获取技师技能项列表
获取技师认证时的技能选项列表
### URL及请求方法
`GET /api/mobile/pub/technician/skills`

### 请求参数
无

### 返回数据

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": [
        {
            "name": "隔热层",
            "id": 1
        },
        {
            "name": "隐形车衣",
            "id": 2
        },
        {
            "name": "车身改色",
            "id": 3
        },
        {
            "name": "美容清洁",
            "id": 4
        }
    ]
}
```

## 2. 获取订单类型列表
获取订单种类列表
### URL及请求方法
`GET /api/mobile/pub/orderTypes`

### 请求参数
无

### 返回数据

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": [
        {
            "name": "隔热层",
            "id": 1
        },
        {
            "name": "隐形车衣",
            "id": 2
        },
        {
            "name": "车身改色",
            "id": 3
        },
        {
            "name": "美容清洁",
            "id": 4
        }
    ]
}
```

## 3. 获取订单工作项
获取订单工作项列表

### URL及请求方法
GET /api/mobile/pub/technician/workItems

### 请求参数

| 参数名称 | 是否可选 | 说明 | 举例 |
| ------ | ------- | ---- | ---- |
| orderType | 否 | 订单类型编号 | 1 |
| carSeat | 是 | 施工车辆座椅个数, 5或7. 不填时, 返回5座和7座所有工作项 | 5 |



### 返回数据
```
{
    "result": true,
    "message": "",
    "error": "",
    "data": [
        {
            "seat": 5,
            "name": "前风挡",
            "id": 1
        },
        {
            "seat": 5,
            "name": "左前门",
            "id": 2
        },
        {
            "seat": 5,
            "name": "右前门",
            "id": 3
        },
        {
            "seat": 5,
            "name": "左后门",
            "id": 4
        },
        {
            "seat": 5,
            "name": "右后门",
            "id": 5
        },
        {
            "seat": 5,
            "name": "左大角",
            "id": 6
        },
        {
            "seat": 5,
            "name": "右大角",
            "id": 7
        },
        {
            "seat": 5,
            "name": "后风档",
            "id": 8
        },
        {
            "seat": 7,
            "name": "前风挡",
            "id": 9
        },
        {
            "seat": 7,
            "name": "左前门",
            "id": 10
        },
        {
            "seat": 7,
            "name": "右前门",
            "id": 11
        },
        {
            "seat": 7,
            "name": "左中门",
            "id": 12
        },
        {
            "seat": 7,
            "name": "右中门",
            "id": 13
        },
        {
            "seat": 7,
            "name": "左后门",
            "id": 14
        },
        {
            "seat": 7,
            "name": "右后门",
            "id": 15
        },
        {
            "seat": 7,
            "name": "左大角",
            "id": 16
        },
        {
            "seat": 7,
            "name": "右大角",
            "id": 17
        },
        {
            "seat": 7,
            "name": "后风档",
            "id": 18
        }
    ]
}
```