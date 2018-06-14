package com.gmail.grind3x;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/")
public class MyController {

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/get_zip", produces = "application/zip")
    public ResponseEntity<byte[]> onGetZip(@RequestParam MultipartFile[] files) {
        try (ByteOutputStream bos = new ByteOutputStream();
             ZipOutputStream zos = new ZipOutputStream(bos);) {
            ZipEntry entry;
            for (MultipartFile file : files) {
                entry = new ZipEntry(file.getOriginalFilename());
                entry.setSize(file.getSize());
                zos.putNextEntry(entry);
                zos.write(file.getBytes());
                zos.closeEntry();
            }
            zos.flush();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-disposition", "attachment; filename=" + System.currentTimeMillis() + ".zip");
            return new ResponseEntity<>(bos.getBytes(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
