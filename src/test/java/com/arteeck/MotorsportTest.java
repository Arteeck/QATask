package com.arteeck;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class MotorsportTest {

    private String mail = "artemy.gordon@gmail.com";
    private String password = "12345678";
    private LocalDateTime expectedSubscriptionStart = LocalDateTime.now();
    private LocalDateTime expectedSubscriptionEnd = expectedSubscriptionStart.plusMonths(1);
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    public void monthlySubscriptionTest() {
        open("http://t.motorsport.tv");
        //Selenide.sleep(2000);//потому что через VPN долго прогружается страничка
        $(By.className("sl8C2")).click();
        $(By.xpath("//input[@placeholder='Email']")).setValue(mail);
        $(By.xpath("//input[@placeholder='Password']")).setValue(password);
        $(By.xpath("//button[@class='_2boEn _1Kthq']")).click();
        $(By.className("OyGFk")).click();
        $(By.xpath("//a[@href='/payment']")).click();
        Selenide.sleep(2000);//потому что через VPN долго прогружается страничка
        $(By.xpath("//div[@class='_2cf4-']//span[@data-qa]")).click();
        $(By.className("_9W4Tu")).click();
        $(By.xpath("//input[@placeholder='First name']")).setValue("Artemy");
        $(By.xpath("//input[@placeholder='Last name']")).setValue("Gordon");
        setValueInFrame(
                "braintree-hosted-field-number",
                "credit-card-number",
                "2223000048400011");
        setValueInFrame("braintree-hosted-field-postalCode", "postal-code", "12345-6789");
        setValueInFrame("braintree-hosted-field-expirationDate", "expiration", "04/23");
        setValueInFrame("braintree-hosted-field-cvv", "cvv", "102");
        $(By.className("M30V8")).click();
        $(By.className("_2boEn")).click();
        ElementsCollection results = $$(By.className("_2YgDV"));
        results.get(0).$(By.xpath("div[2]")).should(text("Monthly"));
        results.get(1).$(By.xpath("div[2]")).should(text("Active"));
        results.get(2).$(By.xpath("div[2]")).should(text(String.format(
                "%s through %s",
                expectedSubscriptionStart.format(dateTimeFormatter),
                expectedSubscriptionEnd.format(dateTimeFormatter))));
    }

    private void setValueInFrame(String frameNameOrId, String className, String value) {
        switchTo().frame(frameNameOrId);
        $(By.id(className)).setValue(value);
        switchTo().defaultContent();
    }
}
