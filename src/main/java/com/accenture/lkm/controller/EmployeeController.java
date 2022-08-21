package com.accenture.lkm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.accenture.lkm.business.bean.EmployeeBean;
import com.accenture.lkm.service.EmployeeService;

/** all method of the Controller will return a publisher
either Mono or Flux*/
@RestController
public class EmployeeController {
 
	private EmployeeService employeeService;

	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}
	/**
	 * findAll() of the EmployeeService is invoked.
	 * There is no need to subscribe to the publisher,
	 * as subscription will be done by the front End controller.
	 * In web flux the name of the front end controller is DispatcherHandler.
	 * */
	@GetMapping(value="/employees")
    public Flux<EmployeeBean> getAllEmployees() {
        return employeeService.findAll();
    }	
	
	/** Above  method Flux<EmployeeBean> getAllEmployees() 
	 * will return a Empty mono  if nothing is found, 
	 * but it is appropriate to return an HTTP status also
	 * hence below method implementation is used.
	 * Here PathVariable is retrieved and findById() of the EmployeeService is invoked
	 * by passing the obtained @PathVariable.
	 * map converts Mono of Employee type to other Mono of ResponseEntity type
	 * if converted mono is empty then defaultIfEmpty is invoked and NotFound result is returned.
	 * There is no need to subscribe to the publisher,
	 * as subscription will be done by the front End controller.
	 * In web flux the name of the front end controller is DispatcherHandler.
	 * */
	@GetMapping(value="/employee/{employeeId}")
    public Mono<ResponseEntity<EmployeeBean>> getEmployeesById(@PathVariable("employeeId")Integer employeeId) {
        return employeeService.findById(employeeId)
        		.map(emp -> ResponseEntity.ok(emp)) 
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
	
	/** Request body is obtained by using the @RequestBody.
	 *  save() of EmployeeService is invoked by passing the obtained RequestBody which in turn 
	 *  returns the publisher of type Mono<EmployeeBean>.
	 *  map converts Mono<EmployeeBean> to other Mono<ResponseEntity<String>>. 
	 *  Converted publisher is sent by the method.
	 *  There is no need to subscribe to the publisher,
	 *  as subscription will be done by the front End controller.
	 *  In web flux the name of the front end controller is DispatcherHandler.  
	 * */
	@RequestMapping(value="saveEmployee",method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> saveEmployee(@RequestBody EmployeeBean employee) {
        return employeeService.save(employee)
        		.map(emp -> 
        			new ResponseEntity<String>("Employee created with Id: "+emp.getEmployeeId(),
        					HttpStatus.CREATED)
        			
        );
    }
	
	/** Request body is obtained by using the @RequestBody.
	 *  findById() of the EmployeeService is invoked by passing the Id from the
	 *  RequestBody obtained above which is turn returns the Mono<EmployeeBean>.
	 *  
	 *  On the found Mono<EmployeeBean> save() of EmployeeService is invoked 
	 *  by passing the updating the found Mono<EmployeeBean> with RequestBody data. 
	 *  This will in turn return the publisher of type Mono<EmployeeBean>.
	 *  To chain the data from the findById() to save() operation, flatMap() is used,
	 *  as save method will return Mono<EmployeeBean>, 
	 *  where as source of chaining findById() is also Giving Mono<EmployeeBean>.
	 *  otherwise if used map operator to chain then it will Give Mono<Mono<EmployeeBean>>,
	 *  which will be wrong.
	 *  
	 *  
	 *  After obtaining the data with the flatMap(),
	 *  map() converts Mono<EmployeeBean> to other Mono<ResponseEntity<EmployeeBean>>. 
	 *  Converted publisher is sent by the method.
	 *  If converted mono is empty then defaultIfEmpty is invoked 
	 *  and NotFound result is returned.
	 *  There is no need to subscribe to the publisher,
	 *  as subscription will be done by the front End controller.
	 *  In web flux the name of the front end controller is DispatcherHandler.  
	 * */
	@PutMapping(value="updateEmployee",consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<EmployeeBean>> updateEmployee(@RequestBody EmployeeBean employee) {
        return employeeService.findById(employee.getEmployeeId())
        		
        		.flatMap(redeemedExistingEmp->{
			        	redeemedExistingEmp.setDepartmentCode(employee.getDepartmentCode());
			        	redeemedExistingEmp.setSalary(employee.getSalary());
			        	redeemedExistingEmp.setEmployeeName(employee.getEmployeeName());
			        	return employeeService.save(redeemedExistingEmp);
        }).
        map(updatedEmp -> ResponseEntity.ok(updatedEmp))
        .defaultIfEmpty(ResponseEntity.notFound().build());
    }

	
	/** PathVariable is obtained by using the @PathVariable.
	 *  findById() of the EmployeeService is invoked by passing the Id from the
	 *  @PathVariable obtained above which is turn returns the Mono<EmployeeBean>.
	 *  
	 *  On the found Mono<EmployeeBean> delete() of EmployeeService is invoked 
	 *  by passing the found Mono<EmployeeBean>.
	 *   
	 *  This will in turn returns the publisher of type Mono<EmployeeBean>.
	 *  To chain the data from the findById() to delete() operation, flatMap() is used,
	 *  as delete method will return Mono<Void>, 
	 *  where as source of chaining findById() is also Giving Mono<EmployeeBean>.
	 *  otherwise if used map operator to chain then it will Give Mono<Mono<Void>>,
	 *  which will be wrong.
	 *  
	 *  Note here inside of the flatMap(), then() is also used. 
	 *  This then() will execute something when the delete() operation is successful and in stead
	 *  of returning void, it will return Mono<EmployeeBean>.
	 *  
	 *  After obtaining the data with the flatMap(),
	 *  map() converts Mono<EmployeeBean> to other Mono<ResponseEntity<EmployeeBean>>. 
	 *  Converted publisher is sent by the method.
	 *  If converted mono is empty then defaultIfEmpty is invoked 
	 *  and NotFound result is returned.
	 *  There is no need to subscribe to the publisher,
	 *  as subscription will be done by the front End controller.
	 *  In web flux the name of the front end controller is DispatcherHandler.  
	 * */
	@DeleteMapping(value="/employee/{employeeId}",produces=MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<EmployeeBean>> deleteEmployee(@PathVariable("employeeId")Integer employeeId) {
        return employeeService.findById(employeeId)
        		.flatMap(
        				rdeemedexistingEmp->employeeService.delete(rdeemedexistingEmp)
        				.then(Mono.just(ResponseEntity.ok(rdeemedexistingEmp)))
        				)		
                .defaultIfEmpty(ResponseEntity.notFound().build());
        		
    }
	
}
/**Common Note:
 * When ever current operation has to be chained to other operation
 * which in turn is returning an other publisher,
 * but needed output should be a one single dimensional publisher  
 * (not a publisher which is publisher of publishers),
 * Then to have values of both publisher flatMap() is used.
 * As normal map cannot do flattening, hence normal map cannot be used
 * defaultIfEmpty requires a raw value that will be converted to publisher
 * switchIfEmpty requires a publisher only 
*/