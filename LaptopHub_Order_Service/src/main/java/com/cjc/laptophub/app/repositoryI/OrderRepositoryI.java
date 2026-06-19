package com.cjc.laptophub.app.repositoryI;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjc.laptophub.app.model.Order;
import com.cjc.laptophub.app.model.Payment;


@Repository
public interface OrderRepositoryI extends JpaRepository<Order, Integer>
{

	public List<Order> findByUserId(int userId);

	public Payment save(Payment payment);

}
