package com.accenture.lkm.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.accenture.lkm.entity.EmployeeEntity;


public interface EmployeeRepository
        extends ReactiveCrudRepository<EmployeeEntity, Integer> {
	
}
//https://spring.io/guides/gs/accessing-data-r2dbc/