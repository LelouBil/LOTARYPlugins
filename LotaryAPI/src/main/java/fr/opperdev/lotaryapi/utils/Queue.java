package fr.opperdev.lotaryapi.utils;

import java.util.Iterator;
import java.util.LinkedList;


public class Queue<T> implements Iterable<T> {
	
	private LinkedList<T> inQueue;
	private int maxQueueLength;
	
	public Queue(int max) {
		maxQueueLength = max;
	}
	
	public void add(T t) {
		if(maxQueueLength != -1)
			if(inQueue.size() >= maxQueueLength)
				return;
		inQueue.add(t);
	}
	
	public void remove(int i) {
		inQueue.remove(i);
	}
	
	public void remove(T t) {
		inQueue.remove(t);
	}
	
	public T getFirst(boolean remove) {
		if(remove)inQueue.removeFirst();
		return inQueue.getFirst();
	}
	
	public T getLast(boolean remove) {
		if(remove)inQueue.removeLast();
		return inQueue.getLast();
	}

	public LinkedList<T> getLinkedList(){
		return inQueue;
	}
	
	public Iterator<T> iterator() {
		return inQueue.iterator();
	}
	
}
