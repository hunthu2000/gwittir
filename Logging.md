# Introduction #

GWT includes basic logging, but GWT.log() is only really good for development time logging. When you start building applications, there are times you will need more than this. It also helps to have a logging system that can carry all the way through to the JavaScript so you can capture logging messages from staging and production environments. To that end, Gwittir provides its own logging system.


# How it Works #

The com.totsp.gwittir.client.log package provides a Logger object that works much like you would expect a Logger to work in other Java-based logging environments. This Logger object will log both to the GWTShell, using the GWT.log() method, and make a call back to the remote /LogService RPC service. The RPC service by default, will pass log messages on to the default context logger ( Servlet.log() ). It will also look for implementations of the com.totsp.gwittir.service.ServerLogService using the standard [JAR SPI](http://java.sun.com/j2se/1.4.2/docs/guide/jar/jar.html#Service%20Provider) method. This can be used to pass through these log messages to whatever logging system you wish to use on the server side. Gwittir currently includes a log4j-spi module that logs to Log4J, with more to come.

The logging system also defined a new property: log.level. This is defined for the application in the same way "locale" is for I18N. Either via a 

&lt;meta&gt;

 tag in the HTML Host Page, or via a request parameter. **Note**: This property only defines what messages are sent back to the server, NOT what log level is set on the server. If you have a log.level of "debug" in the client, and a log level of "warn" on the server, you will be sending a whole lot of pointless HTTP requests back to the server, since the server will only capture "warn" or better. This does not apply to standard context logging, but will impact SPI's registered on the server.

# Usage #

First, you need to inherit the logging module:
```
   <inherits name="com.totsp.gwittir.Logging"/>
```


Typical usage would be in the form of:

```
    private Logger log = Logger.getLogger("com.my.package.log.Name");
   //...

    log.log(Level.WARN, "An exception was thrown!", exception);
```

A few things to keep in mind:
  * Full stack information is unavailable outside the GWT Shell. If you are wanting to debug deployed JavaScript, make sure your exception catching is fine grained enough to give you useful information.
  * There is no handling of failed calls to the logging RPC service right now. If the Logger fails a call to the remote service, it will die silently. At some point we may provide queuing of messages or Gears-based persistence of messages to ensure delivery. For now, the short version is, don't make this a business level system.
  * If you are not using GWT-Maven or similar tool to automatically mount service servlets, you need to make sure the com.totsp.gwittir.service.LogServiceServlet gets registered to [ModuleName](ModuleName.md)/LogService in your web.xml file.

Using the runtime log level configuration can significantly slow your compile time, as a version must be compiled for each log level. To reduce this, there are specific level module files you can include that will specify the log level at build time, and this cut the compile speed.

To use this, include ONE and ONLY ONE of the following:
```
   <inherits name="com.totsp.gwittir.LoggingInfo"/> 
```
for info level or better, or
```
   <inherits name="com.totsp.gwittir.LoggingWarn"/>
```
for warn level or better.


# Future Considerations #
  * Customizing the log level by logger name for use inside the GWT shell via a props file.
  * More reliable log message delivery.