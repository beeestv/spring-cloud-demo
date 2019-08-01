# 基于Spring Cloud的微服务组件概览

## 相关组件列表

1. 服务治理：Consul、Eureka、Zookeeper、Nacos
2. 服务消费：RestTemplate+Ribbon、Feign
3. 熔断器：Hystrix
4. 网关：Zuul、Spring Gateway
5. 配置中心：Nacos、Spring Cloud Config
6. 链路追踪：Zipkin
7. 服务监控：Spring Boot Admin、Hystrix Dashboard、Turbine

## 详细介绍

### 服务治理

服务治理主要包括服务注册、服务发现和服务状态监控。
常用的有**Eureka**和**Consul**，它们都提供了精美的界面来查看服务信息，也都支持集群部署。
但是使用Eureka集群需要在配置中指定所有**Eureka**实例地址，而**Consul**实例有**server**和**client**两种模式，**client**负责对**server**集群转发服务注册和发现请求，在使用和部署上更为方便。

### 服务消费

Spring Cloud的服务消费有两种形式，都是通过服务名+接口path来调用，都能很简单地使用熔断器。
RestTemplate+Ribbon：需要手动解析response，Ribbon负责负载均衡。
Feign：声明式调用，自动解析response为声明的对象，默认使用Ribbon实现负载均衡，无需任何代码。

```code
spring:
  application:
    name: user-service
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        preferIpAddress: true
        instanceId: ${spring.application.name}:${spring.cloud.client.ip-address}:${server.port}
        healthCheckPath: /actuator/health
        healthCheckInterval: 15s
```

> 因为请求在同一线程内被处理，所以服务间公共信息(如用户信息)传递可以用RequestInterceptor向request header添加公共信息，然后用ThreadLocal存储，供全局读取。
> 请求结束后需要删除ThreadLocal中存储的信息，因为SpringBoot内置的tomcat会使用线程池，不删除的话可能被其他访问者获取。

### 熔断器

Hystrix熔断器可以实现服务降级和服务监控功能。

```code
feign:
  hystrix:
    enabled: true
```

#### 服务降级

在一定时间内超过一定量请求响应超时就会触发熔断器，后续的所有请求直接执行降级的逻辑，不再请求其他服务。

#### 服务监控

Hystrix dashboard可以定期收集Hystrix的接口提供的请求信息，并以图形化界面展示，开发人员可以根据相关信息优化代码。
信息包括：近期正常响应数、超时响应数、降级响应数、最大/最小响应时间、成功响应率等统计信息。

![](https://miro.medium.com/max/1280/0*3-rQw1wkj-Zs5WHH.png)

![](https://lh6.googleusercontent.com/NXJRK08UXGovWbRx3-ffEJg4YWhx12k2Xet0bnu7NU5Pm9gq3yHcu_jLY8GRXmxh79YjTtg5dNUEAgZsdNVOa3pygc3ubX2XgEKur2oZEtQcmQ3Yh20)

### 网关

网关作为所有外部请求的入口，可以负责权限控制、限流、控制对外暴露的接口、监控。目前主流的Zuul和Spring Cloud Gateway都支持配置文件和代码配置，使用起来非常方便。

#### Zuul

Netflix公司开发的微服务网关。Zuul 1使用Servlet开发基于阻塞IO，很方便和Spring Cloud其他组件整合。现在已经发布了Zuul 2，是基于非阻塞IO的Zuul升级版本，目前不支持Spring Cloud，也没有支持计划。

```code
zuul:
  routes:
    user-service:
      path: /user/**
      serviceId: user-service
      strip-prefix: false
    asset-service:
      path: /asset/**
      serviceId: asset-service
      strip-prefix: false
```

#### Spring Cloud Gateway

因为Zuul 1性能不佳，Zuul 2迟迟未发布，Spring官方使用函数式框架WebFlux开发的网关，根据官方测试项目，比Zuul 1性能好50%左右。但是因为使用WebFlux框架，开发权限控制、限流、监控等功能有一定上手难度。

### 配置中心

目前主流的配置中心有Spring官方的Spring Cloud Config(配置保存在本地和git)、携程的Apollo(配置保存在MySQL)、阿里的Nacos，这些服务除了配置文件的保存方式以外没有太多区别。
如果不会频繁变更配置的话，我个人认为Spring Cloud Config比较合适，它主要使用git保存配置，git自带的权限控制和版本控制可以方便配置管理。
Apollo和Nacos提供了图形化界面方便发布配置和修改变更等操作，如果会频繁修改配置，还是挺方便的。

```code
spring:
  application:
    name: config-service
  profiles:
    active: dev
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        preferIpAddress: true
        instanceId: ${spring.application.name}:${spring.cloud.client.ip-address}:${server.port}
        healthCheckPath: /actuator/health
        healthCheckInterval: 15s
    config:
      server:
        git:
          uri: http://192.168.14.50/huzw/project-config.git
```

> 有一个相关的项目叫Spring Cloud Bus，配合@RefreshScope注解，可以实时更新配置。

### 链路追踪

微服务架构的服务间调用比较复杂，问题定位和代码优化都不方便。所以必须实现链路追踪来跟进一次请求有哪些服务参与，参与的顺序是如何。Spring Cloud Sleuth集成了Twitter开发的Zipkin，能够清晰的查看每个请求步骤，快速定位问题。
> 如果同时引入zipkin和Spring Cloud Bus，需要设置spring.zipkin.sender.type=web，否则zipkin会使用rabbitmq发送信息，导致zipkin server无法收集信息。

```code
spring:
  zipkin:
    base-url: http://localhost:10005
    sender:
      type: web
```

![](https://i.bmp.ovh/imgs/2019/07/c3f55f396d18fcf6.png)

![](https://i.bmp.ovh/imgs/2019/07/26aea81409d6ffe4.png)

![](https://i.bmp.ovh/imgs/2019/07/8b6a749c11613ece.png)

### bootstrap application

