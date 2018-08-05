package com.sasha.eventsys;

/**
 * Created by Sasha on 05/08/2018 at 9:42 AM
 **/
public abstract class SimpleCancellableEvent extends SimpleEvent {

    private boolean isCancelled = false;

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
