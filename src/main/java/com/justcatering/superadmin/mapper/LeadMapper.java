package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.LeadDetailsResponse;
import com.justcatering.superadmin.dto.response.LeadDropdownResponse;
import com.justcatering.superadmin.dto.response.LeadListResponse;
import com.justcatering.superadmin.entity.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for {@link Lead} projections.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface LeadMapper {

    /**
     * Maps a lead to a list response.
     *
     * @param lead lead entity with product loaded
     * @return list response
     */
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.code")
    LeadListResponse toList(Lead lead);

    /**
     * Maps a lead to a details response.
     *
     * @param lead lead entity with product loaded
     * @return details response
     */
    @Mapping(target = "product", source = "product")
    LeadDetailsResponse toDetails(Lead lead);

    /**
     * Maps a lead to a dropdown response.
     *
     * @param lead lead entity
     * @return dropdown response
     */
    LeadDropdownResponse toDropdown(Lead lead);
}
