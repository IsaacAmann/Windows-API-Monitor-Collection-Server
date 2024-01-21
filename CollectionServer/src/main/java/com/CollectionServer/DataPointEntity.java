package com.CollectionServer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class DataPointEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;
	
	
}
