package com.cjc.laptophub.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cjc.laptophub.app.model.Laptop;
import com.cjc.laptophub.app.serviceI.LaptopServiceI;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@Slf4j
@RestController
@RequestMapping("/product")
public class LaptopController 
{
	
	@Autowired
	LaptopServiceI lsi;
		
	
	@GetMapping("/category/{cname}/laptops")
	public ResponseEntity<?> getCategorywiseLaptop(@PathVariable String cname )
	{
	    System.out.println("Category Received : " + cname);
		
		List<Laptop> allCategoriesedLaptop = lsi.getCategorywiseLaptop(cname);
		
		return new ResponseEntity<>(allCategoriesedLaptop, HttpStatus.OK);  
	}
	
	
	@PostMapping("/saveLaptop")
	public ResponseEntity<?> saveLaptop(@RequestBody Laptop laptop)
	{
		Laptop dbLaptop = lsi.saveLaptop(laptop);
		
		return new ResponseEntity<>(dbLaptop, HttpStatus.CREATED);
	}
	
	
	@PutMapping("/updateLaptop/{lId}")
	public ResponseEntity<?> updateLaptop(@PathVariable int lId, @RequestBody Laptop laptop)
	{
		Laptop updatedLaptop = lsi.updateLaptop(lId, laptop);
		
		return new ResponseEntity<>(updatedLaptop, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteLaptop/{lId}")
	public ResponseEntity<?> deleteLaptop(@PathVariable int lId)
	{
		List<Laptop> remainingLaptop = lsi.deleteLaptop(lId);
		
		if(remainingLaptop!=null)
		{
			return new ResponseEntity<>(remainingLaptop, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("No laptop records found.", HttpStatus.OK);
		}
		
	}
	
	
	
	@GetMapping("/getSingleLaptop/{lId}")
	public ResponseEntity<?> getSingleLaptop(@PathVariable int lId)
	{
		Laptop singleLaptop = lsi.getSingleLaptop(lId);
		
		
		if(singleLaptop!=null)
		{
			return new ResponseEntity<>(singleLaptop, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("No laptop records found.", HttpStatus.OK);
		}

	}
	
	
	@GetMapping("/searchSingleLaptopByName/{lname}")
	public ResponseEntity<?> getSingleLaptopByName(@PathVariable String lname)
	{
		Laptop singleLaptop = lsi.getSingleLaptopByName(lname);
		
		
		if(singleLaptop!=null)
		{
			return new ResponseEntity<>(singleLaptop, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("No laptop records found.", HttpStatus.OK);
		}
		
	}
	
	
	@GetMapping("/viewAllLaptop")
	public ResponseEntity<?> getAllLaptop()
	{
		List<Laptop> AllLaptop = lsi.getAllLaptop();
		
		
		if(AllLaptop!=null)
		{
			return new ResponseEntity<>(AllLaptop, HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("No laptop records found.", HttpStatus.OK);
		}

	}
	
	
	
	@GetMapping("/stock/{lId}")
	public ResponseEntity<?> viewStock(@PathVariable int lId)
	{
	    Laptop laptop = lsi.getSingleLaptop(lId);

	    if(laptop != null)
	    {
	        return new ResponseEntity<>(
	                "Available Stock : " + laptop.getStockQuantity(),
	                HttpStatus.OK);
	    }

	    return new ResponseEntity<>("Laptop not found.", HttpStatus.NOT_FOUND);
	}
	
	
	
	@PutMapping("/updateStock/{lId}/{stockQuantity}")
	public ResponseEntity<?> updateStock(@PathVariable int lId,
	                                     @PathVariable int stockQuantity)
	{
	    Laptop laptop = lsi.updateStock(lId, stockQuantity);

	    if(laptop != null)
	    {
	        return new ResponseEntity<>(laptop, HttpStatus.OK);
	    }

	    return new ResponseEntity<>("Laptop not found.",
	            HttpStatus.NOT_FOUND);
	}
	
	
	@PutMapping("/reduceStock/{lId}/{quantity}")
	public ResponseEntity<?> reduceStock(@PathVariable int lId, @PathVariable int quantity)
	{
	    Laptop laptop = lsi.reduceStock(lId, quantity);

	    if(laptop != null)
	    {
	        return new ResponseEntity<>(laptop, HttpStatus.OK);
	    }

	    return new ResponseEntity<>("Insufficient stock or laptop not found.", HttpStatus.BAD_REQUEST);
	}
}
