package com.swissquote.foundation.gson.model.impl;

import com.swissquote.foundation.gson.model.Animal;

public class Cat implements Animal {
	public String name;

	public Cat(String name) {
		this.name = name;
	}

	@Override
	public String makeSound() {
		return name + " : \"meaow\"";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Cat)) {
			return false;
		}
		Cat cat = (Cat) o;
		return name.equals(cat.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "Cat [name=" + name + "]";
	}
}
