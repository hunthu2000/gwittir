# Introduction #

I have a confession to make. I have hated almost every UI toolkit I have ever used, in almost any environment. The Model-View-Controller pattern has been with us for a long time now, and other UI toolkits like Swing or SWT in the Java space employ them to a great degree when they build their UI components. The problem has always been that THEIR models and YOUR models are never the same. When you are working with a UI toolkit, you are trying to build a UI for working with your business model, not the abstract model used by the toolkit designers.

The only reason Gwittir is a GWT library and not a Swing library is we like GWT and having come from a long background of web development, we understand the underlying systems that are abstracted by the GWT UI library better than we do, say, Swing or SWT. To that end, Gwittir represents a NEW UI library, built on top of the GWT APIs, and designed to be intermingled as needed. Many of the core elements like TextBox or Checkbox are duplicated, but those are done so for a reason: we want them to match the Gwittir UI pattern.

# Self-Similarity #

One of the problems with most UI toolkits is you have to know exactly what kind of widget you are working with at a pretty fundamental level in your code. Things like ListBoxes in JavaScript or Swing have their own usage patterns and require a lot of support code to map to your object model. Eventing varies from type to type and usually requires setting up listeners and dealing with a TON of housekeeping to clean them up where appropriate. To address this, all Gwittir components support a common API scheme, and 5 properties:

  * model Which is simply a holder for your business object that the component relates to.
  * value Which is the raw value of the widget. This might be a Boolean for a checkbox, a String for a text box or a business object itself for a list box. The point is, you can bind the value property on a Gwittir widget directly to a property on a business object no matter what.

The other two are supporting properties that the widget can use for internal representation on your object as needed.

  * renderer Which can control how your business object is rendered by the widget. For  instance, you might format a double value into a currency format with a renderer.
  * comparator Which simply provides a pluggable external comparator for the widget to determine relative value. Sometimes == and .equals aren't enough. Maybe you just want to compare "id" properties on objects from various dirty database states. This lets you do it.

Finally is:
  * action This is a default behavior strategy to associate with the object. The most basic Action interface simply defines an "execute()" method. This method will be called on whatever the generally accepted "default" action of a BoundWidget might be... A Button, for instance, will call it on a Click event. A Checkbox in the change event, etc. This lets you keep your code generic enough to reuse business logic based on the context.

Taken together, these allow you to build your UIs in a recursively self-similar way. Graphs of UI components map easily to graphs of business objects. We are under no illusion here. This kind of flexibility comes at a price: a lot of your code will look very similar, and sometimes it will require poking around to figure out exactly what comes from where. However, your code will also be very simple, and once you get accustomed to the patterns, you can build very complex UIs very quickly.

# Building an Application #

TODO

# The Gwittir Packages #


  * action : This package defines the interfaces for actions in Gwittir as above.
  * beans : This package contains the client-side [Introspection](Introspection.md) API.
  * flow : This package contains information related to multi-widget flows within page areas
  * fx : This package contains classes for doing [Animation](Animation.md)s and other "special effects" in your applicaiton.
  * fx.ui : This package contains pre-built UI elements that use the fx. classes to create on-screen effects.
  * log : This package contains the Gwittir [Logging](Logging.md) system.
  * ui : This package contains the Gwittir-patterned UI elements.
  * ui.table : This package contains complex widgets for working with collection tables and tabular forms.
  * ui.util : This package contains utility classes for use by UI elements, including the very important BoundWidgetTypeFactory and BoundWidgetProvider classes.
  * util : This package contains general-purpose utility classes.
  * validation : This package contains interface definitions and common Validators and ValidationFeedback implementations.

# Gwittir Conventions #

For many classes that you might need to reuse, but are in-effect stateless, like the IntegerValidator, they are provided as field singletons, referenced by the .INSTANCE static. eg IntegerValidator.INSTANCE or Introspector.INSTANCE. These are basically there to make sure you aren't needlessly creating multiple copies of these objects, when there is no object state. In the case of Introspector, this can be a very heavy object, and creating multiple copies is needless.

Many classes represent a composition of other objects. With these, the Builder pattern is used extensively, basically returning "this" at the end of mutator method calls in order to simplify your code. For a CompositeValidator, for instance, you might use:

```
        CompositeValidator v = new CompositeValidator()
            .add( NotNullValidator.INSTANCE )
            .add( IntegerValidator.INSTANCE );
```