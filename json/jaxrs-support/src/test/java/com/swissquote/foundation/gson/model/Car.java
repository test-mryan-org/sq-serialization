package com.swissquote.foundation.gson.model;

import java.util.ArrayList;
import java.util.List;

public class Car implements Vehicle {

	private final String number;
	private List<Animal> animals = new ArrayList<Animal>();

	public Car(String number) {
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
		return "Car [animals=" + animals + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Car)) {
			return false;
		}

		Car car = (Car) o;

		return number.equals(car.number);

	}

	@Override
	public int hashCode() {
		return number.hashCode();
	}
}
