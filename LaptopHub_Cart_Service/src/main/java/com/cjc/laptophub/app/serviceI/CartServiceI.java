package com.cjc.laptophub.app.serviceI;

import com.cjc.laptophub.app.model.Cart;

public interface CartServiceI 
{

	public Cart addToCart(int userId, int laptopId, int quantity);

	public Cart viewCart(int userId);

	public Cart deleteCartItem(int userId, int cartItemId);

	public Cart updateQuantity(int userId, int cartItemId, int quantity);

	public Cart ClearWholeCart(int userId);

}
