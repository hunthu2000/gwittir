# Introduction #

Make it dance! This is of course one of those requirements for AJAX/RIA applications, and can help make your app look good. Because Gwittir brings basic Beans-type reflection to GWT, it also opens the door for animation more in-line with what you would see in the Swing Timing Framework. Here we will take a look at how this work.


# Basic Animation #

Basic animation is achieve through the com.totsp.gwittir.client.fx.PropertyAnimator class. This is a fairly simple class that works with the following attributes, specified in the constructors in various combinations:

  * AnimationFinishedCallback callback : a callback fired when an animation is finished.
  * Introspectable target : the object on which a property will be animated.
  * MutationStrategy strategy : an implementation of the MutationStrategy interface which determines the value at a point in time.
  * Object finalValue : the end value the property will have when the animation completes.
  * Object initialValue : the initial value of the property
  * Property property : the property to change (this is passed in as simply a name on the constructor)
  * int duration : the duration in milliseconds the animation should take to complete.
  * int stepTime : the number of miliseconds between evaluations of the property within the duration.

Here perhaps the least obvious is the MutationStrategy. This is an interface that has a single method:

```
    Object mutateValue(Object from, Object to, double percentComplete);
```
Where the initial value, the final value, and the percentage complete the animation should be are passed in, and the intermediate value of the property is returned.

Directly on the MutationStrategy interface are several implementations, including:

  * INTEGER\_SINOIDAL, INTEGER\_LINEAR, INTEGER\_CUBIC : for working with int or Integer properties.
  * DOUBLE\_SINOIDAL,DOUBLE\_LINEAR, DOUBLE\_CUBIC : for working with double or Double properties
  * UNITS\_SINOIDAL, UNITS\_LINEAR, UNITS\_CUBIC : for working with string properties in the form of [value](value.md)[units](units.md), eg: 10px, 20%, 5em, etc.

Here you have SINOIDAL (wave or slow-fast-slow, this generally gives the most appealing, organic feel), LINEAR (moves at a constant pace), and CUBIC (accelerates at x<sup>3</sup> speed).

So, to animate the scrollPosition property on a Gwittir ScrollArea:

```

 scrollAnimator = new PropertyAnimator(scroll area, 
                "scrollPosition",  // the property to animate
                new Integer(newPosition), // the final value, 
                                          //using the current as the start
                MutationStrategy.INTEGER_SINOIDAL,  // The mutation strategy
                duration,                 // duration of the animation ,with a default
                                          // step time of 500ms
                new AnimationFinishedCallback() {  // a callback to monitor the results.
                    public void onFinish(PropertyAnimator animator) {
                        
                    }

                    public void onFailure(PropertyAnimator animator, Exception e) {
                        
                    }
                });
        
 scrollAnimator.start(); // start the animation.

```

# Additional Wrappers #

In addition to Gwittir Widget properties, the fx package includes wrappers to let you animate properties not otherwise supported, including Position and Opacity. For instance, to animate the opacity of something:

```
 PropertyAnimator a = new PropertyAnimator(
            new OpacityWrapper( myUIObject ), 
            "opacity", new Double(0), new Double(1), 
            MutationStrategy.DOUBLE_SINOIDAL, 3000);
 a.start();
```

Animates from transparent (0) to opaque (1).