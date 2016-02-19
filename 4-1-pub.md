# 一、获取技能项及工作项

## 1.获取技能项
认证时获取技能选项
### URL及请求方法
GET /api/mobile/pub/getSkill

### 请求参数
无

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| GET | /api/mobile/pub/getSkill | | | {"result":true,"message":"skill","error":null,"data":["汽车贴膜","美容清洁","隐形车衣","车身改色"]} |

## 2.获取工作项
获取工作项

### URL及请求方法
GET /api/mobile/pub/getWork

### 请求参数

| 参数名称 | 说明 | 举例 |
| ------ | ---- | --- |
| codemap | 技能字典 | skill |

### 用例
| Method | URL | Header | Body | Result |
| ------ | --- | ------ | ---- | ------ |
| GET | /api/mobile/pub/getWork | | | {"result":true,"message":"work","error":null,"data":["前档","后档"]} |
