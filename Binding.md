# Introduction #

Of course, once the GWT Introspection system was in place, the next step is to come up with a binding framework. One of the great problems with building GWT applications is dealing with a great number of Listeners to handle simple binding for widgets. Gwittir provides a shorthand way for doing this.


While you should continue to read this document, be sure to check out the BindingBuilder page for an easy way to construct complex bindings.

# Requirements for Binding #

The simplest way to to enable Binding on your classes is to implement the com.totsp.gwittir.beans.Bindable interface. This interface defines, simply:

```
public interface Bindable extends SourcesPropertyChangeEvents, Introspectable {
}
```

For PropertyChangeSupport and Events, please see the [GWTx](http://code.google.com/p/gwtx) project.

# Creating Bindings #

The simplest way to create a binding is to simply specify two Bindable objects and create a Binding class specifying the property names:

```

  Foo foo = new Foo();
  Bar bar = new Bar();

  Binding b = new Binding( foo, "baz", bar, "quux" );

  b.bind();
```

This will establish a two-way binding between the "baz" property on Foo and the "quux" property on Bar.

This binding doesn't change the initial values of either of your instances. Generally you will want to initialize one of these classes with the value from the other. To do this, you should invoke the set() methods on the Binding. For instance. To copy the "quux" value from bar to foo you would invoke:

```
   b.setLeft();
```

The "Left" and "Right" notations are simply based on the order in which the objects were passed into the constructor.

The Binding object also has a List of Child bindings that can be controlled with the single instance. This is useful when you need to manage multiple bindings at the same time. For example:

```

  Foo foo = new Foo();
  Bar bar = new Bar();
  
  Binding b = new Binding();
  b.getChildren.add( new Binding( foo, "property1", bar, "propertyOne" ) );
  b.getChildren.add( new Binding( foo, "property2", bar, "propertyTwo" ) );
  
  b.setLeft();
  b.bind();
```

Actions will now cascade into the the child bindings. This allows you to easily clean up listeners and release bindings.

When specifying a property for binding, you can use a limited syntax to crawl down the object graph to, for instance, bind a Label's value to a property on a child object. The simplest form of this is to use the dot notation in the property:

```
        Person mark = new Person("Mark", "Lanford", 31);
        mark.setSpouse(new Person("Benay", "Krissel", 37));
        Label spouseName= new Label();
        Binding b = new Binding(spouseName, "value", 
                                mark, "spouse.firstName"); //Note this
        b.setLeft();
        b.bind();

```

This will establish a binding between the value property of spouceName to the firstName property _of the object from the roots .getSpouse() method_. However, the binding will watch all the properties you move through and keep track of changes. So if you change the spouse property on the root object, it will unbind from the old one, rebind to the new one, and reissue the last setLeft()/setRight() method you called to get the relationships into the appropriate state. This means that all the objects you intend to traverse here have to implement Bindable, with two exceptions: Arrays and Collections.

If you have an array of Bindables or a Collection of Bindables, you can use the limited selection syntax to bind to specific objects in the collection. For example:

```

        Person[] children = {new Person("Delaney", "Krissel", 12),
            new Person("Jonny", "Doe", 1),
            new Person("Jenny", "Nobody", 3)
        };
        mark.setChildren(children);
        b = new Binding(secondChildName /*pretend this is a Label */, "value", 
                        mark, "children[2].firstName"); // Note this
        b.setLeft();
        b.bind();
```

This says bind to the Bindable in position 2 on the array. This simple index works in the standard way for arrays, or will retrieve the second index value from an Iterator on a collection.

The second form of selector you can use is the simple String equality selector. This allows you to perform a simple comparison of a String value to the .toString() value of a property. For example:

```
   b = new Binding(doesFirstName, "value", 
                   mark, "children[lastName=Doe].firstName"); //Note this

```

This will bind to the first instance in the children property whose lastName property is "Doe". In this case, it is exactly the same as using [1](1.md), since the [1](1.md) index position contains Jonny Doe.

This is a very basic evaluation here. You can't select sub-properties in the selector. There is no != > < >= <= operations, etc.

You can also specify Converter implementation to adapt properties between differing types. The Converter interface is very simple:

```
public interface Converter {
    Object convert(Object original);
}
```

When constructing a Binding object, you can pass in a Converter that can be used to adapt an object to the correct format or type.

```

  Converter toStringConverter = new Converter(){
     public Object convert(Object original){
         return original = null ? null : original.toString();
     }
  };

  Converter toIntegerConverter = new Converter(){
     public Object convert(Object original){
         return original == null ? new Integer(0) : Integer.valueOf( original.toString() );
     }
  }

  Binding b = new Binding( foo, "stringProperty", toStringConverter,
                           bar, "integerProperty", toIntegerConverter );

  b.bind();

```


Converters are useful for basic conversion, but many times you want to validate a value and provide user feedback. The binding supports this as well.



# Validators #

Validation is a two step process. The first involves validating data, the second is displaying errors to the user. For validating data, the key interface is the Validator. This looks very much like the Converter interface, but can also throw a ValidationException.

```

public interface Validator {
    public Object validate(Object value) throws ValidationException;
}

```

Unlike the Converter, which returns an object suitable for the bean it is associated with -- as above, where the "stringProperty" is associated with the toStringConverter, a Validator returns an object sutable for the DESTINATION property.

So, for instance, the IntegerValidator looks like:

```

 public Object validate(Object value) throws ValidationException {
        
        if(value == null || value instanceof Integer) {
            return value;
        }

        Integer i;

        try {
            i = Integer.valueOf(value.toString());
        } catch(NumberFormatException nfe) {
            GWT.log(null, nfe);
            throw new ValidationException("Must be an integer value.",
                IntegerValidator.class);
        }

        return i;
    }
```

Notice that null is a default passthrough for the Validator. We will come back to that. Here we try and create the Integer. If we cannot, we throw a ValidatorException, passing in the Validator class that failed. Otherwise, we return the Integer object.


Validators like this can be useful, but seem very simplistic. The goal of the built in Validators is to provide very basic operations, but you can create more complex validation rules by using the CompositeValidator. This class lets you chain multiple validators together.

For example:

```
    CompositeValidator cv = new CompositeValidator()
        .add(NotNullValidator.INSTANCE)       // validators that don't have state
        .add(IntegerValidator.INSTANCE)       // are treated as public static final
        .add(new IntegerRangeValidator(1, 5));

```

Here we create a new CompositeValidator, and add a chain of 3 validators to it. Now we can validate that a property is "Not Null", "An Integer" and "Between 1 and 5 inclusive."


# ValidationFeedback #


Once you are validating code, you need to provide feedback to the user that a validation error has occurred. This is where the ValidationFeedback inteface comes in. This is, once again, a very simple interface:

```
public interface ValidationFeedback {
    public void handleException(Object source, ValidationException exception);
    public void resolve(Object source);
}
```

When a validation exception is thrown, the Binding will pass the source object of the exception and the exception itself to the handleException() method. When the state of the object changes again, it will call resolve(). This is the VF implementations cue to clean up whatever it has done.

Gwittir includes several ValidationFeedback implementations. A good example is the PopupValidationFeedback class. This will create a PopupPanel with a message in it, positioned next to the "source" object, generally a widget.

```

 ValidationFeedback pvf =
                new PopupValidationFeedback(PopupValidationFeedback.BOTTOM)
                .addMessage(NotNullValidator.class, "You must provide a value")
                .addMessage(IntegerValidator.class, "Enter an integer")
                .addMessage(IntegerRangeValidator.class, "Value must be 1..5");

```

Here we have created a PopupValidationFeedback object, positioned BOTTOM, or below the widget that caused the exception. Next we add messages, associated with the Validator classes we created above to the PVF. These determine the message that will be displayed when an exception is thrown from a specific Valdiator implementation.

Now we can create a Binding using the CompositeValidator and the PopupValidationFeedback objects:

```
   TextBox intBox = new TextBox();
   Binding b2 = new Binding(intBox, "value", cv, pvf,            
                            bar, "integerProperty", null, null);
```

With this binding, we specify we are binding to the "value" property of the TextBox (more on this in the next section), we pass in the Validator and the ValidationFeedback implemetations, and the target object, our Bar class from above. Since we don't feed the need to validate the data coming from Bar before we put it into the text box, we simply pass in nulls for the right-size validation arguments.

Just as with the validation, we provide a CompositeValidationFeedback class so you can chain multiple VFs together.

```
  CompositeValidationFeedback cvf = new CompositeValidationFeedback()
        .add(pvf)
        .add(new StyleValidationFeedback("validation-error"));

```

Here we have created a composite using the PopupValidationFeedback from above, and added a "StyleValidationFeedback". This will "add" a style name on the Object that cause the validation exception. You can then use the CSS to mark the widget that is invalid, as well as get the Popup below it.

```
.validation-error {
    color: red;
    background-color: white;
    border: 1px dotted red;
}
```


Finally, each Binding can be checked quickly for validity by calling isValid(). This will check the validity on this binding and all children. This can be used as a final check before moving your application to the next state, or sending a bound model object off to a remote service.


You can also use Validators in combination with a Converter, though the need to do so is rare. In this case, the converter will be used for data coming INTO the bound object and the validator for data LEAVING the bound object for it's target partner.


# BoundWidgets #

BoundWidgets are a concept specific to Gwittir. These are, in effect, regular GWT widgets that support some common interface conventions to make them easier to work with. This ease of use, however, can come with a certain level of complexity, due to the generic nature of the bound widget.

BoundWidgets support 3 main properties:

  * model This contains YOUR model object, typically a business class of some kind. The model is really just a placeholder property.
  * value This is the raw value of the widget. The type of this value will vary from widget to widget. For a Composite bound widget for working with a business object, it may simply be the same as the model. For Collection-based bound widgets, like the ListBox or the BoundTable, this can be a collection of business objects. For simple types like TextBox or Checkbox, this will be a String or boolean value directly. The idea here is that when you are Binding the BoundWidget, you are usually binding to the "value" property.
  * action This is the Controller for the bound widget. The most basic Action interface simply defines an "execute()" method. This method will be called on whatever the generally accepted "default" action of a BoundWidget might be... A Button, for instance, will call it on a Click event. A Checkbox in the change event, etc. This lets you keep your code generic enough to reuse business logic based on the context.

BoundWidgets also have 2 other properties that may, or may not, be used by various implementations. They are...

  * renderer This takes a business object and "renders" it to a form suitable for the Widget. For instance, you might have a Collection of Person objects going into a ListBox. The list box will use its renderer to determine the text that will appear in the options. You can then provide a renderer that will return: person.getLastName()+", "+person.getFirstName(). Unlike Swing, which always uses toString() for this kind of operation, you can customize this. BoundWidgets should provide a default implementation that is "good enough" -- such as a simple .toString() call -- so you should only have to use this when you really want to.
  * comparator This is used to compare value objects. Especially if you are dealing with objects that might have come from a server at different times or in different states, being able to say "id equality is OK here, but not here" is handy. Some widgets also use comparators for sorting operations. Again, most widgets will provide a default implementation that should be good enough for most uses.

Obviously, because we want to do Binding with BoundWidgets, they all implement the Bindable interface.


It is worth noting that the excessive use of Object as a property type here is sub-optimal. Frankly, we are waiting for GWT 1.5 with generics, at which time we can genericize the classes to provide better type safety and less casting when working with BoundWidgets.

# BindingActions #

The most common type of Action you will use is the BindingAction. In addition to .execute(), the BindingAction contains 3 methods that represent the lifecycle of a BoundWidget on the screen.

  * set : This should set the initial values of a component to the values of the business object.
  * bind : This establishes bindings between the two.
  * unbind : This breaks the bindings and should clean up listeners.

These methods are called, generally, in this order and represent the attach/detach lifecycle of an object. Since each of these methods takes a BoundWidget as an argument, you are working with the .getModel() object as the binding target.

Lets look at an example:
```
public class AddressEditAction implements BindingAction {
    
    private Binding binding = new Binding();             // this is a parent binding
                                                         // we will use this just for 
                                                         // lifecycle.
    /** Creates a new instance of AddressEditAction */
    public AddressEditAction() {

    }

    public void unbind(BoundWidget widget) {             // To unbind, we should clean
        binding.unbind();                                // up everything we can. unbind()
        binding.getChildren().clear();                   // removes all listeners 
    }                                                    // recursively. clear() cleans up
                                                         // child bindings.

    public void bind(BoundWidget widget) {
        binding.bind();                                  // this is easy!
    }

    public void set(BoundWidget widget) {
        Address a = (Address) widget.getModel();
        AddressEdit e = (AddressEdit) widget;
        // here we are going to add a child binding to the "value" on each Gwittir
        // widget to the appropriate property on our business object.
        binding.getChildren().add( new Binding(e.address1, "value", a, "address1" ));
        binding.getChildren().add( new Binding(e.address2, "value", a, "address2" ));
        binding.getChildren().add( new Binding(e.city, "value", a, "city" ));
        binding.getChildren().add( new Binding(e.state, "value",  a, "state" ));
        binding.getChildren().add( new Binding(e.zip, "value", a, "zip" ));
        binding.getChildren().add( new Binding(e.type, "value",  a, "type" ));
        // Now we are going to set all the Gwittir Widgets values to the values on the 
        // business object.
        binding.setLeft();
   
        // Now we are going to configure renderers and comparators. We just want the
        // "name" attribute to be the text value in the list box.
        e.state.setRenderer( new Renderer(){
            public Object render(Object o) {
                return ((StateLookup) o).name;
            }
            
        });
        // And we want to compare the "id" property to determine if they are the same
        // from the database.
        e.state.setComparator( new Comparator(){
            public int compare(Object o, Object c ) {
                return ((StateLookup) o).id.compareTo( ((StateLookup) c).id );
            }
            
        });
        
    }

    public void execute(BoundWidget model) {                    
       // This widget doesn't have an execute. That will likely come from a parent who
       // has a call to a "save" service.
    }
```