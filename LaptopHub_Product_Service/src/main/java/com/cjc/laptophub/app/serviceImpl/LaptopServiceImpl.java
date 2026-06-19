package com.cjc.laptophub.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjc.laptophub.app.model.Category;
import com.cjc.laptophub.app.model.Laptop;
import com.cjc.laptophub.app.repositoryI.LaptopRepositoryI;
import com.cjc.laptophub.app.serviceI.LaptopServiceI;

@Service
public class LaptopServiceImpl implements LaptopServiceI
{
	
	@Autowired
	LaptopRepositoryI lrepo;

	@Override
	public List<Laptop> getCategorywiseLaptop(String cname) 
	{
		
		List<Laptop> categoryWiseAllLaptop = new ArrayList<Laptop>();
		
		List<Laptop> allList = lrepo.findAll();
		
		for(Laptop laptop : allList)
		{
			String dbCname = laptop.getCategory().getCname();
			
			if(dbCname.equalsIgnoreCase(cname))
			{
				categoryWiseAllLaptop.add(laptop);
			}
			
		}
		
		return categoryWiseAllLaptop;
	}

	
	@Override
	public Laptop saveLaptop(Laptop laptop) 
	{
		if(laptop.getStockQuantity() > 0)
		{
			laptop.setStatus("AVAILABLE");
		}
		else
		{
		    laptop.setStatus("OUT_OF_STOCK");
		}
			
		Laptop dbLaptop = lrepo.save(laptop);
		
		return dbLaptop;
	}

	@Override
	public Laptop updateLaptop(int lId, Laptop laptop) 
	{
		Optional<Laptop> data = lrepo.findById(lId);
		
		if(data.isPresent()) 
		{
			Laptop dbLaptop = data.get();
			
			dbLaptop.setLname(laptop.getLname());
			dbLaptop.setPrice(laptop.getPrice());
			dbLaptop.setStockQuantity(laptop.getStockQuantity());

			if(dbLaptop.getStockQuantity() > 0)
			{
			    dbLaptop.setStatus("AVAILABLE");
			}
			else
			{
			    dbLaptop.setStatus("OUT_OF_STOCK");
			}			
			
			dbLaptop.setStatus(laptop.getStatus());
			dbLaptop.setProcessor(laptop.getProcessor());
			dbLaptop.setRam(laptop.getRam());
			dbLaptop.setStorage(laptop.getStorage());
			dbLaptop.setGpu(laptop.getGpu());
			dbLaptop.setDisplaySize(laptop.getDisplaySize());
			dbLaptop.setDisplayResolution(laptop.getDisplayResolution());
			dbLaptop.setBattery(laptop.getBattery());
			dbLaptop.setOperatingSystem(laptop.getOperatingSystem());
			dbLaptop.setWeight(laptop.getWeight());
			dbLaptop.setColor(laptop.getColor());
			dbLaptop.setImageUrl(laptop.getImageUrl());
			
			dbLaptop.setCategory(laptop.getCategory());
			
			
			Laptop updatedLaptop = lrepo.save(dbLaptop);
			
			return updatedLaptop;
			
		}
		
		else
		{
			return null;
		}
		
	}

	@Override
	public List<Laptop> deleteLaptop(int lId) 
	{
		Optional<Laptop> data = lrepo.findById(lId);
		
		if(data.isPresent())
		{
			Laptop dbLaptop = data.get();
			
			Category category = dbLaptop.getCategory();
				
			
			lrepo.delete(dbLaptop);
			
			//after deletion of laptop, we return remaining laptop in same category
			List<Laptop> categorywiseLaptop = getCategorywiseLaptop(category.getCname());
			
			return categorywiseLaptop;
			
		}
		else
		{
			return null;
		}
		
		
	}

	@Override
	public Laptop getSingleLaptop(int lId) 
	{
		Optional<Laptop> data = lrepo.findById(lId);
		
		if(data.isPresent())
		{
			Laptop dbLaptop = data.get();
			
			return dbLaptop;
			
		}
		else
		{
			return null;
		}

	}

	@Override
	public Laptop getSingleLaptopByName(String lname) 
	{
		Laptop dbLaptop = lrepo.findByLname(lname);
		
		return dbLaptop;
	}


	@Override
	public List<Laptop> getAllLaptop() 
	{
		return lrepo.findAll();
	}

	
	
	
	@Override
	public Laptop updateStock(int lId, int stockQuantity)
	{
	    Optional<Laptop> data = lrepo.findById(lId);

	    if(data.isPresent())
	    {
	        Laptop dbLaptop = data.get();

	        dbLaptop.setStockQuantity(stockQuantity);

	        if(stockQuantity > 0)
	        {
	            dbLaptop.setStatus("AVAILABLE");
	        }
	        else
	        {
	            dbLaptop.setStatus("OUT_OF_STOCK");
	        }

	        return lrepo.save(dbLaptop);
	    }

	    return null;
	}
	
	
	
	@Override
	public Laptop reduceStock(int lId, int quantity)
	{
	    Optional<Laptop> data = lrepo.findById(lId);

	    if(data.isPresent())
	    {
	        Laptop dbLaptop = data.get();

	        int currentStock = dbLaptop.getStockQuantity();

	        if(currentStock >= quantity)
	        {
	            dbLaptop.setStockQuantity(currentStock - quantity);

	            if(dbLaptop.getStockQuantity() == 0)
	            {
	                dbLaptop.setStatus("OUT_OF_STOCK");
	            }
	            else
	            {
	                dbLaptop.setStatus("AVAILABLE");
	            }

	            return lrepo.save(dbLaptop);
	        }
	    }

	    return null;
	}
	
	
	

}

