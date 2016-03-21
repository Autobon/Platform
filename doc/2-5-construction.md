# 后台对施工表的管理

## 1.根据施工ID查询施工详细信息
根据施工ID查询施工详细信息
### URL及请求方法
`/api/web/admin/construction/{consId}`

### 请求参数
| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| consId | 是 | URL占位符参数，施工id| 1 |

### 返回数据

```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 5,
        "orderId": 0,
        "techId": 0,
        "positionLon": null,
        "positionLat": null,
        "startTime": null,
        "signinTime": null,
        "endTime": null,
        "beforePhotos": null,
        "afterPhotos": null,
        "payment": 0,
        "payStatus": 0,
        "workItems": null,
        "workPercent": 0,
        "carSeat": 0
    }
}

```


## 2.更新施工资料
后台更新施工资料
### URL及请求方法
`/api/web/admin/construction/update/{consId}`

### 请求参数
| 参数名称 | 是否必须 | 说明 | 举例 |
| ------ | -------- | ---- | --- |
| consId | 是 | URL占位符参数，施工id| 1 |
| payment | 否 | 结算金额 | 2000.1 |
| workItems | 否 | 工作项 | 车身改色 |


### 返回数据
```
{
    "result": true,
    "message": "",
    "error": "",
    "data": {
        "id": 4,
        "orderId": 0,
        "techId": 0,
        "positionLon": null,
        "positionLat": null,
        "startTime": null,
        "signinTime": null,
        "endTime": null,
        "beforePhotos": null,
        "afterPhotos": null,
        "payment": 2000.1,
        "payStatus": 0,
        "workItems": "车身改色",
        "workPercent": 0,
        "carSeat": 0
    }
}

```