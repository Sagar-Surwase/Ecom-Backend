package com.cjc.laptophub.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjc.laptophub.app.model.Cart;
import com.cjc.laptophub.app.serviceI.CartServiceI;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@Slf4j
@RequestMapping("/cart")
@RestController
public class CartContoller 
{
	
	@Autowired
	CartServiceI csi;
	
	
	@PostMapping("/cart/add/{userId}/{laptopId}/{quantity}")
	public ResponseEntity<?> addToCart(@PathVariable int userId, @PathVariable int laptopId, @PathVariable int quantity)
	{
		Cart cartWithItem = csi.addToCart(userId,laptopId, quantity);
		
		return new ResponseEntity<>(cartWithItem, HttpStatus.OK);
	}
	
	
	@GetMapping("/viewCart/{userId}")
	public ResponseEntity<?> viewCart(@PathVariable int userId)
	{
		Cart cart = csi.viewCart(userId);
		
		if(cart != null)
		{
			return new ResponseEntity<>(cart, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("Looks like your cart is empty.", HttpStatus.OK);
		}
	}
	
	
	
	@PutMapping("/updateQuantity/{userId}/{cartItemId}/{quantity}")
	public ResponseEntity<?> updateQuantity(@PathVariable int userId, @PathVariable int cartItemId, @PathVariable int quantity)
	{
	    Cart updatedCart = csi.updateQuantity(userId, cartItemId, quantity);

	    if(updatedCart != null)
	    {
	        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
	    }
	    else
	    {
	        return new ResponseEntity<>("Cart item not found.", HttpStatus.NOT_FOUND);
	    }
	}
	
	
	
	//single cart item deleted from cart 
	@DeleteMapping("/deleteCartItem/{userId}/{cartItemId}")
	public ResponseEntity<?> deleteCartItem(@PathVariable int userId, @PathVariable int cartItemId)
	{
	    Cart updatedCart = csi.deleteCartItem(userId, cartItemId);

	    if(updatedCart != null)
	    {
	        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
	    }
	    else
	    {
	        return new ResponseEntity<>("Cart item not found.", HttpStatus.NOT_FOUND);
	    }
	}
	
	
	//Whole cart item deleted from cart whenever they get added in order
	@DeleteMapping("/deleteCartItem/{userId}")
	public ResponseEntity<?> ClearWholeCart(@PathVariable int userId)
	{
	    Cart updatedEmptyCart = csi.ClearWholeCart(userId);

	    if(updatedEmptyCart != null)
	    {
	        return new ResponseEntity<>("Cart cleared successfully.", HttpStatus.OK);
	    }
	    else
	    {
	        return new ResponseEntity<>("Cart not found.", HttpStatus.NOT_FOUND);
	    }
	}
	
	
	

}
