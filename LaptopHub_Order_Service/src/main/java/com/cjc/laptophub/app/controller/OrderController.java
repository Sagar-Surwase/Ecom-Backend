package com.cjc.laptophub.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjc.laptophub.app.model.Order;
import com.cjc.laptophub.app.model.Payment;
import com.cjc.laptophub.app.serviceI.OrderServiceI;

@CrossOrigin("*")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceI osi;

    // Place Order
    @PostMapping("/place/{userId}")
    public ResponseEntity<?> placeOrder(@PathVariable Integer userId)
    {
    	
        try
        {
            Order order = osi.placeOrder(userId);

            return new ResponseEntity<>(order, HttpStatus.CREATED);
        }
        catch(RuntimeException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);  //if unable to place order...
        }
    }

    // View Order by Order ID
    @GetMapping("/{orderId}")
    public ResponseEntity<?> viewOrder(@PathVariable int orderId)
    {
    	
        Order order = osi.viewOrder(orderId);

        if (order != null) 
        {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }

        return new ResponseEntity<>("Order not found.", HttpStatus.NOT_FOUND);
    }

    
    // View Orders of User
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> viewUserOrders(@PathVariable int userId) 
    {
    	
        List<Order> ordersList = osi.viewUserOrders(userId);

        if (!ordersList.isEmpty()) 
        {
            return new ResponseEntity<>(ordersList, HttpStatus.OK);
        }

        return new ResponseEntity<>("No orders found for this user.", HttpStatus.OK);
    }
    
    

    // Cancel Order
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable int orderId) 
    {
    	
        Order order = osi.cancelOrder(orderId);

        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }

        return new ResponseEntity<>("Order not found.", HttpStatus.NOT_FOUND);
    }
    
    
    //Make Payment
    @PostMapping("/payment/{orderId}")
    public ResponseEntity<?> makePayment(@PathVariable int orderId, @RequestBody Payment payment)
    {
        Payment dbPayment = osi.makePayment(orderId, payment);

        if(dbPayment != null)
        {
            return new ResponseEntity<>(dbPayment, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Order not found.", HttpStatus.NOT_FOUND);
    }
    
    
    //View Payment
    @GetMapping("/payment/{orderId}")
    public ResponseEntity<?> viewPayment(@PathVariable int orderId)
    {
        Payment payment = osi.viewPayment(orderId);

        if(payment != null)
        {
            return new ResponseEntity<>(payment, HttpStatus.OK);
        }

        return new ResponseEntity<>("Payment details not found.", HttpStatus.NOT_FOUND);
    }
    
    
}