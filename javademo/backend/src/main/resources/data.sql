-- 让数据与当前 JPA 实体结构匹配：students / courses / enrollments
-- Optional seed data matching JPA schema, without truncating existing tables.

INSERT INTO students (id, name, email) VALUES
(1001, '张明', 'zhangming@stu.edu.cn'),
(1002, '李雪', 'lixue@stu.edu.cn'),
(1003, '王浩', 'wanghao@stu.edu.cn'),
(1004, '刘佳', 'liujia@stu.edu.cn'),
(1005, '赵阳', 'zhaoyang@stu.edu.cn'),
(1006, '陈雨', 'chenyu@stu.edu.cn'),
(1007, '孙磊', 'sunlei@stu.edu.cn'),
(1008, '周婷', 'zouting@stu.edu.cn'),
(1009, '吴悠', 'wuyou@stu.edu.cn'),
(1010, '郑凯', 'zhengkai@stu.edu.cn')
ON DUPLICATE KEY UPDATE name = VALUES(name), email = VALUES(email);

INSERT INTO courses (id, code, title, description, capacity) VALUES
(1, 'CS101', 'Java 程序设计', '掌握Java基础语法、面向对象编程及常用API，具备简单项目开发能力', 60),
(2, 'CS102', '数据库原理与应用', '学习关系型数据库设计、SQL语句编写、MySQL核心功能及数据库优化基础', 50),
(3, 'CS103', '前端开发技术', '掌握HTML/CSS/JavaScript基础，Vue框架核心用法，能完成中小型前端页面开发', 45),
(4, 'CS104', '机器学习基础', '了解机器学习基本概念，掌握线性回归、决策树等经典算法，使用Python实现简单预测模型', 30),
(5, 'CS105', '计算机网络', '讲解网络分层模型、TCP/IP协议簇、网络安全基础，掌握网络故障排查与配置基本方法', 55),
(6, 'CS106', '软件测试技术', '学习黑盒/白盒测试方法、测试用例设计、自动化测试工具使用，提升软件质量保障能力', 40),
(7, 'CS107', '数据结构与算法', '掌握数组、链表、栈、队列、树等数据结构，及排序、查找等经典算法的原理与实现', 50)
ON DUPLICATE KEY UPDATE code = VALUES(code), title = VALUES(title), description = VALUES(description), capacity = VALUES(capacity);

INSERT IGNORE INTO enrollments (student_id, course_id, created_at) VALUES
(1001, 1, '2025-09-01 00:00:00'),
(1001, 3, '2025-09-01 00:00:00'),
(1002, 2, '2025-09-02 00:00:00'),
(1003, 1, '2025-09-03 00:00:00'),
(1004, 4, '2025-09-01 00:00:00'),
(1005, 5, '2025-09-02 00:00:00'),
(1006, 6, '2025-09-03 00:00:00'),
(1007, 1, '2025-09-01 00:00:00'),
(1008, 4, '2025-09-02 00:00:00'),
(1009, 7, '2025-09-03 00:00:00'),
(1010, 2, '2025-09-01 00:00:00');
