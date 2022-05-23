package com.masai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masai.beans.Customer;
import com.masai.beans.Login;
import com.masai.beans.Product;
import com.masai.beans.Seller;
import com.masai.beans.UserDTO;
import com.masai.service.LoginServiceInterface;
import com.masai.service.SellerService;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/ecommerce/sellerPortal")
public class SellerController {
	
	@Autowired
	private SellerService sellerService;
	
	@Autowired 
	private LoginServiceInterface loginService;
	
	// Handle		 --> /ecommerce/sellerPortal/seller
	// What is does? --> Adds new seller
	// Request Type? --> Post Request
	// Input 		 --> Seller object
	@PostMapping("/seller")
	public ResponseEntity<Seller> addSeller(@RequestBody @Valid Seller seller, HttpSession session) {
		Seller addedSeller = sellerService.addSeller(seller);
		session.setAttribute("SellerData", seller);
		return new ResponseEntity(addedSeller, HttpStatus.CREATED);
	}
	
	// Handle		 --> /ecommerce/sellerPortal/seller/{id}
	// What is does? --> Updates the fields provided in the userInfo (any field except userId can be updated)
	// Request Type? --> Put Request
	// Input 		 --> UserDTO object (All fields allowed) and Id in the path variable 
	@PutMapping("/seller")
	public ResponseEntity<String> updateCustomer(@RequestBody @Valid UserDTO userInfo, @RequestParam String key) {
		Login currentLogin = loginService.isTokenValid(key);
		Seller updatedSeller = sellerService.updateSeller(userInfo, currentLogin.getUser().getUserId());
		return new ResponseEntity(updatedSeller, HttpStatus.OK);
	}
	
	
	// Handle		 --> /ecommerce/sellerPortal/seller/{sellerId}
	// What is does? --> Deletes seller
	// Request Type? --> Delete Request
	// Input 		 --> UserDTO object (All fields allowed) and Id in the path variable 
	@DeleteMapping("/seller")
	public ResponseEntity<Seller> removeSeller(@RequestBody @Valid UserDTO userInfo, Integer sellerId) {
		String deleteSeller = sellerService.removeSeller(userInfo);
		return new ResponseEntity(deleteSeller, HttpStatus.ACCEPTED);
	}
	
	
	// Handle		 --> /ecommerce/sellerPortal/seller
	// What is does? --> View all seller
	// Request Type? --> view Request
	// Input 		 --> nothing
	@GetMapping("/seller")
	public ResponseEntity<Seller> viewSellers() {
		List<Seller> sellerList = sellerService.viewAllSeller();
		return new ResponseEntity(sellerList, HttpStatus.FOUND);
	}
	
	// Handle		 --> /ecommerce/sellerPortal/seller
	// What is does? --> Adds product
	// Request Type? --> Post Request
	// Input 		 --> Product Object
	@PostMapping("/seller/addProduct")
	public ResponseEntity<Seller> addSeller(@RequestParam String key, @RequestBody Product product) {
		Login currentLogin = loginService.isTokenValid(key);
		Seller addedProduct = sellerService.addProducts(currentLogin.getUser().getUserId(), product);
		return new ResponseEntity(addedProduct, HttpStatus.CREATED);
	}
	
}
