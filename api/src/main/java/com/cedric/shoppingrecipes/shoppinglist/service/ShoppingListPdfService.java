package com.cedric.shoppingrecipes.shoppinglist.service;

import com.cedric.shoppingrecipes.shoppinglist.dto.ShoppingListResponse;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.time.LocalDateTime;

@Service
public class ShoppingListPdfService {

    private final TemplateEngine templateEngine;

    public ShoppingListPdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(ShoppingListResponse list){

        // 1. Préparer les variables Thymeleaf
        Context context = new Context();
        context.setVariable("list", list);
        context.setVariable("exportDate", LocalDateTime.now().toString());

        // 2. Générer le HTML final à partir du template
        String html = templateEngine.process("shoppingListPdf", context);

        // 3. Convertir HTML -> PDF avec Flying Saucer
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération PDF", e);
        }
    }
}