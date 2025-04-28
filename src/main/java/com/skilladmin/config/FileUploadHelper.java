//package com.skilladmin.config;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@Component
//public class FileUploadHelper {
//    
//  ///  public final String FILE_DIR = new ClassPathResource("static/images/").getFile().getAbsolutePath();
//
//    public FileUploadHelper() throws IOException {
//
//    }
//
//        public String saveFile(MultipartFile file) throws IOException {
//            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//            Path targetLocation = Paths.get(FILE_DIR).resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return fileName;
//        }
//
//        public Resource loadFile(String fileName) {
//            try {
//                Path filePath = Paths.get(FILE_DIR).resolve(fileName).normalize();
//                Resource resource = new UrlResource(filePath.toUri());
//                if (resource.exists() || resource.isReadable()) {
//                    return resource;
//                } else {
//                    throw new RuntimeException("File not found: " + fileName);
//                }
//            } catch (MalformedURLException ex) {
//                throw new RuntimeException("File not found: " + fileName, ex);
//            }
//        }
//}
