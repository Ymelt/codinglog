## docker安装

1. 设置源

~~~
$ sudo yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2

~~~

仓库设置（第一个命令似乎有错）

~~~
$ sudo yum-config-manager \
    --add-repo \
  http: // mirrors.aliyun.com / docker-ce / linux / centos / docker-ce.repo

~~~

~~~
yum-config-manager --add-repo   http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
~~~



安装docker-ce

~~~
sudo yum install docker-ce docker-ce-cli containerd.io
~~~



## jar部署

~~~
docker run -d \
--restart=always \
-v /home/newProject/behavior_management_system/target/behavior_management_system-v.01.jar:/project.jar \
-v /home/newProject/logs:/data/wwwroot/backProject/behavior_management_system/log \
-p 8081:8081 \
-e SPRING_PROFILES_ACTIVE="test" \
--name behavior-mgt-project \
openjdk:8 java -jar \
-Duser.timezone=GMT+08 \
/project.jar
~~~





## Nginx

~~~
docker run -d --name nginx -p 80:80 -p 443:443 nginx:1.25
~~~

复制资源

~~~
docker cp nginx:/etc/nginx/nginx.conf /home/nginx/conf
docker cp nginx:/etc/nginx/conf.d /home/nginx/conf
docker cp nginx:/usr/share/nginx/html /home/nginx/www
docker cp nginx:/var/log/nginx /home/nginx/logs
~~~

~~~
docker run -d --name nginx \
-p 80:80 -p 443:443 -p 445:445 \
-v /home/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /home/nginx/conf/conf.d:/etc/nginx/conf.d \
-v /home/nginx/www/html:/usr/share/nginx/html \
-v /home/nginx/logs/nginx:/var/log/nginx \
nginx:1.25
~~~





## Mysql

~~~
docker run -d --restart=always --name mysql \
-v /home/mysql/data/mysql:/var/lib/mysql \
-v /home/mysql/conf/mysql:/etc/mysql \
-v /home/mysql/log/mysql.log:/var/log/mysqld.log \
-p 3306:3306 \
-e TZ=Asia/Shanghai \
-e MYSQL_ROOT_PASSWORD=123456 \
mysql:5.7 \
--character-set-server=utf8mb4 \
--collation-server=utf8mb4_general_ci
~~~

~~~
docker cp mysql:/var/lib/mysql /home/mysql/data/mysql
docker cp mysql:/etc/mysql /home/mysql/conf/mysql
docker cp mysql:/var/log/mysqld.log /home/mysql/log/mysql.log

~~~



## Redis

~~~
docker run --name redis \
-p 6379:6379 \
-v /home/redis/config/redis.conf:/etc/redis/redis.conf \
-v /home/redis/data:/data \
-d redis:5.0.14 redis-server /etc/redis/redis.conf --appendonly yes
~~~





## openssl生成自签证书

1、首先安装openssl和openssl-devel

~~~
yum install openssl

yum install openssl-devel
~~~

2、生成私钥文件

~~~
openssl genrsa -des3 -out server.key 2048
~~~

3、依据私钥文件生成csr证书文件

~~~
openssl req -new -key server.key -out server.csr
~~~

| 字段                     | 说明                  | 示例         |
| ------------------------ | --------------------- | ------------ |
| Country Name             | ISO国家代码(两位字符) | CN           |
| State or Province Name   | 所在省份              | Zhejiang     |
| Locality Name            | 所在城市              | Hangzhou     |
| Organization Name        | 公司名称              | Oray         |
| Organizational Unit Name | 部门名称              | sales Dep    |
| Common Name              | 申请SSL证书的域名     | www.oray.com |
| Email Address            | 不需要填入            |              |
| a challenge password     | 不需要填入            |              |

4、为了不需要再每次重启nginx的时候都输入密码

~~~
cp server.key server.key.org

openssl rsa -in server.key.org -out server.key
~~~

5、生成crt证书文件

~~~
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
~~~

6、nginx配置文件

~~~
server
    {
        listen 888;
        listen 443 ssl;

     ssl_certificate      /app/hd/ssl/server.crt;
     ssl_certificate_key /app/hd/ssl/server.key;
     }
~~~

