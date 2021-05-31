package com.aljumaro.library;

/**
 * Least Recently Used Cache.
 * 
 * This implementation is backed by a HashMap and a double linked list.
 * 
 * The put and get operations are executed in constant time O(1).
 * 
 */
public class LRUCache<K, V> extends BaseCache<K, V> {

	public LRUCache(int capacity) {
		super(capacity);
	}

	protected void reorder(CacheEntry<K, V> entry) {
		if (entry == first) {
			return;
		}
		if (entry == last) {
			last = entry.getPrev();
			entry.getPrev().setNext(null);
			entry.setPrev(null);
		}
		if (entry.getNext() != null) {
			entry.getNext().setPrev(entry.getPrev());
		}
		if (entry.getPrev() != null) {
			entry.getPrev().setNext(entry.getNext());
		}
		entry.setNext(first);
		entry.setPrev(null);
		first.setPrev(entry);
		first = entry;
	}

}
