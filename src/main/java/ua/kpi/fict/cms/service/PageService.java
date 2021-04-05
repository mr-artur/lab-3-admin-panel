package ua.kpi.fict.cms.service;

import ua.kpi.fict.cms.dto.response.PageDto;
import ua.kpi.fict.cms.entity.Page;
import ua.kpi.fict.cms.entity.enums.Language;

import java.util.List;

public interface PageService {

    List<Page> findChildPages(String parentCode);

    void save(Page page);

    void update(Page page);

    void delete(String pageCode);

    Page findPagByCode(String pageCode);

    String purifyPageCode(String pageCode);

    PageDto render(Language language, String pageCode);
}
