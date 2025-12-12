# 学生选课系统

基于 Java Spring Boot + MySQL + Vue3（Vite）的简易选课示例。

## 环境准备
- JDK 17+
- Maven 3.9+
- Node.js 18+ / npm 9+
- MySQL 已安装并可连接

创建数据库：
```sql
CREATE DATABASE courseapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 后端（Spring Boot）
mvn spring-boot:run
路径：`backend`

1) 配置数据库账号：修改 `backend/src/main/resources/application.properties` 中的 `spring.datasource.username/password`。  
2) 启动：
```bash
cd backend
mvn spring-boot:run
```
默认端口：`http://localhost:8080`

主要 API（返回 JSON）：
- 学生：`GET/POST /api/students`，`GET/PUT/DELETE /api/students/{id}`
- 课程：`GET/POST /api/courses`，`GET/PUT/DELETE /api/courses/{id}`
- 选课：`POST /api/enrollments`，退课：`DELETE /api/enrollments`
- 查询学生的课表：`GET /api/students/{id}/courses`
- 查询课程的学生：`GET /api/courses/{id}/students`

## 前端（Vue3 + Vite）
npm run dev
路径：`frontend`

1) 安装依赖：
```bash
cd frontend
npm install
```
2) 开发模式运行：
```bash
npm run dev
```
默认端口：`http://localhost:5173`，已在 Axios 中指向 `http://localhost:8080/api`。如有需要可在 `frontend/src/api.js` 修改。

构建：
```bash
npm run build
```

## 功能说明
- 新增/删除学生与课程。
- 为学生选课与退课；课程容量超限会提示错误。
- 查看指定学生的课程列表，以及指定课程的学生列表。

## 目录结构
```
backend/  # Spring Boot 服务（JPA + MySQL）
frontend/ # Vue3 前端（Vite）
```
