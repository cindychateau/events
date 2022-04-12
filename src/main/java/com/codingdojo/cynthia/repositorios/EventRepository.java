package com.codingdojo.cynthia.repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.cynthia.modelos.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>{
	
}
