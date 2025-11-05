package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountResponse;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsSCRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ProductsSparePartsSCResponse;
import com.warrantyclaim.warrantyclaim_api.dto.SparePartInfoScDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.service.ProductsSparePartsSCService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/spare-parts/sc")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class ProductsSparePartsSCController {

    private final ProductsSparePartsSCService service;

    // CREATE
    @PostMapping
    public ResponseEntity<ProductsSparePartsSCResponse> createProduct(
            @Valid @RequestBody ProductsSparePartsSCRequest request) {
        ProductsSparePartsSCResponse response = service.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/count/office_branch")
    public List<PartTypeCountResponse> count(
            @RequestParam OfficeBranch branch,
            @RequestParam String partTypeId
    ) {
        return service.countPartTypeByOfficeBranch(branch, partTypeId);
    }

    @GetMapping("/findByOfficeBranch")
    public ResponseEntity<List<SparePartInfoScDTO>> findByOfficeBranch(
            @RequestParam OfficeBranch branch) {
        return ResponseEntity.ok(service.getByOfficeBranch(branch));
    }

    @PatchMapping("/{id}/vehicles")
    public ResponseEntity<ProductsSparePartsSCResponse> mapSerialNumberToVin(@PathVariable String id,
                                                                             @PathVariable String vehicleId) {
        ProductsSparePartsSCResponse response = service.mapSerialToVehicle(id, vehicleId);
        return ResponseEntity.ok(response);
    }

    // READ - Get All
    @GetMapping
    public ResponseEntity<List<ProductsSparePartsSCResponse>> getAllProducts() {
        List<ProductsSparePartsSCResponse> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // READ - Get By ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductsSparePartsSCResponse> getProductById(@PathVariable String id) {
        ProductsSparePartsSCResponse response = service.getProductById(id);
        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ProductsSparePartsSCResponse> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductsSparePartsSCRequest request) {
        ProductsSparePartsSCResponse response = service.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // SEARCH - By Brand


    // SEARCH - By Name
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductsSparePartsSCResponse>> searchProductsByName(
            @RequestParam String name) {
        List<ProductsSparePartsSCResponse> products = service.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

}
