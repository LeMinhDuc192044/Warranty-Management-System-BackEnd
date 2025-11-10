package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.service.ProductsSparePartsEVMService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/spare-parts/evm")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductsSparePartsEVMController {

    private final ProductsSparePartsEVMService service;


    // CREATE
    @PostMapping
    @PreAuthorize("hasRole('EVM_ADMIN', 'EVM_STAFF')")
    public ResponseEntity<ProductsSparePartsEVMResponse> createProduct(
            @Valid @RequestBody ProductsSparePartsEVMRequest request) {
        ProductsSparePartsEVMResponse response = service.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // READ - Get All
    @GetMapping
    @PreAuthorize("hasRole('EVM_ADMIN', 'EVM_STAFF')")
    public ResponseEntity<List<ProductsSparePartsEVMResponse>> getAllProducts() {
        List<ProductsSparePartsEVMResponse> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/transfer/{partEvmId}")
    public ResponseEntity<String> transferFromScToEvm(@PathVariable String partEvmId,
                                                      @RequestParam OfficeBranch officeBranch) {

        service.transferFromEVMToScOfficeBranch(partEvmId, officeBranch);
        return ResponseEntity.ok("Transfer successful for SC ID: " + partEvmId);
    }


    // READ - Get By ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EVM_ADMIN', 'EVM_STAFF')")

    public ResponseEntity<ProductsSparePartsEVMResponse> getProductById(@PathVariable String id) {
        ProductsSparePartsEVMResponse response = service.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<List<PartTypeCountEVMResponse>> countByType(
            @RequestParam String partId) {
        return ResponseEntity.ok(service.countEvmPartByType(partId));
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EVM_ADMIN', 'EVM_STAFF')")
    public ResponseEntity<ProductsSparePartsEVMResponse> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductsSparePartsEVMRequest request) {
        ProductsSparePartsEVMResponse response = service.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EVM_ADMIN', 'EVM_STAFF')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // SEARCH - By Brand


    // SEARCH - By Name
    @GetMapping("/search/name")
    @PreAuthorize("hasRole('EVM_ADMIN', 'EVM_STAFF')")
    public ResponseEntity<List<ProductsSparePartsEVMResponse>> searchProductsByName(
            @RequestParam String name) {
        List<ProductsSparePartsEVMResponse> products = service.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/count/type/condition")
    public ResponseEntity<List<PartTypeAndPartStatusCountEVMResponse>> countByTypeAndCondition(
            @RequestParam String partTypeId,
            @RequestParam PartStatus statuses) {

        List<PartTypeAndPartStatusCountEVMResponse> result =
                service.countEvmPartByTypeAndCondition(partTypeId, statuses);

        return ResponseEntity.ok(result);
    }


    @PostMapping("/transfer/multiple-evm-to-sc")
    @Operation(summary = "Transfer multiple EVM parts to SC by part type")
    public ResponseEntity<List<PartsEvmTransferMultipleResponse>> transferMultipleEVMToSC(
            @RequestBody @Valid TransferMultipleRequest request) {

        List<PartsEvmTransferMultipleResponse> result = service.transferMultipleEVMPartTypeToSC(
                request.getQuantity(),
                request.getPartTypeId(),
                request.getOfficeBranch()
        );

        return ResponseEntity.ok(result);
    }


}
