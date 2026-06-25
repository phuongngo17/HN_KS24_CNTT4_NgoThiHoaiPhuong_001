PHẦN 1 — System Architect: Đề xuất Tech Stack cho Rikkei Logistics
1. Kiến trúc tổng thể đề xuất

Với startup logistics, team 5-7 dev, tôi không chọn Microservices ngay từ đầu vì:

Dev team nhỏ → chi phí vận hành cao
Debug khó
Deploy phức tạp

Đề xuất:

Modular Monolith + Event Driven + có khả năng tách Microservice sau

Kiến trúc:

                Mobile App
          +----------------+
          | Customer App   |
          | Driver App     |
          +----------------+
                  |
                  |
             REST API
                  |
                  v

          Backend Application

     +--------------------------------+
     |                                |
     | User Module                    |
     | Order Module                   |
     | Pricing Module                 |
     | Payment Module                 |
     | Voucher Module                 |
     | Tracking Module                |
     | Notification Module             |
     |                                |
     +--------------------------------+

          |
          |
   ----------------------
|         |          |
Database   Redis     Message Queue
PostgreSQL  Cache     Kafka/RabbitMQ

2. Backend
   Spring Boot 3 + Java 21

Đề xuất:

Java 21
Spring Boot 3
Spring Security
Spring Data JPA
Hibernate
Lý do:

Logistics có:

Order workflow
Payment
User permission
Transaction

Java/Spring rất mạnh về:

Enterprise system
Security
Transaction
Maintain lâu dài

Ví dụ module:

com.rikkei.logistics

├── user
├── order
├── pricing
├── payment
├── voucher
├── tracking
└── notification

3. Authentication
   JWT + Refresh Token

Stack:

Spring Security
JWT
OAuth2 Resource Server

Role:

CUSTOMER
DRIVER
ADMIN

JWT payload:

{
"userId":1001,
"role":"DRIVER",
"exp":123456
}

Lý do:

Mobile friendly
Stateless
Scale nhiều server
4. Mobile App
   Flutter
   Flutter
   Dart

Lý do:

Một codebase:

Android
+
iOS

Team nhỏ tiết kiệm:

thời gian
nhân lực
maintain

Modules:

Customer App

- tạo đơn
- xem giá
- thanh toán
- tracking


Driver App

- nhận đơn
- cập nhật trạng thái
- gửi GPS

5. Web Admin
   React + TypeScript

Stack:

React
TypeScript
Ant Design
Redux Toolkit

Lý do:

Admin dashboard cần:

bảng dữ liệu
realtime update
chart
filter

Ví dụ:

Order Management

[Pending]
[Delivering]
[Completed]


Map Tracking

Driver A
|
|
Order Location

6. Database
   PostgreSQL

Lý do:

Dữ liệu logistics có quan hệ:

User
|
Order
|
Payment
|
Voucher

Cần:

ACID transaction
Foreign Key
Complex query

Schema:

users

orders

payments

vouchers

tracking_logs

7. Redis

Dùng cho:

Cache

Ví dụ:

Pricing:

Distance 10km
Weight 5kg
VIP

= 120.000


Cache:

pricing_rule_10_5_VIP
Driver location realtime

Không lưu GPS liên tục vào PostgreSQL.

Dùng Redis:

driver_location_1001

{
lat:21.02,
lng:105.8
}

8. Real-time Tracking
   WebSocket

Stack:

Spring WebSocket
STOMP
Redis Pub/Sub

Flow:

Driver App

GPS update

      |
      v

WebSocket Server

      |
      v

Customer App

Map update

9. Message Queue
   RabbitMQ

Dùng cho:

Notification
Payment callback
Order event

Ví dụ:

Order Created

        |
        v

RabbitMQ

|        |

Email     Push Notification

10. Map / Distance Calculation

Google Maps API hoặc Mapbox

Dùng:

Calculate distance
Route
ETA

Pricing:

price =
base_fee
+
(distance * km_rate)
+
(weight * weight_rate)

-
voucher

11. DevOps
    Docker

Mọi service:

Backend container

Database container

Redis container

CI/CD

GitHub Actions:

Push code

|
v

Build

|
v

Test

|
v

Deploy

Cloud

Startup:

Giai đoạn đầu:

AWS / Azure / GCP

Deploy:

Docker
+
AWS ECS
+
RDS PostgreSQL
+
Redis ElastiCache

Tổng Tech Stack
Layer	Công nghệ
Backend	Java 21 + Spring Boot 3
Security	Spring Security + JWT
Mobile	Flutter
Web Admin	React + TypeScript
Database	PostgreSQL
Cache	Redis
Realtime	WebSocket
Queue	RabbitMQ
Map	Google Maps API
Container	Docker
CI/CD	Github Actions
Cloud	AWS
PHẦN 2 — System Analyst: Entity Analysis
1. User

Quản lý tất cả user.

Entity:

User

Fields:

id
fullName
email
phone
password
role
status
createdAt

Role:

CUSTOMER
DRIVER
ADMIN

Relationship:

User 1 --- n Order

User 1 --- n Review
2. Customer Profile

Fields:

customerId
userId
vipLevel
point

Quan hệ:

User 1 --- 1 CustomerProfile
3. Driver Profile

Fields:

driverId
userId
licenseNumber
vehicleType
availableStatus
currentLocation

Quan hệ:

User 1 --- 1 DriverProfile

Driver 1 --- n Order
4. Order

Entity trung tâm.

Fields:

orderId
customerId
driverId

pickupAddress

deliveryAddress

distance

weight

status

totalAmount

createdAt

Status:

CREATED
PICKING
DELIVERING
DONE
CANCELLED
Quan hệ:
Customer 1 --- n Order
Driver 1 --- n Order

5. OrderItem
Chi tiết hàng hóa.
Fields:
id
orderId
itemName
quantity
weight
description
Quan hệ:
Order 1 --- n OrderItem

6. PricingRule
Cấu hình tính phí.
Fields:
id
minDistance
maxDistance
weightRange
vipDiscount
basePrice
Quan hệ:
PricingRule 1 --- n Order
7. 
7. Voucher
Fields:
id
code
discountType
discountValue
expiredDate
quantity
Quan hệ:
Voucher 1 --- n Order
8. 
8. Payment
Fields:
id
orderId
method
amount
status
transactionId
createdAt
Quan hệ:
Order 1 --- 1 Payment
9. DeliveryRoute
Lưu lịch sử tuyến đường.
Fields:
id
orderId
driverId
startLocation
endLocation
distance
duration
Quan hệ:
Driver 1 --- n DeliveryRoute
Order 1 --- 1 DeliveryRoute
10. TrackingLog
Realtime tracking history.
Fields:
id
orderId
latitude
longitude
timestamp
Quan hệ:
Order 1 --- n TrackingLog
11. Review
Fields:
id
orderId
customerId
driverId
rating
comment
Quan hệ:
Order 1 --- 1 Review
Customer 1 --- n Review
Driver 1 --- n Review