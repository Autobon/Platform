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

