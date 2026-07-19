package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.PaymentDetailsResponse;
import com.justcatering.superadmin.dto.response.PaymentDropdownResponse;
import com.justcatering.superadmin.dto.response.PaymentListResponse;
import com.justcatering.superadmin.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for {@link Payment} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, ProductMapper.class})
public interface PaymentMapper {

    /**
     * Maps a payment to a list response.
     *
     * @param payment payment entity with relations loaded
     * @return list response
     */
    @Mapping(target = "clientUuid", source = "client.uuid")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "productUuid", source = "product.uuid")
    @Mapping(target = "productName", source = "product.name")
    PaymentListResponse toList(Payment payment);

    /**
     * Maps a payment to a details response without balance fields.
     *
     * @param payment payment entity with relations loaded
     * @return details response
     */
    @Mapping(target = "client", source = "client")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "productCost", ignore = true)
    @Mapping(target = "amountPaid", ignore = true)
    @Mapping(target = "remainingBalance", ignore = true)
    PaymentDetailsResponse toDetails(Payment payment);

    /**
     * Maps a payment to a dropdown response.
     *
     * @param payment payment entity
     * @return dropdown response
     */
    @Mapping(target = "clientName", source = "client.clientName")
    PaymentDropdownResponse toDropdown(Payment payment);
}
