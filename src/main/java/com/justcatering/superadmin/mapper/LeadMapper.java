package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.LeadDetailsResponse;
import com.justcatering.superadmin.dto.response.LeadDropdownResponse;
import com.justcatering.superadmin.dto.response.LeadListResponse;
import com.justcatering.superadmin.entity.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

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
    @Mapping(target = "ownerName", expression = "java(resolveOwnerName(lead))")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.code")
    LeadListResponse toList(Lead lead);

    /**
     * Maps a lead to a details response.
     *
     * @param lead lead entity with product loaded
     * @return details response
     */
    @Mapping(target = "ownerName", expression = "java(resolveOwnerName(lead))")
    @Mapping(target = "product", source = "product")
    LeadDetailsResponse toDetails(Lead lead);

    /**
     * Maps a lead to a dropdown response.
     *
     * @param lead lead entity
     * @return dropdown response
     */
    @Mapping(target = "ownerName", expression = "java(resolveOwnerName(lead))")
    LeadDropdownResponse toDropdown(Lead lead);

    /**
     * Builds a single owner name from legacy first/last name columns.
     *
     * @param lead lead entity
     * @return owner name
     */
    default String resolveOwnerName(Lead lead) {
        if (lead == null || !StringUtils.hasText(lead.getFirstName())) {
            return null;
        }
        if (!StringUtils.hasText(lead.getLastName())) {
            return lead.getFirstName().trim();
        }
        return (lead.getFirstName().trim() + " " + lead.getLastName().trim()).trim();
    }
}
