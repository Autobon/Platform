# 一、施工

## 1.上传工作图片
上传工作前和工作后图片，工作前可上传1-3张图片，工作后可上传3-6张图片

### URL及请求方法
POST /api/mobile/technician/construct/uploadPhoto

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| file | 上传的图片 | |
| no | 图片序号 | 1 |
| orderId | 订单ID | 1 |
| isBefore | 是否是工作前图片，true或false  | true |

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
#### b.你没有这个订单

```
{
    "result": false,
    "error": "ILLEGAL_OPERATION",
    "message": "你没有这个订单",
    "date": null
}
```
#### c.非施工中订单, 不允许上传照片

```
{
    "result": false,
    "error": "ILLEGAL_OPERATION",
    "message": "非施工中订单, 不允许上传照片",
    "date": null
}
```
#### d.没有上传文件

```
{
    "result": false,
    "error": "NO_UPLOAD_FILE",
    "message": "没有上传文件",
    "date": null
}
```
#### e.系统没有你的施工单

```
{
    "result": false,
    "error": "SYSTEM_CORRUPT",
    "message": "系统没有你的施工单",
    "date": null
}
```
#### f.施工前照片序号只能为1,2,3

```
{
    "result": false,
    "error": "ILLEGAL_PARAM",
    "message": "施工前照片序号只能为1,2,3",
    "date": null
}
```
#### g.施工后照片序号只能为1-6

```
{
    "result": false,
    "error": "ILLEGAL_PARAM",
    "message": "施工后照片序号只能为1-6",
    "date": null
}
```

