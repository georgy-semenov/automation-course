package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import components.DragAndDropArea;

public class DragAndDropPage extends BasePage{
    private DragAndDropArea dragAndDropArea;

    public DragAndDropPage(Page page) {
        super(page);
    }

    public DragAndDropArea dragAndDropArea(){
        if (dragAndDropArea == null) {
            dragAndDropArea = new DragAndDropArea(page);
        }
        return dragAndDropArea;
    }

    public void navigateToDragDrop(){
        page.navigate("https://the-internet.herokuapp.com/drag_and_drop",
                new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }
}
