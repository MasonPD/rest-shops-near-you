package com.shops.data;

import java.util.List;

public interface Store<T, M> {

	/**
	 * retrieve an item
	 * 
	 * @return
	 */
	public T get(M searchCriteria);

	/**
	 * retrieve all the items
	 * 
	 * @return
	 */
	public List<T> getAll();

	/**
	 * add an item
	 * 
	 * @param item
	 */
	public T add(T item);
}
