package com.accenture.lkm.service;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.accenture.lkm.business.bean.EmployeeBean;
import com.accenture.lkm.dao.EmployeeDAOWrapper;

@Service
public class EmployeeService {
	private EmployeeDAOWrapper daoWrapper;

	public EmployeeService(EmployeeDAOWrapper daoWrapper) {
		this.daoWrapper = daoWrapper;
	}

	public Flux<EmployeeBean> findAll() {
		return daoWrapper.findAll(); 	
	}

	public Mono<EmployeeBean> findById(Integer employeeId) {
		return daoWrapper.findById(employeeId);
	}

	public Mono<EmployeeBean> save(EmployeeBean employee) {

		return daoWrapper.save(employee);
	}

	public Mono<Void> delete(EmployeeBean employee) {
		
		return daoWrapper.delete(employee);
	}

}
