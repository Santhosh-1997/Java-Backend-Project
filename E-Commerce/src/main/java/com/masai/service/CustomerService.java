package com.masai.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.beans.Customer;
import com.masai.exception.CustomerAlreadyExistsException;
import com.masai.repository.CustomerCrudRepo;

@Service
public class CustomerService implements CustomerServiceInterface {
	
	@Autowired
	private CustomerCrudRepo customerCrudRepo;
	
	@Override
	public Customer addCustomer(Customer customer) {
		Optional<Customer> opt = customerCrudRepo.findByUserName(customer.getUserName());
		
		if(opt.isEmpty()) {
			Customer savedCustomer = customerCrudRepo.save(customer);
			return savedCustomer;
		} else {
			throw new CustomerAlreadyExistsException("Customer with the given username already exists.");
		}
	}

	@Override
	public Customer removeCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> viewAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer updateCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//Todo
	//Add methods to find customers by different parameters
}
