package Supplynest.Auth.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SupplyNestAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyNestAuthServiceApplication.class, args);
	}

}
