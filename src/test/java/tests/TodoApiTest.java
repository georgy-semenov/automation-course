package tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TodoApiTest {
    Playwright playwright;
    APIRequestContext requestContext;
    ObjectMapper objectMapper = new ObjectMapper();
    APIResponse response;

    @BeforeEach
    public void setUp(){
        playwright = Playwright.create();
        requestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://jsonplaceholder.typicode.com")
        );
    }

    @Test
    public void testTodoApi() throws Exception{
        response = requestContext.get("/todos/1");
        assertTrue(response.status()==200);
        User user = objectMapper.readValue(response.text(), User.class);

        String responseBody = response.text();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        assertTrue(jsonNode.has("userId"));
        assertTrue(jsonNode.has("id"));
        assertTrue(jsonNode.has("title"));
        assertTrue(jsonNode.has("completed"));

        System.out.println("User userId: " +user.getUserId());
        System.out.println("User id: " +user.getId());
        System.out.println("User title: " +user.getTitle());
        System.out.println("User completed: " +user.getCompleted());
    }

    @AfterEach
    public void tearDown(){
        response.dispose();
        requestContext.dispose();
        playwright.close();
    }


    @Data
    public static class User{
        private Integer userId;
        private Integer Id;
        private String title;
        private Boolean completed;
    }
}

