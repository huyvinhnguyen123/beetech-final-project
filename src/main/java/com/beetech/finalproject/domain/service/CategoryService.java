package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.Category;
import com.beetech.finalproject.domain.entities.CategoryImage;
import com.beetech.finalproject.domain.entities.ImageForCategory;
import com.beetech.finalproject.domain.repository.CategoryImageRepository;
import com.beetech.finalproject.domain.repository.CategoryRepository;
import com.beetech.finalproject.domain.repository.ImageForCategoryRepository;
import com.beetech.finalproject.exception.ValidFileExtensionException;
import com.beetech.finalproject.web.dtos.category.CategoryCreateDto;
import com.beetech.finalproject.web.dtos.category.CategoryRetrieveDto;
import com.beetech.finalproject.web.dtos.category.CategoryUpdateDto;
import com.beetech.finalproject.web.dtos.category.ImageRetrieveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageForCategoryRepository imageForCategoryRepository;
    private final CategoryImageRepository categoryImageRepository;

    // get directory from source
    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    /**
     * upload image for category
     *
     * @param file - input image(only accept .jpg)
     * @return url
     */
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();

            // Check file extension
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!fileExtension.equalsIgnoreCase("jpg")) {
                throw new ValidFileExtensionException("Invalid file format. Only JPG files are allowed.");
            }

            // Get the value of the file.upload.directory property
            String uploadDirectory = "src/main/resources/upload/category";

            // Create the upload directory if it doesn't exist
            Path uploadDirectoryPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadDirectoryPath)) {
                Files.createDirectories(uploadDirectoryPath);
            }

           Files.copy(file.getInputStream(), uploadDirectoryPath.resolve(file.getOriginalFilename()));

            String destinationPath = uploadDirectory + File.separator + fileName;
            String fileUrl = destinationPath.substring(destinationPath.lastIndexOf(File.separator) + 1);
            return fileUrl;
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }


    /**
     * create new category
     *
     * @param categoryCreateDto - input categoryCreateDto's properties
     * @return - category
     */
    @Transactional
    public Category createCategory(CategoryCreateDto categoryCreateDto) {
        Category category = new Category();
        category.setCategoryName(categoryCreateDto.getCategoryName());
        categoryRepository.save(category);
        log.info("Save new category success!");

        ImageForCategory imageForCategory = new ImageForCategory();
        imageForCategory.setPath(uploadFile(categoryCreateDto.getImage()));
        imageForCategory.setName(categoryCreateDto.getImage().getOriginalFilename());
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

    /**
     * find all categories
     *
     * @return
     */
    public Iterable<CategoryRetrieveDto> findAllCategories() {
        List<CategoryRetrieveDto> categoryRetrieveDtos = new ArrayList<>();

        List<Category> categories = categoryRepository.findAllCategories();
        for (Category c : categories) {
            CategoryRetrieveDto categoryRetrieveDto = new CategoryRetrieveDto();
            categoryRetrieveDto.setCategoryId(c.getCategoryId());
            categoryRetrieveDto.setCategoryName(c.getCategoryName());

            List<ImageForCategory> imageForCategories = new ArrayList<>();
            for (CategoryImage ci : c.getCategoryImages()) {
                imageForCategories.add(ci.getImageForCategory());
            }

            List<ImageRetrieveDto> imageRetrieveDtos = new ArrayList<>();
            for(ImageForCategory ifc: imageForCategories) {
                ImageRetrieveDto imageRetrieveDto = new ImageRetrieveDto();
                imageRetrieveDto.setName(ifc.getName());
                imageRetrieveDto.setPath(ifc.getPath());
                imageRetrieveDtos.add(imageRetrieveDto);
            }

            categoryRetrieveDto.setImageRetrieveDtos(imageRetrieveDtos);
            categoryRetrieveDtos.add(categoryRetrieveDto);
        }

        log.info("find all categories success!");
        return categoryRetrieveDtos;
    }
}
