package ua.kpi.fict.cms.service;

import ua.kpi.fict.cms.dto.response.AdminPanelPageDto;
import ua.kpi.fict.cms.dto.response.PageDto;
import ua.kpi.fict.cms.entity.MessageType;
import ua.kpi.fict.cms.entity.Page;
import ua.kpi.fict.cms.entity.enums.Language;

import java.util.List;

public interface PageService {

    List<Page> findChildPages(String parentCode);

    void save(Page page);

    void update(Page page);

    void delete(Page page);

    AdminPanelPageDto getIndexPage(Language language, MessageType messageType);

    AdminPanelPageDto getCreatePage(Language language);

    AdminPanelPageDto getEditPage(Language language);

    Page findPageByCode(String pageCode);

    String purifyPageCode(String pageCode);

    PageDto render(Language language, String pageCode);
}
