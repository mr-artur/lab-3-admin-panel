package ua.kpi.fict.cms.statics;

import ua.kpi.fict.cms.entity.enums.Language;

public class StaticTextManager {

    public static String getSuccessfulPageCreationText(Language language) {
        return language == Language.UA
                ? "Сторінка була успішно збережена"
                : "Page was successfully saved";
    }

    public static String getSuccessfulPageUpdateText(Language language) {
        return language == Language.UA
                ? "Сторінка була успішно оновлена"
                : "Page was successfully updated";
    }

    public static String getSuccessfulPageDeletionText(Language language) {
        return language == Language.UA
                ? "Сторінка була успішно видалена"
                : "Page was successfully deleted";
    }

    public static String getAdminPanelRootPageHeader(Language language) {
        return language == Language.UA
                ? "Наявна рут-сторінка"
                : "Root page";
    }

    public static String getAdminPanelPageHeaderTemplate(Language language) {
        return language == Language.UA
                ? "Наявні сторінки, що доступні із %s"
                : "Pages available from %s";
    }

    public static String getCaptionEnText(Language language) {
        return language == Language.UA
                ? "Заголовок (АНГ)"
                : "Caption (EN)";
    }

    public static String getCaptionUaText(Language language) {
        return language == Language.UA
                ? "Заголовок (УКР)"
                : "Caption (UA)";
    }

    public static String getCodeText(Language language) {
        return language == Language.UA
                ? "Код"
                : "Code";
    }

    public static String getContainerTypeText(Language language) {
        return language == Language.UA
                ? "Тип контейнера"
                : "Container type";
    }

    public static String getContentEnText(Language language) {
        return language == Language.UA
                ? "Контент (АНГ)"
                : "Content (EN)";
    }

    public static String getContentUaText(Language language) {
        return language == Language.UA
                ? "Контент (УКР)"
                : "Content (UA)";
    }

    public static String getCreationDateText(Language language) {
        return language == Language.UA
                ? "Дата створення"
                : "Creation date";
    }

    public static String getImageUrlText(Language language) {
        return language == Language.UA
                ? "Url картинки"
                : "Image url";
    }

    public static String getIntroEnText(Language language) {
        return language == Language.UA
                ? "Інтро (АНГ)"
                : "Intro (EN)";
    }

    public static String getIntroUaText(Language language) {
        return language == Language.UA
                ? "Інтро (УКР)"
                : "Intro (UA)";
    }

    public static String getOrderNumText(Language language) {
        return language == Language.UA
                ? "Порядок"
                : "Order";
    }

    public static String getOrderTypeText(Language language) {
        return language == Language.UA
                ? "Тип сортування"
                : "Order type";
    }

    public static String getUpdateDateText(Language language) {
        return language == Language.UA
                ? "Дата оновлення"
                : "Update date";
    }

    public static String getAliasOfText(Language language) {
        return language == Language.UA
                ? "Псевдонім для"
                : "Alias of";
    }

    public static String getParentCodeText(Language language) {
        return language == Language.UA
                ? "Батьківський код"
                : "Parent code";
    }

    public static String getShowButtonText(Language language) {
        return language == Language.UA
                ? "Переглянути"
                : "Show";
    }

    public static String getUpdateButtonText(Language language) {
        return language == Language.UA
                ? "Оновити"
                : "Update";
    }

    public static String getDeleteButtonText(Language language) {
        return language == Language.UA
                ? "Видалити"
                : "Delete";
    }

    public static String getCreateButtonText(Language language) {
        return language == Language.UA
                ? "+ Створити"
                : "+ Create";
    }

    public static String getHeaderText(Language language) {
        return language == Language.UA
                ? "Лаб 3 - Адмін-панель CMS - Розробка інтелектуальних Web-систем"
                : "Lab 3 - CMS Admin panel - Development of Intelligent Web Systems";
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
