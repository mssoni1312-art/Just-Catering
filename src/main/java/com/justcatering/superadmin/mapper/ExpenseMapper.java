package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ExpenseDetailsResponse;
import com.justcatering.superadmin.dto.response.ExpenseListResponse;
import com.justcatering.superadmin.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for {@link Expense} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class})
public interface ExpenseMapper {

    /**
     * Maps an expense to a list response.
     *
     * @param expense expense entity with relations loaded
     * @return list response
     */
    @Mapping(target = "clientUuid", source = "client.uuid")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "memberUserUuid", source = "memberUser.uuid")
    @Mapping(
            target = "memberUserName",
            expression = "java(expense.getMemberUser() != null ? expense.getMemberUser().getFullName() : null)"
    )
    ExpenseListResponse toList(Expense expense);

    /**
     * Maps an expense to a details response.
     *
     * @param expense expense entity with relations loaded
     * @return details response
     */
    @Mapping(target = "client", source = "client")
    @Mapping(target = "memberUser", source = "memberUser")
    ExpenseDetailsResponse toDetails(Expense expense);
}
