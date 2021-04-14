package ua.kpi.fict.cms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.kpi.fict.cms.dto.response.AdminPanelPageDto;
import ua.kpi.fict.cms.dto.response.PageDto;
import ua.kpi.fict.cms.entity.MessageType;
import ua.kpi.fict.cms.entity.Page;
import ua.kpi.fict.cms.entity.enums.ContainerType;
import ua.kpi.fict.cms.entity.enums.Language;
import ua.kpi.fict.cms.entity.enums.OrderType;
import ua.kpi.fict.cms.repository.PageRepository;
import ua.kpi.fict.cms.service.PageService;
import ua.kpi.fict.cms.statics.StaticTextManager;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    @Override
    public String purifyPageCode(String pageCode) {
        Page page = findPageByCode(pageCode);
        return page.getAliasOf() != null
                ? page.getAliasOf().getCode()
                : page.getCode();
    }

    @Override
    public PageDto render(Language language, String pageCode) {
        Page page = findPageByCode(pageCode);

        String meta = buildMeta(page, language);
        String header = buildHeader(language);
        String subheader = buildSubheader(page, language);
        String title = page.getCaption(language);
        String imageUrl = page.getImageUrl();
        String content = buildContent(page, language);
        String footer = buildFooter(language);

        return PageDto.builder()
                .meta(meta)
                .header(header)
                .subheader(subheader)
                .title(title)
                .imageUrl(imageUrl)
                .content(content)
                .footer(footer)
                .build();
    }

    private String buildMeta(Page page, Language language) {

        return String.format(
                "        <title>%s</title>" +
                        "<meta name=\"description\" content=\"%s\">",
                page.getCaption(language), page.getIntro(language)
        );
    }

    private String buildSubheader(Page page, Language language) {
        return buildBackButton(page, language);
    }

    private String buildBackButton(Page page, Language language) {
        if (page.getParentPage() == null) {
            return "";
        }
        String parentCode = page.getParentPage().getCode(language);

        return String.format(
                "<a class=\"custom-a back-link\" href=\"%s\">← %s</a>",
                parentCode.equals("root")
                        ? "/"
                        : parentCode.equals("/en/root")
                        ? "/en"
                        : parentCode,
                page.getParentPage().getCaption(language)
        );
    }

    private String buildContent(Page page, Language language) {
        StringBuilder builder = new StringBuilder();

        String baseContent = page.getContent(language);
        builder.append(baseContent);

        String childrenContainer = buildChildrenContainer(page, language);
        builder.append(childrenContainer);

        return builder.toString();
    }

    private String buildChildrenContainer(Page page, Language language) {
        if (page.getContainerType() == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(
                "<div class=\"child-container child-container-%s\">",
                page.getContainerType().toString().toLowerCase())
        );

        List<Page> childPages = new ArrayList<>(page.getChildPages());
        OrderType orderType = page.getOrderType() != null ? page.getOrderType() : OrderType.DEFAULT;
        childPages.sort(orderType.getComparator());

        for (Page childPage : childPages) {
            builder.append(String.format(
                    "<div class=\"child-reference %s\">",
                    page.getContainerType() == ContainerType.GRID
                            ? ""
                            : "child-reference-list")
            );
            builder.append(String.format(
                    "<img class=\"img-small\" src=\"%s\" />",
                    childPage.getImageUrl())
            );
            builder.append(String.format(
                    "<h3>%s</h3>",
                    childPage.getCaption(language))
            );
            builder.append(String.format(
                    "<p>%s</p>",
                    childPage.getIntro(language))
            );
            builder.append(String.format(
                    "<a class=\"custom-a\" href=\"%s\">%s</a>",
                    childPage.getCode(language),
                    page.getContainerType() == ContainerType.GRID
                            ? StaticTextManager.getOpenText(language)
                            : StaticTextManager.getOpenTextShortened())
            );
            builder.append("</div>");
        }
        builder.append("</div>");

        return builder.toString();
    }

    private String buildHeader(Language language) {
        return String.format(
                "<h2>%s</h2>",
                StaticTextManager.getHeaderText(language)
        );
    }

    private String buildFooter(Language language) {
        return String.format(
                "          <h3>%s</h3>"
                        + "<h5>%s</h5>",
                StaticTextManager.getFooterSignText(language),
                StaticTextManager.getFooterCopyrightsText(language)
        );
    }

    @Override
    public List<Page> findChildPages(String parentCode) {
        return pageRepository.findByParentPageCode(parentCode);
    }

    @Override
    public void save(Page page) {
        page.setCreationDate(new Date(System.currentTimeMillis()));
        page.setUpdateDate(new Date(System.currentTimeMillis()));
        updateDependencies(page);
        pageRepository.save(page);
    }

    @Override
    public void update(Page page) {
        updateDependencies(page);
        Page pageToSave = updateOriginalPage(page);
        pageRepository.save(pageToSave);
    }

    private void updateDependencies(Page page) {
        page.setParentPage(findPageByCode(page.getParentPage().getCode()));
        if (!page.getAliasOf().getCode().isEmpty()) {
            page.setAliasOf(findPageByCode(page.getAliasOf().getCode()));
        } else {
            page.setAliasOf(null);
        }
    }

    private Page updateOriginalPage(Page page) {
        Page original = findPageByCode(page.getCode());
        original.setCaptionEn(page.getCaptionEn());
        original.setCaptionUa(page.getCaptionUa());
        original.setCode(page.getCode());
        original.setContainerType(page.getContainerType());
        original.setContentEn(page.getContentEn());
        original.setContentUa(page.getContentUa());
        original.setImageUrl(page.getImageUrl());
        original.setIntroEn(page.getIntroEn());
        original.setIntroUa(page.getIntroUa());
        original.setOrderNum(page.getOrderNum());
        original.setOrderType(page.getOrderType());
        original.setAliasOf(page.getAliasOf());
        original.setParentPage(page.getParentPage());
        original.setUpdateDate(new Date(System.currentTimeMillis()));
        return original;
    }

    @Override
    public void delete(Page page) {
        pageRepository.delete(page);
    }

    @Override
    public AdminPanelPageDto getIndexPage(String parentCode, Language language, MessageType messageType) {
        String header = buildHeader(language);
        String content = buildIndexPageContent(parentCode, language, messageType);
        String footer = buildFooter(language);

        return AdminPanelPageDto.builder()
                .header(header)
                .content(content)
                .footer(footer)
                .build();
    }

    private String buildIndexPageContent(String parentCode, Language language, MessageType messageType) {
        StringBuilder builder = new StringBuilder();

        String messageBlock = buildMessageBlock(language, messageType);
        builder.append(messageBlock);

        String backButton = buildAdminBackButton(parentCode, language);
        builder.append(backButton);

        String header = buildIndexPageHeader(parentCode, language);
        builder.append(header);

        String table = buildIndexTable(parentCode, language);
        builder.append(table);

        String createButton = buildCreateButton(parentCode, language);
        builder.append(createButton);

        return builder.toString();
    }

    private String buildMessageBlock(Language language, MessageType messageType) {
        if (messageType == null) {
            return "";
        }
        switch (messageType) {
            case SAVED:
                return String.format(
                        "<div class=\"alert alert-success mb-4\">%s</div>",
                        StaticTextManager.getSuccessfulPageCreationText(language)
                );
            case UPDATED:
                return String.format(
                        "<div class=\"alert alert-success mb-4\">%s</div>",
                        StaticTextManager.getSuccessfulPageUpdateText(language)
                );
            case DELETED:
                return String.format(
                        "<div class=\"alert alert-danger mb-4\">%s</div>",
                        StaticTextManager.getSuccessfulPageDeletionText(language)
                );
            default:
                return "";
        }
    }

    private String buildAdminBackButton(String parentCode, Language language) {
        Page parentPage = findPageByCode(parentCode).getParentPage();
        return parentCode != null && !parentCode.isEmpty()
                ? String.format("<div class=\"row d-flex justify-content-start mt-4 mb-5 ml-1\">" +
                        "            <a href=\"%s/admin/pages?parentCode=%s\">" +
                        "               <button class=\"btn btn-primary\">← /%s</button>" +
                        "            </a>" +
                        "        </div>",
                language == Language.UA ? "" : "/en",
                parentPage != null ? parentPage.getCode() : "",
                parentPage != null ? parentPage.getCode() : ""
        )
                : "";
    }

    private String buildIndexPageHeader(String parentCode, Language language) {
        String template = "<h2 class=\"mb-4\">%s</h2>";
        String text = parentCode != null && !parentCode.isEmpty()
                ? String.format(StaticTextManager.getAdminPanelPageHeaderTemplate(language), parentCode)
                : StaticTextManager.getAdminPanelRootPageHeader(language);

        return String.format(template, text);
    }

    private String buildIndexTable(String parentCode, Language language) {
        StringBuilder builder = new StringBuilder();
        builder.append("<table class=\"table-striped table-responsive-lg table-bordered table-sm table-hover shadow rounded mt-5\">");

        String header = buildIndexTableHeader(language);
        builder.append(header);

        String body = buildIndexTableBody(parentCode, language);
        builder.append(body);

        builder.append("</table>");

        return builder.toString();
    }

    private String buildCreateButton(String parentCode, Language language) {
        return parentCode != null
                ? String.format("<div class=\"row d-flex justify-content-center mt-4\">" +
                        "            <a href=\"%s/admin/pages/create?parentCode=%s\">" +
                        "               <button class=\"btn btn-success\">%s</button>" +
                        "            </a>" +
                        "        </div>",
                language == Language.UA ? "" : "/en",
                parentCode,
                StaticTextManager.getCreateButtonText(language)
        )
                : "";
    }

    private String buildIndexTableHeader(Language language) {
        StringBuilder builder = new StringBuilder();

        builder.append("<thead>");

        builder.append(buildTableHeaderCell(""));
        builder.append(buildTableHeaderCell("id"));
        builder.append(buildTableHeaderCell(StaticTextManager.getCaptionEnText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getCaptionUaText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getCodeText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getContainerTypeText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getContentEnText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getContentUaText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getCreationDateText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getImageUrlText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getIntroEnText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getIntroUaText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getOrderNumText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getOrderTypeText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getUpdateDateText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getAliasOfText(language)));
        builder.append(buildTableHeaderCell(StaticTextManager.getParentCodeText(language)));
        builder.append(buildTableHeaderCell(""));
        builder.append(buildTableHeaderCell(""));
        builder.append(buildTableHeaderCell(""));

        builder.append("</thead>");

        return builder.toString();
    }

    private String buildIndexTableBody(String parentCode, Language language) {
        StringBuilder builder = new StringBuilder();

        builder.append("<tbody>");

        Page parentPage = findPageByCode(parentCode);
        if (parentPage.getCode() != null) {
            for (Page page : parentPage.getChildPages()) {
                String childRow = getChildPageRow(page, language);
                builder.append(childRow);
            }
        } else {
            Page rootPage = findPageByCode("root");
            String rootChildRow = getChildPageRow(rootPage, language);
            builder.append(rootChildRow);
        }
        builder.append("</tbody>");

        return builder.toString();
    }

    private String getChildPageRow(Page page, Language language) {
        StringBuilder builder = new StringBuilder();

        builder.append("<tr>");

        builder.append(buildTableCell(
                page.getChildPages().isEmpty()
                        ? ""
                        : buildChildrenButton(page.getCode(), language)
        ));
        builder.append(buildTableCell(page.getId().toString()));
        builder.append(buildTableCell(ellipsize(emptyIfNull(page.getCaptionEn()), 9)));
        builder.append(buildTableCell(ellipsize(page.getCaptionUa(), 9)));
        builder.append(buildTableCell(page.getCode()));
        builder.append(buildTableCell(emptyIfNull(page.getContainerType())));
        builder.append(buildTableCell(page.getContentEn().isEmpty() ? "" : "+"));
        builder.append(buildTableCell(page.getContentUa().isEmpty() ? "" : "+"));
        builder.append(buildTableCell(page.getCreationDate().toString()));
        builder.append(buildTableCell(ellipsize(page.getImageUrl(), 9)));
        builder.append(buildTableCell(ellipsize(page.getIntroEn(), 9)));
        builder.append(buildTableCell(ellipsize(page.getIntroUa(), 9)));
        builder.append(buildTableCell(emptyIfNull(page.getOrderNum())));
        builder.append(buildTableCell(emptyIfNull(page.getOrderType())));
        builder.append(buildTableCell(page.getUpdateDate().toString()));
        builder.append(buildTableCell(
                page.getAliasOf() != null
                        ? emptyIfNull(page.getAliasOf().getCode())
                        : ""
        ));
        builder.append(buildTableCell(
                page.getParentPage() != null
                        ? emptyIfNull(page.getParentPage().getCode())
                        : ""
        ));
        builder.append(buildTableCell(buildShowButton(page.getCode(), language)));
        builder.append(buildTableCell(buildUpdateButton(page.getCode(), language)));
        builder.append(buildTableCell(buildDeleteButton(page.getCode(), language)));

        builder.append("</tr>");

        return builder.toString();
    }

    private String buildChildrenButton(String code, Language language) {
        return String.format("<a href=\"%s/admin/pages?parentCode=%s\">" +
                        "         <button class=\"btn btn-success\">⟱</button>" +
                        "     </a>",
                language == Language.UA ? "" : "/en",
                code);
    }

    private String buildShowButton(String code, Language language) {
        return String.format("<a href=\"%s/admin/pages/%s\">" +
                        "         <button class=\"btn btn-primary\">%s</button>" +
                        "     </a>",
                language == Language.UA ? "" : "/en",
                code,
                StaticTextManager.getShowButtonText(language));
    }

    private String buildUpdateButton(String code, Language language) {
        return String.format("<a href=\"%s/admin/pages/%s/edit\">" +
                        "         <button class=\"btn btn-warning\">%s</button>" +
                        "     </a>",
                language == Language.UA ? "" : "/en",
                code,
                StaticTextManager.getUpdateButtonText(language));
    }

    private String buildDeleteButton(String code, Language language) {
        return String.format("<form action=\"%s/admin/pages/%s\" method=\"post\">" +
                        "         <input name=\"_method\" type=\"hidden\" value=\"delete\" />" +
                        "         <button class=\"btn btn-danger\">%s</button>" +
                        "     </form>",
                language == Language.UA ? "" : "/en",
                code,
                StaticTextManager.getDeleteButtonText(language));
    }

    private String buildTableCell(String text) {
        return String.format("<td class=\"text-center\"><span>%s</span></td>", text);
    }

    private String buildTableHeaderCell(String text) {
        return String.format("<th class=\"text-center\">%s</th>", text);
    }

    @Override
    public AdminPanelPageDto getCreatePage(Language language) {
        String header = buildHeader(language);
        String footer = buildFooter(language);

        return AdminPanelPageDto.builder()
                .header(header)
                .footer(footer)
                .build();
    }

    @Override
    public AdminPanelPageDto getEditPage(Language language) {
        String header = buildHeader(language);
        String footer = buildFooter(language);

        return AdminPanelPageDto.builder()
                .header(header)
                .footer(footer)
                .build();
    }

    @Override
    public Page findPageByCode(String pageCode) {
        return pageRepository.findByCode(pageCode).orElse(new Page());
    }

    private String emptyIfNull(Object object) {
        return object != null
                ? object.toString()
                : "";
    }

    private String ellipsize(String text, int maxWidth) {
        if (text.length() <= maxWidth) {
            return text;
        }
        return text.substring(0, maxWidth - 3).concat("...");
    }
}
