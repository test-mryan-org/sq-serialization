package com.swissquote.foundation.gson.model;

import java.util.ArrayList;
import java.util.List;

public class Garage {

	private final String address;
	private final List<Vehicle> vehicles = new ArrayList<Vehicle>();

	public Garage(String address) {
		this.address = address;
	}

	public List<Vehicle> getVehicles() {
		return new ArrayList<Vehicle>(vehicles);
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles.clear();
		this.vehicles.addAll(vehicles);
	}

	public String getAddress() {
		return address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Garage)) {
			return false;
		}

		Garage garage = (Garage) o;

		return address.equals(garage.address);

	}

	@Override
	public int hashCode() {
		return address.hashCode();
	}
}
