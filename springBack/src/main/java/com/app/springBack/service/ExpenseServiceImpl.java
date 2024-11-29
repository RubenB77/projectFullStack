package com.app.springBack.service;

import com.app.springBack.model.Category;
import com.app.springBack.model.Expense;
import com.app.springBack.model.User;
import com.app.springBack.model.Address;
import com.app.springBack.repository.AddressRepository;
import com.app.springBack.repository.CategoryRepository;
import com.app.springBack.repository.ExpenseRepository;
import com.app.springBack.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.app.springBack.dto.AddressDto;
import com.app.springBack.dto.ExpenseDto;
import com.app.springBack.dto.ExpenseDtoInput;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private ExpenseRepository expenseRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private AddressRepository addressRepository;

    public ExpenseServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository,
            ExpenseRepository expenseRepository, AddressRepository addressRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Expense createExpenseFromDto(ExpenseDto expenseDto) {

        Category category = this.categoryRepository.findById(expenseDto.getExpenseCategory().getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String
                        .format("Category with id %d not found", expenseDto.getExpenseCategory().getCategoryId())));

        User user = this.userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with id %d not found", expenseDto.getUserId())));
        ;

        return new Expense(expenseDto, category, user);
    }

    @Override
    public List<Expense> getExpensesByUserId(int userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public ExpenseDto createExpenseFromDtoInput(int userId, ExpenseDtoInput expenseDtoInput) {

        Category category = this.categoryRepository.findById(expenseDtoInput.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Category with id %d not found", expenseDtoInput.getCategoryId())));

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with id %d not found", userId)));

        AddressDto addressDto = expenseDtoInput.getExpenseAddress();

        Expense expense;
        if (addressDto != null) {
            Address address;
            Optional<Address> existingAddress = this.addressRepository.findByAddressName(addressDto.getAddressName());
            if(existingAddress.isPresent()) {
                address = existingAddress.get();
            }
            else {
                address = new Address(addressDto);
            }
            Address savedAddress = this.addressRepository.save(address);
            expenseDtoInput.setExpenseAddress(new AddressDto(savedAddress));
            expense = this.expenseRepository.save(new Expense(expenseDtoInput, savedAddress, category, user));
        } else {
            expense = this.expenseRepository.save(new Expense(expenseDtoInput, category, user));
        }

        ExpenseDto expenseDto = new ExpenseDto(expense);

        return expenseDto;
    }

}
