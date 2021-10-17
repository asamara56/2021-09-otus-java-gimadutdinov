package ru.asamara56.testing.exercise;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.asamara56.testing.exercise.DefaultMessageTemplateProvider.DEFAULT_TEMPLATE;

public class MessageBuilderImplTest {

    private static final String TEMPLATE_NAME = "templateName";
    private static final String MESSAGE_TEXT = "messageText";
    private static final String SIGNATURE = "signature";

    private MessageTemplateProvider provider;
    private MessageBuilder messageBuilder;

    @BeforeEach
    void setUp() {
        System.out.println("Run @BeforeEach");
        provider = mock(MessageTemplateProvider.class);
        //provider = spy(new DefaultMessageTemplateProvider());
        messageBuilder = new MessageBuilderImpl(provider);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Run @AfterEach");
    }

    @Test
    void buildMessageTest_1() {
        System.out.println("Test_1 start");

        when(provider.getMessageTemplate(eq(TEMPLATE_NAME))).thenReturn(DEFAULT_TEMPLATE);

        final String expected = String.format(DEFAULT_TEMPLATE, MESSAGE_TEXT, SIGNATURE);
        final String actual = messageBuilder.buildMessage(TEMPLATE_NAME, MESSAGE_TEXT, SIGNATURE);

        assertEquals(expected, actual);

        System.out.println("Test_1 finish");
    }

    @Test
    void buildMessageTest_2() {
        System.out.println("Test_2 start");

        when(provider.getMessageTemplate(TEMPLATE_NAME)).thenReturn(DEFAULT_TEMPLATE);

        final String actual = messageBuilder.buildMessage(TEMPLATE_NAME, null, null);
        System.out.println("Actual is " + (actual.isBlank() ? "!blank" : actual));

        verify(provider, times(1)).getMessageTemplate(TEMPLATE_NAME);

        System.out.println("Test_2 finish");
    }

    @Test
    void buildMessageTest_3() {
        System.out.println("Test_3 start");

        assertThrows(TemplateNotFoundException.class, () -> messageBuilder.buildMessage(null, null, null));

        when(provider.getMessageTemplate(TEMPLATE_NAME)).thenReturn(DEFAULT_TEMPLATE);
        assertDoesNotThrow(() -> messageBuilder.buildMessage(TEMPLATE_NAME, null, null));

        System.out.println("Test_3 finish");
    }
}
