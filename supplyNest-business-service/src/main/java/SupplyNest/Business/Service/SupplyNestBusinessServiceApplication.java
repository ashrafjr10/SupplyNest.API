package SupplyNest.Business.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SupplyNestBusinessServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyNestBusinessServiceApplication.class, args);
	}

}
