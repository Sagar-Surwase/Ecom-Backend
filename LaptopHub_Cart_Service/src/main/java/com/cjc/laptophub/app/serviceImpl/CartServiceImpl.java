package com.cjc.laptophub.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cjc.laptophub.app.model.Cart;
import com.cjc.laptophub.app.model.CartItem;
import com.cjc.laptophub.app.model.Laptop;
import com.cjc.laptophub.app.repositoryI.CartRepositoryI;
import com.cjc.laptophub.app.serviceI.CartServiceI;

@Service
public class CartServiceImpl implements CartServiceI 
{
	@Autowired
	RestTemplate rt;
	
	
	@Autowired
	CartRepositoryI crepo;
	
	
	public Cart createCartItem(Cart cart, int laptopId, int quantity)
	{
	    // Fetch laptop details from Product-Laptop-Service
	    Laptop laptop = rt.getForObject("http://localhost:9092/product/getSingleLaptop/" + laptopId, Laptop.class);

	    
	    // Set cart creation time only for new carts
	    if (cart.getCreatedAt() == null)
	    {
	        cart.setCreatedAt(LocalDateTime.now());
	    }

	    // Initialize cart item list if it is null
	    if (cart.getCartItem() == null)
	    {
	        cart.setCartItem(new ArrayList<>());
	    }

	    // Create a new cart item
	    CartItem cartItem = new CartItem();

	    cartItem.setLaptopId(laptop.getLId());
	    cartItem.setQuantity(quantity);
	    cartItem.setUnitPrice(laptop.getPrice());
	    cartItem.setSubtotal(laptop.getPrice() * quantity);

	    // Add item to cart
	    cart.getCartItem().add(cartItem);

	    return cart;
	}
	
	
	@Override
	public Cart addToCart(int userId, int laptopId, int quantity)
	{
	    Optional<Cart> cart = crepo.findByUserId(userId);

	    if(cart.isPresent())
	    {
	        Cart existingCart = cart.get();				 // Existing user: get the existing cart from DB
 
	        Cart updatedCart = createCartItem(existingCart, laptopId, quantity);

	        return crepo.save(updatedCart);
	    }
	    else
	    {
	        Cart newCart = new Cart();					// New user: create a new cart

	        newCart.setUserId(userId);

	        Cart updatedCart = createCartItem(newCart, laptopId, quantity);

	        return crepo.save(updatedCart);
	    }
	}



	@Override
	public Cart viewCart(int userId)
	{
		Optional<Cart> cart = crepo.findByUserId(userId);
		
		if(cart.isPresent())
		{
			Cart dbCart= cart.get();
			
			return dbCart;
		}
		else
		{
			return null;
		}
	}





	@Override
	public Cart deleteCartItem(int userId, int cartItemId) 
	{
		Optional<Cart> cart = crepo.findByUserId(userId);
		
		if(cart.isPresent())
		{
			Cart dbCart = cart.get();
			
			List<CartItem> cartItemList = dbCart.getCartItem();
			
			CartItem toDeleteItem = null;
			
			for(CartItem item : cartItemList)
			{
				if(item.getCartItemId() == cartItemId)
				{
					toDeleteItem = item;
				}
			}
			
			if(toDeleteItem == null)
			{
				return null;
			}
			else
			{
				cartItemList.remove(toDeleteItem);
				
				dbCart.setCartItem(cartItemList);
				
				Cart updatedCart = crepo.save(dbCart);
				
				return updatedCart;
			}
			
		}
		else
		{
			return null;
		}
	}





	@Override
	public Cart updateQuantity(int userId, int cartItemId, int quantity)
	{
	    Optional<Cart> cart = crepo.findByUserId(userId);

	    if(cart.isPresent())
	    {
	        Cart dbCart = cart.get();

	        List<CartItem> cartItemList = dbCart.getCartItem();

	        for(CartItem item : cartItemList)
	        {
	            if(item.getCartItemId() == cartItemId)
	            {
	                // Update quantity
	                item.setQuantity(quantity);

	                // Recalculate subtotal
	                item.setSubtotal(item.getUnitPrice() * quantity);

	                return crepo.save(dbCart);
	                
	            }
	        }
	    }

	    return null;
	}


	@Override
	public Cart ClearWholeCart(int userId) 
	{
		Optional<Cart> cart = crepo.findByUserId(userId);
		
		if(cart.isPresent())
		{
			Cart dbCart = cart.get();
			
			dbCart.getCartItem().clear();
			
			Cart updatedEmptyCart = crepo.save(dbCart);
			
			return updatedEmptyCart;
			
		}
		
		return null;
	}

}
