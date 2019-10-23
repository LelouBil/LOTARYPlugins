package fr.opperdev.lotaryapi.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class CustomEvent extends Event implements Cancellable {
	
	public abstract String getEventName();
	
}
