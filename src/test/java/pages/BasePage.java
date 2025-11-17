package pages;

import base.BaseTest;
import com.microsoft.playwright.*;


public class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

}
