package com.nurtech.inventoryservice;

import com.nurtech.inventoryservice.model.Inventory;
import com.nurtech.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	@Autowired
	private InventoryRepository inventoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData() {
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setQuantity(50);
			inventory.setSkuCode("iphone_13");

			Inventory inventory1 = new Inventory();
			inventory1.setQuantity(100);
			inventory1.setSkuCode("macbook_pro");

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
