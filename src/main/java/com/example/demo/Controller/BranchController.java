package com.example.demo.Controller;

import com.example.demo.Model.Branches;
import com.example.demo.Repository.BranchRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchRepository branchRepository;

    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @GetMapping("/{branchId}")
    public Branches getBranch(@PathVariable int branchId) throws SQLException {
        return branchRepository.findById(branchId);
    }

    @GetMapping
    public List<Branches> getAllBranches() throws SQLException {
        return branchRepository.findAll();
    }

    @PostMapping
    public int createBranch(@RequestBody Branches branch) throws SQLException {
        return branchRepository.save(branch);
    }
}