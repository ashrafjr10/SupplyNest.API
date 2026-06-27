package SupplyNest.Catalog.Service.services;

import SupplyNest.Catalog.Service.controllers.BusinessGroupClient;
import SupplyNest.Catalog.Service.controllers.UserClient;
import SupplyNest.Catalog.Service.dtos.CreateProductRequestDTO;
import SupplyNest.Catalog.Service.models.*;
import SupplyNest.Catalog.Service.repositories.BrandRepository;
import SupplyNest.Catalog.Service.repositories.CategoryRepository;
import SupplyNest.Catalog.Service.repositories.ProductRepository;
import SupplyNest.Common.constants.AppConstants;
import SupplyNest.Common.constants.HeaderConstants;
import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final UserClient userClient;
    private final BusinessGroupClient businessGroupClient;
//    private final FileService fileService;

    public CommonResponse createProduct(CreateProductRequestDTO requestDTO, String businessGroupCode, String businessCode, HttpServletRequest httpServletRequest) {
        CommonResponse userResponse = validateUser(httpServletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        CommonResponse businessResponse = businessGroupClient.getBusiness(businessCode, businessGroupCode, httpServletRequest).getBody();
        if (!businessResponse.getStatus().equals(AppConstants.STATUS_SUCCESS)) {
            return businessResponse;
        }
        Business business = (Business) businessResponse.getData();

        if (!business.getBusinessCode().equals(httpServletRequest.getHeader(HeaderConstants.BUSINESS_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        Optional<Brand> brandOptional = brandRepository.findById(requestDTO.getBrandId());
        if (brandOptional.isEmpty())
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(AppConstants.MESSAGE_NOT_FOUND).build();
        Brand brand = brandOptional.get();

        Optional<Category> categoryOptional = categoryRepository.findById(requestDTO.getCategoryId());
        if (categoryOptional.isEmpty())
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(AppConstants.MESSAGE_NOT_FOUND).build();
        Category category = categoryOptional.get();

        Product product = Product.builder()
                .productName(requestDTO.getProductName())
                .description(requestDTO.getDescription())
                .brand(brand)
                .category(category)
                .businessId(business.getBusinessId())
                .build();

        productRepository.save(product);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(String.format(AppConstants.MESSAGE_CREATED, "Brand")).build();
    }



    private CommonResponse validateUser(HttpServletRequest request){
        UUID userId = UUID.fromString(request.getHeader(HeaderConstants.USER_ID));
        CommonResponse userResponse = userClient.getUser(userId).getBody();
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        User user = (User) userResponse.getData();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(user).build();
    }
}
