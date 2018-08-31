package com.swissquote.foundation.gson;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.reflect.TypeToken;
import com.swissquote.foundation.gson.model.Animal;
import com.swissquote.foundation.gson.model.Car;
import com.swissquote.foundation.gson.model.Garage;
import com.swissquote.foundation.gson.model.Truck;
import com.swissquote.foundation.gson.model.Vehicle;
import com.swissquote.foundation.gson.model.impl.Cat;
import com.swissquote.foundation.gson.model.impl.Dog;
import com.swissquote.foundation.soa.gson.GsonProvider;

public class GsonProviderTest {
	private static final Logger logger = LoggerFactory.getLogger(GsonProviderTest.class);

	private GsonProvider writeGsonProvider;

	private GsonProvider readGsonProvider;

	@Before
	public void initTest() {

		Set<String> packages = Collections.singleton(Vehicle.class.getPackage().getName());

		writeGsonProvider = new GsonProvider();
		readGsonProvider = new GsonProvider();
	}

	@Test
	public void test() {
		MediaType.valueOf("application/json;charset=utf-8");
	}

	@Test
	public void rootPolymorphicClassTest() throws IOException {
		Car car = new Car("AA1234AB");
		Cat cat = new Cat("Murka");
		Dog dog = new Dog("Palcan", 5);
		car.addPassanger(cat);
		car.addPassanger(dog);

		Object obj = processByGsonProvider(car, Vehicle.class);

		Assert.assertTrue(obj instanceof Car);
		Car newCar = (Car) obj;
		List<Animal> animals = newCar.getAnimals();
		Assert.assertNotNull(animals);
		assertEquals(car, newCar);
		Assert.assertTrue(animals.equals(Arrays.asList(cat, dog)));
	}

	@Test
	public void fieldPolymorphicClassTest() throws IOException {
		Garage garage = new Garage("Krasnogo komsomola square, 7");
		Car firstCar = new Car("AA1234AB");
		Truck secondTruk = new Truck("AA1235AB");
		List<Vehicle> garageVehicles = Arrays.asList(firstCar, secondTruk);
		garage.setVehicles(garageVehicles);

		Object res = processByGsonProvider(garage, garage.getClass());

		Assert.assertTrue(res instanceof Garage);
		Garage newGarage = (Garage) res;
		List<Vehicle> vehicles = newGarage.getVehicles();
		assertEquals(garage, newGarage);
		Assert.assertNotNull(vehicles);
		Assert.assertTrue(vehicles.equals(garageVehicles));
	}

	@Test
	public void genericPolymorphicClassTest() throws IOException {
		List<Animal> homePets = new ArrayList<Animal>();
		Cat cat = new Cat("Tom");
		Dog dog = new Dog("Spike", 10);
		homePets.addAll(Arrays.asList(cat, dog));

		Object res = processByGsonProvider(homePets, new TypeToken<List<Animal>>() {
		}.getType());

		Assert.assertTrue(res instanceof List);
		List newHomePets = (List) res;
		Assert.assertTrue(newHomePets.equals(homePets));
	}

	@Test
	public void standard_string() throws IOException {

		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream(inputStream);
		MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<>();

		writeGsonProvider.writeTo("nټrd", String.class, String.class, null, //
				MediaType.APPLICATION_JSON_TYPE, httpHeaders, outputStream);
		outputStream.close();
		String json = fetchJsonFromInputStream(inputStream);

		assertEquals(1, httpHeaders.size());
	}

	private Object processByGsonProvider(Object object, Type type) throws IOException {
		PipedInputStream inputStream = new PipedInputStream();
		PipedOutputStream outputStream = new PipedOutputStream(inputStream);
		MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<>();

		writeGsonProvider.writeTo(object, object.getClass(), type, null, //
				MediaType.APPLICATION_JSON_TYPE, httpHeaders, outputStream);
		outputStream.close();
		String json = fetchJsonFromInputStream(inputStream);

		assertEquals(1, httpHeaders.size());

		logger.info("Desired type: " + type + ". Generated json: " + json);

		return readGsonProvider.readFrom(Object.class, type, null, //
				MediaType.APPLICATION_JSON_TYPE, null, new ByteArrayInputStream(json.getBytes()));
	}

	private String fetchJsonFromInputStream(InputStream stream) {
		java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
