# Currency Application

This is a  project that implements a simple runnable local server that exposes endpoints which take arguments and
return plain simple data after performing certain internal operations. 
The goal is to query data from the Narodowy Bank Polski's public APIs and return relevant information from them.
#### Used technologies:
* Java 17
* Spring 2.7.11
* Swagger-UI 3.0.0
* JUnit 5
* Docker
* Redis

## Getting Started

To build and run the project using Docker, follow these steps:

1. Clone the repository to your local machine: 
   ```
   git clone https://github.com/PKraciuk/CurrencyApplication
   ```
2. Run the application with this command after entering repository main folder : 
   ```
   docker-compose up
   ```

### Swagger-ui (assuming 8080 port was selected)
``` 
http://localhost:8080/swagger-ui/#/
```
# Endpoints

 The server exposes the following endpoints:
### All calls to NBP api are being cached for 5 minutes

## 1. Get average exchange rate for a currency on a specific date
### GET /api/currency/current-rate/{currencyCode}/{date}
- `{currencyCode}`: A three-letter currency code from the NBP's table A (e.g., USD, EUR).
- `{date}`: A date in the format YYYY-MM-DD.

Returns the average exchange rate for the specified currency on the specified date.

Example request:
```
curl -X GET "http://localhost:8080/api/currency/current-rate/USD/2023-04-21" -H "accept: */*"
```


## 2. Get max and min average exchange rate for a currency for the last N quotations
### GET /api/currency/extrema/{currencyCode}/{n}
- `{currencyCode}`: A three-letter currency code from the NBP's table A (e.g., USD, EUR).
- `{n}`: An integer representing the number of quotations to consider (up to 255).

Returns the maximum and minimum average exchange rates for the specified currency over the last N quotations.

Example request:
```
curl -X GET "http://localhost:8080/api/currency/extrema/EUR/10" -H "accept: */*"
```

## 3. Get major difference between buy and ask rate for a currency for the last N quotations
### GET /api/currency/major-difference/{currencyCode}/{n}

- `{currencyCode}`: A three-letter currency code from the NBP's table C (e.g., USD, EUR).
- `{n}`: An integer representing the number of quotations to consider (up to 255).

Returns the major difference between the buy and ask rates for the specified currency over the last N quotations.

Example request:
```
curl -X GET "http://localhost:8080/api/currency/major-difference/GBP/255" -H "accept: */*"
```

## Optional Features

The following optional features have been implemented:

- [x] Unit tests
- [x] Docker image of the application
- [x] Swagger UI 

## References

- [Narodowy Bank Polski API User Manual](http://api.nbp.pl/)
- [RESTful API Design Best Practices](https://learn.microsoft.com/en-us/azure/architecture/best-practices/api-design)