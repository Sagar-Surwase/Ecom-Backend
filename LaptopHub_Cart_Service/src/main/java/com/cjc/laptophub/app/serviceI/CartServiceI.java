package com.cjc.laptophub.app.serviceI;

import com.cjc.laptophub.app.model.Cart;

public interface CartServiceI 
{

	public Cart addToCart(int userId, int laptopId, int quantity);

}
