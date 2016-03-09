# 施工

## 1. 开始工作
技师抢单成功后或接受合作邀请后, 点选**开始工作**
### URL及请求方法
`POST /api/mobile/technician/construct/start`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单编号 | 1 |
| ignoreInvitation | 否 | 是否忽略已发出的尚未回复的合作邀请, 默认为false | false |

### 返回数据

#### a.请求成功

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 4,
        "orderId": 41,
        "techId": 1,
        "positionLon": null,
        "positionLat": null,
        "startTime": 1456977763488,
        "signinTime": null,
        "endTime": null,
        "beforePhotos": null,
        "afterPhotos": null,
        "payment": null,
        "workItems": null,
        "carSeat": 0
    }
}
```

data字段是一个施工单对象

#### b.你没有这个订单

```
{"result": false,
"message": "你没有这个订单",
"error": "NO_ORDER",
"data": null}
```

#### c.订单已取消

```
{"result": false,
"message": "订单已取消",
"error": "ORDER_CANCELED",
"data": null}
```

#### d.订单已施工完成

```
{"result": false,
"message": "订单已施工完成",
"error": "ORDER_ENDED",
"data": null}
```

#### e.你邀请的合作人还未接受或拒绝邀请

```
{"result": false,
"message": "你邀请的合作人还未接受或拒绝邀请",
"error": "INVITATION_NOT_FINISH",
"data": null}
```

### f.你已开始工作,请不要重复操作

```
{"result": false,
"message": "你已开始工作,请不要重复操作",
"error": "REPEATED_OPERATION",
"data": null}
```

## 2. 施工签到
技师到达施工位置后，点选**签到**
### URL及请求方法
`POST /api/mobile/technician/construct/signIn`

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单编号 | 1 |
| positionLon | 是 | 签到位置经度 | 23.25478 |
| positionLat | 是 | 签到位置纬度 | 45.23145 |

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

#### b.订单已取消

```
{"result": false,
"message": "订单已取消",
"error": "ORDER_CANCELED",
"data": null}
```

#### c.系统没有你的施工单

```
{"result": false,
"message": "系统没有你的施工单, 请先点选\"开始工作\"",
"error": "NO_CONSTRUCTION",
"data": null}
```

#### d.订单还未开始工作或已结束工作

```
{"result": false,
"message": "订单还未开始工作或已结束工作",
"error": "ILLEGAL_OPERATION",
"data": null}
```
### e.你已签到, 请不要重复操作

```
{"result": false,
"message": "你已签到, 请不要重复操作",
"error": "REPEATED_OPERATION",
"data": null}
```

## 3.上传施工图片
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

## 4.提交施工前图片地址

### URL及请求方法
POST /api/mobile/technician/construct/beforePhoto

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| orderId | 订单ID | 1 |
| urls | 多个图片地址用逗号拼接而成的字符串,至少1张,不能有多余空格 | /uploads/1.jpg,/uplods/2.jpg |

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
    "error": "URLS_PATTERN_MISMATCH",
    "data": null
}
```

### c.图片数量超出限制, 最多3张

```
{
    "result": false,
    "message": "图片数量超出限制, 最多3张",
    "error": "PHOTO_LIMIT_EXCEED",
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

## 5.完成施工

### URL及请求方法
POST /api/mobile/technician/construct/finish

### 请求参数

| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| orderId | 是 | 订单ID | 1 |
| afterPhotos | 是 | 多个图片地址用逗号拼接而成的字符串,3-6张,不能有多余空格 | /uploads/1.jpg,/uplods/2.jpg |
| carSeat | 否 | 车辆座位数种类,5或7,美容清洁时可以省略,其它情况须指定 | 5 |
| workItems | 否 | 施工工作项, 用逗号分隔.美容清洁时可以省略,其它情况须指定 | 1,2 |
| percent | 否 | 美容清洁时必选, 美容清洁工作百分比, 用小数表示 | 0.8 |


### 返回数据

#### a.请求成功

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 25,
        "orderId": 30,
        "techId": 1,
        "positionLon": null,
        "positionLat": null,
        "startTime": 1457513028797,
        "signinTime": 1457513028797,
        "endTime": 1457513028964,
        "beforePhotos": "a.jpg",
        "afterPhotos": "a.jpg,b.jpg,c.jpg",
        "payment": 20,
        "workItems": null,
        "workPercent": 0.2,
        "carSeat": 0
    }
}
```

#### b.afterPhotos参数格式错误, 施工完成后照片应至少3张, 至多6张

```
{
    "result": false,
    "message": "afterPhotos参数格式错误, 施工完成后照片应至少3张, 至多6张",
    "error": "AFTER_PHOTOS_PATTERN_MISMATCH",
    "data": null
}
```

#### c.workItems参数格式错误

```
{
    "result": false,
    "message": "workItems参数格式错误",
    "error": "WORK_ITEMS_PATTERN_MISMATCH",
    "data": null
}
```

#### d.百分比值应在0-1之间

```
{
    "result": false,
    "message": "百分比值应在0-1之间",
    "error": "PERCENT_VALUE_INVALID",
    "data": null
}
```

#### e.没有这个订单

```
{
    "result": false,
    "message": "没有这个订单",
    "error": "NO_SUCH_ORDER",
    "data": null
}
```

#### f.订单未开始或已结束

```
{
    "result": false,
    "message": "订单未开始或已结束",
    "error": "ORDER_NOT_IN_PROGRESS",
    "data": null
}
```

#### g.没有你的施工单

```
{
    "result": false,
    "message": "没有你的施工单",
    "error": "NO_CONSTRUCTION",
    "data": null
}
```

#### h.没有上传施工前照片

```
{
    "result": false,
    "message": "没有上传施工前照片",
    "error": "NO_BEFORE_PHOTO",
    "data": null
}
```

#### i.请等待主技师先提交完成

```
{
    "result": false,
    "message": "请等待主技师先提交完成",
    "error": "MAIN_TECH_NOT_COMMIT",
    "data": null
}
```

#### j.主技师提交车型为...座, 请保持一致

```
{
    "result": false,
    "message": "主技师提交车型为5座, 请保持一致",
    "error": "CAR_SEAT_MISMATCH",
    "data": null
}
```

#### k.主技师已提交: ...., 请不要提交与主技师相同的工作项

```
{
    "result": false,
    "message": "主技师已提交: \"左前窗\", 请不要提交与主技师相同的工作项",
    "error": "WORK_ITEM_CONFLICT",
    "data": null
}
```

#### l.工作量百分比与主技师之和超过100%, 主技师已完成..%

```
{
    "result": false,
    "message": "工作量百分比与主技师之和超过100%, 主技师已完成80%",
    "error": "PERCENT_SUM_EXCEED",
    "data": null
}
```

#### m.无效工作项: ...

```
{
    "result": false,
    "message": "无效工作项: 1,2",
    "error": "WORK_ITEM_NOT_IN_LIST",
    "data": null
}
```