package com.CollectionServer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.annotation.*;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.Map;


import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.HashMap;

@Entity
public class DataPointEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	
	public String executablePath;

	public UUID originClientId;

	public Date dateCreated;

	@ElementCollection(fetch = FetchType.EAGER)
	public Map<String, Integer> WinAPICounts;
	
	@ElementCollection(fetch = FetchType.EAGER)
	public Map<String, Float> winAPIRatios;

	public DataPointEntity()
	{
		dateCreated = new Date();
	}
	
	public void updateRatios()
	{
		winAPIRatios = new HashMap<String, Float>();
		//Get total calls made
		int totalCalls = 0;
		for(Integer	value : WinAPICounts.values())
		{
			totalCalls += value;
		}
		
		for (Map.Entry<String, Integer> entry : WinAPICounts.entrySet())
		{
			String key = entry.getKey();
			Integer value = entry.getValue();
			float ratio = ((float)value) / totalCalls;
			
			winAPIRatios.put(key, ratio);
		}
	}

	/*
	Monitored API calls include some selected from the following sources:
	https://book.hacktricks.xyz/reversing-and-exploiting/common-api-used-in-malware
	https://gist.github.com/404NetworkError/a81591849f5b6b5fe09f517efc189c1d

	List will also include other common Win32 API calls used by normal programs as well
	*/
}
