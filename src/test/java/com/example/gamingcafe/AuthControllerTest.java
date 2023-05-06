package com.example.gamingcafe;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.RequestEntity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import com.example.gamingcafe.controller.auth.AuthController;
import com.example.gamingcafe.model.auth.Creds;
import com.example.gamingcafe.repo.auth.CredsRepo;
import com.example.gamingcafe.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.NestedServletException;

import java.net.BindException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@RunWith(BlockJUnit4ClassRunner.class)
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    @Mock
    private CredsRepo credsRepo;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController controller;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void CheckValidGamerLogin() throws Exception {
        Creds user = new Creds(99,"gamer1","santhil@mail.com", "123",1,1);
        when(credsRepo.findByUsername("gamer1"))
                .thenReturn(user);

        String requestBody = "{\"username\": \"gamer1\", \"password\": \"123\", \"role\": 1}";
        String expectedResponseBody = "generatedToken";

//        System.out.println(requestBody);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gamer/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
//        System.out.println(requestBuilder);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
    }

    @Test

    public void CheckInValidGamerLogin() throws Exception {
        Creds user = new Creds(99,"gamer1","santhil@mail.com", "123",1,0);
        when(credsRepo.findByUsername("gamer1"))
                .thenReturn(user);

        String requestBody = "{\"username\": \"gamer1\", \"password\": \"123\", \"role\": 1}";
        String expectedResponseBody = "{\"message\":\"User not activated\",\"status\":\"BAD_REQUEST\"}";

//        System.out.println(expectedResponseBody);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/gamer/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
//        System.out.println(requestBuilder);
        try {
            MvcResult x = mockMvc.perform(requestBuilder)
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof NestedServletException))
                    .andReturn();
        }catch (Exception e){
            assertEquals(e.getMessage(),"Request processing failed; nested exception is java.lang.Exception: User not activated");
        }

    }
}
