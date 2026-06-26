package SupplyNest.Business.Service.services;

import SupplyNest.Business.Service.dtos.CommonResponse;
import SupplyNest.Business.Service.dtos.CreateBusinessGroupRequestDTO;
import SupplyNest.Business.Service.enums.modelEnums;
import SupplyNest.Business.Service.models.Business;
import SupplyNest.Business.Service.models.BusinessAddress;
import SupplyNest.Business.Service.models.BusinessGroup;
import SupplyNest.Business.Service.repositories.BusinessGroupRepository;
import SupplyNest.Business.Service.repositories.BusinessRepository;
import SupplyNest.Business.Service.utils.Codes;
import SupplyNest.Common.constants.AppConstants;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessGroupService {

    private final BusinessGroupRepository businessGroupRepository;
    private final BusinessRepository businessRepository;

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
}
