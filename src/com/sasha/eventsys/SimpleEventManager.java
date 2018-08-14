package com.sasha.eventsys;

import com.sasha.eventsys.exception.ListenerRegistrationException;
import com.sun.istack.internal.NotNull;

import javax.management.ListenerNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sasha on 05/08/2018 at 9:43 AM
 **/
public class SimpleEventManager {

    private HashMap<Method, Object> registeredMethods = new HashMap<>();

    public void registerListener(@NotNull SimpleListener listener, Object instance) {
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
            if (registeredMethods.containsKey(method)){
                return;
            }
            registeredMethods.put(method, instance);
        }
    }
    public void deregisterListener(@NotNull SimpleListener listener){
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
            if (!registeredMethods.containsKey(method)){
                return;
            }
            registeredMethods.remove(method);
        }
    }
    public void invokeEvent(SimpleEvent e){
        for (Map.Entry<Method, Object> set : registeredMethods.entrySet()) {
            if (set.getKey().getParameterTypes()[0] == e.getClass()) {
                try {
                    set.getKey().setAccessible(true);
                    Class clasz=set.getKey().getClass();
                    for (Field field : clasz.getDeclaredFields()) {
                        field.setAccessible(true);
                    }
                    for (Method meth : clasz.getDeclaredMethods()) {
                        meth.setAccessible(true);
                    }
                    for (Constructor<?> constructor : clasz.getDeclaredConstructors()) {
                        constructor.setAccessible(true);
                    }
                    set.getKey().invoke(set.getValue(), e);
                } catch (Exception ex) {
                    System.out.println("FATAL EXCEPTION DURING " + e.getClass().getName() + "'s EXECUTION");
                    ex.getCause().printStackTrace();
                }
            }
        }
    }
}
