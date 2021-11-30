R2DBC MySQL X Protocol Implementation
=====================================

# Why R2DBC over MySQL X Protocol

* Non-blocking, asynchronous calls follow common host language patterns

# Parameter binding

**Attention**: Please use '?' for parameter binding. "$1", ":name" and null binding are not supported by MySQL X Protocol.

```
Statement statement = connection.createStatement("SELECT title FROM books WHERE author = ? and publisher = ?");
statement.bind(0, "John Doe");
statement.bind(1, "Happy Books LLC");
```

# Unsupported R2DBC features

Not supported by MySQL X Protocol.

* Batch
* Parameter binding: "$1"
* named Parameter binding:  ":name"

# Todo

* transaction & save point

# References

* MySQL X Protocol: https://dev.mysql.com/doc/dev/mysql-server/8.0.21/mysqlx_protocol.html
* R2DBC: https://r2dbc.io/
* R2DBC Specification: https://r2dbc.io/spec/0.8.2.RELEASE/spec/html/
* MySQL X Comparison to MySQL C/S Protocol: https://dev.mysql.com/doc/dev/mysql-server/8.0.21/mysqlx_protocol_comparison.html
