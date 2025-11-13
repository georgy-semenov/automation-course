package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Веб-интерфейс тестов")
@Feature("Операции с чекбоксами")
public class CheckboxTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private final String firstCheckbox = "//input[1]";
    private final String secondCheckbox = "//input[2]";


    @BeforeEach
    @Step("Инициализация браузера и контекста")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    @Story("Проверка работы чекбоксов")
    @DisplayName("Тестирование выбора/снятия чекбоксов")
    @Severity(SeverityLevel.CRITICAL)
    void testCheckboxes() {
        try {
            navigateToCheckboxesPage();
            verifyInitialState();
            toggleCheckboxes();
            verifyToggledState();
        } catch (Exception e) {
            Allure.addAttachment("Скриншот при ошибке", "image/png",
                    new ByteArrayInputStream(page.screenshot()), ".png");
            throw e;
        }
    }

    @Step("Переход на страницу /checkboxes")
    private void navigateToCheckboxesPage() {
        page.navigate("https://the-internet.herokuapp.com/checkboxes",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }

    @Step("Проверка начального состояния чекбоксов")
    private void verifyInitialState() {
        assertFalse(page.locator(firstCheckbox).isChecked(), "1 Чекбокс должен быть не отмечен");
        assertTrue(page.locator(secondCheckbox).isChecked(), "2 Чекбокс должен быть отмечен");

    }

    @Step("Изменение состояния чекбоксов")
    private void toggleCheckboxes() {
        page.locator(firstCheckbox).check();
        page.locator(secondCheckbox).uncheck();
    }

    @Step("Проверка измененного состояния")
    private void verifyToggledState(){
        assertTrue(page.locator(firstCheckbox).isChecked(), "1 Чекбокс должен быть отмечен");
        assertFalse(page.locator(secondCheckbox).isChecked(), "2 Чекбокс должен быть не отмечен");
    }



    @AfterEach
    @Step("Закрытие ресурсов")
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}
