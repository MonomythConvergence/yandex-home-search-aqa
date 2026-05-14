package helpers;

import com.codeborne.selenide.logevents.LogEvent;
import io.qameta.allure.selenide.AllureSelenide;

public class CustomAllureSelenide extends AllureSelenide {

    public CustomAllureSelenide() {
        screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true);
    }

    @Override
    public void afterEvent(LogEvent event) {
        super.afterEvent(event);
    }
}
