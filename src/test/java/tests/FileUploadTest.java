package tests;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class FileUploadTest {
    private Playwright playwright;
    private APIRequestContext request;

    private byte[] generatedPngBytes;

    @BeforeEach
    void setUp() throws IOException {
        playwright = Playwright.create();
        request = playwright.request().newContext();

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", output);
        generatedPngBytes = output.toByteArray();
    }

    @Test
    void testFileUploadAndDownload() throws IOException {
        Path testFile = Files.createTempFile("test-", ".png");
        try {
            Files.write(testFile, generatedPngBytes);

            APIResponse uploadResponse = request.post(
                    "https://httpbin.org/post",
                    RequestOptions.create().setMultipart(
                            FormData.create().set("file", testFile)
                    )
            );

            assertTrue(uploadResponse.ok());

            String responseBody = uploadResponse.text();
            assertTrue(responseBody.contains("data:image/png;base64"));

            String base64Data = responseBody.split("\"file\": \"")[1].split("\"")[0];
            String base64Content = base64Data.split(",")[1];
            byte[] receivedBytes = Base64.getDecoder().decode(base64Content);

            assertArrayEquals(generatedPngBytes, receivedBytes);

            APIResponse downloadResponse = request.get("https://httpbin.org/image/png");
            assertTrue(downloadResponse.ok());

            byte[] content = downloadResponse.body();
            assertArrayStartsWithPngSignature(content);
        } finally {
            Files.deleteIfExists(testFile);
        }
    }

    private void assertArrayStartsWithPngSignature(byte[] content) {
        assert content.length >= 8 : "Content to short for PNG signature";
        assertEquals(0x89, content[0] & 0xFF);
        assertEquals(0x50, content[1] & 0xFF);
        assertEquals(0x4E, content[2] & 0xFF);
        assertEquals(0x47, content[3] & 0xFF);
        assertEquals(0x0D, content[4] & 0xFF);
        assertEquals(0x0A, content[5] & 0xFF);
        assertEquals(0x1A, content[6] & 0xFF);
        assertEquals(0x0A, content[7] & 0xFF);
    }

    @AfterEach
    void tearDown() {
        request.dispose();
        playwright.close();
    }
}
