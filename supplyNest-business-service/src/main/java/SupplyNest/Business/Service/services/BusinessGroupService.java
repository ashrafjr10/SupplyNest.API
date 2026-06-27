package SupplyNest.Business.Service.services;

import SupplyNest.Business.Service.controllers.UserClient;
import SupplyNest.Business.Service.dtos.*;
import SupplyNest.Business.Service.enums.modelEnums;
import SupplyNest.Business.Service.models.Business;
import SupplyNest.Business.Service.models.BusinessAddress;
import SupplyNest.Business.Service.models.BusinessGroup;
import SupplyNest.Business.Service.models.User;
import SupplyNest.Business.Service.repositories.BusinessGroupRepository;
import SupplyNest.Business.Service.repositories.BusinessRepository;
import SupplyNest.Business.Service.utils.Codes;
import SupplyNest.Common.constants.AppConstants;
import SupplyNest.Common.constants.HeaderConstants;
import SupplyNest.Common.dtos.CommonResponse;
import SupplyNest.Common.dtos.RoleResponseDTO;
import SupplyNest.Common.enums.ModelEnums;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BusinessGroupService {

    private final BusinessGroupRepository businessGroupRepository;
    private final BusinessRepository businessRepository;
    private final UserClient userClient;
    private final ObjectMapper objectMapper;
    private final FileService fileService;

    @Transactional
    public CommonResponse createBusinessGroup(@Valid CreateBusinessGroupRequestDTO request) {

        String groupCode = Codes.generateBusinessGroupCode(request.getBusinessGroupName());
        while (businessGroupRepository.existsByBusinessGroupCode(groupCode)){
            groupCode = Codes.generateBusinessGroupCode(request.getBusinessGroupName());
        }

        BusinessGroup businessGroup = BusinessGroup.builder()
                .businessGroupCode(groupCode)
                .status(modelEnums.BusinessGroupStatus.ACTIVE)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getMobileNumber())
                .userId(request.getUserId())
                .build();

        request.getBusinesses().forEach(requestDTO -> {

            String businessCode = Codes.generateBusinessCode(requestDTO.getBusinessName());
            while (businessRepository.existsByBusinessCode(businessCode)){
                businessCode = Codes.generateBusinessCode(requestDTO.getBusinessName());
            }

            Business business = Business.builder()
                    .businessName(requestDTO.getBusinessName())
                    .type(requestDTO.getType())
                    .status(modelEnums.BusinessStatus.ACTIVE)
                    .gstNumber(requestDTO.getGstNumber())
                    .businessGroup(businessGroup)
                    .businessCode(businessCode)
                    .businessAddress(BusinessAddress.builder()
                            .addressLine1(requestDTO.getAddressLine1())
                            .addressLine2(requestDTO.getAddressLine2())
                            .city(requestDTO.getCity())
                            .state(requestDTO.getState())
                            .pinCode(requestDTO.getPinCode())
                            .build())
                    .build();
            businessGroup.addBusiness(business);
        });

        businessGroupRepository.save(businessGroup);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).build();
    }

    public CommonResponse updateBusinessGroup(@Valid UpdateBusinessGroupRequestDTO requestDTO, String businessGroupCode, HttpServletRequest servletRequest) {
        CommonResponse userResponse = validateUser(servletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        Optional<BusinessGroup> businessGroupOptional = businessGroupRepository.findByBusinessGroupCode(businessGroupCode);
        if (businessGroupOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business group")).build();
        }
        BusinessGroup businessGroup = businessGroupOptional.get();

        if (!businessGroup.getBusinessGroupCode().equals(servletRequest.getHeader(HeaderConstants.BUSINESS_GROUP_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        businessGroup.setFirstName(requestDTO.getFirstName());
        businessGroup.setLastName(requestDTO.getLastName());
        businessGroup.setEmail(requestDTO.getEmail());
        businessGroup.setPhoneNumber(requestDTO.getMobileNumber());

        businessGroupRepository.save(businessGroup);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).build();
    }

    public CommonResponse getBusinessGroups(String businessGroupCode, HttpServletRequest servletRequest) {
        CommonResponse userResponse = validateUser(servletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        Optional<BusinessGroup> businessGroupOptional = businessGroupRepository.findByBusinessGroupCode(businessGroupCode);
        if (businessGroupOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business group")).build();
        }
        BusinessGroup businessGroup = businessGroupOptional.get();

//        RoleResponseDTO role = objectMapper.convertValue(servletRequest.getAttribute(HeaderConstants.ROLE), RoleResponseDTO.class);
//        if (!businessGroup.getBusinessGroupCode().equals(servletRequest.getHeader(HeaderConstants.BUSINESS_GROUP_CODE))
//                || !role.getType().equals(ModelEnums.RoleTypes.SUPER_ADMIN.name()))
//            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        BusinessGroupResponseDTO responseDTO = getBusinessGroupResponseDTO(businessGroup);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(responseDTO).build();
    }

    public CommonResponse getAllBusinessGroups(HttpServletRequest request, int page, int size) {

        CommonResponse userResponse = validateUser(request);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        if (page < 0) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(AppConstants.PAGE_ERROR_INDEX).build();
        }
        if (size < 1) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(AppConstants.PAGE_ERROR_SIZE).build();
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<BusinessGroup> businessGroups = businessGroupRepository.findAll(pageable);

        List<BusinessGroupResponseDTO> businessGroupResponseDTOs = businessGroups.getContent().stream()
                .map(this::getBusinessGroupResponseDTO).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("page", businessGroups.getTotalPages());
        response.put("size", businessGroups.getSize());
        response.put("total", businessGroups.getTotalElements());
        response.put("data", businessGroupResponseDTOs);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(response).build();
    }

    public CommonResponse createBusiness(@Valid CreateBusinessRequestDTO request, String businessGroupCode, HttpServletRequest servletRequest) {
        CommonResponse userResponse = validateUser(servletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        Optional<BusinessGroup> businessGroupOptional = businessGroupRepository.findByBusinessGroupCode(businessGroupCode);
        if (businessGroupOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business group")).build();
        }
        BusinessGroup businessGroup = businessGroupOptional.get();

        if (!businessGroup.getBusinessGroupCode().equals(servletRequest.getHeader(HeaderConstants.BUSINESS_GROUP_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        Business business = Business.builder()
                .businessName(request.getBusinessName())
                .type(request.getType())
                .status(modelEnums.BusinessStatus.ACTIVE)
                .gstNumber(request.getGstNumber())
                .businessGroup(businessGroup)
                .businessAddress(BusinessAddress.builder()
                        .addressLine1(request.getAddressLine1())
                        .addressLine2(request.getAddressLine2())
                        .city(request.getCity())
                        .state(request.getState())
                        .pinCode(request.getPinCode())
                        .build())
                .build();

        String businessCode = Codes.generateBusinessCode(request.getBusinessName());
        while (businessRepository.existsByBusinessCode(businessCode)){
            businessCode = Codes.generateBusinessCode(request.getBusinessName());
        }
        business.setBusinessCode(businessCode);

        businessRepository.save(business);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).build();
    }

    public CommonResponse updateBusiness(@Valid CreateBusinessRequestDTO requestDTO, String businessGroupCode, String businessCode, HttpServletRequest servletRequest) {
        CommonResponse userResponse = validateUser(servletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        Optional<BusinessGroup> businessGroupOptional = businessGroupRepository.findByBusinessGroupCode(businessGroupCode);
        if (businessGroupOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business group")).build();
        }
        BusinessGroup businessGroup = businessGroupOptional.get();

        Optional<Business> businessOptional = businessRepository.findByBusinessCode(businessCode);
        if (businessOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business")).build();
        }
        Business business = businessOptional.get();

        if (!business.getBusinessCode().equals(servletRequest.getHeader(HeaderConstants.BUSINESS_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        BusinessAddress businessAddress = business.getBusinessAddress();
        businessAddress.setAddressLine1(requestDTO.getAddressLine1());
        businessAddress.setAddressLine2(requestDTO.getAddressLine2());
        businessAddress.setCity(requestDTO.getCity());
        businessAddress.setState(requestDTO.getState());
        businessAddress.setPinCode(requestDTO.getPinCode());

        business.setBusinessName(requestDTO.getBusinessName());
        business.setGstNumber(requestDTO.getGstNumber());
        business.setType(requestDTO.getType());
        business.setBusinessAddress(businessAddress);

        businessRepository.save(business);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).build();
    }



    public CommonResponse updateBusinessLogo(MultipartFile logo, String businessGroupCode, String businessCode, HttpServletRequest servletRequest) {
        CommonResponse userResponse = validateUser(servletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        Optional<BusinessGroup> businessGroupOptional = businessGroupRepository.findByBusinessGroupCode(businessGroupCode);
        if (businessGroupOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business group")).build();
        }

        Optional<Business> businessOptional = businessRepository.findByBusinessCode(businessCode);
        if (businessOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business")).build();
        }
        Business business = businessOptional.get();

        if (!business.getBusinessCode().equals(servletRequest.getHeader(HeaderConstants.BUSINESS_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        try {
            String initialPath = "business-group/" + businessGroupCode + "/business/" + businessCode + "/logo/";
            String finalPath = fileService.saveFile(logo, initialPath);
            business.setLogo(finalPath);
            businessRepository.save(business);
            return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).build();
        }catch (Exception e){
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(e.getMessage()).build();
        }
    }

    public CommonResponse getBusiness(String businessGroupCode, String businessCode, HttpServletRequest servletRequest) {
        CommonResponse userResponse = validateUser(servletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        Optional<BusinessGroup> businessGroupOptional = businessGroupRepository.findByBusinessGroupCode(businessGroupCode);
        if (businessGroupOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business group")).build();
        }

        Optional<Business> businessOptional = businessRepository.findByBusinessCode(businessCode);
        if (businessOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business")).build();
        }
        Business business = businessOptional.get();

        if (!business.getBusinessCode().equals(servletRequest.getHeader(HeaderConstants.BUSINESS_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        BusinessResponseDTO businessResponseDTO = getBusinessResponseDTO(business);

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(businessResponseDTO).build();
    }

    public CommonResponse getBusinessNames(String businessGroupCode, HttpServletRequest servletRequest) {
        CommonResponse userResponse = validateUser(servletRequest);
        if (userResponse.getStatus() != AppConstants.STATUS_SUCCESS) {
            return userResponse;
        }

        Optional<BusinessGroup> businessGroupOptional = businessGroupRepository.findByBusinessGroupCode(businessGroupCode);
        if (businessGroupOptional.isEmpty()) {
            return CommonResponse.builder().status(AppConstants.STATUS_BAD_REQUEST).message(String.format(AppConstants.NOT_FOUND, "business group")).build();
        }
        BusinessGroup businessGroup = businessGroupOptional.get();

        if (!businessGroup.getBusinessGroupCode().equals(servletRequest.getHeader(HeaderConstants.BUSINESS_GROUP_CODE)))
            return CommonResponse.builder().status(AppConstants.STATUS_UNAUTHORIZED).message(AppConstants.MESSAGE_UNAUTHORIZED).build();

        List<String> businessNames = businessGroup.getBusinessList().stream().map(Business::getBusinessName).toList();

        return CommonResponse.builder().status(AppConstants.STATUS_SUCCESS).message(AppConstants.MESSAGE_SUCCESS).data(businessNames).build();
    }

    //helper methods

    private BusinessGroupResponseDTO getBusinessGroupResponseDTO(BusinessGroup businessGroup){
        return BusinessGroupResponseDTO.builder()
                .businessGroupId(businessGroup.getBusinessGroupId())
                .firstName(businessGroup.getFirstName())
                .lastName(businessGroup.getLastName())
                .businessGroupCode(businessGroup.getBusinessGroupCode())
                .status(businessGroup.getStatus())
                .email(businessGroup.getEmail())
                .phoneNumber(businessGroup.getPhoneNumber())
                .userId(businessGroup.getUserId())
                .businesses(businessGroup.getBusinessList().stream().map(this::getBusinessResponseDTO).toList())
                .build();
    }

    private BusinessResponseDTO getBusinessResponseDTO(Business business){
        return BusinessResponseDTO.builder()
                .businessId(business.getBusinessId())
                .businessName(business.getBusinessName())
                .businessCode(business.getBusinessCode())
                .type(business.getType())
                .status(business.getStatus())
                .gstNumber(business.getGstNumber())
                .businessAddress(BusinessAddressResponseDTO.builder()
                        .businessAddressId(business.getBusinessAddress().getBusinessAddressId())
                        .addressLine1(business.getBusinessAddress().getAddressLine1())
                        .addressLine2(business.getBusinessAddress().getAddressLine2())
                        .city(business.getBusinessAddress().getCity())
                        .state(business.getBusinessAddress().getState())
                        .pinCode(business.getBusinessAddress().getPinCode())
                        .build())
                .build();
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
