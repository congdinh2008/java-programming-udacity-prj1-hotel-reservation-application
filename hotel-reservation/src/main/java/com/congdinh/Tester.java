package com.congdinh;

import com.congdinh.model.Customer;

public class Tester {
    public static void main(String[] args) {
        // Add Customer customer = new Customer ("first", "second", "j@domain.com");
        Customer customer = new Customer("first", "second", "j@domain.com");
        // Next line Add System. out. println (customer) ; Note: If you have overridden the toString() method for the Customer class, this line will display the class contents
        System.out.println(customer);

        // Add Customer customer1 = new Customer ("first", "second", "email");
        Customer customer1 = new Customer("first", "second", "email");
        // An exception will be thrown when the email is not valid
        System.out.println(customer1);
    }
}
