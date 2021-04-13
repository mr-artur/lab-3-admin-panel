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
    private static final String EN_PREFIX = "/en";

    private final PageService pageService;

    @GetMapping(value = ADMIN_PREFIX + "/pages")
    public String indexUa(@RequestParam(value = "parentCode", required = false) String parentCode,
                          @RequestParam(value = "saved", required = false) boolean saved,
                          @RequestParam(value = "updated", required = false) boolean updated,
                          @RequestParam(value = "deleted", required = false) boolean deleted,
                          Model model) {

        log.info("Request to show ua pages list for parent code : {}", parentCode);
        indexAction(parentCode, saved, updated, deleted, model, Language.UA);
        return "admin/index";
    }

    @GetMapping(value = ADMIN_PREFIX + "/pages/create")
    public String createUa(@RequestParam(value = "parentCode", required = false) String parentCode,
                           @ModelAttribute("page") Page page,
                           Model model) {

        log.info("Request to get create ua page");
        createAction(parentCode, model, Language.UA);
        return "admin/create";
    }

    @PostMapping(value = ADMIN_PREFIX + "/pages")
    public String storeUa(@RequestParam(value = "parentCode", required = false) String parentCode,
                          @ModelAttribute("page") Page page) {

        log.info("Request to save ua page : {}", page);
        storeAction(parentCode, page);
        return "redirect:/admin/pages?saved=true&parentCode=" + page.getParentPage().getCode();
    }

    @GetMapping(value = ADMIN_PREFIX + "/pages/{pageCode}")
    public String showUa(@PathVariable String pageCode, Model model) {
        log.info("Request to show ua page with code : {}", pageCode);
        return getUaPage(pageCode, model);
    }

    @GetMapping(value = ADMIN_PREFIX + "/pages/{pageCode}/edit")
    public String editUa(@PathVariable String pageCode,
                         Model model) {

        log.info("Request to get edit ua page for code : {}", pageCode);
        editAction(pageCode, model, Language.UA);
        return "admin/edit";
    }

    @PutMapping(value = ADMIN_PREFIX + "/pages/{pageCode}")
    public String updateUa(@ModelAttribute("page") Page page) {
        log.info("Request to update ua page : {}", page);
        updateAction(page);
        return "redirect:/admin/pages?updated=true&parentCode=" + page.getParentPage().getCode();
    }

    @DeleteMapping(value = ADMIN_PREFIX + "/pages/{pageCode}")
    public String destroyUa(@PathVariable String pageCode) {
        log.debug("Request to delete ua page with code : {}", pageCode);
        Page page = destroyAction(pageCode);
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

    private void indexAction(String parentCode,
                             boolean saved,
                             boolean updated,
                             boolean deleted,
                             Model model,
                             Language language) {

        MessageType messageType = getMessageType(saved, updated, deleted);
        AdminPanelPageDto page = pageService.getIndexPage(parentCode, language, messageType);
        buildAdminPanelPageModel(page, model);
    }

    private void createAction(String parentCode,
                              Model model,
                              Language language) {

        AdminPanelPageDto headerFooter = pageService.getCreatePage(language);
        model.addAttribute("parentCode", parentCode);
        model.addAttribute("header", headerFooter.getHeader());
        model.addAttribute("footer", headerFooter.getFooter());
    }

    private void storeAction(String parentCode, Page page) {
        page.setParentPage(Page.builder().code(parentCode).build());
        pageService.save(page);
    }

    private void editAction(String pageCode,
                            Model model,
                            Language language) {

        Page page = pageService.findPageByCode(pageCode);
        AdminPanelPageDto headerFooter = pageService.getEditPage(language);
        model.addAttribute("page", page);
        model.addAttribute("header", headerFooter.getHeader());
        model.addAttribute("footer", headerFooter.getFooter());
    }

    private void updateAction(Page page) {
        pageService.update(page);
    }

    private Page destroyAction(String pageCode) {
        Page page = pageService.findPageByCode(pageCode);
        pageService.delete(page);
        return page;
    }

    @GetMapping(value = EN_PREFIX + ADMIN_PREFIX + "/pages")
    public String indexEn(@RequestParam(value = "parentCode", required = false) String parentCode,
                          @RequestParam(value = "saved", required = false) boolean saved,
                          @RequestParam(value = "updated", required = false) boolean updated,
                          @RequestParam(value = "deleted", required = false) boolean deleted,
                          Model model) {

        log.info("Request to show en pages list for parent code : {}", parentCode);
        indexAction(parentCode, saved, updated, deleted, model, Language.EN);
        return "admin/index";
    }

    @GetMapping(value = EN_PREFIX + ADMIN_PREFIX + "/pages/create")
    public String createEn(@RequestParam(value = "parentCode", required = false) String parentCode,
                           @ModelAttribute("page") Page page,
                           Model model) {

        log.info("Request to get create en page");
        createAction(parentCode, model, Language.EN);
        return "admin/create";
    }

    @PostMapping(value = EN_PREFIX + ADMIN_PREFIX + "/pages")
    public String storeEn(@RequestParam(value = "parentCode", required = false) String parentCode,
                          @ModelAttribute("page") Page page) {

        log.info("Request to save en page : {}", page);
        storeAction(parentCode, page);
        return "redirect:/en/admin/pages?saved=true&parentCode=" + page.getParentPage().getCode();
    }

    @GetMapping(value = EN_PREFIX + ADMIN_PREFIX + "/pages/{pageCode}")
    public String showEn(@PathVariable String pageCode, Model model) {
        log.info("Request to show en page with code : {}", pageCode);
        return getEnPage(pageCode, model);
    }

    @GetMapping(value = EN_PREFIX + ADMIN_PREFIX + "/pages/{pageCode}/edit")
    public String editEn(@PathVariable String pageCode,
                         Model model) {

        log.info("Request to get edit en page for code : {}", pageCode);
        editAction(pageCode, model, Language.EN);
        return "admin/edit";
    }

    @PutMapping(value = EN_PREFIX + ADMIN_PREFIX + "/pages/{pageCode}")
    public String updateEn(@ModelAttribute("page") Page page) {
        log.info("Request to update en page : {}", page);
        updateAction(page);
        return "redirect:/en/admin/pages?updated=true&parentCode=" + page.getParentPage().getCode();
    }

    @DeleteMapping(value = EN_PREFIX + ADMIN_PREFIX + "/pages/{pageCode}")
    public String destroyEn(@PathVariable String pageCode) {
        log.debug("Request to delete en page with code : {}", pageCode);
        Page page = destroyAction(pageCode);
        return "redirect:/en/admin/pages?deleted=true&parentCode=" + page.getParentPage().getCode();
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
