package com.magrega.demo.dto.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateReviewDTOTest {

    private CreateReviewDTO createReviewDTO;

    @BeforeEach
    void setUp() {
        createReviewDTO = new CreateReviewDTO();
    }

    @Test
    void testSetAndGetUserId() {
        createReviewDTO.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertEquals(10, createReviewDTO.getUserId());
    }

    @Test
    void testSetAndGetProductId() {
        createReviewDTO.setProductId(99);
        assertEquals(99, createReviewDTO.getProductId());
    }

    @Test
    void testSetAndGetRating() {
        createReviewDTO.setRating(5);
        assertEquals(5, createReviewDTO.getRating());
    }

    @Test
    void testSetAndGetComment() {
        createReviewDTO.setComment("Excellent product, highly recommend!");
        assertEquals("Excellent product, highly recommend!", createReviewDTO.getComment());
    }

    @Test
    void testRatingBoundaryMinimum() {
        createReviewDTO.setRating(1);
        assertEquals(1, createReviewDTO.getRating());
    }

    @Test
    void testRatingBoundaryMaximum() {
        createReviewDTO.setRating(5);
        assertEquals(5, createReviewDTO.getRating());
    }

    @Test
    void testDefaultFieldsAreNull() {
        assertNull(createReviewDTO.getUserId());
        assertNull(createReviewDTO.getProductId());
        assertNull(createReviewDTO.getRating());
        assertNull(createReviewDTO.getComment());
    }

    @Test
    void testEmptyComment() {
        createReviewDTO.setComment("");
        assertEquals("", createReviewDTO.getComment());
    }

    @Test
    void testLongComment() {
        String longComment = "A".repeat(1000);
        createReviewDTO.setComment(longComment);
        assertEquals(1000, createReviewDTO.getComment().length());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateReviewDTO dto1 = new CreateReviewDTO();
        dto1.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto1.setProductId(2);
        dto1.setRating(4);
        dto1.setComment("Great!");

        CreateReviewDTO dto2 = new CreateReviewDTO();
        dto2.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        dto2.setProductId(2);
        dto2.setRating(4);
        dto2.setComment("Great!");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEqualWhenRatingDiffers() {
        CreateReviewDTO dto1 = new CreateReviewDTO();
        dto1.setRating(3);

        CreateReviewDTO dto2 = new CreateReviewDTO();
        dto2.setRating(5);

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testNotEqualWhenCommentDiffers() {
        CreateReviewDTO dto1 = new CreateReviewDTO();
        dto1.setComment("Good");

        CreateReviewDTO dto2 = new CreateReviewDTO();
        dto2.setComment("Bad");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        createReviewDTO.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        createReviewDTO.setRating(5);
        createReviewDTO.setComment("Fantastic!");
        String result = createReviewDTO.toString();
        assertTrue(result.contains("10"));
        assertTrue(result.contains("5"));
        assertTrue(result.contains("Fantastic!"));
    }
}