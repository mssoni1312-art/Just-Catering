package com.justcatering.superadmin.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Product name list/detail response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductNameResponse {

    private UUID uuid;
    private String name;
    private UUID productTypeUuid;
    private String productTypeName;
}
