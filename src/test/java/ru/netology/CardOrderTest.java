package ru.netology;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardOrderTest {
    @BeforeAll
    static void SetUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @Test
    public void testOrderForm() {
        FormData formData = DataGenerator.generateFormData();
        open("http://localhost:9999");

        $(By.cssSelector("[data-test-id='city'] input")).setValue(formData.getCity());
        $(By.cssSelector("[data-test-id='date'] input")).setValue(formData.getDate());
        $(By.cssSelector("[data-test-id='name'] input")).setValue(formData.getName());
        $(By.cssSelector("[data-test-id='phone'] input")).setValue(formData.getPhone());
        $(By.cssSelector("[data-test-id='agreement']")).click();
        $(By.className("button")).click();
        $(By.className("notification__content"))
                .shouldHave(text("Встреча успешно запланирована на " + formData.getDate()), Duration.ofSeconds(15))
                .shouldBe(visible);

        $(By.cssSelector("[data-test-id='date'] input")).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $(By.cssSelector("[data-test-id='date'] input")).setValue(formData.getNewDate());
        $(By.className("button")).click();
        $(By.className("button_view_extra")).click();
        $(By.className("notification__content"))
                .shouldHave(text("Встреча успешно запланирована на " + formData.getNewDate()), Duration.ofSeconds(15))
                .shouldBe(visible);
    }
}