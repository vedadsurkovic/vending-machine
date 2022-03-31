package com.mvpmatch.vendingmachine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvpmatch.vendingmachine.model.ProductEntity;
import com.mvpmatch.vendingmachine.model.Role;
import com.mvpmatch.vendingmachine.model.UserEntity;
import com.mvpmatch.vendingmachine.repository.ProductRepository;
import com.mvpmatch.vendingmachine.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class VendingMachineApplicationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testDeposit() {

	}

	@Test
	void testBuy() {

	}

	@Test
	void testCreateUser() throws Exception {
        final UserEntity testUser = createMockUserEntityBuyer();

		mvc.perform(post("/user")
			.content(asJsonString(testUser))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
			.andExpect(jsonPath("$.username").value(testUser.getUsername()));

	}

	private UserEntity createMockUserEntityBuyer() {
		final UserEntity userEntity = new UserEntity();
		userEntity.setUsername("vsurkovic");
		userEntity.setPassword(UUID.randomUUID().toString());
		userEntity.setRole(Role.BUYER);
		userEntity.setDeposit(0L);

		return userEntity;
	}

	private UserEntity createMockUserEntitySeller() {
		final UserEntity userEntity = new UserEntity();
		userEntity.setUsername("vsurkovic");
		userEntity.setPassword(UUID.randomUUID().toString());
		userEntity.setRole(Role.SELLER);
		userEntity.setDeposit(0L);

		return userEntity;
	}

	private ProductEntity createMockProductEntity() {
		final ProductEntity productEntity = new ProductEntity();
		productEntity.setId(1L);
		productEntity.setProductName("Snickers");
		productEntity.setAmountAvailable(10);
		productEntity.setCost(5L);
		productEntity.setSeller(createMockUserEntitySeller());

		return productEntity;
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
