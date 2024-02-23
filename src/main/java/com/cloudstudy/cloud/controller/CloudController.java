package com.cloudstudy.cloud.controller;

import com.cloudstudy.cloud.domain.Cloud;
import com.cloudstudy.cloud.domain.CloudFile;
import com.cloudstudy.cloud.domain.CloudFileDTO;
import com.cloudstudy.cloud.domain.CloudForm;
import com.cloudstudy.cloud.ncloud.FileService;
import com.cloudstudy.cloud.repository.CloudFileRepository;
import com.cloudstudy.cloud.repository.CloudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/cloudStudy")
public class CloudController {

    private final CloudRepository cloudRepository;
    private final CloudFileRepository cloudFileRepository;
    private final FileService fileService;


    @GetMapping
    public String cloudStudy(Model model){
        List<Cloud> all = cloudRepository.findAll();

        model.addAttribute("clouds", all);
        return "main";
    }



    @GetMapping("/{cloudId}")
    public String cloud(@PathVariable long cloudId, Model model) {
        Cloud cloud = cloudRepository.findById(cloudId);
        List<CloudFile> byIds = cloudFileRepository.findByIds(cloudId);
        cloud.setCloudFiles(byIds);
        model.addAttribute("cloud", cloud);
        return "cloud";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws IOException {

        InputStream inputStream = fileService.downloadImage(filename); // 이미지를 다운로드하는 메서드 호출

        // 이미지를 InputStreamResource로 변환하여 반환
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }


    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("cloudForm", new CloudForm());
        return "addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute CloudForm cloudForm,
                          RedirectAttributes redirectAttributes) {

        Cloud cloud = Cloud.builder()
                .name(cloudForm.getName())
                .build();

        Cloud savedCloud = cloudRepository.save(cloud);

        List<CloudFileDTO> cloudFileDTOS = fileService.uploadFilesSample(cloudForm.getImageFiles());

        cloudFileDTOS.forEach(item -> item.setCloudId(savedCloud.getId()));

        cloudFileRepository.savaAll(cloudFileDTOS);

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
