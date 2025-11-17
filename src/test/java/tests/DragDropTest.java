package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import pages.DragAndDropPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DragDropTest extends BaseTest {

    @Test
    public void testDragAndDrop(){
        DragAndDropPage dragAndDropPage = new DragAndDropPage(page);
        dragAndDropPage.navigateToDragDrop();
        dragAndDropPage.dragAndDropArea().dragAToB();
        assertEquals("A", dragAndDropPage.dragAndDropArea().getTextB());
    }
}
