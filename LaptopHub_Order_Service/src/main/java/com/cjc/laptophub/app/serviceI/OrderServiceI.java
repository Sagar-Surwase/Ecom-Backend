package com.cjc.laptophub.app.serviceI;

import java.util.List;

import com.cjc.laptophub.app.model.Order;
import com.cjc.laptophub.app.model.Payment;

public interface OrderServiceI
{
    public Order placeOrder(int userId);

    public Order viewOrder(int orderId);

    public List<Order> viewUserOrders(int userId);

    public Order cancelOrder(int orderId);

	public Payment makePayment(int orderId, Payment payment);

	public Payment viewPayment(int orderId);
}