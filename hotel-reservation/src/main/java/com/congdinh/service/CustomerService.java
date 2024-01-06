package com.congdinh.service;

import java.util.ArrayList;
import java.util.Collection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.congdinh.model.Customer;

public class CustomerService {
    private final static String HOTEL_APPLICATION_DATA = "Hotel_Application_Data.xlsx";
    private final static String CUSTOMERS_SHEET = "Customers";

    private static CustomerService customerService = null;

    private Collection<Customer> customers;

    public CustomerService() {
        this.customers = new ArrayList<Customer>();
    }

    public static CustomerService getInstance() {
        if (customerService == null) {
            customerService = new CustomerService();
        }
        return customerService;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        this.customers.add(new Customer(firstName, lastName, email));
    }

    public Customer getCustomer(String customerEmail) {
        for (Customer customer : this.customers) {
            if (customer.getEmail().equals(customerEmail)) {
                return customer;
            }
        }
        return null;
    }

    public Collection<Customer> getAllCustomers() {
        return this.customers;
    }

    public void readCustomersFromFile() {
        // Read collection of customers from file
        // Check if file exists
        // If file exists, read customers from file
        // If file does not exist, do nothing

        try {
            File file = new File(HOTEL_APPLICATION_DATA);
            if (!file.exists()) {
                System.out.println("File not found.");
            } else {
                Workbook workbook = new XSSFWorkbook(HOTEL_APPLICATION_DATA);
                Sheet sheet = workbook.getSheet(CUSTOMERS_SHEET);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);

                    String firstName = row.getCell(0).getStringCellValue();
                    String lastName = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();

                    if (getCustomer(email) == null) {
                        this.customers.add(new Customer(firstName, lastName, email));
                    }
                }

                workbook.close();
            }

        } catch (Exception e) {
            System.out.println("Error while reading customers from file: " + e.getMessage());
        }
    }

    public void saveStateToFile() {
        // Write collection of customers to file
        writeCustomersToFile();
    }

    private void writeCustomersToFile() {
        // Write collection of customers to file
        if (this.customers == null || this.customers.isEmpty()) {
            return;
        } else {
            try {
                File file = new File(HOTEL_APPLICATION_DATA);
                Workbook workbook;
                // Check if file not exist, create a new file
                if (!file.exists()) {
                    workbook = new XSSFWorkbook();
                } else {
                    // If file exists, open it
                    try (FileInputStream fileIn = new FileInputStream(HOTEL_APPLICATION_DATA)) {
                        workbook = new XSSFWorkbook(fileIn);
                    } catch (IOException e) {
                        System.out.println("Error while opening file: " + e.getMessage());
                        return; // If there's an error opening the file, exit the method
                    }
                }

                if (workbook.getSheet(CUSTOMERS_SHEET) != null) {
                    workbook.removeSheetAt(workbook.getSheetIndex(CUSTOMERS_SHEET));
                }

                Sheet sheet = workbook.createSheet(CUSTOMERS_SHEET);

                // Create header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Customer First Name");
                headerRow.createCell(1).setCellValue("Customer Last Name");
                headerRow.createCell(2).setCellValue("Customer Email");

                int rowNum = 1;
                for (Customer customer : this.customers) {
                    Row row = sheet.createRow(rowNum++);

                    row.createCell(0).setCellValue(customer.getFirstName());
                    row.createCell(1).setCellValue(customer.getLastName());
                    row.createCell(2).setCellValue(customer.getEmail());
                }

                try (FileOutputStream fileOut = new FileOutputStream(HOTEL_APPLICATION_DATA)) {
                    workbook.write(fileOut);
                } catch (Exception e) {
                    System.out.println("Error while writing customers to file: " + e.getMessage());
                }

                workbook.close();

            } catch (Exception e) {
                System.out.println("Error while writing customers to file: " + e.getMessage());
            }
        }

    }

    public void loadStateFromFile() {
        readCustomersFromFile();
    }
}
