package com.cjc.laptophub.app.repositoryI;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cjc.laptophub.app.model.Cart;

@Repository
public interface CartRepositoryI extends JpaRepository<Cart, Integer>
{

	public Optional<Cart> findByUserId(int userId);
	

}
