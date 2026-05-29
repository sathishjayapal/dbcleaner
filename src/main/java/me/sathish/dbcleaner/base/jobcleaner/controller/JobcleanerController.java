package me.sathish.dbcleaner.base.jobcleaner.controller;

import jakarta.validation.Valid;
import me.sathish.dbcleaner.base.JobcleanerService;
import me.sathish.dbcleaner.base.jobcleaner.model.JobcleanerDTO;
import me.sathish.dbcleaner.base.util.WebUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/jobcleaners")
public class JobcleanerController {

    private final JobcleanerService jobcleanerService;

    public JobcleanerController(final JobcleanerService jobcleanerService) {
        this.jobcleanerService = jobcleanerService;
    }

    @GetMapping
    public String list(@RequestParam(name = "filter", required = false) final String filter,
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final Page<JobcleanerDTO> jobcleaners = jobcleanerService.findAll(filter, pageable);
        model.addAttribute("jobcleaners", jobcleaners);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(jobcleaners));
        return "jobcleaner/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("jobcleaner") final JobcleanerDTO jobcleanerDTO) {
        return "jobcleaner/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("jobcleaner") @Valid final JobcleanerDTO jobcleanerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jobcleaner/add";
        }
        jobcleanerService.create(jobcleanerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jobcleaner.create.success"));
        return "redirect:/jobcleaners";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("jobcleaner", jobcleanerService.get(id));
        return "jobcleaner/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("jobcleaner") @Valid final JobcleanerDTO jobcleanerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jobcleaner/edit";
        }
        jobcleanerService.update(id, jobcleanerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jobcleaner.update.success"));
        return "redirect:/jobcleaners";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        jobcleanerService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("jobcleaner.delete.success"));
        return "redirect:/jobcleaners";
    }

}
