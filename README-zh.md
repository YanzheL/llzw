<div align="center">
  <img src="logo.png"><br><br>
</div>

-----------------

##### 语言: [English](README.md) | 中文

基于 [Spring](https://spring.io/) 和 [Angular](https://angular.io/) 实现的简单电商网站

这是我的一个 ***软件工程*** 课程设计项目。

**当前项目状态:**

前端: 即将发布

后端: 即将发布

**在线演示网站:** [https://llzw.hitnslab.com](https://llzw.hitnslab.com)

## 持续集成与自动化测试状态

| Branch     | Status                                                    |
| ---------- | --------------------------------------------------------- |
| **master** | ![](https://travis-ci.org/YanzheL/llzw.svg?branch=master) |
| **dev**    | ![](https://travis-ci.org/YanzheL/llzw.svg?branch=dev)    |


## 前置条件

- **JRE 运行时:** 9 或更高
- **MySQL:** 8 或更高, 运行在 [localhost:3306](localhost:3306),  否则你将需要编辑位于 [application-dev.yml](api-gate/src/main/resources/application-dev.yml) 的配置文件。

## 开始使用

两种选择

后端服务默认将会监听在[localhost:8981](localhost:8981)

### 使用预编译的JAR包

请按照 [发布页面](https://github.com/YanzheL/llzw/releases) 所描述的步骤进行

### 使用Docker镜像

你需要预先安装好Docker环境

两种选择

- 运行单个后端服务

  拉取镜像

  ```shell
  docker pull leeyanzhe/llzw-api-gate
  ```

  运行

  ```shell
  docker run -d -p "8981:8981" leeyanzhe/llzw-api-gate
  ```

- 运行整个系统

  下载项目源码

  ```shell
  git clone https://github.com/YanzheL/llzw.git
  ```

  进入项目目录并启动整个服务包

  ```shell
  cd llzw
  docker-compose up -d --build
  ```

### 从源码编译

为了编译项目源码， 你需要一个有效的JDK开发环境， 版本9或更高。

下载项目源码

```shell
git clone https://github.com/YanzheL/llzw.git
```

编译JAR包

```shell
./mvnw package -DskipTests
```

运行

```shell
java "-Dspring.profiles.active=dev" -jar target/*.jar
```

如果你没有外部数据库，也可以使用内存数据库运行测试版本。

```shell
java "-Dspring.profiles.active=test" -jar target/*.jar
```

## 特性

* RESTful API 后端

* Angular 单页面应用

* Travis CI 持续集成与自动化测试

* Docker Hub自动构建

  每次 `master` 和 `dev` 分支的提交都会自动触发DockerHub的镜像自动构建, 使[我们的Docker镜像](https://hub.docker.com/r/leeyanzhe/llzw-api-gate)始终保持最新版本

  `latest` 镜像标签对应 `dev` 分支

  `stable` 镜像标签对应 `master` 分支

## 文档

- [API文档](https://llzw.readthedocs.io)

## 贡献者:

#### LLZW Web 前端:

位于 `web/` 目录

[@windwen](https://github.com/windwen)

[@ZhangTong1999](https://github.com/ZhangTong1999)

#### LLZW 后端和其他辅助代码

位于 `api-gate/` 目录，以及一些其他文件。

[我](https://github.com/YanzheL)

[@WennyXY](https://github.com/WennyXY)

## 许可证

[Apache License 2.0](LICENSE)
