package com.aljumaro.library;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class BaseCache<K, V> implements Cache<K, V> {

	private final Map<K, CacheEntry<K, V>> map;
	protected CacheEntry<K, V> first, last;
	private int size;
	private int initialCapacity;

	public BaseCache(int capacity) {
		map = new ConcurrentHashMap<>(capacity);
		this.initialCapacity = capacity;
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

	protected abstract void reorder(CacheEntry<K, V> entry);

	@Override
	public String toString() {
		return "LRUCache [map= " + map.entrySet() + ", linkedList= " + printLinkedList(false) + ", linkedListReverse= "
				+ printLinkedList(true) + ", first= " + first.getKey() + ", last= " + last.getKey() + "]";
	}

	K getFirstKey() {
		return first.getKey();
	}

	K getLastKey() {
		return last.getKey();
	}

	int getCurrentSize() {
		return size;
	}

	private boolean isFull() {
		return initialCapacity == size;
	}

	protected String printLinkedList(boolean reverse) {
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

	protected static class CacheEntry<K, V> {

		private K key;
		private V value;
		private CacheEntry<K, V> next, prev;
		private AtomicInteger hitCount = new AtomicInteger();

		public CacheEntry(K key, V value) {
			super();
			this.key = key;
			this.value = value;
			this.hitCount.set(0);
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

		public void incrementHitCount() {
			this.hitCount.addAndGet(1);
		}

		public int getHitCount() {
			return this.hitCount.get();
		}

		@Override
		public String toString() {
			return "[key=" + key + ", hitCount=" + getHitCount() + "]";
		}

	}
	
}
