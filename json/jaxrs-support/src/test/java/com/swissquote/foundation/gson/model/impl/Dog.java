package com.swissquote.foundation.gson.model.impl;

import com.swissquote.foundation.gson.model.Animal;

public class Dog implements Animal {
	public String name;
	public int ferocity;

	public Dog(String name, int ferocity) {
		this.name = name;
		this.ferocity = ferocity;
	}

	@Override
	public String makeSound() {
		return name + " : \"bark\" (ferocity level:" + ferocity + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Dog)) {
			return false;
		}
		Dog dog = (Dog) o;
		return ferocity == dog.ferocity && name.equals(dog.name);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + ferocity;
		return result;
	}

	@Override
	public String toString() {
		return "Dog [name=" + name + ", ferocity=" + ferocity + "]";
	}
}
