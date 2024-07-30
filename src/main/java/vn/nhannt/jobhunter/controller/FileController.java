package vn.nhannt.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.nhannt.jobhunter.domain.response.ResUploadFileDto;
import vn.nhannt.jobhunter.service.FileService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.StorageException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // TO DO: download file

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDto> uploadSingleFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder)
            throws URISyntaxException, IOException, StorageException {
        // validate
        this.fileService.validate(file);
        // create a directory if not exist
        this.fileService.createDirectory(folder);

        // store file
        final String uploadedFile = this.fileService.store(file, folder);
        final ResUploadFileDto resUploadFile = new ResUploadFileDto(uploadedFile, Instant.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(resUploadFile);
    }

}
