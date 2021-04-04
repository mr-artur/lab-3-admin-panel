package ua.kpi.fict.cms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.kpi.fict.cms.dto.response.PageDto;
import ua.kpi.fict.cms.entity.enums.Language;
import ua.kpi.fict.cms.service.PageService;

@RequiredArgsConstructor
@Log4j2
@Controller
public class PageController {

    private final PageService pageService;

    @GetMapping(value = "/{pageCode}")
    public String getUaPage(@PathVariable String pageCode, Model model) {
        log.info("handling ua request with pageCode: {}", pageCode);
        String purePageCode = pageService.purifyPageCode(pageCode);
        if (!purePageCode.equals(pageCode)) {
            log.info("redirecting from {} to {}", pageCode, purePageCode);
            return "redirect:/" + purePageCode;
        }
        buildPage(pageCode, Language.UA, model);
        return "base_template";
    }

    @GetMapping(value = "/en/{pageCode}")
    public String getEnPage(@PathVariable String pageCode, Model model) {
        log.info("handling en request with pageCode: {}", pageCode);
        String purePageCode = pageService.purifyPageCode(pageCode);
        if (!purePageCode.equals(pageCode)) {
            log.info("redirecting from {} to {}", pageCode, purePageCode);
            return "redirect:/en/" + purePageCode;
        }
        buildPage(pageCode, Language.EN, model);
        return "base_template";
    }

    private void buildPage(String pageCode, Language language, Model model) {
        PageDto page = pageService.render(language, pageCode);
        model.addAttribute("language", language);
        model.addAttribute("meta", page.getMeta());
        model.addAttribute("header", page.getHeader());
        model.addAttribute("subheader", page.getSubheader());
        model.addAttribute("title", page.getTitle());
        model.addAttribute("imageUrl", page.getImageUrl());
        model.addAttribute("content", page.getContent());
        model.addAttribute("footer", page.getFooter());
    }

    @GetMapping(value = "/en")
    public String getEnRootPage(Model model) {
        log.info("handling en root page request");
        return getEnPage("root", model);
    }

    @GetMapping
    public String getUaRootPage(Model model) {
        log.info("handling ua root page request");
        return getUaPage("root", model);
    }

    @GetMapping(value = "favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
