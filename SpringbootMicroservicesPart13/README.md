## Modules
- currency-exchange-service
- currency-conversion-service
- naming-server
- api-gateway


## Tech Stack V2
java15, springboot-starter 2.4.4, spring-cloud 2020.0.2,H2 database,spring cloud config server,spring cloud config client, feign client,Eureka Server, Eureka Client,spring cloud load balancer,Spring Cloud API Gateway,Resilience4j circuit breaker, Spring cloud sleuth, Zipkin tracing server.

## h2 database console
- http://localhost:8000/h2-console
- http://localhost:8001/h2-console

## currency-exchange-service urls
- http://localhost:8000/currency-exchange-static/from/USD/to/INR
- http://localhost:8000/currency-exchange/from/USD/to/INR
- Response Structure
{
   "id":10001,
   "from":"USD",
   "to":"INR",
   "conversionMultiple":65.00,
   "environment":"8000 instance-id"
}
- http://localhost:8001/currency-exchange/from/USD/to/INR

## currency-conversion-service urls
- http://localhost:8100/currency-conversion-static/from/USD/to/INR/quantity/10
- Response Structure
{
  "id": 10001,
  "from": "USD",
  "to": "INR",
  "conversionMultiple": 65.00,
  "quantity": 10,
  "totalCalculatedAmount": 650.00,
  "environment": "8000 instance-id"
}
- http://localhost:8100/currency-conversion-resttemplate/from/USD/to/INR/quantity/10
- http://localhost:8100/currency-conversion-feign/from/USD/to/INR/quantity/10

## eureka naming-server urls
- http://localhost:8761/

## api-gateway urls
- http://localhost:8765/CURRENCY-EXCHANGE/currency-exchange/from/USD/to/INR
- http://localhost:8765/CURRENCY-CONVERSION/currency-conversion/from/USD/to/INR/quantity/10
- http://localhost:8765/CURRENCY-CONVERSION/currency-conversion-feign/from/USD/to/INR/quantity/10

- http://localhost:8765/currency-exchange/currency-exchange/from/USD/to/INR
- http://localhost:8765/currency-conversion/currency-conversion/from/USD/to/INR/quantity/10
- http://localhost:8765/currency-conversion/currency-conversion-feign/from/USD/to/INR/quantity/10

- http://localhost:8765/currency-exchange/from/USD/to/INR
- http://localhost:8765/currency-conversion/from/USD/to/INR/quantity/10
- http://localhost:8765/currency-conversion-feign/from/USD/to/INR/quantity/10

## zipkin urls
- http://localhost:9411/zipkin

