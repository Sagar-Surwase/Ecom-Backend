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
import com.cjc.laptophub.app.model.Order;
import com.cjc.laptophub.app.model.OrderItem;
import com.cjc.laptophub.app.model.Payment;
import com.cjc.laptophub.app.repositoryI.OrderRepositoryI;
import com.cjc.laptophub.app.serviceI.OrderServiceI;

@Service
public class OrderServiceImpl implements OrderServiceI
{
	@Autowired
	RestTemplate rt1;
	
	@Autowired
	OrderRepositoryI orepo;	
	
	
	@Override
	public Order placeOrder(int userId)
	{
	    // Fetch user's cart from Cart Service
	    Cart cart = rt1.getForObject("http://localhost:9093/cart/viewCart/" + userId, Cart.class);
	    

	    // Check whether cart exists and contains items
	    if(cart == null || cart.getCartItem() == null || cart.getCartItem().isEmpty())
	    {
	        throw new RuntimeException("Cart is empty");
	    }	    
	    

	    Order order = new Order();

	    order.setUserId(userId);

	    // Generate order number
	    order.setOrderNumber("ORD" + System.currentTimeMillis());

	    order.setOrderStatus("PENDING");

	    order.setOrderDate(LocalDateTime.now());

	    List<OrderItem> orderItemsList = new ArrayList<>();

	    double totalAmount = 0.0;

	    
	 // Convert CartItems to OrderItems
	    for(CartItem cartItem : cart.getCartItem())
	    {
	        // Fetch laptop details
	        Laptop laptop = rt1.getForObject("http://localhost:9092/product/getSingleLaptop/" + cartItem.getLaptopId(), Laptop.class);

	        if(laptop == null)
	        {
	            throw new RuntimeException("Laptop not found for id: " + cartItem.getLaptopId());
	        }

	        // Check stock availability
	        if(laptop.getStockQuantity() < cartItem.getQuantity())
	        {
	            // Not enough stock available
	            throw new RuntimeException("Insufficient stock for laptop: " + laptop.getLname());
	        }

	        // Reduce stock in Product Service
	        rt1.put("http://localhost:9092/product/reduceStock/" + cartItem.getLaptopId()+ "/"+ cartItem.getQuantity(), null);

	        // Create OrderItem
	        OrderItem orderItem = new OrderItem();

	        orderItem.setLaptopId(cartItem.getLaptopId());

	        orderItem.setLaptopName(laptop.getLname());

	        orderItem.setQuantity(cartItem.getQuantity());

	        orderItem.setUnitPrice(cartItem.getUnitPrice());

	        orderItem.setSubtotal(cartItem.getSubtotal());

	        totalAmount = totalAmount + cartItem.getSubtotal();

	        orderItemsList.add(orderItem);
	    }

	    order.setOrderItems(orderItemsList);

	    order.setTotalAmount(totalAmount);

	    // Save Order
	    Order savedOrder = orepo.save(order);

	    // Clear user's cart after successful order placement
	    rt1.delete("http://localhost:9093/cart/deleteCartItem/" + userId);

	    return savedOrder;
	}


	@Override
	public Order viewOrder(int orderId) 
	{
		Optional<Order> order = orepo.findById(orderId);
		
		if(order.isPresent())
		{
			Order dbOrder = order.get();
			
			return dbOrder;
			
		}
		
		return null;
	}


	@Override
	public List<Order> viewUserOrders(int userId)
	{
	    List<Order> ordersList = orepo.findByUserId(userId);

	    if(!ordersList.isEmpty())
	    {
	        return ordersList;
	    }

	    return null;
	}

	@Override
	public Order cancelOrder(int orderId) 
	{
		Optional<Order> order = orepo.findById(orderId);
		
		if(order.isPresent())
		{
			Order dbOrder = order.get();
			
	        dbOrder.setOrderStatus("CANCELLED");

	        return orepo.save(dbOrder);
			
		}
		
		return null;
	}

	
	@Override
	public Payment makePayment(int orderId, Payment payment)
	{
	    Optional<Order> order = orepo.findById(orderId);

	    if(order.isPresent())
	    {
	        Order dbOrder = order.get();

	        payment.setOrderId(orderId);

	        payment.setAmount(dbOrder.getTotalAmount());

	        payment.setPaymentStatus("SUCCESS");

	        payment.setTransactionId("TXN" + System.currentTimeMillis());

	        payment.setPaidAt(LocalDateTime.now());

	        Payment savedPayment = orepo.save(payment);

	        dbOrder.setPayment(savedPayment);

	        dbOrder.setOrderStatus("CONFIRMED");

	        orepo.save(dbOrder);

	        return savedPayment;
	    }

	    return null;
	}
	
	
	
	@Override
	public Payment viewPayment(int orderId)
	{
	    Optional<Order> order = orepo.findById(orderId);

	    if(order.isPresent())
	    {
	        Order dbOrder = order.get();

	        return dbOrder.getPayment();
	    }

	    return null;
	}
	
}
