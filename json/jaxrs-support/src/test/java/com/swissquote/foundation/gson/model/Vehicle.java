package com.swissquote.foundation.gson.model;

import com.swissquote.foundation.soa.support.api.annotations.GsonPolymorphic;

@GsonPolymorphic(typeField = "TYPE")
public interface Vehicle {

	String getNumber();

	String showPassangers();

	void addPassanger(Animal animal);
}
