package vimedia.service.ReportApp.controller.tools;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vimedia.service.ReportApp.controller.report.MainController;
import vimedia.service.ReportApp.exceptions.StorageFileNotFoundException;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.report.Views;
import vimedia.service.ReportApp.model.tools.InventoryEvent;
import vimedia.service.ReportApp.model.tools.Tool;
import vimedia.service.ReportApp.repo.report.FacilityRepo;
import vimedia.service.ReportApp.repo.report.UserRepo;
import vimedia.service.ReportApp.repo.tools.EventRepo;
import vimedia.service.ReportApp.repo.tools.InventoryEventRepo;
import vimedia.service.ReportApp.repo.tools.ToolRepo;
import vimedia.service.ReportApp.service.MyDecoderMultiPart;
import vimedia.service.ReportApp.service.MyUserDetails;
import vimedia.service.ReportApp.service.StorageService;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class InventoryEventController {
    private final ToolRepo toolRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final FacilityRepo facilityRepo;
    private final InventoryEventRepo inventoryEventRepo;
    private final StorageService storageService;

    @Autowired
    public InventoryEventController(ToolRepo toolRepo, EventRepo eventRepo, UserRepo userRepo, FacilityRepo facilityRepo, InventoryEventRepo inventoryEventRepo, StorageService storageService) {
        this.toolRepo = toolRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
        this.facilityRepo = facilityRepo;
        this.inventoryEventRepo = inventoryEventRepo;
        this.storageService = storageService;
    }

    // Фильтрация и пагинация
    @GetMapping("/api/inventory")
    @JsonView(Views.IdName.class)
    public List<InventoryEvent> findByName(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                           @RequestParam(value = "limit",required = false) Integer limit)
    {
        List<InventoryEvent> inventoryEvents;

        if (limit != null) {
            inventoryEvents = inventoryEventRepo.findAll(
                    PageRequest.of(offset, limit, Sort.by("dateAndTime"))
            ).stream().collect(Collectors.toList());
        } // сортировка
        else {
            inventoryEvents = inventoryEventRepo.findAll();
        }

        System.out.println(inventoryEvents);

        return inventoryEvents;
    }

    // Поиск по инструменту
    @GetMapping("/api/inventory/tool/{id}")
    @JsonView(Views.IdName.class)
    public List<InventoryEvent> findEventByTool(@PathVariable("id") Tool tool, @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                                @RequestParam(value = "limit",required = false) Integer limit)
    {
        List<InventoryEvent> inventoryEvents;

        if (limit != null) {
            inventoryEvents = inventoryEventRepo.findAll(
                    PageRequest.of(offset, limit, Sort.by("dateAndTime"))
            ).stream()
                    .filter(event -> (event.getTool().getId() == tool.getId()))
                    .collect(Collectors.toList());
        } // сортировка
        else {
            inventoryEvents = inventoryEventRepo.findByTool(tool);
        }

        System.out.println(inventoryEvents);

        return inventoryEvents;
    }

    // Получаем одно событие
    @GetMapping("/api/inventory/{id}")
    @JsonView(Views.IdName.class)
    public InventoryEvent getOne(@PathVariable("id") InventoryEvent event) {
        return event;
    }

    // Создание события
    @PostMapping("/api/inventory")
    public InventoryEvent create(@RequestBody InventoryEvent inventoryEvent, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        inventoryEvent.setDateAndTime(LocalDateTime.now());

        // Находим пользователя
        User user = userRepo.findByName(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        // Вставляем его id в пользователя для события
        user.setId(user.getId());
        inventoryEvent.setUser(user);

        return inventoryEventRepo.save(inventoryEvent);
    }

    // Создание события
    @PutMapping("/api/inventory/{id}")
    @JsonView(Views.IdName.class)
    public InventoryEvent update(@PathVariable("id") InventoryEvent inventoryEventFromDB, // из базы данных
                                 @RequestBody InventoryEvent inventoryEvent) { // от пользователя

        BeanUtils.copyProperties(inventoryEvent,inventoryEventFromDB,"id"); // заменяет поля кроме id

        return inventoryEventRepo.save(inventoryEventFromDB);
    }

    @DeleteMapping("/api/inventory/{id}")
    public void delete(@PathVariable("id") InventoryEvent inventoryEvent) {
        inventoryEventRepo.delete(inventoryEvent);
    }


    ////////////////////// Картинки //////////////////////////////////////////////

    @GetMapping("/api/upload")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(MainController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/api/upload/files/inventory/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/api/upload/inventory")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("eventId") Integer eventId,
                                   RedirectAttributes redirectAttributes) throws IOException {

        // Сжимаем картинку и получаем массив байт
        byte[] array = compressImage(file);

        // Создаем из массива байт картинку
        MyDecoderMultiPart multipartFile = new MyDecoderMultiPart(array);

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String dateAndTime = localDateTime.format(formatter);

        multipartFile.setOriginalFileName("tool_inventory_photo_numb_" + eventId + "_time_" + dateAndTime + ".jpg");

        System.out.println(multipartFile.getSize());


        // Поиск инструмента к которому будет прикрепление
        Optional<InventoryEvent> inventoryEvent = inventoryEventRepo.findById(eventId);

        // Прикрепоение файла к инструменту если инструмент нашелся в базе
        inventoryEvent.ifPresent(value -> value.setImage(multipartFile.getOriginalFilename()));

        if (!multipartFile.isEmpty()) {
            storageService.store(multipartFile);
        }

//        redirectAttributes.addFlashAttribute("message",
//                "You successfully uploaded " + multipartFile.getOriginalFilename() + "!");

        return "redirect:/mainTableToolsAll";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    public byte[] compressImage(MultipartFile image) throws IOException
    {

        InputStream inputStream = image.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        float imageQuality = 0.5f;

        // Create the buffered image
        //  BufferedImage bufferedImageBeforeResizing = ImageIO.read(inputStream); // если нужен resize
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        // Create the buffered image
        //  BufferedImage bufferedImage = resizeImage(bufferedImageBeforeResizing, 800, 600); // если нужен resize

        // Get image writers
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg"); // Input your Format Name here

        if (!imageWriters.hasNext())
            throw new IllegalStateException("Writers Not Found!!");

        ImageWriter imageWriter = imageWriters.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);

        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

        // Set the compress quality metrics
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);

        // Compress and insert the image into the byte array.
        imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

        byte[] imageBytes = outputStream.toByteArray();

        // close all streams
        inputStream.close();
        outputStream.close();
        imageOutputStream.close();
        imageWriter.dispose();


        return imageBytes;
    }
}
