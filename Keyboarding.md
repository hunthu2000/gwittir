# Introduction #

One thing that is a must-have for a good web application is providing keyboard support. While the basic access key support in GWT matches what browsers normally do for keyboard support, it doesn't really match what you expect in a true rich application. To that end, Gwittir includes its own keyboard support system.



# KeyBindings and the KeyboardController #

The basics of keyboard support come from the KeyBinding and KeyboardController classes. KeyBinding is simply a reference class that refers to a mapped combo.  The constructor is fairly self-evident:

```
public KeyBinding(
        final char key, final boolean control, final boolean alt,
        final boolean shift)
```

The key value should generally be an **uppercase** A..Z|0..9 character, or one of the special values for KeyBinding.F1..KeyBinding.F12. The modifiers should be fairly obvious.

You can use a KeyBinding to register an operation with the KeyboardController class. This is a singleton class that manages the current bindings in your application. It has several methods that are also pretty clear:

```
public void register(final KeyBinding binding, final BoundWidget widget)

public void register(final KeyBinding binding, final Action action)

public void register(final KeyBinding binding, final Task task)
```

The first method registers a BoundWidget. This will take the Action assigned to the BoundWidget, and invoke the execute() method with the widget as the argument. The second  ndings. The reason for this should be apparent in the next section.

# Binding Suggestions #

The KeyBinding class as a subclass call SuggestedKeyBinding. This is a binding that should be used if there is not a "proper" KeyBinding that wants a particular combination. Honestly, this is a complicated class to use well, but proper usage should chain with the KeyBindingEventListener.

The KeyboardController class has a method, ` public AutoMappedBinding findSuggestedMapping(String)`, that will take a String value and search for the first character where ALT+[character](character.md) is not a mapped key. It with then return an AutoMappedBinding class which contains an HTML String with the selected character underlined (to denote the mapping to the user) and a SuggestedKeyBinding class for that mapping.

Because the available mappings might change at any time, if a "hard-bound" mapping is added to the KeyboardController, you should use a KeyMappingEventListener on this SuggestedKeyBinding to watch for it being unbound, then take the new SuggestedKeyBinding and register it with the same operation as the now inactive mapping. All of this logic should be included in the bind() lifecycle of a BindingAction, to make sure the full set of attach()es has preceeded it. Yeah, it is complicated.

Let's look at very simple example:

```
final Button suggest = new Button("Suggest");        //create the button
        suggest.setAction(new BindingAction(){
            
            public void execute(BoundWidget model) {
                Window.alert("Suggested");
            }
            
            public void bind(BoundWidget widget) {
                KeyboardController.AutoMappedBinding auto =  // find an auto binding
               KeyboardController.INSTANCE.findSuggestedMapping( suggest.getText() );
                if( auto != null ){
                    suggest.setHTML( auto.newHtml );
                    suggest.setKeyBinding( auto.binding );
                    auto.binding.addKeyBindingEventListener( //add an listener for unbind
                      new KeyBindingEventListener(){
                        public void onUnbind(KeyBinding binding) {
                                                                 // when the key is 
                                                                 // cleared, repeat the 
                                                                 // process until a match
                                                                 // is found, or not
                            KeyboardController.AutoMappedBinding auto = 
                KeyboardController.INSTANCE.findSuggestedMapping( suggest.getText() );
                            if( auto != null ){
                                suggest.setHTML( auto.newHtml );
                                suggest.setKeyBinding( auto.binding );
                            } else {
                                suggest.setValue( suggest.getValue() );
                            }
                            
                        }
                        
                        public void onBind(KeyBinding binding) { 
                        }
                        
                    });
                }
            }
            
            public void unbind(BoundWidget widget) {
            }
            
            public void set(BoundWidget widget) {
            }
        });

```

"In the year 2000" we will make sure relevant Gwittir widgets (Like Button or MenuOption) support their own auto binding, and provide a utility class to negotiate this for you.