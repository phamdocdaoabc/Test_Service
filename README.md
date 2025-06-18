# Test_UserService
QL USER
=======
# 🧑‍💻 User Management REST API

Hệ thống quản lý người dùng với các chức năng:
- CRUD user (Soft Delete)
- Authentication & Authorization với JWT(oauth2)
- Quản lý phân quyền (Role)

---

## 🚀 Tech Stack

- ✅ Java 21
- ✅ Maven 3.9.9
- ✅ Spring Boot 3.5.0
- ✅ Spring Security + JWT
- ✅ JPA/Hibernate
- ✅ MySQL
- ✅ Lombok, Validation

---

## 📦 Cài đặt môi trường

### 1. Cài đặt
- Cài đặt JDK 21+ nếu chưa thì cài đặt JDK
- Install Maven 3.9.9 nếu chưa thì cài đặt Maven
- Install MySQL 8.0.41-debian nếu chưa thì cài đặt Docker và pull image MySQL 8.0.41-debian
- Install IntelliJ nếu chưa thì cài đặt IntelliJ

### 2. Clone repo

```bash
git clone https://github.com/phamdocdaoabc/Test_Service.git
```

### 3. Tạo database

- Mở MySQL và tạo database:

```sql
CREATE DATABASE test_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
- import file sql: "test_SQL" để tạo table cho database
