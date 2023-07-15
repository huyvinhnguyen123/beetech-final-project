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
