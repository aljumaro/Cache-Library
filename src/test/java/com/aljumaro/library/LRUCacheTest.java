package com.aljumaro.library;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LRUCacheTest {

	private LRUCache<Integer, Element> cache;

	@BeforeEach
	public void before() {
		cache = new LRUCache<>(4);
	}

	@Test
	public void testPut() {
		// Given
		Element t1 = new Element(1);

		// When
		cache.put(t1.getId(), t1);
		System.out.println(cache.toString());

		// Then
		Assertions.assertEquals(cache.getCurrentSize(), 1);
	}

	@Test
	public void testPutOverflow() {
		// Given
		int numElements = 5;

		// When
		for (int i = 1; i <= numElements; i++) {
			cache.put(i, new Element(i));
		}
		System.out.println(cache.toString());

		// Then
		Assertions.assertEquals(4, cache.getCurrentSize());
		Assertions.assertTrue(cache.get(1).isEmpty());
	}

	@Test
	public void testReorder() {
		// Given
		int numElements = 3;

		// When
		for (int i = 1; i <= numElements; i++) {
			cache.put(i, new Element(i));
		}

		cache.get(2);
		System.out.println(cache.toString());

		// Then
		Assertions.assertEquals(2, cache.getFirstKey());
		Assertions.assertEquals(1, cache.getLastKey());
	}

	@Test
	public void testReorderFirstElement() {
		// Given
		int numElements = 3;

		// When
		for (int i = 1; i <= numElements; i++) {
			cache.put(i, new Element(i));
		}

		Optional<Element> e = cache.get(3);
		System.out.println(cache.toString());

		// Then
		Assertions.assertEquals(3, cache.getFirstKey());
		Assertions.assertEquals(1, cache.getLastKey());
		Assertions.assertEquals(3, e.get().getId());
	}

	@Test
	public void testReorderLastElement() {
		// Given
		int numElements = 3;

		// When
		for (int i = 1; i <= numElements; i++) {
			cache.put(i, new Element(i));
		}

		System.out.println(cache.toString());
		Optional<Element> e = cache.get(1);
		System.out.println(cache.toString());

		// Then
		Assertions.assertEquals(1, cache.getFirstKey());
		Assertions.assertEquals(2, cache.getLastKey());
		Assertions.assertEquals(1, e.get().getId());
	}

	@Test
	public void testElementNotFound() {
		// Given
		int numElements = 1;

		// When
		for (int i = 1; i <= numElements; i++) {
			cache.put(i, new Element(i));
		}

		Optional<Element> e = cache.get(2);
		System.out.println(cache.toString());

		// Then
		Assertions.assertEquals(1, cache.getFirstKey());
		Assertions.assertEquals(1, cache.getLastKey());
		Assertions.assertTrue(e.isEmpty());
	}

	@Test
	public void fullTest() {
		// Given
		int numElements = 10;

		// When
		for (int i = 1; i <= numElements; i++) {
			cache.put(i, new Element(i));
			if (i == 3 || i == 7) {
				cache.get(i - 1);
			}
		}
		System.out.println(cache.toString());

		// Then
		Assertions.assertEquals(10, cache.getFirstKey());
		Assertions.assertEquals(6, cache.getLastKey());
		Assertions.assertTrue(cache.get(2).isEmpty());
		Assertions.assertTrue(!cache.get(8).isEmpty());
	}
}
