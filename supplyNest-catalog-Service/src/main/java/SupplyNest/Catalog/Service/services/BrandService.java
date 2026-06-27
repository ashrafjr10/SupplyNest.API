package SupplyNest.Catalog.Service.services;

import SupplyNest.Catalog.Service.controllers.BusinessGroupClient;
import SupplyNest.Catalog.Service.controllers.UserClient;
import SupplyNest.Catalog.Service.dtos.BrandResponseDTO;
import SupplyNest.Catalog.Service.dtos.CreateBrandRequestDTO;
import SupplyNest.Catalog.Service.models.Brand;
import SupplyNest.Catalog.Service.models.Business;
import SupplyNest.Catalog.Service.models.User;
import SupplyNest.Catalog.Service.repositories.BrandRepository;
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
public class BrandService {

    private final BrandRepository brandRepository;
    private final UserClient userClient;
    private final BusinessGroupClient businessGroupClient;

    public CommonResponse createBrand(CreateBrandRequestDTO requestDTO, String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
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

        Brand brand = Brand.builder()
                .brandName(requestDTO.getBrandName())
                .businessId(business.getBusinessId())
                .build();

        brandRepository.save(brand);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(String.format(AppConstants.MESSAGE_CREATED, "Brand")).build();
    }

    public CommonResponse updateBrand(CreateBrandRequestDTO requestDTO, UUID brandId, String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
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

        Optional<Brand> brandOptional = brandRepository.findById(brandId);
        if (brandOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(AppConstants.MESSAGE_NOT_FOUND).build();
        }
        Brand brand = brandOptional.get();

        brand.setBrandName(requestDTO.getBrandName());

        brandRepository.save(brand);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(String.format(AppConstants.MESSAGE_UPDATED_SUCCESS, "Brand")).build();
    }

    public CommonResponse getBrand(UUID brandId, String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
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

        Optional<Brand> brandOptional = brandRepository.findById(brandId);
        if (brandOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_NOT_FOUND).message(AppConstants.MESSAGE_NOT_FOUND).build();
        }
        Brand brand = brandOptional.get();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(brandResponseDTO(brand)).build();
    }

    public CommonResponse getAllBrand(String businessCode, String businessGroupCode, HttpServletRequest httpServletRequest) {
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

        List<BrandResponseDTO> brandResponseDTOs = brandRepository.findByBusinessId(business.getBusinessId()).stream().map(this::brandResponseDTO).toList();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(brandResponseDTOs).build();
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

    private BrandResponseDTO brandResponseDTO(Brand brand){
        return BrandResponseDTO.builder()
                .brandId(brand.getBrandId())
                .brandName(brand.getBrandName())
                .build();
    }
}
