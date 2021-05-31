package com.aljumaro.library;

/**
 * Least Frequently Used Cache.
 * 
 * This implementation is backed by a HashMap and a double linked list.
 * 
 * The put and get operations are executed in constant time O(1).
 * 
 */
public class LFUCache<K, V> extends BaseCache<K, V> {

	public LFUCache(int capacity) {
		super(capacity);
	}

	protected void reorder(CacheEntry<K, V> entry) {
		entry.incrementHitCount();

		if (entry == first) {
			return;
		}

		CacheEntry<K, V> prev = entry.getPrev();

		while (prev != null) {
			if (prev.getHitCount() > entry.getHitCount()) {
				break;
			}
			
			if (last == entry) {
				last = entry.getPrev();
			}
			
			CacheEntry<K, V> next = entry.getNext();

			prev.setNext(next);
			if (next != null) {
				next.setPrev(prev);
			}
			entry.setPrev(prev.getPrev());
			entry.setNext(prev);
			entry.getNext().setPrev(entry);
			if (entry.getPrev() != null) {
				entry.getPrev().setNext(entry);
			}
			
			prev = entry.getPrev();
		}

		if (entry.getPrev() == null) {
			first = entry;
		}
	}
}
