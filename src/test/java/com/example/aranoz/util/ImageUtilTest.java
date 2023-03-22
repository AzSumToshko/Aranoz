package com.example.aranoz.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Base64;

public class ImageUtilTest {

    @Test
    public void testCompressImage() {
        byte[] data = "test".getBytes();
        byte[] compressedData = ImageUtil.compressImage(data);
        assertTrue(compressedData.length < data.length, "Compressed data should be smaller than original data.");
    }

    @Test
    public void testDecompressImage() {
        byte[] data = "test".getBytes();
        byte[] compressedData = ImageUtil.compressImage(data);
        byte[] decompressedData = ImageUtil.decompressImage(compressedData);
        assertArrayEquals(data, decompressedData, "Decompressed data should be equal to original data.");
    }

    @Test
    public void testGetImgData() {
        byte[] data = "test".getBytes();
        String imgData = ImageUtil.getImgData(data);
        String expectedImgData = Base64.getMimeEncoder().encodeToString(data);
        assertEquals(expectedImgData, imgData, "Encoded image data should match expected value.");
    }
}