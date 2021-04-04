package ua.kpi.fict.cms.service;

import ua.kpi.fict.cms.dto.response.PageDto;
import ua.kpi.fict.cms.entity.enums.Language;

public interface PageService {

    String purifyPageCode(String pageCode);

    PageDto render(Language language, String pageCode);
}
