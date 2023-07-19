package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.domain.entities.Category;
import com.beetech.finalproject.domain.entities.CategoryImage;
import com.beetech.finalproject.domain.entities.ImageForCategory;
import com.beetech.finalproject.domain.entities.Product;
import com.beetech.finalproject.domain.repository.CategoryImageRepository;
import com.beetech.finalproject.domain.repository.CategoryRepository;
import com.beetech.finalproject.domain.repository.ImageForCategoryRepository;
import com.beetech.finalproject.exception.DuplicateException;
import com.beetech.finalproject.exception.ValidFileExtensionException;
import com.beetech.finalproject.web.dtos.category.CategoryCreateDto;
import com.beetech.finalproject.web.dtos.category.CategoryRetrieveDto;
import com.beetech.finalproject.web.dtos.category.CategoryUpdateDto;
import com.beetech.finalproject.web.dtos.category.ImageRetrieveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

            // Check if the file with the same name already exists
            Path filePath = uploadDirectoryPath.resolve(fileName);
            int count = 1;
            while (Files.exists(filePath)) {
                // If the file exists, append (count) before the extension and try again
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" + count + ")." + fileExtension;
                filePath = uploadDirectoryPath.resolve(fileName);
                count++;
            }

            // upload file to folder
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "src/main/resources/upload/category/" + fileName;
            return fileUrl;
        } catch (IOException e) {
            log.error("Failed to upload file: " + e.getMessage());
            return "Failed to upload file";
        }
    }

    /**
     * delete image that's exist in folder
     *
     * @param fileUrl - input file url from upload file
     */
    public void deleteFile(String fileUrl) {
        try {
            Path filePath = Paths.get(fileUrl).toAbsolutePath().normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Deleted file: {}", fileUrl);
            } else {
                log.warn("File not found: {}", fileUrl);
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileUrl);
            e.printStackTrace();
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
     * @return - list categories
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

    /**
     * update category
     *
     * @param categoryId - input categoryId
     * @param categoryUpdateDto - input categoryUpdateDto
     * @return - update category
     */
    @Transactional
    public Category updateCategory(Long categoryId, CategoryUpdateDto categoryUpdateDto) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    log.error("Not found this category");
                    return new NullPointerException("Not found this category: " + categoryId);
                }
        );
        log.info("Found this category");

        existingCategory.setCategoryName(categoryUpdateDto.getCategoryName());
        categoryRepository.save(existingCategory);

        if(categoryUpdateDto.getImage() != null || !categoryUpdateDto.getImage().isEmpty() ) {
            for(CategoryImage ci: existingCategory.getCategoryImages()) {
                deleteFile(ci.getImageForCategory().getPath());
                categoryImageRepository.deleteById(ci.getCategoryImageId());
                log.info("delete category image success!");
                imageForCategoryRepository.deleteById(ci.getImageForCategory().getImageId());
                log.info("delete image for category success!");
            }

            ImageForCategory imageForCategory = new ImageForCategory();
            imageForCategory.setPath(uploadFile(categoryUpdateDto.getImage()));
            imageForCategory.setName(categoryUpdateDto.getImage().getOriginalFilename());
            imageForCategoryRepository.save(imageForCategory);
            log.info("Save new image for category success!");

            CategoryImage categoryImage = new CategoryImage();
            categoryImage.setCategory(existingCategory);
            categoryImage.setImageForCategory(imageForCategory);
            categoryImageRepository.save(categoryImage);
            log.info("Save new category and image success!");
        }
        log.info("Update category success!");
        return existingCategory;
    }

    /**
     * delete category
     *
     * @param categoryId - input categoryId
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    log.error("Not found this category");
                    return new NullPointerException("Not found this category: " + categoryId);
                }
        );
        log.info("Found this category");

        for(Product p: existingCategory.getProducts()) {
            if(p.getProductId() != null ) {
                log.error("The category is still relation with product");
                throw new RuntimeException("The category still relation with product");
            }
        }

        for(CategoryImage ci: existingCategory.getCategoryImages()) {
            deleteFile(ci.getImageForCategory().getPath());
            categoryImageRepository.deleteById(ci.getCategoryImageId());
            log.info("delete category image success!");
            imageForCategoryRepository.deleteById(ci.getImageForCategory().getImageId());
            log.info("delete image for category success!");
            categoryRepository.deleteById(categoryId);
            log.info("delete category success");
        }
    }
}
