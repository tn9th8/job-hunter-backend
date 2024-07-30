package vn.nhannt.jobhunter.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.nhannt.jobhunter.util.error.StorageException;

@Service
public class FileService {

    @Value("${hoidanit.upload-file.base-uri}")
    private String baseURI;

    private final List<String> allowedExtensions = Arrays.asList(
            "pdf", "jpg", "jpeg", "png", "doc", "docx");

    // TO DO: check nhiều case khác nữa
    public void validate(MultipartFile file) throws StorageException {
        if (file == null || file.isEmpty()) {
            throw new StorageException(
                    "File is empty. Please upload a file");
        }

        boolean isValidExtension = allowedExtensions.stream().anyMatch(ext -> {
            return file.getOriginalFilename().toLowerCase().endsWith("." + ext);
        });
        if (!isValidExtension) {
            throw new StorageException(
                    "Only accept file extensions: pdf, jpg, jpeg, png, doc, docx");
        }
    }

    // TO DO catch exception
    public void createDirectory(String folder) throws URISyntaxException {
        // TO DO phân biệt URI, URL, Path
        /*
         * URI: là đường link truy cập file, có giao thức file:///
         * Path: là việc sử dụng 1 file trên máy tính của mình
         */
        URI uri = new URI(baseURI + folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + tmpDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException {
        // create unique filename
        final String fileName = System.currentTimeMillis() + "-"
                + file.getOriginalFilename().toLowerCase()
                        .replace(" ", "-")
                        .replaceAll("[^\\p{ASCII}]", "x");
        // .replaceAll("[^a-zA-Z0-9]", "");
        final URI uri = new URI(baseURI + folder + "/" + fileName);
        final Path path = Paths.get(uri);
        try (final InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;

    }

}
