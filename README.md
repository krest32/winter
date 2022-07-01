# Winter项目介绍

## 整体结构介绍

### 总体介绍

整个项目是B2C商业模式，使用微服务架构，项目采用前后端分离开发，项目的开发借鉴了尚硅谷课堂中的谷粒学院模版进行开发
### 项目功能模块
+ 前端系统：博客管理、文章上传、视频上传、视频播放、账号单点登录、购物车、微信支付等
+ 后端系统：文章管理、视频管理、商品管理等

### 涉及技术
+ 实现前后端分离，通过 **Json** 进行数据交互，前端再也不用关注后端技术
+ 前端页面使用 **Nuxt.JS**，服务器渲染
+ 后端页面使用**VUE**与**Element-Ui**，提升开发效率
+ 引入**Swagger** 文档支持，方便编写 **API** 接口文档。
+ 引入**RabbitMQ** 消息队列，用于分布式事务中的消息传递
+ 引入**ElasticSearch** 为杂货铺功能提供检索服务，商品的上下架均在**ElasticSearch**中完成
+ 使用**Jenkins+Jenkins+Docker**，完成实现持续集成、持续部署。
+ 引入阿里云对象存储
+ 购物车公车使用**Redis**缓存功能进行实现
+ 引入**阿里云视频点播**，实现视频的上传与播放
+ 引入**阿里云短信服务**，支持手机号码注册登陆
+ 采用 **Nacos** 作为服务发现和配置中心
+ 使用**微信支付**，实现第三方支付功能

### 其他代码仓库

+ Winter 后端页面代码：https://gitee.com/krest202/winter-admin
+ Winter 前端页面代码：https://gitee.com/krest202/winter-web

### 待完善功能

+ 评论
+ 商品详情页
+ 链路追踪
+ 后台权限管理
+ ········

### 后端技术

|      技术      |           说明            |                             官网                             |
| :------------: | :-----------------------: | :----------------------------------------------------------: |
|   SpringBoot   |          MVC框架          | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
|  SpringCloud   |        微服务框架         |           https://spring.io/projects/spring-cloud/           |
| SpringSecurity |      认证和授权框架       |          https://spring.io/projects/spring-security          |
|  MyBatis-Plus  |          ORM框架          |                   https://mp.baomidou.com/                   |
|   Swagger-UI   |       文档生产工具        | [ https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
|     Kibana     |     分析和可视化平台      |               https://www.elastic.co/cn/kibana               |
| Elasticsearch  |         搜索引擎          | [ https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
| Github Actions |        自动化部署         |              https://help.github.com/en/actions              |
|    Logstash    | 用于接收Beats的数据并处理 |              https://www.elastic.co/cn/logstash              |
|      JWT       |        JWT登录支持        |                 https://github.com/jwtk/jjwt                 |
|    RabbitMQ    |         消息队列          |   [ https://www.rabbitmq.com/](https://www.rabbitmq.com/)    |
|     Redis      |        分布式缓存         |                      https://redis.io/                       |
|     Docker     |        容器化部署         |      [ https://www.docker.com](https://www.docker.com/)      |
|     Druid      |       数据库连接池        | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|   阿里云oss    |         对象储存          | https://www.aliyun.com/product/oss?spm=5176.19720258.J_8058803260.53.33d12c4aB0oFo8 |
| 阿里云短信服务 |       短信发送平台        | https://www.aliyun.com/product/sms?spm=5176.7933691.J_8058803260.68.41114c59k1SLio |
| 阿里云视频点播 |         视频服务          | https://www.aliyun.com/product/vod?spm=5176.10695662.5694434980.1.7fce51efz4dLhq |
|     Lombok     |     简化对象封装工具      | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      |  HTTP和反向代理web服务器  |                      http://nginx.org/                       |
|     SLF4J      |         日志框架          |                    http://www.slf4j.org/                     |



## 未来计划

- [ ] 添加评论功能
- [ ] 支持第三方登陆
- [ ] 网站后台的权限控制
- [ ] 完善前端用户中心
- [ ] 完善用户详情页面
- [ ] 添加后台信息统计功能
- [ ] 







## 前端页面预览图（仅作为效果展示）

### 首页

![image-20210422134121356](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134121.png)

![image-20210422134253527](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134253.png)

![image-20210422134318865](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134319.png)

![image-20210422134330162](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134330.png)

### 博客页面

#### 全部文章

![image-20210422134359058](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134359.png)

#### 单个文章阅读

![image-20210422134414641](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134414.png)

### 作者管理

#### 全部作者页面

![image-20210422134443159](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134443.png)

#### 单独作者页面

![image-20210422134501384](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134501.png)

### 视频页面

#### 全部视频页面

![image-20210422134528783](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134529.png)

#### 视频分类展示

![image-20210422134553539](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134553.png)

![image-20210422134617338](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134617.png)

#### 收费视频页面

![image-20210422134744835](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134745.png)

#### 视频搜索功能

![image-20210422135212372](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135212.png)

#### 视频播放页面

![image-20210422134812536](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134812.png)



### 商品页面

#### 全部商品页面

![image-20210422134837331](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134837.png)



#### 购物车页面

![image-20210422134927383](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422134927.png)

#### 订单确认页面

![image-20210422135529940](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135530.png)

#### 订单支付页面

![image-20210422135603762](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135603.png)

### 用户管理

#### 视频收藏

![image-20210422135657339](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135657.png)

#### 收货地址管理

![image-20210422135742984](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135743.png)

#### 历史订单

![image-20210422135806694](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135806.png)

#### 登陆页面

![image-20210422135843174](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135843.png)

####  注册页面

![image-20210422135911481](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422135911.png)

## 后端管理页面（仅作为效果展示）

### 博客列表

![image-20210422140312582](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422140312.png)

### 商品分类列表

![image-20210422140328672](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422140328.png)

### 商品列表

![image-20210422140346012](https://duxin2010.oss-cn-beijing.aliyuncs.com/20210422140346.png)



## 后端的相关知识点



