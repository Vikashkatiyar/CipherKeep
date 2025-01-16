package com.encription.controller;


import com.encription.constants.ApplicationConstants;
import com.encription.dto.SecretPageResponseDTO;
import com.encription.dto.SecretRequestDTO;
import com.encription.dto.SecretResponseDTO;
import com.encription.service.SecretService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class SecretController {

    @Autowired
    private SecretService secretService;

    @PostMapping("/createSecret")
    public ResponseEntity<SecretResponseDTO> createSecret(@RequestBody SecretRequestDTO secretRequestDTO){
        return new ResponseEntity<>(secretService.createSecret(secretRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretResponseDTO> getSecretById(@PathVariable Long id){
        return new ResponseEntity<>(secretService.getSecretById(id),HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SecretResponseDTO>> getAllSecret(){
        return new ResponseEntity<>(secretService.getAllSecret(),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SecretResponseDTO> updateById(@PathVariable("id") Long id, @RequestBody SecretRequestDTO secretRequestDTO){
        return new ResponseEntity<>(secretService.updateSecret(id, secretRequestDTO),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSecretById(@PathVariable Long id){
        boolean response=secretService.deleteSecret(id);
        if(response)
           return new ResponseEntity<>("Secret Deleted Successfully",HttpStatus.OK);
        else
            return new ResponseEntity<>("Secret Deletion Unsuccessful", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/allSecretPages")
    public ResponseEntity<SecretPageResponseDTO> getAllSecretWithPagination(
            @RequestParam(defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize)
    {
            return ResponseEntity.ok(secretService.getAllSecretWithPagination(pageNumber, pageSize));
    }


    @GetMapping("/allSecretPagesBySorting")
    public ResponseEntity<SecretPageResponseDTO> getAllSecretWithPaginationAndSorting(
            @RequestParam(defaultValue = ApplicationConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(defaultValue = ApplicationConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = ApplicationConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = ApplicationConstants.SORT_DIR, required = false) String dir
    ){
        return ResponseEntity.ok(secretService.getAllSecretWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));
    }



}
