# Introduction #

One of the problems with making developer friendly API's in GWT is the lack of the Reflection capabilities that are present in standard Java. While a full Java-compatible reflection API would be ideal, it is both a large and complex API and hard to break up into small implementations. To this end, Gwittir provides a "Just enough" Java Beans style Introspector that is assembled at compile time.


# Enabling Introspection for Your Classes #

To use Gwittir's Introspector, you simply need to tag your class with the com.totsp.gwittir.client.beans.Introspectable interface. This will tell Gwittir's rebind library to build out the needed classes for your Introspectable classes at compile time.

You should note: the compile time generation is done ENTIRELY based on the get/is/set naming convention (the standard Java idiom). If you use a BeanInfo implementation for your Java code, it will not be honored at this time. Support for this might come in the future, but our initial implementation of this function ran into problems with the way the GWTCompiler runs.

Once this is done, you can use the Introspector to find properties on your beans:

```
    BeanDescriptor bd = Introspector.INSTANCE.getDescriptor(MyClass.class);
    // Alternatively you can pass in an instance of an Introspectable class
    Property[] properties = bd.getProperties()
    // or
    // Property property = bd.getProperty("propertyName");
```

> The Property object has getters on it for:
  * accessorMethod
  * mutatorMethod
  * type
  * name

The accessor and mutator methods return a method with very simple operations:

```
public interface Method {
    public String getName();

    public abstract Object invoke(Object target, Object[] args)
        throws Exception;
}
```

Note the invoke() method simply throws Exception. It will simply pass on any exceptions thrown by the invocation, and will not wrap it in another exception class.

The Type property is an instance of the GWT restricted [Type](Type.md).class reference. This is really only good for equality checks with other [Type](Type.md).class associations.



# Notes on Usage #

The Introspection implementation generates a good bit of code into your final JavaScript. We have worked to minimize this as much as possible, including simplifying the classes above. We also have plans for further optimizations to the final output, but it does grow your final output.  Here are a few general tips on using the Introspector efficiently:

  * Don't tag more classes than you need as Introspectable.
  * Introspectable inheritance is more efficient than multiple introspectable classes. Where you can, define the getters/setters for common properties into interfaces.

For example:
```

public interface HasName {

  public String getName();
  public void setName(String name);

}

public class Person implements HasName, Introspectable {
  // ...
  public String getAddress(){...}
  public void setAddress(String address){...}
}

public class Product implements HasName, Introspectable {
  //...
  public String getUPC(){...}
  public void setUPC(String upc){...}
}

public class Book extends Product {
  //...
  public String getISBN(){...}
  //...
}
```

This will cause code for handling the name property to only be generated a single time. Each of the Method objects referencing name (in Java anyway) will cast the target to HasName for the invocation. Of course, the interfaces wash away in the final JavaScript, but having a common declaring interface for the getters and setters reduces the number of Method implementations inserted into your code.