package com.accenture.lkm.dao;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import com.accenture.lkm.business.bean.EmployeeBean;
import com.accenture.lkm.entity.EmployeeEntity;


@Service
public class EmployeeDAOWrapper {
	private Scheduler employeeDAOThread = Schedulers.newParallel("employeeDAOWrapperThreads", 4);
	private EmployeeRepository employeeRepository;
	
	@Autowired
	public EmployeeDAOWrapper(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}

	public Flux<EmployeeBean> findAll() {
		return employeeRepository.findAll().publishOn(employeeDAOThread)
				.map(entity->convertEntityToBean(entity)); 	
	}
	
	public Mono<EmployeeBean> findById(Integer employeeId) {
		return employeeRepository.findById(employeeId).publishOn(employeeDAOThread)
				.map(entity->convertEntityToBean(entity));
	}

	public Mono<EmployeeBean> save(EmployeeBean employee) {
		return employeeRepository.save(convertBeanToEntity(employee)).publishOn(employeeDAOThread)
				.map(entity->convertEntityToBean(entity));
	}
	
	
	public Mono<Void> delete(EmployeeBean employee) {
		
		return employeeRepository
				.delete(convertBeanToEntity(employee))
				.publishOn(employeeDAOThread);
	}


	public EmployeeBean convertEntityToBean(EmployeeEntity employeeEntity){
		EmployeeBean bean = new EmployeeBean();
		System.out.println("Thread Name: "+Thread.currentThread().getName());
		BeanUtils.copyProperties(employeeEntity, bean);
		return bean;
	}
	
	public EmployeeEntity convertBeanToEntity(EmployeeBean employeeBean){
		EmployeeEntity employeeEntity = new EmployeeEntity();
		System.out.println("Thread Name: "+Thread.currentThread().getName());
		BeanUtils.copyProperties(employeeBean, employeeEntity);
		return employeeEntity;
	}


	
	
	

}
