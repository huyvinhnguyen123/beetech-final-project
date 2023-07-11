package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.Category;
import com.beetech.finalproject.domain.entities.CategoryImage;
import com.beetech.finalproject.domain.entities.ImageForCategory;
import com.beetech.finalproject.domain.repository.CategoryImageRepository;
import com.beetech.finalproject.domain.repository.CategoryRepository;
import com.beetech.finalproject.domain.repository.ImageForCategoryRepository;
import com.beetech.finalproject.exception.ValidTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageForCategoryRepository imageForCategoryRepository;
    private final CategoryImageRepository categoryImageRepository;

    // get directory from source
    @Value("${file.upload.directory}")
    private String uploadDirectory;

    /**
     * upload image for category
     *
     * @param file - input image(only accept .jpg)
     * @return url
     */
    public String uploadFile(MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();

            // Check file extension
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!fileExtension.equalsIgnoreCase("jpg")) {
                throw new ValidFileExtensionException("Invalid file format. Only JPG files are allowed.");
            }

            String destinationPath = uploadDirectory + fileName;
            File destination = new File(destinationPath);
            file.transferTo(destination);

            String fileUrl = destinationPath.substring(destinationPath.lastIndexOf("/") + 1);
            return fileUrl;
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    /**
     * create new category
     *
     * @param categoryName - input category's name
     * @param file - input image
     * @return - category
     */
    @Transactional
    public Category createCategory(String categoryName, MultipartFile file) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        categoryRepository.save(category);
        log.info("Save new category success!");

        ImageForCategory imageForCategory = new ImageForCategory();
        imageForCategory.setPath(uploadFile(file));
        imageForCategory.setName(file.getOriginalFilename());
        imageForCategoryRepository.save(imageForCategory);
        log.info("Save new image for category success!");

        CategoryImage categoryImage = new CategoryImage();
        categoryImage.setCategory(category);
        categoryImage.setImageForCategory(imageForCategory);
        categoryImageRepository.save(categoryImage);
        log.info("Save new category and image success!");

        log.info("Create category success!");
        return category;
    }


}
