package com.swissquote.foundation.gson.model;

import java.util.ArrayList;
import java.util.List;

public class Truck implements Vehicle {

	private final String number;
	private List<Animal> animals = new ArrayList<Animal>();

	public Truck(String number) {
		this.number = number;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public String showPassangers() {
		return toString();
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}

	@Override
	public void addPassanger(Animal animal) {
		animals.add(animal);
	}

	@Override
	public String toString() {
		return "Truck [animals=" + animals + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Truck)) {
			return false;
		}

		Truck truck = (Truck) o;

		return number.equals(truck.number);

	}

	@Override
	public int hashCode() {
		return number.hashCode();
	}
}
