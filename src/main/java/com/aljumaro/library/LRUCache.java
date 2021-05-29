package com.aljumaro.library;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  Least Recently Used Cache.
 * 
 *  This implementation is backed by a HashMap and a double linked list.
 * 
 *  The put and get operations are executed in constant time O(1). 
 * 
 * */
public class LRUCache<K, V> {

	private final Map<K, CacheEntry<K, V>> map;
	private CacheEntry<K, V> first, last;
	private int size;
	private int initialCapacity;

	public LRUCache(int capacity) {
		map = new ConcurrentHashMap<>(capacity);
		this.initialCapacity = capacity;
	}

	public int getCurrentSize() {
		return size;
	}

	public void put(K key, V value) {
		CacheEntry<K, V> entry = new CacheEntry<K, V>(key, value);
		if (isFull()) {
			removeLast();
		}
		map.put(key, entry);
		if (first != null) {
			addBeforeFirst(entry);
		}
		first = entry;
		if (last == null) {
			last = entry;
		}
		size++;
	}

	public Optional<V> get(K key) {
		if (!map.containsKey(key)) {
			return Optional.empty();
		}

		CacheEntry<K, V> entry = map.get(key);
		reorder(entry);
		return Optional.of(entry.getValue());
	}

	K getFirstKey() {
		return first.getKey();
	}

	K getLastKey() {
		return last.getKey();
	}

	private void addBeforeFirst(CacheEntry<K, V> entry) {
		entry.setNext(first);
		first.setPrev(entry);
	}

	private void removeLast() {
		map.remove(last.getKey());
		last = last.getPrev();
		last.getNext().setPrev(null);
		last.setNext(null);
		size--;
	}

	private void reorder(CacheEntry<K, V> entry) {
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

	private boolean isFull() {
		return initialCapacity == size;
	}

	@Override
	public String toString() {
		return "LRUCache [map= " + map.keySet() + ", linkedList= " + printLinkedList(false) + ", linkedListReverse= "
				+ printLinkedList(true) + ", first= " + first.getKey() + ", last= " + last.getKey() + "]";
	}

	private String printLinkedList(boolean reverse) {
		CacheEntry<K, V> entry = (!reverse) ? first : last;
		StringBuilder result = new StringBuilder();
		do {
			if (!reverse) {
				result.append(entry.getKey() + " --> ");
				entry = entry.getNext();
			} else {
				result.insert(0, " <-- " + entry.getKey());
				entry = entry.getPrev();
			}

		} while (entry != null);
		return result.toString();
	}
	
	private static class CacheEntry<K, V> {

		private K key;
		private V value;
		private CacheEntry<K, V> next, prev;

		public CacheEntry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public CacheEntry<K, V> getNext() {
			return next;
		}

		public void setNext(CacheEntry<K, V> next) {
			this.next = next;
		}

		public CacheEntry<K, V> getPrev() {
			return prev;
		}

		public void setPrev(CacheEntry<K, V> prev) {
			this.prev = prev;
		}

		@Override
		public String toString() {
			return "CacheEntry [key=" + key + ", value=" + value + "]";
		}

	}

}
