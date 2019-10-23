package fr.opperdev.lotaryapi.utils;

import java.util.ArrayList;
import java.util.List;

public class Paginator<T> {
	
	private T[] objects;
	private double pagSize;
	private int currentPage;
	private int amountOfPages;
	
	public Paginator(T[] objs, int max) {
		objects = objs;
		pagSize = max;
	}
	
	public Paginator(List<T> objs, int max) {
		this(objs.toArray((T[]) new Object[0]), max);
	}
	
	public void setElements(List<T> objs) {
		objects = objs.toArray((T[]) new Object[0]);
	}
	
	public boolean hasPage(int page) {
		return amountOfPages >= page;
	}
	
	public boolean hasNext() {
		return currentPage < amountOfPages;
	}
	
	public int getNext() {
		return currentPage + 1;
	}
	
	public boolean hasPrev() {
		return currentPage > 1;
	}
	
	public int getPrev() {
		return currentPage - 1;
	}
	
	public List<T> getPage(int pageNum){
		List<T> page = new ArrayList<T>();
		double total = objects.length / pagSize;
		amountOfPages = (int) Math.ceil(total);
		currentPage = pageNum;
		
		if(objects.length == 0)return page;
		
		double startC = pagSize * (pageNum - 1);
		double finalC = startC + pagSize;
		
		for(; startC < finalC; startC++)
			if(startC < objects.length)
				page.add(objects[(int) startC]);
		return page;
	}
	
}
