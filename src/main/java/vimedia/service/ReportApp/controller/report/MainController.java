package vimedia.service.ReportApp.controller.report;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vimedia.service.ReportApp.exceptions.StorageFileNotFoundException;
import vimedia.service.ReportApp.model.report.User;
import vimedia.service.ReportApp.model.tools.Tool;
import vimedia.service.ReportApp.repo.report.UserRepo;
import vimedia.service.ReportApp.repo.tools.ToolRepo;
import vimedia.service.ReportApp.service.MyDecoderMultiPart;
import vimedia.service.ReportApp.service.MyUserDetails;
import vimedia.service.ReportApp.service.StorageService;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserRepo userRepo;
    private final StorageService storageService;
    private final ToolRepo toolRepo;

    @Autowired
    public MainController(UserRepo userRepo, StorageService storageService, ToolRepo toolRepo) {
        this.userRepo = userRepo;
        this.storageService = storageService;
        this.toolRepo = toolRepo;
    }


    @GetMapping
    public String getIndex(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        HashMap<Object, Object> data = new HashMap<>();
        if(myUserDetails != null) {
            data.put("name", myUserDetails.getUsername());

            data.put("id", userRepo.findByName(myUserDetails.getUsername()).orElseThrow(
                    () -> new UsernameNotFoundException("Пользователь не найден!")).getId());
            data.put("role", myUserDetails.getAuthorities());
        }
        model.addAttribute("frontendData", data);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        List<User> users = userRepo.findAll().stream().sorted(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        }).collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("sessionId", session.getId());
        return "login";
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(MainController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/upload/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("toolId") Integer toolId,
                                   RedirectAttributes redirectAttributes) throws IOException {

        // Сжимаем картинку и получаем массив байт
        byte[] array = compressImage(file);

        // Создаем из массива байт картинку
        MyDecoderMultiPart multipartFile = new MyDecoderMultiPart(array);

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String dateAndTime = localDateTime.format(formatter);

        multipartFile.setOriginalFileName("tool_general_photo_numb_" + toolId + "_time_" + dateAndTime + ".jpg");

        System.out.println(multipartFile.getSize());


        // Поиск инструмента к которому будет прикрепление
        Optional<Tool> tool = toolRepo.findById(toolId);

        // Прикрепоение файла к инструменту если инструмент нашелся в базе
        tool.ifPresent(value -> value.setImage(multipartFile.getOriginalFilename()));

        if (!multipartFile.isEmpty())
        storageService.store(multipartFile);

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + multipartFile.getOriginalFilename() + "!");

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

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
//          graphics2D.rotate(Math.toRadians(90), 800.0 / 2, 600.0 / 2);
            graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            graphics2D.dispose();
            return resizedImage;
        }

}
