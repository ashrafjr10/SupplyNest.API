package SupplyNest.Catalog.Service.services;

import SupplyNest.Catalog.Service.controllers.BusinessGroupClient;
import SupplyNest.Catalog.Service.controllers.UserClient;
import SupplyNest.Catalog.Service.dtos.CategoryResponseDTO;
import SupplyNest.Catalog.Service.dtos.CreateCategoryRequestDTO;
import SupplyNest.Catalog.Service.models.Business;
import SupplyNest.Catalog.Service.models.Category;
import SupplyNest.Catalog.Service.models.User;
import SupplyNest.Catalog.Service.repositories.CategoryRepository;
import SupplyNest.Common.constants.AppConstants;
import SupplyNest.Common.constants.HeaderConstants;
import SupplyNest.Common.dtos.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BusinessGroupClient businessGroupClient;
    private final UserClient userClient;

    public CommonResponse createCategory(CreateCategoryRequestDTO requestDTO, String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
        CommonResponse userResponse = validateUser(httpServletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        CommonResponse businessGroupResponse = businessGroupClient.getBusinessGroups(businessGroupCode, httpServletRequest).getBody();
        if (!businessGroupResponse.getStatus().equals(AppConstants.STATUS_SUCCESS)) {
            return businessGroupResponse;
        }

        CommonResponse businessResponse = businessGroupClient.getBusiness(businessCode, businessGroupCode, httpServletRequest).getBody();
        if (!businessResponse.getStatus().equals(AppConstants.STATUS_SUCCESS)) {
            return businessResponse;
        }
        Business business = (Business) businessResponse.getData();

        if (!business.getBusinessCode().equals(httpServletRequest.getHeader(HeaderConstants.BUSINESS_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        Category category = Category.builder()
                .categoryName(requestDTO.getCategoryName())
                .businessId(business.getBusinessId())
                .build();

        categoryRepository.save(category);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(String.format(AppConstants.MESSAGE_CREATED, category.getCategoryName())).build();
    }

    public CommonResponse updateCategory(CreateCategoryRequestDTO requestDTO, UUID categoryId, String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
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

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(AppConstants.MESSAGE_NOT_FOUND).build();
        }

        Category category = categoryOptional.get();
        category.setCategoryName(requestDTO.getCategoryName());

        categoryRepository.save(category);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(String.format(AppConstants.MESSAGE_UPDATED_SUCCESS, category.getCategoryName())).build();
    }

    public CommonResponse deleteCategory(UUID categoryId, String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
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

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(AppConstants.MESSAGE_NOT_FOUND).build();
        }

        Category category = categoryOptional.get();

        categoryRepository.delete(category);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(String.format(AppConstants.MESSAGE_DELETED_SUCCESS, category.getCategoryName())).build();
    }

    public CommonResponse getCategory(UUID categoryId, String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
        CommonResponse userResponse = validateUser(httpServletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        CommonResponse businessGroupResponse = businessGroupClient.getBusinessGroups(businessGroupCode, httpServletRequest).getBody();
        if (!businessGroupResponse.getStatus().equals(AppConstants.STATUS_SUCCESS)) {
            return businessGroupResponse;
        }

        CommonResponse businessResponse = businessGroupClient.getBusiness(businessCode, businessGroupCode, httpServletRequest).getBody();
        if (!businessResponse.getStatus().equals(AppConstants.STATUS_SUCCESS)) {
            return businessResponse;
        }
        Business business = (Business) businessResponse.getData();

        if (!business.getBusinessCode().equals(httpServletRequest.getHeader(HeaderConstants.BUSINESS_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(AppConstants.MESSAGE_NOT_FOUND).build();
        }
        Category category = categoryOptional.get();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(getCategoryResponse(category)).build();
    }

    public CommonResponse getAllCategory(String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
        CommonResponse userResponse = validateUser(httpServletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        CommonResponse businessGroupResponse = businessGroupClient.getBusinessGroups(businessGroupCode, httpServletRequest).getBody();
        if (!businessGroupResponse.getStatus().equals(AppConstants.STATUS_SUCCESS)) {
            return businessGroupResponse;
        }

        CommonResponse businessResponse = businessGroupClient.getBusiness(businessCode, businessGroupCode, httpServletRequest).getBody();
        if (!businessResponse.getStatus().equals(AppConstants.STATUS_SUCCESS)) {
            return businessResponse;
        }
        Business business = (Business) businessResponse.getData();

        if (!business.getBusinessCode().equals(httpServletRequest.getHeader(HeaderConstants.BUSINESS_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        List<CategoryResponseDTO> responseDTOS = categoryRepository.findAllByBusinessId(business.getBusinessId()).stream().map(this::getCategoryResponse).toList();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(responseDTOS).build();
    }

    //helper methods
    private CommonResponse validateUser(HttpServletRequest request){
        UUID userId = UUID.fromString(request.getHeader(HeaderConstants.USER_ID));
        CommonResponse userResponse = userClient.getUser(userId).getBody();
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        User user = (User) userResponse.getData();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(user).build();
    }

    private CategoryResponseDTO getCategoryResponse(Category category){
        return CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .name(category.getCategoryName())
                .build();
    }
}
