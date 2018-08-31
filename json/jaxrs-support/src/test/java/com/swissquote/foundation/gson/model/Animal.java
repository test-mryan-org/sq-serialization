package com.swissquote.foundation.gson.model;

import com.swissquote.foundation.soa.support.api.annotations.GsonPolymorphic;

@GsonPolymorphic
public interface Animal {
	String makeSound();
}
