<div align="center">
  <img src="logo.png"><br><br>
</div>

-----------------

##### 语言: [English](README.md) | 中文

基于 [Spring](https://spring.io/) 和 [Angular](https://angular.io/) 实现的简单电商网站

这是我的一个 ***软件工程*** 课程设计项目。

当前项目状态: **仍在开发**

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

#### 使用预编译的JAR包

请按照 [发布页面](https://github.com/YanzheL/llzw/releases) 所描述的步骤进行

#### 从源码编译

为了编译项目源码， 你需要一个有效的JDK开发环境， 版本9或更高。

下载项目源码

```shell
git clone https://github.com/YanzheL/llzw.git
```

安装Maven依赖

```shell
cd api-gate
./mvnw install -DskipTests
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

* RESTful API 后端, 并使用Spring Security模块
* Angular 单页面应用

## 文档

- [API文档](https://llzw.readthedocs.io)

## 许可证

[Apache License 2.0](LICENSE)
