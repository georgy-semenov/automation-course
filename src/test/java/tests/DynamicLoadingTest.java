package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicLoadingTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @Test
    public void testDynamicLoading(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        BrowserContext context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
                .setTitle("My Java Trace"));
        page = context.newPage();

        page = context.newPage();
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

        page.onResponse(response -> {
            if(response.url().contains("/dynamic_loading"))
                assertTrue(response.status() ==200);
        });

        page.click("//button");
        page.locator("#finish").waitFor();
        assertTrue(page.locator("#finish").isVisible(), "Элемент не отображается");

        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace/trace-success.zip")));
    }
}
