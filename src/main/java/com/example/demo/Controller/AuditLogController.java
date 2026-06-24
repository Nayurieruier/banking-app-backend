package com.example.demo.Controller;

import com.example.demo.Model.AuditLog;
import com.example.demo.Repository.AuditLogRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/{logId}")
    public AuditLog getLog(@PathVariable BigInteger logId) throws SQLException {
        return auditLogRepository.findById(logId);
    }

    @GetMapping("/customer/{customerId}")
    public List<AuditLog> getLogsForCustomer(@PathVariable int customerId) throws SQLException {
        return auditLogRepository.findByCustomerId(customerId);
    }
}