package com.beetech.finalproject.domain.service;

import com.beetech.finalproject.common.DeleteFlag;
import com.beetech.finalproject.domain.entities.Category;
import com.beetech.finalproject.domain.entities.ImageForProduct;
import com.beetech.finalproject.domain.entities.Product;
import com.beetech.finalproject.domain.entities.ProductImage;
import com.beetech.finalproject.domain.repository.CategoryRepository;
import com.beetech.finalproject.domain.repository.ImageForProductRepository;
import com.beetech.finalproject.domain.repository.ProductImageRepository;
import com.beetech.finalproject.domain.repository.ProductRepository;
import com.beetech.finalproject.web.dtos.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageForProductRepository imageForProductRepository;
    private final ProductImageRepository productImageRepository;

    /**
     * upload image for product
     *
     * @param file - input image(only accept .jpg)
     * @return url
     */
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();

            // Get the value of the file.upload.directory property
            String uploadDirectory = "src/main/resources/upload/product";

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

    public Product createProduct(ProductCreateDto productCreateDto) {
        Product product = new Product();
        product.setSku(productCreateDto.getSku());
        product.setProductName(productCreateDto.getProductName());
        product.setDetailInfo(productCreateDto.getDetailInfo());
        product.setPrice(productCreateDto.getPrice());

        List<Category> categoryForProductList = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();
        for(Category c: categories) {
            if(c.getCategoryId().equals(productCreateDto.getCategoryId())) {
                categoryForProductList.add(c);
            }
        }

        product.setCategories(categoryForProductList);
        product.setDeleteFlag(DeleteFlag.NON_DELETE.getCode());
        productRepository.save(product);
        log.info("Save product success!");

        ImageForProduct imageForProduct = new ImageForProduct();
        imageForProduct.setPath(uploadFile(productCreateDto.getThumbnailImage()));
        imageForProduct.setName(productCreateDto.getThumbnailImage().getOriginalFilename());
        imageForProductRepository.save(imageForProduct);
        log.info("Save image for product success");

        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageForProduct(imageForProduct);
        productImageRepository.save(productImage);
        log.info("Save product image success");

        log.info("create product success");
        return product;
    }

    public Page<ProductRetrieveDto> findAllProductsAndPagination(ProductSearchInputDto productSearchInputDto,
                                                                 Pageable pageable) {

        Page<Product> products = productRepository.findAllProductsAndPagination(productSearchInputDto.getCategoryId(),
                productSearchInputDto.getSku(), productSearchInputDto.getProductName(),
                pageable);

        return products.map(product -> {
            ProductRetrieveDto productRetrieveDto = new ProductRetrieveDto();
            productRetrieveDto.setProductId(product.getProductId());
            productRetrieveDto.setProductName(product.getProductName());
            productRetrieveDto.setSku(product.getSku());
            productRetrieveDto.setDetailInfo(product.getDetailInfo());
            productRetrieveDto.setPrice(product.getPrice());

            List<ImageForProduct> imageForProducts = new ArrayList<>();
            for(ProductImage productImage: product.getProductImages()) {
                imageForProducts.add(productImage.getImageForProduct());
            }

            List<ImageRetrieveDto> imageRetrieveDtos = new ArrayList<>();
            for(ImageForProduct ifp: imageForProducts) {
                ImageRetrieveDto imageRetrieveDto = new ImageRetrieveDto();
                imageRetrieveDto.setName(ifp.getName());
                imageRetrieveDto.setPath(ifp.getPath());
                imageRetrieveDtos.add(imageRetrieveDto);
            }

            productRetrieveDto.setImageRetrieveDtos(imageRetrieveDtos);

            log.info("Find all product by category success");
            return productRetrieveDto;
        });
    }
}
