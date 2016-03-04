# 技师端推送消息

## 1. 技师认证
当后台对技师提交的认证内容进行认证操作时, 推送给技师的消息.

### a. 认证通过



### b. 认证失败



## 2. 推送新订单
当有商户创建订单或后台管理员新建订单时,所有技师收到的推送消息.

```
{
    "action": "NEW_ORDER",
    "order": {
        "id": 47,
        "orderNum": "20160303120243AS54GE",
        "orderType": 0,
        "photo": null,
        "orderTime": null,
        "addTime": 1456977763758,
        "creatorType": 0,
        "creatorId": 0,
        "creatorName": null,
        "contactPhone": null,
        "positionLon": null,
        "positionLat": null,
        "remark": null,
        "mainTechId": 1,
        "secondTechId": 0,
        "status": "NEWLY_CREATED"
    },
    "title": "你收到新订单推送消息"
}
```

## 3. 合作邀请

### a. 邀请消息
当有技师向你发出合作邀请时, 被邀技师收到的推送消息.

```
{
    "action": "INVITE_PARTNER",
    "title": "张三向你发起订单合作邀请",
    "order": {
        "id": 47,
        "orderNum": "20160303120243AS54GE",
        "orderType": 0,
        "photo": null,
        "orderTime": null,
        "addTime": 1456977763758,
        "creatorType": 0,
        "creatorId": 0,
        "creatorName": null,
        "contactPhone": null,
        "positionLon": null,
        "positionLat": null,
        "remark": null,
        "mainTechId": 1,
        "secondTechId": 0,
        "status": "NEWLY_CREATED"
    },
    "owner": {
        "id": 3,
        "phone": "18827075330",
        "name": "张三",
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1455868034249,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": null,
        "statusCode": "VERIFIED"
    },
    "partner": {
        "id": 5,
        "phone": "18827071234",
        "name": "李四",
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1455868034249,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": null,
        "statusCode": "VERIFIED"
    }
}
```

### b. 邀请被接受

```
{
    "action": "INVITATION_ACCEPTED",
    "title": "李四已接受你的邀请",
    "order": {
        "id": 47,
        "orderNum": "20160303120243AS54GE",
        "orderType": 0,
        "photo": null,
        "orderTime": null,
        "addTime": 1456977763758,
        "creatorType": 0,
        "creatorId": 0,
        "creatorName": null,
        "contactPhone": null,
        "positionLon": null,
        "positionLat": null,
        "remark": null,
        "mainTechId": 1,
        "secondTechId": 0,
        "status": "NEWLY_CREATED"
    },
    "owner": {
        "id": 3,
        "phone": "18827075330",
        "name": "张三",
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1455868034249,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": null,
        "statusCode": "VERIFIED"
    },
    "partner": {
        "id": 5,
        "phone": "18827071234",
        "name": "李四",
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1455868034249,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": null,
        "statusCode": "VERIFIED"
    }
}
```

### c. 邀请被拒绝

```
{
    "action": "INVITATION_REJECTED",
    "title": "李四已拒绝你的邀请",
    "order": {
        "id": 47,
        "orderNum": "20160303120243AS54GE",
        "orderType": 0,
        "photo": null,
        "orderTime": null,
        "addTime": 1456977763758,
        "creatorType": 0,
        "creatorId": 0,
        "creatorName": null,
        "contactPhone": null,
        "positionLon": null,
        "positionLat": null,
        "remark": null,
        "mainTechId": 1,
        "secondTechId": 0,
        "status": "NEWLY_CREATED"
    },
    "owner": {
        "id": 3,
        "phone": "18827075330",
        "name": "张三",
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1455868034249,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": null,
        "statusCode": "VERIFIED"
    },
    "partner": {
        "id": 5,
        "phone": "18827071234",
        "name": "李四",
        "gender": null,
        "avatar": null,
        "idNo": null,
        "idPhoto": null,
        "bank": null,
        "bankAddress": null,
        "bankCardNo": null,
        "verifyAt": null,
        "lastLoginAt": null,
        "lastLoginIp": null,
        "createAt": 1455868034249,
        "star": 0,
        "voteRate": 0,
        "skill": null,
        "pushId": null,
        "statusCode": "VERIFIED"
    }
}
```
