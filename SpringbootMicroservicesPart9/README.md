## Modules
- limits-service
- spring-cloud-config-server
- currency-exchange-service
- currency-conversion-service

## Non Modules
- git-localconfig-repo

## Tech Stack V2
java15, springboot-starter 2.4.4, spring-cloud 2020.0.2,H2 database,spring cloud config server,spring cloud config client, feign client

## h2 database console
- http://localhost:8000/h2-console
- http://localhost:8001/h2-console

## limits-service module urls
- http://localhost:8080/limits

## spring-cloud-config-server urls
- http://localhost:8888/limits-service/default
- http://localhost:8888/limits-service/dev
- http://localhost:8888/limits-service/qa

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



