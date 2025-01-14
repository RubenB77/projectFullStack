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
    /**
     * Function to create an Expense
     * @param expenseDto the DTO object containg the infos to create the Expense
     * @return Expense the created Expense
     */
    public Expense createExpenseFromDto(ExpenseDto expenseDto) {

        Category category = this.categoryRepository.findById(expenseDto.getExpenseCategory().getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String
                        .format("Category with id %d not found", expenseDto.getExpenseCategory().getCategoryId())));

        User user = this.userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with id %d not found", expenseDto.getUserId())));

        return new Expense(expenseDto, category, user);
    }

    @Override
    /**
     * Function to retrieve the list of expenses of the user
     * @param int The Id of the user of which we want to retrieve the expenses
     * @return List<Expense> The expenses of the user
     */
    public List<Expense> getExpensesByUserId(int userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    /**
     * @param userId the Id of the User who is creating the Expense
     * @param expenseDtoInput the DTO object containing the informatiosn to create the Expense
     * @return ExpenseDto the DTO object containing the information of the Expense created to return
     */
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
            if (existingAddress.isPresent()) {
                address = existingAddress.get();
            } else {
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

    @Override
    /**
     * Function to update an Expense
     * @param expenseId The Id of the expense to update
     * @param expenseDtoInput The DTO object containing the informations with which to update the expense
     * @return ExpenseDto The DTO object containing the informations to return 
     */
    public ExpenseDto updateExpense(int expenseId, ExpenseDtoInput expenseDtoInput) {
        System.out.println(expenseId);
        // Fetch the existing expense
        Expense existingExpense = this.expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Expense with id %d not found", expenseId)));

        // Fetch the category
        Category category = this.categoryRepository.findById(expenseDtoInput.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Category with id %d not found", expenseDtoInput.getCategoryId())));

        // Update or clear address
        AddressDto addressDto = expenseDtoInput.getExpenseAddress();
        if (addressDto != null) {
            Address address = this.addressRepository.findByAddressName(addressDto.getAddressName())
                    .orElseGet(() -> new Address(addressDto));
            Address savedAddress = this.addressRepository.save(address);
            existingExpense.setExpenseAdress(savedAddress);
        } 
        else {
            existingExpense.setExpenseAdress(null);
        }

        // Update other fields
        existingExpense.setExpenseName(expenseDtoInput.getExpenseName());
        existingExpense.setExpensePrice(expenseDtoInput.getExpensePrice());
        existingExpense.setExpenseNote(expenseDtoInput.getExpenseNote());
        existingExpense.setExpenseCategory(category);
        existingExpense.setExpenseDate(expenseDtoInput.getExpenseDate());

        System.out.println(existingExpense.getExpenseName());

        // Save the updated expense
        Expense updatedExpense = this.expenseRepository.save(existingExpense);

        // Convert to DTO and return
        return new ExpenseDto(updatedExpense);
    }

}
