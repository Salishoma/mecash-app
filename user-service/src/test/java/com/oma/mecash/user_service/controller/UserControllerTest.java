//package com.oma.mecash.user_service.controller;
//
//import com.oma.mecash.user_service.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(properties = "spring.security.enabled=false")
//@AutoConfigureMockMvc
//class UserControllerTest {
//
//    private final String PATH = "/api/users";
//    @Autowired
//    private MockMvc mockMvc;
//
//    private UserService userService;
//
//
//    @Test
//    void createAccount() throws Exception {
//        mockMvc.perform(get(PATH + "/account")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
////                .accept(MediaType.APPLICATION_JSON_VALUE)
//        ).andExpect(status().isOk());
//    }
//
//    @Test
//    void updateAccount() {
//    }
//
//    @Test
//    void generateAccessToken() {
//    }
//
//    @Test
//    void getAccount() throws Exception {
//        mockMvc.perform(get(PATH + "/account")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
////                .accept(MediaType.APPLICATION_JSON_VALUE)
//        ).andExpect(status().isOk());
//    }
//
//    @Test
//    void createPin() {
//    }
//
//    @Test
//    void updatePin() {
//    }
//}