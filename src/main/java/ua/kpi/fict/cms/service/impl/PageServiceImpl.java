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
                "<a class=\"back-link\" href=\"%s\">← %s</a>",
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
                    "<a href=\"%s\">%s</a>",
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
        pageRepository.save(page);
    }

    @Override
    public void update(Page page) {
        pageRepository.save(page);
    }

    @Override
    public void delete(Page page) {
        pageRepository.delete(page);
    }

    @Override
    public AdminPanelPageDto getIndexPage(Language language, MessageType messageType) {
        String header = buildHeader(language);
        String content = buildIndexPageContent(language);
        String footer = buildFooter(language);

        return AdminPanelPageDto.builder()
                .header(header)
                .content(content)
                .footer(footer)
                .build();
    }

    private String buildIndexPageContent(Language language) {
        return null;
    }

    @Override
    public AdminPanelPageDto getCreatePage(Language language) {
        String header = buildHeader(language);
        String content = buildCreatePageContent(language);
        String footer = buildFooter(language);

        return AdminPanelPageDto.builder()
                .header(header)
                .content(content)
                .footer(footer)
                .build();
    }

    private String buildCreatePageContent(Language language) {
        return null;
    }

    @Override
    public AdminPanelPageDto getEditPage(Language language) {
        String header = buildHeader(language);
        String content = buildEditPageContent(language);
        String footer = buildFooter(language);

        return AdminPanelPageDto.builder()
                .header(header)
                .content(content)
                .footer(footer)
                .build();
    }

    private String buildEditPageContent(Language language) {
        return null;
    }

    @Override
    public Page findPageByCode(String pageCode) {
        return pageRepository.findByCode(pageCode).orElse(new Page());
    }
}
