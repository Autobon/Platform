# 一、施工

## 1.上传工作图片
上传工作前和工作后图片

### URL及请求方法
POST /api/mobile/technician/construct/uploadPhoto

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| file | 上传的图片 | |

### 返回数据

#### a.请求成功

```
{
    "result": true,
    "error": "",
    "message": "",
    "date": "/uploads/order/b120160304165030-1.jpg"
}
```

#### b.没有上传文件

```
{
    "result": false,
    "error": "NO_UPLOAD_FILE",
    "message": "没有上传文件",
    "date": null
}
```

## 2.提交施工前图片地址

### URL及请求方法
POST /api/mobile/technician/construct/beforePhoto

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| orderId | 订单ID | 1 |
| urls | 多个图片地址用逗号拼接而成的字符串,不能有多余空格 | /uploads/1.jpg,/uplods/2.jpg |

### 返回数据

#### a.请求成功

```
{
    "result": true,
    "message": null,
    "error": null,
    "data": null
}
```

### b.图片地址格式错误, 请查阅urls参数说明

```
{
    "result": false,
    "message": "图片地址格式错误, 请查阅urls参数说明",
    "error": "PHOTO_PATTERN_MISMATCH",
    "data": null
}
```

### c.图片数量超出限制, 最多3张

```
{
    "result": false,
    "message": "图片数量超出限制, 最多3张",
    "error": "PHOTO_LIMIT_EXCCED",
    "data": null
}
```

### d.系统没有你的施工单

```
{
    "result": false,
    "message": "系统没有你的施工单",
    "error": "NO_CONSTRUCTION",
    "data": null
}
```

### d.签到前不可上传照片

```
{
    "result": false,
    "message": "系统没有你的施工单",
    "error": "CONSTRUCTION_NOT_SIGNIN",
    "data": null
}
```

### d.你已完成施工,不可再次上传照片

```
{
    "result": false,
    "message": "你已完成施工,不可再次上传照片",
    "error": "CONSTRUCTION_ENDED",
    "data": null
}
```