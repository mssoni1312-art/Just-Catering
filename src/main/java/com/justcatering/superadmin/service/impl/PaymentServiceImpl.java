package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.PaymentCreateRequest;
import com.justcatering.superadmin.dto.request.PaymentUpdateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.PaymentDetailsResponse;
import com.justcatering.superadmin.dto.response.PaymentDropdownResponse;
import com.justcatering.superadmin.dto.response.PaymentListResponse;
import com.justcatering.superadmin.dto.response.PaymentOverviewResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.Payment;
import com.justcatering.superadmin.entity.Product;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.PaymentMode;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.PaymentMapper;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.PaymentRepository;
import com.justcatering.superadmin.repository.ProductRepository;
import com.justcatering.superadmin.service.PaymentService;
import com.justcatering.superadmin.specification.PaymentSpecification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link PaymentService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PaymentMapper paymentMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentDetailsResponse create(PaymentCreateRequest request) {
        Client client = resolveActiveClient(request.getClientUuid());
        String invoiceNumber = normalizeInvoiceNumber(request.getInvoiceNumber());
        ensureInvoiceNumberUnique(invoiceNumber, null);

        Payment payment = Payment.builder()
                .client(client)
                .product(resolveProduct(request.getProductUuid(), client))
                .invoiceNumber(invoiceNumber)
                .paymentDate(request.getPaymentDate())
                .amount(request.getAmount())
                .bankType(normalizeOptional(request.getBankType()))
                .paymentMode(request.getPaymentMode() != null ? request.getPaymentMode() : PaymentMode.CASH)
                .remarks(normalizeOptional(request.getRemarks()))
                .receiptUrl(normalizeOptional(request.getReceiptUrl()))
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        payment = paymentRepository.save(payment);
        log.info("Created payment receipt: {}", payment.getInvoiceNumber());
        return enrichWithBalance(paymentMapper.toDetails(reloadWithRelations(payment.getUuid())), client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentDetailsResponse update(UUID uuid, PaymentUpdateRequest request) {
        Payment payment = findOrThrow(uuid);
        Client client = resolveActiveClient(request.getClientUuid());
        String invoiceNumber = normalizeInvoiceNumber(request.getInvoiceNumber());
        ensureInvoiceNumberUnique(invoiceNumber, payment.getId());

        payment.setClient(client);
        payment.setProduct(resolveProduct(request.getProductUuid(), client));
        payment.setInvoiceNumber(invoiceNumber);
        payment.setPaymentDate(request.getPaymentDate());
        payment.setAmount(request.getAmount());
        payment.setBankType(normalizeOptional(request.getBankType()));
        if (request.getPaymentMode() != null) {
            payment.setPaymentMode(request.getPaymentMode());
        }
        payment.setRemarks(normalizeOptional(request.getRemarks()));
        payment.setReceiptUrl(normalizeOptional(request.getReceiptUrl()));
        if (request.getStatus() != null) {
            payment.setStatus(request.getStatus());
        }

        paymentRepository.save(payment);
        log.info("Updated payment receipt: {}", payment.getInvoiceNumber());
        return enrichWithBalance(paymentMapper.toDetails(reloadWithRelations(uuid)), client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentDetailsResponse getByUuid(UUID uuid) {
        Payment payment = reloadWithRelations(uuid);
        return enrichWithBalance(paymentMapper.toDetails(payment), payment.getClient());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Payment payment = findOrThrow(uuid);
        payment.softDelete();
        paymentRepository.save(payment);
        log.info("Soft-deleted payment receipt: {}", payment.getInvoiceNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<PaymentListResponse> search(
            String search,
            EntityStatus status,
            PaymentMode paymentMode,
            UUID clientUuid,
            UUID productUuid,
            LocalDate paymentFrom,
            LocalDate paymentTo,
            Pageable pageable
    ) {
        Page<Payment> page = paymentRepository.findAll(
                PaymentSpecification.filter(
                        search, status, paymentMode, clientUuid, productUuid, paymentFrom, paymentTo
                ),
                pageable
        );
        return PageResponse.from(page.map(paymentMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentOverviewResponse getClientOverview(UUID clientUuid) {
        Client client = resolveActiveClient(clientUuid);
        BigDecimal productCost = client.getTotalAmount() != null ? client.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal amountPaid = paymentRepository.sumAmountByClientId(client.getId());
        BigDecimal remainingBalance = productCost.subtract(amountPaid);

        List<PaymentListResponse> recentReceipts = paymentRepository
                .findRecentByClientUuid(clientUuid, EntityStatus.ACTIVE)
                .stream()
                .map(paymentMapper::toList)
                .toList();

        PaymentOverviewResponse response = new PaymentOverviewResponse();
        response.setClientUuid(client.getUuid());
        response.setClientName(client.getClientName());
        response.setProductCost(productCost);
        response.setAmountPaid(amountPaid);
        response.setRemainingBalance(remainingBalance);
        response.setRecentReceipts(recentReceipts);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentDropdownResponse> dropdown() {
        return paymentRepository
                .findByDeletedFalseAndStatusOrderByPaymentDateDescInvoiceNumberAsc(EntityStatus.ACTIVE)
                .stream()
                .map(paymentMapper::toDropdown)
                .toList();
    }

    private Client resolveActiveClient(UUID clientUuid) {
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));
        if (client.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected client is not active");
        }
        return client;
    }

    private Product resolveProduct(UUID productUuid, Client client) {
        if (productUuid != null) {
            Product product = productRepository.findByUuidAndDeletedFalse(productUuid)
                    .orElseThrow(() -> new EntityNotFoundException("Product", productUuid));
            if (product.getStatus() != EntityStatus.ACTIVE) {
                throw new BusinessException("Selected product is not active");
            }
            return product;
        }
        return client.getProduct();
    }

    private Payment findOrThrow(UUID uuid) {
        return paymentRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Payment", uuid));
    }

    private Payment reloadWithRelations(UUID uuid) {
        return paymentRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Payment", uuid));
    }

    private void ensureInvoiceNumberUnique(String invoiceNumber, Long excludeId) {
        boolean exists = excludeId == null
                ? paymentRepository.existsByInvoiceNumberIgnoreCaseAndDeletedFalse(invoiceNumber)
                : paymentRepository.existsByInvoiceNumberIgnoreCaseAndDeletedFalseAndIdNot(invoiceNumber, excludeId);
        if (exists) {
            throw new BusinessException("Invoice number already exists: " + invoiceNumber);
        }
    }

    private PaymentDetailsResponse enrichWithBalance(PaymentDetailsResponse response, Client client) {
        BigDecimal productCost = client.getTotalAmount() != null ? client.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal amountPaid = paymentRepository.sumAmountByClientId(client.getId());
        response.setProductCost(productCost);
        response.setAmountPaid(amountPaid);
        response.setRemainingBalance(productCost.subtract(amountPaid));
        return response;
    }

    private String normalizeInvoiceNumber(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new BusinessException("Invoice number is required");
        }
        return invoiceNumber.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
