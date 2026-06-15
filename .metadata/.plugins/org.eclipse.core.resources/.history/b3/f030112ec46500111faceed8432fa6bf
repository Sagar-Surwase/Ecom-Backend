package com.cjc.laptophub.app.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cjc.laptophub.app.model.User;

public interface UserRepositoryI extends JpaRepository<User, Integer>
{

	public User findByEmailAndPassword(String email, String password);
	

}
