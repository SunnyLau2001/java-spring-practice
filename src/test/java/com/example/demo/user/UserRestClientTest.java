package com.example.demo.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(UserRestClient.class)
class UserRestClientTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    UserRestClient client;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldFindAllUsers() throws JsonProcessingException {
        // given
        User user1 = new User(1,
                "Leanne",
                "lgraham",
                "lgraham@gmail.com",
                new Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", new Geo("-37.3159", "81.1496")),
                "1-770-736-8031 x56442",
                "hildegard.org",
                new Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        List<User> users = List.of(user1);

        // when
        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(users), MediaType.APPLICATION_JSON));

        // then
        List<User> allUsers = client.findAll();
        assertEquals(users, allUsers);
    }

    @Test
    void shouldFindUserById() throws JsonProcessingException {
        // given
        User user = new User(1,
                "Leanne",
                "lgraham",
                "lgraham@gmail.com",
                new Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", new Geo("-37.3159", "81.1496")),
                "1-770-736-8031 x56442",
                "hildegard.org",
                new Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"));

        // when
        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users/1"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(user), MediaType.APPLICATION_JSON));

        // then
        User leanne = client.findById(1);
        assertEquals(leanne.name(), "Leanne", "User name should be Leanne");
        assertEquals(leanne.username(), "lgraham", "User username should be lgraham");
        assertEquals(leanne.email(), "lgraham@gmail.com");
        assertAll("Address",
                () -> assertEquals(leanne.address().street(), "Kulas Light"),
                () -> assertEquals(leanne.address().suite(), "Apt. 556"),
                () -> assertEquals(leanne.address().city(), "Gwenborough"),
                () -> assertEquals(leanne.address().zipcode(), "92998-3874"),
                () -> assertEquals(leanne.address().geo().lat(), "-37.3159"),
                () -> assertEquals(leanne.address().geo().lng(), "81.1496")
        );
        assertEquals(leanne.phone(), "1-770-736-8031 x56442");
        assertEquals(leanne.website(), "hildegard.org");
        assertAll("Company",
                () -> assertEquals(leanne.company().name(), "Romaguera-Crona"),
                () -> assertEquals(leanne.company().catchPhrase(), "Multi-layered client-server neural-net"),
                () -> assertEquals(leanne.company().bs(), "harness real-time e-markets"));
    }

}