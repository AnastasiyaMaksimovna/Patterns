import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import entities.RegistrationByCardInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import utils.DataGenerator;

import java.time.Duration;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.*;

public class PatternsTest {

    @Test
    void shouldPreventSendRequestMultipleTimes() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        RegistrationByCardInfo registrationByCardInfo = DataGenerator.Registration.generateByCard("ru", 3);
        $("span[data-test-id=city] input").setValue(registrationByCardInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("span[data-test-id=date] input").setValue(DataGenerator.generDate(3));
        $("span[data-test-id=name] input").setValue(registrationByCardInfo.getName());
        $("span[data-test-id=phone] input").setValue(registrationByCardInfo.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//*[contains(text(), 'Запланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + DataGenerator.generDate(3)), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("span[data-test-id=date] input").setValue(DataGenerator.generDate(5));
        $x("//*[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=replan-notification]").shouldHave(Condition.text("Необходимо подтверждение"));
        $("[data-test-id=replan-notification]").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $x("//span[contains(text(), 'Перепланировать')]").click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + DataGenerator.generDate(5)), Duration.ofSeconds(15)).shouldBe(Condition.visible);

    }

}
