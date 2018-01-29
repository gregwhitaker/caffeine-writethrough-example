# caffeine-writethrough-example
[![Build Status](https://travis-ci.org/gregwhitaker/caffeine-writethrough-example.svg?branch=master)](https://travis-ci.org/gregwhitaker/caffeine-writethrough-example)

An example of implementing a write-through cache with [Caffeine](https://github.com/ben-manes/caffeine).

## Prerequisites
This example requires a running PostgreSQL database. Start a PostgreSQL instance as a Docker container using the following commands:

        $ docker pull postgres
        $ docker run --name=proteus-db -p 5432:5432 -d postgres

## Running the Example
The example can be run using the following Gradle command:

    $ ./gradlew clean run
    
If successful, you will see the following in the terminal:

    [main] INFO caffeine.caching.example.Main - Starting the Caffeine Write-Through Cache Example
    [main] INFO com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
    [main] INFO com.zaxxer.hikari.pool.PoolBase - HikariPool-1 - Driver does not support get/set network timeout for connections. (Method org.postgresql.jdbc.PgConnection.getNetworkTimeout() is not yet implemented.)
    [main] INFO com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
    [main] INFO caffeine.caching.example.Main - Creating database connection: jdbc:postgresql://localhost:5432/postgres
    [main] INFO caffeine.caching.example.Main - Adding 1: This is the number 1
    [main] INFO caffeine.caching.example.Main$DatabaseCacheWriter - Adding cache value to database: [key: 1, value: This is the number 1]
    [main] INFO caffeine.caching.example.Main - Adding 2: This is the number 2
    [main] INFO caffeine.caching.example.Main$DatabaseCacheWriter - Adding cache value to database: [key: 2, value: This is the number 2]
    [main] INFO caffeine.caching.example.Main - Adding 3: This is the number 3
    [main] INFO caffeine.caching.example.Main$DatabaseCacheWriter - Adding cache value to database: [key: 3, value: This is the number 3]
    [main] INFO caffeine.caching.example.Main - Getting `1` From Cache: This is the number 1
    [main] INFO caffeine.caching.example.Main - Getting `2` From Cache: This is the number 2
    [main] INFO caffeine.caching.example.Main - Getting `3` From Cache: This is the number 3
    [main] INFO caffeine.caching.example.Main - Loading value from database: [key: 4]
    [main] INFO caffeine.caching.example.Main - Getting `4` From Cache: This is the number 4

## Bugs and Feedback
For bugs, questions, and discussions please use the [Github Issues](https://github.com/gregwhitaker/caffeine-writethrough-example/issues).

## License
MIT License

Copyright (c) 2018 Greg Whitaker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.