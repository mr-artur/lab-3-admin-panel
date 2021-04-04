package ua.kpi.fict.cms.statics;

import ua.kpi.fict.cms.entity.enums.Language;

public class StaticTextManager {

    public static String getProceedText(Language language) {
        return language == Language.UA
                ? "Перейти"
                : "Open";
    }

    public static String getHeaderText(Language language) {
        return language == Language.UA
                ? "Лаб 2 - Ядро CMS-Системи - Розробка інтелектуальних Web-систем"
                : "Lab 2 - CMS Core - Development of Intelligent Web Systems";
    }

    public static String getFooterSignText(Language language) {
        return language == Language.UA
                ? "Виконав : Морозов Артур, студент групи ІП-04мп"
                : "Developed by Morozov Artur, student of IP-04mp";
    }

    public static String getFooterCopyrightsText(Language language) {
        return language == Language.UA
                ? "(c) Всі права знаходяться під охороною."
                : "(c) All rights reserved.";
    }

    public static String getOpenText(Language language) {
        return language == Language.UA
                ? "Перейти →"
                : "Open →";
    }

    public static String getOpenTextShortened() {
        return ">";
    }
}
