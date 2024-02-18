package com.cloudstudy.cloud.controller;

import com.cloudstudy.cloud.domain.Cloud;
import com.cloudstudy.cloud.repository.CloudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cloudStudy")
public class CloudController {

    private final CloudRepository cloudRepository;

    @GetMapping
    public String cloudStudy(Model model){
        List<Cloud> all = cloudRepository.findAll();

        model.addAttribute("clouds", all);
        return "main";
    }



    @GetMapping("/{cloudId}")
    public String cloud(@PathVariable long cloudId, Model model) {
        Cloud cloud = cloudRepository.findById(cloudId);
        model.addAttribute("cloud", cloud);
        return "cloud";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("cloud", new Cloud());
        return "addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Cloud cloud, RedirectAttributes redirectAttributes) {

        Cloud savedCloud = cloudRepository.save(cloud);
        redirectAttributes.addAttribute("cloudId", savedCloud.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/cloudStudy/{cloudId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Cloud cloud = cloudRepository.findById(itemId);
        model.addAttribute("cloud", cloud);

        return "editForm";
    }

    @PostMapping("/{cloudId}/edit")
    public String edit(@PathVariable Long cloudId, @ModelAttribute Cloud cloud) {
        cloudRepository.update(cloudId, cloud);
        return "redirect:/cloudStudy/{cloudId}";
    }
}
