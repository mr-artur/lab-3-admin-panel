package ua.kpi.fict.cms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kpi.fict.cms.dto.response.AdminPanelPageDto;
import ua.kpi.fict.cms.dto.response.PageDto;
import ua.kpi.fict.cms.entity.MessageType;
import ua.kpi.fict.cms.entity.Page;
import ua.kpi.fict.cms.entity.enums.Language;
import ua.kpi.fict.cms.service.PageService;

@RequiredArgsConstructor
@Log4j2
@Controller
public class PageController {

    private static final String ADMIN_PREFIX = "/admin";

    private final PageService pageService;

    @GetMapping(value = ADMIN_PREFIX + "/pages")
    public String index(@RequestParam(value = "parentCode", required = false) String parentCode,
                        @RequestParam(value = "saved",required = false) boolean saved,
                        @RequestParam(value = "updated",required = false) boolean updated,
                        @RequestParam(value = "deleted",required = false) boolean deleted,
                        Model model) {

        log.info("Request to show pages list for parent code : {}", parentCode);
        MessageType messageType = getMessageType(saved, updated, deleted);
        AdminPanelPageDto page = pageService.getIndexPage(parentCode, Language.UA, messageType);
        buildAdminPanelPageModel(page, model);
        return "admin/index";
    }

    @GetMapping(value = ADMIN_PREFIX + "/pages/create")
    public String create(@RequestParam(value = "parentCode", required = false) String parentCode,
                         @ModelAttribute("page") Page page,
                         Model model) {

        log.info("Request to get create page");
        AdminPanelPageDto headerFooter = pageService.getCreatePage(Language.UA);
        model.addAttribute("parentCode", parentCode);
        model.addAttribute("header", headerFooter.getHeader());
        model.addAttribute("footer", headerFooter.getFooter());
        return "admin/create";
    }

    @PostMapping(value = ADMIN_PREFIX + "/pages")
    public String store(@RequestParam(value = "parentCode", required = false) String parentCode,
                        @ModelAttribute("page") Page page) {

        log.info("Request to save page : {}", page);
        page.setParentPage(Page.builder().code(parentCode).build());
        pageService.save(page);
        return "redirect:/admin/pages?saved=true&parentCode=" + page.getParentPage().getCode();
    }

    @GetMapping(value = ADMIN_PREFIX + "/pages/{pageCode}")
    public String show(@PathVariable String pageCode, Model model) {
        log.info("Request to show page with code : {}", pageCode);
        return getUaPage(pageCode, model);
    }

    @GetMapping(value = ADMIN_PREFIX + "/pages/{pageCode}/edit")
    public String edit(@ModelAttribute("page") Page page,
                       @PathVariable String pageCode,
                       Model model) {

        log.info("Request to get edit page for code : {}", pageCode);
        AdminPanelPageDto headerFooter = pageService.getEditPage(Language.UA);
        model.addAttribute("header", headerFooter.getHeader());
        model.addAttribute("footer", headerFooter.getFooter());
        return "admin/edit";
    }

    @PutMapping(value = ADMIN_PREFIX + "/pages/{pageCode}")
    public String update(@ModelAttribute("page") Page page) {
        log.info("Request to update page : {}", page);
        pageService.update(page);
        return "redirect:/admin/pages?updated=true&parentCode=" + page.getParentPage().getCode();
    }

    @DeleteMapping(value = ADMIN_PREFIX + "/pages/{pageCode}")
    public String destroy(@PathVariable String pageCode) {
        Page page = pageService.findPageByCode(pageCode);
        log.debug("Request to delete page with code : {}", pageCode);
        pageService.delete(page);
        return "redirect:/admin/pages?deleted=true&parentCode=" + page.getParentPage().getCode();
    }

    private MessageType getMessageType(boolean saved, boolean updated, boolean deleted) {
        MessageType messageType = null;
        if (saved) {
            messageType = MessageType.SAVED;
        }
        if (updated) {
            messageType = MessageType.UPDATED;
        }
        if (deleted) {
            messageType = MessageType.DELETED;
        }
        return messageType;
    }

    private void buildAdminPanelPageModel(AdminPanelPageDto page, Model model) {
        model.addAttribute("header", page.getHeader());
        model.addAttribute("content", page.getContent());
        model.addAttribute("footer", page.getFooter());
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
