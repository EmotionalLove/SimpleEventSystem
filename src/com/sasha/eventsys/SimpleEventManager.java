package com.sasha.eventsys;

import com.sasha.eventsys.exception.ListenerRegistrationException;
import com.sun.istack.internal.NotNull;

import javax.management.ListenerNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Sasha on 05/08/2018 at 9:43 AM
 **/
public class SimpleEventManager {

    private ArrayList<Method> registeredMethods = new ArrayList<>();

    public void registerListener(@NotNull SimpleListener listener) throws ListenerRegistrationException{
        Method[] methods = listener.getClass().getMethods();
        for (Method method : methods){
            if (method.getAnnotations() == null || method.getAnnotations().length == 0){
                continue;
            }
            final SimpleEventHandler simpleEventHandler = method.getAnnotation(SimpleEventHandler.class);
            if (simpleEventHandler == null){
                continue;
            }
            if (method.getParameterCount() != 1){
                throw new ListenerRegistrationException("Function " + method.getName() + " has invalid amt of parameters (" + method.getParameterCount() + ")");
            }
            if ((!method.getParameterTypes()[0].getSuperclass().getName().matches("com\\.sasha\\.eventsys\\.SimpleEvent|com\\.sasha\\.eventsys\\.SimpleCancellableEvent"))){
                throw new ListenerRegistrationException("Function " + method.getName() + " has invalid type of parameter (" + method.getParameterTypes()[0].getSuperclass().getName() +")");
            }
            registeredMethods.add(method);
        }
    }
    public void invokeEvent(SimpleEvent e){
        registeredMethods.forEach(method -> {
            if (method.getParameterTypes()[0] == e.getClass()){
                try {
                    method.setAccessible(true);
                    method.invoke(method.getDeclaringClass().newInstance(), e);
                    method.setAccessible(false);
                }catch (Exception ex){
                    System.out.println("FATAL EXCEPTION DURING " + e.getClass().getName() + "'s EXECUTION");
                    ex.printStackTrace();
                }
            }
        });
    }
}
