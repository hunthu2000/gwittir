#A look at the Gwittir .flow Package

# Introduction #

In spite of the DHTML nature of your application, you likely have a lot of "Move from Screen to Screen" stuff associated with your app. The FlowController makes building these apps very easy.

# Creating a FlowContext #

The FlowContext defines the flow for a sub-area of your application. It associates BoundWidgets or BoundWidgetProviders and Actions with "activity names." You can then call the activity by name and pass a model object with the call (or not). This will cause the managed area of the screen to transition to the given name with the given model. For example, making a hypothetical iPhone type app you might use:

```
List<MyModelBean> beans = ...;
FlowContext context = new FlowContext()
   .add( "list", new ListViewWidget(), new ListBindingAction())
   .add( "view", new ViewWidget(), new ViewBindingAction() )
   .add("edit", new EditWidgetProvider(), new EditBindingAction());

SimplePanel screen = new SimplePanel();

FlowController.setFlowContext( screen, context );
FlowController.call(screen, "list", beans, false );
```

Then in the ListBindingAction you would have:

```
void execute(ListViewWidget widget){
   FlowController.call(widget, "edit", widget.getSelected() );
}
```

When you call FlowController.call() the flow controller will crawl up the display tree until it finds the first context with the given name from the widget you pass as the first argument. This means you can have nested flow contexts in you app and they will be managed properly even if there are naming collisions.

# Managing History #

You will notice the "false" at the end of the first call. This tells the FlowController that this is an initialization state. That is, it doesn't notify its HistoryManager implementation. HistoryManagers are classes you can register with the FlowController to deal with the GWT History and FlowController state.  A HandyClass is the SimpleSessionHistoryManager. You can enable Back/Forward button support in your FlowController managed app with one line of code:

```
FlowController.setHistoryManager( new SimpleSessionHistoryManager() );
```

The SSHM builds a running history of all the Context-View-Model objects that are invoked in your application and maintains the navigation history between them. This doesn't provide for deep linking, since the models are already there, and they might need to be fetched. You can see it running in the [sample](http://gwittir.appspot.com). As you move between tabs and into and out of "view source" a state slug is set on the anchor. Back and forward will maintain your complete nav history.

Remember, though: Your whole nav history is saved in the SSHM, which means it could become a memory hog if you have large or many model objects.

If you want to implement your own HistoryManager, you might also look at the util.HistoryTokenizer class. This class simplifies storing multiple values on the anchor slug, and can be used to mark multiple states in your app. For instance, you might have ["tabs", "view"],["viewid","1234"]. You could then have your history manager switch to the "view", fetch object 1234 from a service and then set the model on the view.