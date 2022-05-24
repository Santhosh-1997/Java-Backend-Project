package com.masai.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masai.beans.Cart;
import com.masai.beans.Customer;
import com.masai.beans.Item;
import com.masai.beans.ItemDTO;
import com.masai.beans.Login;
import com.masai.beans.Product;
import com.masai.exception.ProductNotFoundException;
import com.masai.repository.CartCrudRepo;
import com.masai.repository.CustomerCrudRepo;
import com.masai.repository.ProductCrudRepo;
import com.masai.service.CartService;
import com.masai.service.ItemServiceInterface;
import com.masai.service.LoginService;

@RestController
@RequestMapping("/ecommerce/customersPortal")
public class CartController {
	
	@Autowired
	private CustomerCrudRepo customerCrudRepo;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartCrudRepo cartCrudRepo;
	
	@Autowired
	private ItemServiceInterface itemService;
	
	@Autowired
	private ProductCrudRepo productCrudRepo;
	
	@PostMapping(value="/cart")
	public ResponseEntity<Cart> addToCart(@RequestParam String key, @RequestBody ItemDTO item) {
			
			Login loggedUser=loginService.isTokenValid(key);

			Customer customer=customerCrudRepo.findByUserId(loggedUser.getUser().getUserId());
			Integer productId=item.getProductId();
			//System.out.println(productId);
			
			
				Optional<Product> optProduct=productCrudRepo.findById(productId);
				
				if(optProduct.isPresent()) {
					Product product=optProduct.get();
				
					Item savedItem=itemService.addItem(product,item.getRequiredQuantity());
					Cart savedCart=cartService.saveCart(customer, savedItem);
					return new ResponseEntity<>(savedCart, HttpStatus.ACCEPTED);
				}
				else {
				throw new ProductNotFoundException("Product with this Id does not exist");
				}
	}
	
	@GetMapping(value="/cart")
	public  ResponseEntity<List<Item>> viewCart(@RequestParam String key){
		
		Login loggedUser=loginService.isTokenValid(key);

		Customer customer=customerCrudRepo.findByUserId(loggedUser.getUser().getUserId());
		
		Optional<Cart> optloggedCustomerCart=cartCrudRepo.findById(customer.getCart().getCartId());
		
			
			Cart loggedCustomerCart=optloggedCustomerCart.get();
			List<Item> items=cartService.getAllItem(loggedCustomerCart);
			
			return  new ResponseEntity<>(items,HttpStatus.OK);
		
		
		
	}
	
	@PostMapping(value="/cart/alter")
	public ResponseEntity<Cart> alterCart(@RequestParam String key,ItemDTO item){
		
		Login loggedUser=loginService.isTokenValid(key);
		Customer customer=customerCrudRepo.findByUserId(loggedUser.getUser().getUserId());

		return null;
	}
	
	
	//ENDPOINT FOR TESTING
	@PostMapping(value="/cart/removeItem")
	public ResponseEntity<List<Item>> removeItemFromCart(@RequestParam String key, @RequestParam String itemId) {
		int itemIdd = Integer.valueOf(itemId);
		Login loggedUser=loginService.isTokenValid(key);
		List<Item> list = cartService.sendToOrder(loggedUser.getUser().getUserId());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
}
