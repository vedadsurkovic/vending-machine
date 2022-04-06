package com.mvpmatch.vendingmachine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvpmatch.vendingmachine.entity.ProductEntity;
import com.mvpmatch.vendingmachine.entity.Role;
import com.mvpmatch.vendingmachine.entity.UserEntity;
import com.mvpmatch.vendingmachine.service.ProductService;
import com.mvpmatch.vendingmachine.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class VendingMachineApplicationTest {

	private static final String AUTHORIZATION = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2c3Vya292aWMiLCJpYXQiOjE2NDg4MTM1NTIsImV4cCI6MTY0ODg5OTk1Mn0.2baIeUqmRMhbzN7u-Jm4OMueBymr66ujZSfqvTm-BVRf8khyZ96TEEHMIn1IvfyoW_7u2NToveoMRoFTWXLbpw";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService userService;

	@MockBean
	private ProductService productService;

	@Test
	void testCreateUser() throws Exception {
		final UserEntity testUser = createMockUserEntityBuyer();

		when(userService.create(testUser))
			.thenReturn(testUser);

		mvc.perform(post("/user")
				.content(asJsonString(testUser))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.username").value(testUser.getUsername()));

	}

	@Test
	void testDeposit() throws Exception {
		final UserEntity testUser = createMockUserEntityBuyer();
		final Map<String, String> body = new HashMap<>();
		body.put("deposit", "50");

		testUser.setDeposit(50L);
		when(userService.deposit(testUser.getId(), 50L))
			.thenReturn(new ResponseEntity(testUser, HttpStatus.OK));

		mvc.perform(patch("/user/deposit")
				.header("Authorization", AUTHORIZATION)
				.content(asJsonString(body))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.deposit").value(50L));
	}

	@Test
	void testBuy() throws Exception {
		final UserEntity testUser = createMockUserEntityBuyer();
		final ProductEntity productEntity = createMockProductEntity();
		final Map<String, String> body = new HashMap<>();
		body.put("productId", "1");
		body.put("amount", "2");

		final Map<String, Object> response = new HashMap<>();
		response.put("product", productEntity.getProductName());
		response.put("totalSpent", productEntity.getCost() * 2);
		response.put("change", 40L);
		when(productService.buy(testUser.getId(), 1L, 2))
			.thenReturn(new ResponseEntity(response, HttpStatus.OK));

		mvc.perform(patch("/product/buy")
				.header("Authorization", AUTHORIZATION)
				.content(asJsonString(body))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.product").value(productEntity.getProductName()))
			.andExpect(jsonPath("$.change").value(40L));
	}

	private UserEntity createMockUserEntityBuyer() {
		final UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setUsername("vsurkovic");
		userEntity.setPassword(UUID.randomUUID().toString());
		userEntity.setRole(Role.BUYER);
		userEntity.setDeposit(50L);

		return userEntity;
	}

	private UserEntity createMockUserEntitySeller() {
		final UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
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
