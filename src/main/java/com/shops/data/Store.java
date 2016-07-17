package com.shops.data;

import java.util.List;

/**
 * the store interface
 * 
 * @author ranjan
 *
 * @param <T>
 *            the object to store
 * @param <M>
 *            the searching criteria
 */
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
	default public List<T> getAll() {
		return null;
	}

	/**
	 * add an item
	 * 
	 * @param item
	 */
	public T add(T item);
}
