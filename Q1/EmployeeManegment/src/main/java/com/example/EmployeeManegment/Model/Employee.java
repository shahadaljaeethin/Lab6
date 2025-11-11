package com.example.EmployeeManegment.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Employee {
//    public Employee(String id, String name, String email, String phoneNumber, int age, String position, boolean onLeave, double annualLeave, LocalDateTime hireDate) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.age = age;
//        this.position = position;
//        this.onLeave = onLeave;
//        this.annualLeave = annualLeave;
//        this.hireDate = hireDate;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public String getPosition() {
//        return position;
//    }

    //================================================================================
    @NotEmpty(message = "write ID")
    @Size(min = 2, message = "ID length must be larger than 2.")
    private String id ;
//-
    @NotEmpty(message = "write name")
    @Size(min = 4,message = "name should have more than 4 letters")
    @Pattern(regexp = "[^0-9]*", message = "Only letters are allowed in employee name")
    private String  name;
//-
    @Email(message = "enter valid Email address")
    private String  email;
//-
    @Size(min = 10,max = 10,message = "Phone number must be 10digits")
    @Pattern(regexp = "^05.*$",message = "Phone number must start with 05")
    private String  phoneNumber;
//-
    @NotNull(message = "enter the age")
    @Positive(message = "age must be in numbers")
    @Min(value = 25,message = "age must be over 25")
    private int age;
//-
    @NotEmpty(message = "choose the position")
    @Pattern(regexp = "^(supervisor|coordinator)$")
    private String  position;
//-
    @AssertFalse(message = "Employee must not be on leave")
    private boolean onLeave;
//-
    @NotNull(message = "enter annual leave")
    @Positive(message = "annual must be in positive amount")
    private double annualLeave;
//-
@NotNull(message = "enter the hire date")
@PastOrPresent(message = "invalid date, must be on present or past")
    private LocalDateTime hireDate;
//================================================================================
}
