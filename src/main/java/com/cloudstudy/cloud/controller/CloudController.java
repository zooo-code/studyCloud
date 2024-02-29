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

    /**
     * 메인 화면에서 사용하는 컨트롤러입니다.
     * @param model 클라우드 도메인의 모든 정보를 담아 넘겨주기 위한 model 입니다.
     * @return
     */
    @GetMapping
    public String cloudStudy(Model model){
        List<Cloud> all = cloudRepository.findAll();

        model.addAttribute("clouds", all);
        return "main";
    }


    /**
     * 요청이 들어온 cloudId 를 통해 Cloud 도메인을 조회합니다.
     * 그 후 cloudId 를 기반으로 파일 저장소에서 연관된 파일을 불러옵니다.
     * 불러운 파일을 cloud 도메인에 주입하고 model 에 담아줍니다.
     * @param cloudId 조회에 필요한 cloudId 입니다.
     * @param model 템플릿 엔진에 cloud 를 담아 넘겨줍니다.
     * @return
     */
    @GetMapping("/{cloudId}")
    public String cloud(@PathVariable long cloudId, Model model) {
        Cloud cloud = cloudRepository.findById(cloudId);
        List<CloudFile> byIds = cloudFileRepository.findByIds(cloudId);
        cloud.setCloudFiles(byIds);
        model.addAttribute("cloud", cloud);
        return "cloud";
    }

    /**
     * 이미지 파일에 대한 조회를 하기 위한 메서드 입니다.
     * /cloudStudy/{cloudId} 의 템플릿인 cloud 에서 이미지 조회를 위해 사용합니다.
     * @param filename 조회할 파일 이미지의 uuid 입니다.
     * @return
     * @throws IOException
     */
    @ResponseBody
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws IOException {

        InputStream inputStream = fileService.downloadImage(filename); // 이미지를 다운로드하는 메서드 호출

        // 이미지를 InputStreamResource로 변환하여 반환
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    /**
     * CloudForm 을 client 에게 전달하는 GetMapping 입니다.
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("cloudForm", new CloudForm());
        return "addForm";
    }

    /**
     * cloud 도메인을 저장하기 위한 도메인입니다.
     * cloud 도메인에 필요한 정보 뿐 아니라 파일 저장에 필요한 List<MultipartFile> imageFiles 을
     * 담고 있어 파일에 대한 저장도 진행합니다.
     * @param cloudForm
     * @param redirectAttributes 저장을 진행한 후 redirect:/cloudStudy/{cloudId} 로 redirect 를 진행합니다.
     * @return
     */
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






    /**
     * 수정할 도메인의 Id 를 기반으로 도메인을 조회하고 수정을 진행합니다.
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Cloud cloud = cloudRepository.findById(itemId);
        model.addAttribute("cloud", cloud);

        return "editForm";
    }

    /**
     * 수정한 내용을 저장합니다.
     * @param cloudId
     * @param cloud
     * @return
     */
    @PostMapping("/{cloudId}/edit")
    public String edit(@PathVariable Long cloudId, @ModelAttribute Cloud cloud) {
        cloudRepository.update(cloudId, cloud);
        return "redirect:/cloudStudy/{cloudId}";
    }
}
