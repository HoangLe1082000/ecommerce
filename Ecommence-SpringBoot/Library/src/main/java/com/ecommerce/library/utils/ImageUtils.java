package com.ecommerce.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUtils {

    private final static String UPLOAD_FORDER = "C:\\SPRING\\Ecommence-SpringBoot\\Admin\\src\\main\\resources\\static\\img\\imageProduct";

    public static boolean uploadImage(MultipartFile imageProduct) {
        boolean isUpload = false;
        try{
            Files.copy(imageProduct.getInputStream(),
                    Paths.get(UPLOAD_FORDER + File.separator, imageProduct.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
            isUpload = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return isUpload;
    }

    public boolean checkExitsFile(MultipartFile imageProduct){
        boolean isExitsFile = false;
        try{
            File file = new File(UPLOAD_FORDER + File.separator + imageProduct.getOriginalFilename());
            isExitsFile = file.exists();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return isExitsFile;
    }
}
