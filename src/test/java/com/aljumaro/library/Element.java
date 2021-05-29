package com.aljumaro.library;

public class Element {

	private int id;

	public Element(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Test [id=" + id + "]";
	}

}
