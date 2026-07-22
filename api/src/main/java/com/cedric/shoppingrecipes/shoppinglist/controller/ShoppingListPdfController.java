package com.cedric.shoppingrecipes.shoppinglist.controller;

import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;
import com.cedric.shoppingrecipes.shoppinglist.service.ShoppingListPdfService;
import com.cedric.shoppingrecipes.shoppinglist.service.ShoppingListService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-lists/pdf")
public class ShoppingListPdfController {

    private final ShoppingListService shoppingListService;
    private final ShoppingListPdfService shoppingListPdfService;

    public ShoppingListPdfController(
            ShoppingListService shoppingListService,
            ShoppingListPdfService shoppingListPdfService
    ) {
        this.shoppingListService = shoppingListService;
        this.shoppingListPdfService = shoppingListPdfService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {

        // 1. Récupérer la liste complète
        ShoppingListResponse list = shoppingListService.getById(id);

        // 2. Générer le PDF
        byte[] pdf = shoppingListPdfService.generatePdf(list);

        // 3. Retourner le PDF au front
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=shopping-list-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}