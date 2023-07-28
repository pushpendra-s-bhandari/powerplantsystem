
# Power Plant System

This is a virtual power plant system for aggregating distributed power sources into
a single cloud based energy provider.
## Table Reference
#### batteries
This table is used for batteries sources storage.


| Field Name     | Type     | Description                         |
|:---------------|:---------|:------------------------------------|
| `id`           | `Long`   | Primary Key                         |
| `name`         | `String` | Name of the Battery Source          |
| `postcode`     | `Long`   | Postcode of the Battery Source      |
| `capacity`     | `Long`   | Watt Capacity of the Battery Source |

## API Reference

#### Save List of Batteries Sources in DB

```http
  POST http://localhost:8080/powerplant/battery/add                           
```
Input JSON:
[
{
"name": "Cannington",
"postcode": 6107,
"capacity": 13500
},
{
"name": "Midland",
"postcode": 6057,
"capacity": 50500
},
{
"name": "Hay Street",
"postcode": 6000,
"capacity": 23500
}
]

Output JSON:
[
{
"id": 1,
"name": "Cannington",
"postcode": 6107,
"capacity": 13500
},
{
"id": 2,
"name": "Midland",
"postcode": 6057,
"capacity": 50500
},
{
"id": 3,
"name": "Hay Street",
"postcode": 6000,
"capacity": 23500
}
]

#### Search Batteries sources based on PostCode ranges along with Statistics like average and total watt capacity

```http
  GET http://localhost:8080/powerplant/battery/range/postcode?postCodeStart=6000&postCodeEnd=6000
```
Input Parameter (as Query String)
:

| Parameter       | Type   | Description             |
|:----------------|:-------|:------------------------|
| `postCodeStart` | `Long` | PostCode Starting range |
| `postCodeEnd`   | `Long` | PostCode Ending range   |

Output JSON:
{
"batteryList": [
{
"id": 3,
"name": "Hay Street",
"postcode": 6000,
"capacity": 23500
},
{
"id": 2,
"name": "Midland",
"postcode": 6057,
"capacity": 50500
}
],
"capacityStat": {
"total": 74000,
"average": 37000.0
}
}

## Unit Testing Reference

Test cases for Service "BatteryServiceImpl.java" has been done.

## Database

This system is currently using H2 Database which is a relational in-memory database. Login with user as 'admin' with password 'admin123' with databasename 'powerplant'. The console of the H2 database can be accessed by using following url

http://localhost:8080/h2-console/login.jsp

## Spring Boot and Java Version

Spring Boot Version and Java versions used were 3.1.2 and 17 respectively
## Dependencies

Following dependencies where used for this project

1. Spring Data JPA
2. H2 Database
3. Spring Web
4. Lombok


## Building and Running Project

Build : mvn package
This will build an executable jar file in the target directory.

Run: java -jar powerplantsystem-0.0.1-SNAPSHOT.jar