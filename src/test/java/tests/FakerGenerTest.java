package tests;

import com.github.javafaker.Faker;
import com.microsoft.playwright.*;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class FakerGenerTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            // Генерация данных
            Faker faker = new Faker();
            String name = faker.name().name();

            // Мокирование API
            page.route("**/dynamic_content", route -> {
                route.fulfill(new Route.FulfillOptions()
                        .setBody("<div class='large-10 columns'>" + name + "</div>"));
            });

            // Запуск теста
            page.navigate("https://the-internet.herokuapp.com/dynamic_content");
            assertTrue(page.locator(".large-10.columns").textContent().contains(name));

        }
    }
}
