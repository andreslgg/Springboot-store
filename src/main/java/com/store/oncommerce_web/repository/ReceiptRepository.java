package com.store.oncommerce_web.repository;

import com.store.oncommerce_web.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
}
