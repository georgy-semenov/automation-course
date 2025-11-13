package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
public class ParallelNavigationTest {

    static Stream<Arguments> providePagesAndBrowsers() {
        String baseUrl = "https://the-internet.herokuapp.com";
        String[] paths = {"/", "/login", "/dropdown", "/javascript_alerts", "/checkboxes", "/hover", "/status_codes"};
        return Stream.of(
                Arguments.of("chromium", baseUrl, paths),
                Arguments.of("firefox", baseUrl, paths)
        );
    }

    @ParameterizedTest
    @MethodSource("providePagesAndBrowsers")
    void testPageLoad(String browserType, String baseUrl, String[] paths) {
        Playwright playwright = Playwright.create();
        Browser browser = null;
        try {
            switch (browserType) {
                case "chromium" -> browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
                case "firefox" -> browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            }

            for (String path : paths) {
                BrowserContext context = browser.newContext();
                Page page = context.newPage();
                page.navigate(baseUrl + path);
                assertThat(page).hasTitle(Pattern.compile(".*"));
                context.close();
            }
        } finally {
            if (browser != null) browser.close();
            playwright.close();
        }
    }


}