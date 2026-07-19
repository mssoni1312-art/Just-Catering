package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ProductDetailsResponse;
import com.justcatering.superadmin.dto.response.ProductDropdownResponse;
import com.justcatering.superadmin.dto.response.ProductListResponse;
import com.justcatering.superadmin.entity.Product;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for {@link Product} projections.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Maps a product to a list response.
     *
     * @param product product entity
     * @return list response
     */
    ProductListResponse toList(Product product);

    /**
     * Maps a product to a details response.
     *
     * @param product product entity
     * @return details response
     */
    ProductDetailsResponse toDetails(Product product);

    /**
     * Maps a product to a dropdown response.
     *
     * @param product product entity
     * @return dropdown response
     */
    ProductDropdownResponse toDropdown(Product product);
}
