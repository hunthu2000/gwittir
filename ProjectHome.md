# Gwittir #
The purpose of this project is to provide a set of code generators, scaffolding, utilities, and a basic MVC framework for use with Google Web Toolkit based applications.

## Getting Started ##
Please see the [Wiki Documentation](http://code.google.com/p/gwittir/w/list) for information about getting started, and more.

Specifically, a nice place to begin is [The Gwittir Way](http://code.google.com/p/gwittir/wiki/TheGwittirWay).

If you have questions please utilize the [Users Group](http://groups.google.com/group/gwittir-users).

Also, Chris Fong wrote [a great intro article](http://www.gwtsite.com/exploring-data-binding-with-gwittir) on GWTSite.com.

To use Gwittir you need to inherit at least 3 modules into your GWT project. The first is [GWTx](http://code.google.com/p/gwtx), which you need to acquire separately. Next is the Gwittir core library. Finally, you need to include at least one logging module. You can see the wiki page on Logging (at right) for information on how this system works. The  short form is:

```
    <!-- Inherit the core Web Toolkit stuff.                  -->
    <inherits name='com.google.gwt.user.User'/>
    <!-- Inherit the core Web Toolkit stuff.                  -->
    <inherits name="com.google.gwt.i18n.I18N"/>
    <inherits name='com.totsp.gwittir.Gwittir' />
    <inherits name='com.totsp.gwittir.LoggingDisabled' />
    
```

**Please note, as of 0.4.5, Gwittir depends on the Google Gears library**

There is also a basic CSS/image resource set packaged as a separate module you can include:
```

    <inherits name='com.totsp.gwittir.skins.basic.Basic' />
```

## API Docs ##

**[JavaDoc 0.4.5](http://gwittir.googlecode.com/svn/sites/gwittir-core/0.4.5/apidocs/index.html)**

**[Source XRef 0.4.5](http://gwittir.googlecode.com/svn/sites/gwittir-core/0.4.5/xref/index.html)**

## Samples ##
You can also view a [Running Sample](http://gwittir.appspot.com). Note that the styles for the BoundTable example do not render correctly in MSIE. Unfortunately, because of a bug in MSIE's strict mode rendering, this BoundTable styling and the SoftScrollArea (and Flickr example), can't be used at the same time. You can [click here](http://gwittir.appspot.com/Example/index_strict.html) to see the MSIE version of the BoundTable demo.

There are blog posts on how the Flickr demo works [here](http://www.screaming-penguin.com/node/7325) and [here](http://www.screaming-penguin.com/node/7326) that serve as a good introduction into how the Binding and BoundWidgets are used to build your application.

There is also a sample included in SVN parallel to the main project:
http://gwittir.googlecode.com/svn/trunk/gwittir-core-sample/. This sample uses JPA to get and set data in an RDBMS through a GWT-RPC DTO layer and Gwittir data binding. It requires a local MySQL installation (see the [persistence.xml file](http://gwittir.googlecode.com/svn/trunk/gwittir-core-sample/src/main/resources/META-INF/persistence.xml) for details on the schema and user/pass). See the [build\_notes](http://gwittir.googlecode.com/svn/trunk/gwittir-core-sample/doc/build_notes.txt) for further information on using this sample.