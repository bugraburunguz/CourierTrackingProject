# **Courier Tracking Project**

## **Introduction**

This project is a courier tracking and order management system that optimizes order management and delivery processes by
tracking the locations of couriers.

## **Technologies**

* Java 17
* Springboot 3
* Feign Client
* Swagger

## **Build Project**

`cd courier-tracking-project`

There is a docker-compose.yml

You have two options:

Terminal

`docker-compose up -d`

Intellij Idea

![preparation.png](images%2Fpreparation.png)

Swaggers

couriers' location data is managed, and _exceptions_ are handled using a _custom exception advice_. 
You can view couriers' locations and distances traveled via the courier-service microservice.
![getcourierFound.png](images%2FgetcourierFound.png)
![getcourierNotFound.png](images%2FgetcourierNotFound.png)
![courierLocations.png](images%2FcourierLocations.png)

* This concise summary should fit well with the Swagger screenshots.

![totalDistance.png](images%2FtotalDistance.png)
![proof.png](images%2Fproof.png)
* Logging when a courier enters a 100-meter radius.

![log.png](images%2Flog.png)

![customer.png](images%2Fcustomer.png)

For an order, only the customer's ID is needed, as the system finds the nearest store based on the customer's location and assigns the nearest available courier to the order.
![orderget.png](images%2Forderget.png)
![order.png](images%2Forder.png)
![deliverErrorifhe isnotclose.png](images%2FdeliverErrorifhe%20isnotclose.png)
