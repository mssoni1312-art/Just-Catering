package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ProductNameResponse;
import com.justcatering.superadmin.dto.response.ProductTypeResponse;
import com.justcatering.superadmin.entity.ProductName;
import com.justcatering.superadmin.entity.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for product type and product name lookups.
 */
@Mapper(componentModel = "spring")
public interface ProductCatalogMapper {

    /**
     * Maps a product type to a response.
     *
     * @param productType product type entity
     * @return response
     */
    ProductTypeResponse toTypeResponse(ProductType productType);

    /**
     * Maps a product name to a response.
     *
     * @param productName product name entity
     * @return response
     */
    @Mapping(target = "productTypeUuid", source = "productType.uuid")
    @Mapping(target = "productTypeName", source = "productType.name")
    ProductNameResponse toNameResponse(ProductName productName);
}
