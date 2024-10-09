package com.store.oncommerce_web.service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.store.oncommerce_web.model.Receipt;
import com.store.oncommerce_web.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }
    @Value("${app.url.base}")
    private String baseUrl;

    public Receipt createAndSaveReceipt(Receipt receipt) throws WriterException, IOException {
        // Generar identificador único
        receipt.setId(UUID.randomUUID());
        // Calcular total
        double total = receipt.getSubtotal() + receipt.getTax() + receipt.getShippingCost();
        receipt.setTotal(total);


        // Guardar en la base de datos
        return receiptRepository.save(receipt);
    }

    public String generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        // Valida y asegura que el texto no tenga caracteres inválidos para un nombre de archivo
        String sanitizedText = text.replaceAll("[^a-zA-Z0-9]", "_");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(baseUrl + File.separator + "receipt" + File.separator + text, BarcodeFormat.QR_CODE, width, height);


        Resource resource = resourceLoader.getResource("classpath:/static/images/");

        File file = resource.getFile();
        if (!Files.exists(Path.of(file.getPath()))) {
            Files.createDirectories(file.toPath());
        }


        String qrCodePath = file.getPath() + File.separator + sanitizedText + ".png";
        Path path = FileSystems.getDefault().getPath(qrCodePath);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return sanitizedText + ".png";
    }

}
