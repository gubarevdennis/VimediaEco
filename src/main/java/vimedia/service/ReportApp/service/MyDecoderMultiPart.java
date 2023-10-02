package vimedia.service.ReportApp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class MyDecoderMultiPart implements MultipartFile {
    private final byte[] imgContent;
    private String name;
    private String originalFileName;
    private String contentType;

    public MyDecoderMultiPart(byte[] imgContent) {
        this.imgContent = imgContent;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFileName;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
