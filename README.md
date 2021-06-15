# lightlark-springmvc
## 一、项目背景
轻量级Web项目开发，一次搞定前后端。

当初学者终于完成前端编码环境，后端编码环境，数据库环境搭建后，基本上热情已经耗去了一大半。

Spring Boot让这一切都变得太简单了。本项目只需要Intellij IDEA一个编辑器搞定，最小学习成本的使用了SpringMVC + Themeleaf + Mybatis + H2等业界流行技术和工具

1. **Web页面**，展现的数据为常见的表格形式，包括文本+图片，支持数据下载，数据保存，前后翻页等
2. **Java后端** api接口开发，bean注入，各项excel表格操作等
3. 各种**数据库操作**

### 使用步骤

1. 如果第一次git clone后出现无法读取application.yml的情况，在idea中File-Project Structure-Modules，将src/main/resources目录标记为Resources（资源）文件夹即可
2. git clone https://github.com/reinal001/lightlark-springmvc.git
3. 在DemoApplication.java中启动main函数
4. 浏览器输入127.0.0.1:8081/person/list，出现页面（无数据）；输入127.0.0.1:8081/person/init进行数据初始化，再次访问127.0.0.1:8081/person/list，出现有数据页面。效果图见下方。

## 二、实现效果
***页面*** 鼠标悬浮文字时显示图片

![image](https://user-images.githubusercontent.com/56336381/122005596-6c6b2500-cde8-11eb-97f9-090e13b8a362.png)

***翻页功能***

![image](https://user-images.githubusercontent.com/56336381/122005609-6ffeac00-cde8-11eb-8060-6cd81a2f504f.png)
## 三、代码说明

### 数据结构
```
drop table record if exists;
create table record (
    timestr varchar(128),
    door_id varchar(128),
    person_name varchar(128),
    direction varchar(8),
    picture varchar(128),
    is_correct boolean,
    wrong_result varchar(8),
    primary key(door_id, timestr)
);

```

![image](https://user-images.githubusercontent.com/56336381/122006430-66c20f00-cde9-11eb-89e4-0f2088c85852.png)

### API文档
|API|说明|
|-|-|
|/person/list|查询所有人员进出信息|
|/person/save|保存页面数据到数据库中|
|/person/init|根据文本数据，初始化数据库|
|/person/download|导出数据库，保存为excel格式|
|/person/delete|清空数据库中所有数据|
|/person/seekPicture|访问存储在本地硬盘的图片，与程序本身隔离。数据较大时，可以动态更新，避免每次编译打包|

### 代码结构

后端代码放在src/main/java下，目录结构及负责的功能如下：

* controller
    > PersonController.java：接收GET/POST请求的API接口，并返回相应结果给前端
* mapper
    > PersonMapper.java: MyBatis要求的操作数据库的接口
    > PersonMapper.xml: MyBatis要求的，对接口意义对应的具体的MySQL语句
* model
    > PersonRecord.java: 最基本的数据结构，代表数据库中一条记录，前后端数据交互用的bean的格式
* service
    > ExcelService.java: 提供读/写Excel功能的服务
    > PersonService.java: 提供具体的业务功能，包括将业务数据的解析和写入/读取数据库
* DemoApplication.java： 项目启动的main函数

前端代码，数据库的schema，项目的配置文件都放在src/main/resources里

* sql
    > data.sql:数据库的初始insert语句，非必选
    > schema.sql:数据库的初始create语句，非必选
* static
    > css：样式文件，从BootStrap复制而来
    > js：样式文件，从BootStrap复制而来
* templates
    > index.html：前端页面，遵循Thymeleaf格式
* application.yml：整个SpringMVC项目的配置文件

