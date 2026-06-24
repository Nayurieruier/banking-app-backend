package com.example.demo.Controller;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.Exception.BankingException;
import com.example.demo.Model.Customers;
import com.example.demo.Repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws SQLException {
        Customers customer = customerRepository.findByEmail(request.getEmail());

        if (customer == null || !passwordEncoder.matches(request.getPassword(), customer.getPasswordHash())) {
            throw new BankingException("Invalid email or password.", 401);
        }

        String token = jwtUtil.generateToken(customer.getCustomerId(), customer.getEmail());

        return new LoginResponse(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                token
        );
    }
}