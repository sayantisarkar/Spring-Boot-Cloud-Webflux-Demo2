package com.accenture.lkm.business.bean;


public class EmployeeBean {
	
    private Integer employeeId;

	
    private String employeeName;

    private Double salary;

    
    private String departmentCode;

	public EmployeeBean(Integer employeeId, String employeeName, Double salary,
			String departmentCode) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.salary = salary;
		this.departmentCode = departmentCode;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeName="
				+ employeeName + ", salary=" + salary + ", departmentCode="
				+ departmentCode + "]";
	}

	public EmployeeBean() {
	}
    

    
    

}
