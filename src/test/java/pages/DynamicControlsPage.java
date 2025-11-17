package pages;

import com.microsoft.playwright.Page;

public class DynamicControlsPage {
    private final Page page;


    public DynamicControlsPage(Page page) {
        this.page = page;
    }

    public void clickRemoveButton() {
        page.locator("//button[text()='Remove']").click();
    }

    public boolean isCheckboxVisible() {
        return page.locator("//input[@type='checkbox']").isVisible();
    }
    public void waitForElement(){
        page.waitForSelector("//p[@id='message']");
    }
}
