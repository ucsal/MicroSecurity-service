package Ucsal.Authservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "eureka.client.enabled=false")
class AuthserviceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void registerAndLoginReturnsJwt() throws Exception {
		String username = "joao";
		String password = "123456";

		mockMvc.perform(post("/auth/register")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "username": "joao",
								  "email": "joao@email.com",
								  "password": "123456"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value(username))
				.andExpect(jsonPath("$.email").value("joao@email.com"));

		MvcResult loginResult = mockMvc.perform(post("/auth/login")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "username": "joao",
								  "password": "123456"
								}
								"""))
				.andExpect(status().isOk())
				.andReturn();

		String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();
		assertFalse(token.isBlank());
		assertEquals(3, token.split("\\.").length);
	}

}
