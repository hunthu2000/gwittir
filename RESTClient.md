Please note the REST API is still under active development and, while usable, should be considered experimental.

# Goals #

The primary goal of the REST client is to be as flexible as you need it to be. It is my personal experience that GWT RPC is fairly brittle code and not easy to bend if you have a special case. The Gwittir REST client attempts to correct these failings, in the REST type model. The ways we address this are:

  * Everything is pluggable:  Transports, Codecs, etc
  * Everything has plenty of hooks to change behavior: (Alter the RequestBuilder before requests are sent)
  * Services are created by composition, making them Guice/Gin friendly.
  * Everything is easily "decoratable" to support things like HTML5/Gears persistence and synchronization.


# The Basic Example #

The first step in making a REST client is to create a service handle for you objects. To get started, you first need a codec. There are only 2 basic codecs included right now: JSONCodec, for use with the Gwittir introspection system, and JSONOverlayCodec, which uses JavaScriptObjectOverlay types. We will start with the first type.

First, a data class:

```
package com.totsp.gwittir.restsample.shared;

import com.totsp.gwittir.client.beans.annotations.Introspectable;
import java.io.Serializable;
import java.util.List;

@Introspectable
public class Foo implements Serializable {
    private List<Foo> children;
    private String stringProperty;
    private int intProperty;

//.. getters and setters truncated.

```

This class must have the @Introspectable annotation, or be included in the gwittir-inrospection.properties file and have a no-args constructor.

Next, create the codec interface.  Because we are using the Gwittir introspection system, objects will be created and have their properties set through the getters and setters. The actual codec will be generated, so all you really need to do is create a marker interface:

```


package com.totsp.gwittir.restsample.client;

import com.totsp.gwittir.restsample.shared.Foo;
import com.totsp.gwittir.serial.json.client.JSONCodec;

public interface FooCodec extends JSONCodec<Foo> {

}

```

Next, we create our service. Here I am setting hard parameters in the constructor, but each of these could be @Named parameters for a Gin config:

```
package com.totsp.gwittir.restsample.client;

import com.google.gwt.core.client.GWT;
import com.totsp.gwittir.rest.client.SimpleRESTService;
import com.totsp.gwittir.rest.client.transports.XHRTransport;
import com.totsp.gwittir.restsample.shared.Foo;

public class FooService extends SimpleRESTService<Foo> {

    public FooService(){
        this.setBaseURL(GWT.getModuleBaseURL()+"../api/foos/");
        this.setCodec( (FooCodec) GWT.create(FooCodec.class ));
        this.setTransport(new XHRTransport() );
    }

}
```

Here we are using the basic XHRTransport. We will talk about the other transports later.

And we are done! Start making calls to your service:
```
        Foo myFoo = new Foo();
        myFoo.setStringProperty("A String property.");
        myFoo.setIntProperty( 404 );
        myFoo.setChildren( Arrays.asList( new Foo(), new Foo() ));
        FooService service = new FooService();
        service.put("myFoo", myFoo, new AsyncCallback<String>(){

            public void onFailure(Throwable caught) {
                Window.alert( ""+ caught );
            }

            public void onSuccess(String result) {
                Window.alert(result);
            }
            
        });
```

Here we are passing in a "key" for the object, so the object becomes /api/foos/myFoo. Not all REST type services work this way, so the callback takes a String value as a result. This string value will, if available be:

  * The "Location" header field returned from the call, if a new URI is generated and returned as a redirect.
  * The text of the response payload.

So, for instance, if I wanted to create a new foo that got an automatic URI, I would say:
```
            service.post(null, myFoo, new AsyncCallback<String>(){

            public void onFailure(Throwable caught) {
                Window.alert( ""+ caught );
            }

            public void onSuccess(String result) {
                Window.alert(result); //
            }
            
```
And I would expect the result to be "/api/foos/1234".

# The JSONOverlay Example #

Lets do the same thing with a JSONOverlay type. First, we need a new data class. This is obviously limited, but serves as an example:

```
public class FooOverlay extends JavaScriptObject {

    protected FooOverlay(){

    }


    public final native String getStringProperty()/*-{ return this.stringProperty; }-*/;

    public final native int getIntProperty()/*-{ return this.intProperty; }-*/;

}

```

Now, lets just create a service and make a call:

```
        SimpleRESTService<FooOverlay> overlayService = new SimpleRESTService<FooOverlay>();
        overlayService.setBaseURL(GWT.getModuleBaseURL()+"../api/foos/");
        overlayService.setCodec( new JSONOverlayCodec<FooOverlay>() );
        overlayService.setTransport(new XHRTransport() );
        overlayService.get("myFoo", new AsyncCallback<FooOverlay>(){

            public void onFailure(Throwable caught) {
                Window.alert(""+caught);
            }

            public void onSuccess(FooOverlay result) {
                Window.alert(result.getStringProperty() +" "+ result.getIntProperty() );
            }
       });
```

Since no code has to be generated for the JSONOverlayCodec type, you simply create one directly with a type param of your JavaScriptObject overlay type.


# Pre and Post Hooks #

For the transports that extend HTTPTransport -- that is, the ones that are based on the plain GWT XHR wrapper libraries -- you can provide PreHook and PostHook implementations. Lets say, for example, you wanted to support a call id/next call id security system on the server. You could do this by saying:

```
public class CallIdHook implements PreHook, PostHook {

    private String callId;

    public CallIdHook(String initialCallId){
        this.callId = initialCallId;
    }

    public RequestBuilder beforeSend(RequestBuilder builder) {
        builder.setHeader("X-Call-Id", callId);
        return builder;
    }

    public Response afterReceive(Response response) {
        this.callId = response.getHeader("X-Next-Call-Id");
        return response;
    }

}

//----
         XHRTransport transport = new XHRTransport();
         CallIdHook hook = new CallIdHook("first");
         transport.setPreHook(hook);
         transport.setPostHook(hook);
         SimpleRESTService<FooOverlay> overlayService = new SimpleRESTService<FooOverlay>();
        overlayService.setBaseURL(GWT.getModuleBaseURL()+"../api/foos/");
        overlayService.setCodec( new JSONOverlayCodec<FooOverlay>() );
        overlayService.setTransport(transport );

```

Now all calls through the service will store the resultant next callback. Obviously this is flawed because if you make multiple calls you will need to queue them so they don't return out of order. This is beyond the scope of this example, however, you could implement this as a new Transport implementation as well.

# X-REST #

Because some older browsers do not support PUT and DELETE methods, the REST API includes a method for forging the methods using the X-REST-Method header. To use the X-REST support, two steps are needed. First, use the XRESTTransport() rather than the XHRTransport in your services. This will replace the PUT and DELETE methods with POST and GET methods respectively, and add the X-REST header. Next you need to apply the XRESTFilter to your REST services on the server side, thusly:

```
     <filter>
        <filter-name>x-rest</filter-name>
        <filter-class>com.totsp.gwittir.rest.server.XRESTFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>x-rest</filter-name>
        <url-pattern>/api/*</url-pattern>
    </filter-mapping>
```