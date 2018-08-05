# SimpleEventSystem
An extremely simple annotated event system for Java. Simply add as a library to your project, no dependencies needed.

## An extremely simple how-to
First, you need to make an event. Make a new class that extends `SimpleEvent` or `CancellableSimpleEvent`

```Java
public class TestEvent extends SimpleEvent {
// You can add a constructor and any functions/variables you'd like here
// It's recommended to use getters and setters for them.
}
```
or
```Java
public class TestEvent extends SimpleCancellableEvent {

}
```
---
Great, now you need a listener. Make a class that implements `SimpleListener`, and create a new instance of `SimpleEventManager`, and register your listener.

```Java
public class MyCuteClass implements SimpleListener {
  public SimpleEventManager manager = new SimpleEventManager();
  public static MyCuteClass INSTANCE;
  
  public void start() throws ListenerRegistrationException {
    INSTANCE = this;
    manager.registerListener(this);
    // do whatever else you like
  }
```
You can register as many different listeners as you'd like (from the same manager).

---

Now, you need to invoke your methods.

```Java
public class SomeIrrelevantClass {
  public static void someIrrelevantFunc(String someString, float irrelevanceLevel){
    // whatever this function does...
    TestEvent /*or whatever your event is called*/ ev = new TestEvent();
    MoreComplexEvent mev = new MoreComplexEvent(irrelevanceLevel /* You can customise your event's constructor as you'd like */);
    MyCuteClass.INSTANCE.manager.invokeEvent(ev);
    MyCuteClass.INSTANCE.manager.invokeEvent(mev);
    if (mev.isCancelled()){
      return;
    }
    // other random stuffs for this function 
  }
}

---
Now, you can set up the actual tasks to execute when a certain Event is invoked. (Yay)

```Java
public class MyCuteClass implements SimpleListener {
  private SimpleEventManager manager = new SimpleEventManager();
  public static MyCuteClass INSTANCE;
  
  public void start() throws ListenerRegistrationException {
    //...
  }
  
  @SimpleEventHandler
  public void onSomeEvent(TestEvent e){
    // do stuff
  }
  @SimpleEventManager
  public void onSomeMoreComplexEvent(MoreComplexEvent e){
    if (/* some condition */){
      e.setCancelled(true);
    }
  }
```

And, well, that's that. Easy as english.
