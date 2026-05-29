package me.sathish.dbcleaner.base.cleanup;

import me.sathish.dbcleaner.base.JobcleanerService;
import me.sathish.dbcleaner.base.jobcleaner.model.JobcleanerDTO;
import me.sathish.dbcleaner.base.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cleanup")
public class CleanupController {

    private final CleanupService cleanupService;
    private final JobcleanerService jobcleanerService;

    public CleanupController(final CleanupService cleanupService, final JobcleanerService jobcleanerService) {
        this.cleanupService = cleanupService;
        this.jobcleanerService = jobcleanerService;
    }

    @GetMapping
    public String index(
            @SortDefault(sort = "deletedAt") @PageableDefault(size = 50) final Pageable pageable,
            final Model model) {
        final Page<JobcleanerDTO> auditEntries = jobcleanerService.findAllAuditEntries(pageable);
        model.addAttribute("auditEntries", auditEntries);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(auditEntries));
        return "cleanup/index";
    }

    @PostMapping("/run")
    public String runCleanup(final RedirectAttributes redirectAttributes) {
        final CleanupResult result = cleanupService.cleanAll();
        redirectAttributes.addFlashAttribute("cleanupResult", result);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS,
                "Cleanup complete. Deleted " + result.total() + " record(s) total: "
                + result.fileImportRecordsDeleted() + " file import record(s), "
                + result.domainEventsDeleted() + " domain event(s), "
                + result.analysisLogsDeleted() + " analysis log(s).");
        return "redirect:/cleanup";
    }

}
