package fr.jbrenier.petfoodingcontrol.di.services;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
@interface ServicesScope {
}
