package com.store.oncommerce_web.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payee;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import com.store.oncommerce_web.model.Address;
import com.store.oncommerce_web.model.Receipt;
import com.store.oncommerce_web.model.User;
import com.store.oncommerce_web.repository.AddressRepository;
import com.store.oncommerce_web.repository.ReceiptRepository;
import com.store.oncommerce_web.service.PaypalService;
import com.store.oncommerce_web.service.ReceiptService;
import com.store.oncommerce_web.service.UserService;
import io.lettuce.core.ScriptOutputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;
    private final ReceiptService receiptService; // Inyección del servicio de recibos
    private final ReceiptRepository receiptRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Value("${app.url.base}")
    private String baseUrl;
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/payment/create")
    public RedirectView createPayment(@RequestParam("amount") String amount,@RequestParam("tax") String tax, @RequestParam("shipping") String shipping,@RequestParam("selectedAddresses") Long selectedAddressId) {
        try {
            Address address = addressRepository.findById(selectedAddressId)
                    .orElseThrow(() -> new RuntimeException("Address not found"));

            String cancelUrl = baseUrl + "/payment/cancel";
            String successUrl = baseUrl + "/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    Double.valueOf(tax),
                    Double.valueOf(shipping),
                    "USD",
                    "paypal",
                    "sale",
                    "Descripción del pago",
                    cancelUrl,
                    successUrl,
                    address
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred: ", e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                List<Transaction>  transactions = payment.getTransactions();
                String address = transactions.getFirst().getCustom();
                String route = createReceipt(payment,address);

                return "redirect:/receipt/" + route; // Aquí se usa el paymentId como identificador único
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred: ", e);
        }
        return "paymentError"; // Manejo de error
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }

    public String createReceipt(Payment payment, String address) {
            Receipt receipt = new Receipt();
        try {

            User user = userService.getAuthenticatedUser();

            Transaction transaction = payment.getTransactions().get(0);
            Payee payee = transaction.getPayee();
            receipt.setUser(user);
            receipt.setInvoiceNumber(transaction.getInvoiceNumber());
            receipt.setInvoiceDate(payment.getCreateTime());
            receipt.setCustomerName(payee.getMerchantId());
            receipt.setCustomerAddress(address);
            receipt.setSubtotal(Double.parseDouble(transaction.getAmount().getDetails().getSubtotal()));
            receipt.setTax(Double.parseDouble(transaction.getAmount().getDetails().getTax()));
            receipt.setShippingCost(Double.parseDouble(transaction.getAmount().getDetails().getShipping()));
            receipt.setTotal(Double.parseDouble(transaction.getAmount().getTotal()));
            receipt.setCustomerEmail(payee.getEmail());

            Receipt savedReceipt = receiptService.createAndSaveReceipt(receipt);

            String qrCodeUrl = receiptService.generateQRCodeImage(String.valueOf(savedReceipt.getId()), 200, 200);


            Resource resource = resourceLoader.getResource("classpath:/static/images/"+qrCodeUrl);
            File file = resource.getFile();
            // Verificar que el archivo del código QR existe antes de continuar

            if (Files.exists(file.toPath())) {
                System.out.println("QR Code already exists");
                savedReceipt.setQrCodeUrl(qrCodeUrl);
                receiptRepository.save(savedReceipt);
                return String.valueOf(savedReceipt.getId());
            } else {
                log.error("Error: Código QR no fue generado correctamente.");
                return null;
            }
        } catch (Exception e) {
            log.error("Error creating receipt: ", e);
            return null;
        }
    }
}