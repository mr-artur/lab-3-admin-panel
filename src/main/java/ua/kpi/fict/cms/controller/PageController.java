package ua.kpi.fict.cms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kpi.fict.cms.dto.response.PageDto;
import ua.kpi.fict.cms.entity.Page;
import ua.kpi.fict.cms.entity.enums.Language;
import ua.kpi.fict.cms.service.PageService;

@RequiredArgsConstructor
@Log4j2
@Controller
public class PageController {

    private final PageService pageService;

    @GetMapping(value = "/pages", params = {"parentCode"})
    public String index(@RequestParam String parentCode, Model model) {
        log.info("Request to show pages list for parent code : {}", parentCode);
        model.addAttribute("pages", pageService.findChildPages(parentCode));
        return "adminpanel/index";
    }

    @GetMapping(value = "/pages/create")
    public String create(@ModelAttribute("page") Page page) {
        log.info("Request to get create page");
        return "adminpanel/create";
    }

    @PostMapping(value = "/pages")
    public String store(@ModelAttribute("page") Page page) {
        log.info("Request to save page : {}", page);
        pageService.save(page);
        return "redirect:/pages/index?saved&parentCode=" + page.getParentPage().getCode();
    }

    @GetMapping(value = "/pages/{pageCode}")
    public String show(@RequestParam String pageCode, Model model) {
        log.info("Request to show page with code : {}", pageCode);
        return getUaPage(pageCode, model);
    }

    @GetMapping(value = "/pages/{pageCode}/edit")
    public String edit(@ModelAttribute("page") Page page, @RequestParam String pageCode) {
        log.info("Request to get edit page for code : {}", pageCode);
        return "adminpanel/edit";
    }

    @PutMapping(value = "/pages/{pageCode}")
    public String update(@ModelAttribute("page") Page page) {
        log.info("Request to update page : {}", page);
        pageService.update(page);
        return "redirect:/pages/index?updated&parentCode=" + page.getParentPage().getCode();
    }

    @DeleteMapping(value = "/pages/{pageCode}")
    public String destroy(@RequestParam String pageCode) {
        Page page = pageService.findPagByCode(pageCode);
        log.debug("Request to delete page with code : {}", pageCode);
        return "redirect:/pages/index?deleted&parentCode=" + page.getParentPage().getCode();
    }

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
