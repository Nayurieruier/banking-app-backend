package com.example.demo.Controller;

import com.example.demo.DTO.CustomerResponse;
import com.example.demo.Model.Customers;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{customerId}")
    public CustomerResponse getCustomer(@PathVariable int customerId) throws SQLException {
        return toResponse(customerRepository.findById(customerId));
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers() throws SQLException {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public int createCustomer(@RequestBody Customers customer) throws SQLException {
        return customerRepository.save(customer);
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(@PathVariable int customerId, @RequestBody Customers customer) throws SQLException {
        customer.setCustomerId(customerId);
        customerRepository.update(customer);
    }

    @DeleteMapping("/{customerId}")
    public void deactivateCustomer(@PathVariable int customerId) throws SQLException {
        customerRepository.deactivate(customerId);
    }

    private CustomerResponse toResponse(Customers customer) {
        if (customer == null) return null;

        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getDateOfBirth(),
                customer.getCreatedAt(),
                customer.isActive()
        );
    }
}