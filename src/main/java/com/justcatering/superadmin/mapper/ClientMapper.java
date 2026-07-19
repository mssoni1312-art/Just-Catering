package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ClientDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientListResponse;
import com.justcatering.superadmin.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for {@link Client} projections.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ClientMapper {

    /**
     * Maps a client to a list response.
     *
     * @param client client entity with product loaded
     * @return list response
     */
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "reward", ignore = true)
    ClientListResponse toList(Client client);

    /**
     * Maps a client to a details response.
     *
     * @param client client entity with product loaded
     * @return details response
     */
    @Mapping(target = "product", source = "product")
    @Mapping(target = "reward", ignore = true)
    ClientDetailsResponse toDetails(Client client);

    /**
     * Maps a client to a dropdown response.
     *
     * @param client client entity
     * @return dropdown response
     */
    ClientDropdownResponse toDropdown(Client client);
}
