package com.example.EmployeeManegment.Controller;
//import java.lang.*;

import com.example.EmployeeManegment.Api.ApiResponse;
import com.example.EmployeeManegment.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee management")
public class EmployeeController {

    ArrayList<Employee> employees=new ArrayList<>();

@PostMapping("/add")
public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee emp, Errors errors)
{
    if(errors.hasErrors())
        return ResponseEntity.status(400).body(new ApiResponse(errors.getFieldError().getDefaultMessage()));
    employees.add(emp);
    return ResponseEntity.status(200).body("employee added"); //to avoid error
}
@GetMapping("/get")
public ResponseEntity<?> getAll(){
    return ResponseEntity.status(200).body(employees);}

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable String id,@RequestBody @Valid Employee employee,Errors error){
    //1) validate given info
    if(error.hasErrors()) return ResponseEntity.status(400).body(new ApiResponse(error.getFieldError().getDefaultMessage()));
    boolean found = false;
    //2) find the employee
    for(int i=0;i<employees.size();i++){ if(employees.get(i).getId().equals(id)){
        employees.set(i,employee);
        return ResponseEntity.status(200).body(new ApiResponse("the employee"+employee.getName()+" updated"));
    }}
    //if not found
     return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
      }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String id){
    //1) find employee
        for(int q=0; q<employees.size();q++){
            if(employees.get(q).getId().equals(id)){
                String empID=employees.get(q).getId();
                employees.remove(q);
                return ResponseEntity.status(200).body(new ApiResponse("the employee with ID#"+empID+" got removed"));}
        }
     return ResponseEntity.status(400).body(new ApiResponse("ID not found"));

    }
@GetMapping("/get/{position}")
    public ResponseEntity<?> filterEmpByPosition(@PathVariable String position){
    if(position.equalsIgnoreCase("coordinator")||position.equalsIgnoreCase("supervisor")){
        ArrayList<Employee> filter = new ArrayList<>();
        for(Employee e:employees) if(e.getPosition().equalsIgnoreCase(position)) filter.add(e);
        //---
        if(filter.isEmpty())
            return ResponseEntity.status(400).body(new ApiResponse("there is no employees in this position currently"));
        return ResponseEntity.status(200).body(filter);
    }
    return ResponseEntity.status(400).body(new ApiResponse("invalid position"));
    }
    @GetMapping("/get/age/{min}/{max}")
    public ResponseEntity<?> filterEmpByAge(@PathVariable int min,@PathVariable int max){
    //1) if min or max values are not valid (min 26 to max 70)
        if(min<26) return ResponseEntity.status(400).body(new ApiResponse("minimum age starts at 26"));
        if(max>70) return ResponseEntity.status(400).body(new ApiResponse("maximum age ends at 70"));

//2) do the end-point
    ArrayList<Employee> filter = new ArrayList<>();
    for(Employee e: employees) if(e.getAge()>=min&&e.getAge()<=max) filter.add(e);
    //----
     if(filter.isEmpty()) return ResponseEntity.status(400).body(new ApiResponse("there is no employees in this age range"));
     return ResponseEntity.status(200).body(filter);
    }
    @PutMapping("/on leave/{id}")
    public ResponseEntity<ApiResponse> applyAnnualLeave(@PathVariable String id){
//1) search for the employee
for(int i=0;i<employees.size();i++){if(employees.get(i).getId().equals(id)){
    //2)check if  employee can take the leave
    Employee e = employees.get(i);
    if(e.getAnnualLeave()==0) return ResponseEntity.status(400).body(new ApiResponse(e.getName()+" does NOT have any leaves left"));
    if(e.isOnLeave()) return ResponseEntity.status(400).body(new ApiResponse("The employee is already on leave"));
//============================ if all validation are ok
    employees.get(i).setOnLeave(true);
    employees.get(i).setAnnualLeave(employees.get(i).getAnnualLeave()-1);
    return ResponseEntity.status(200).body(new ApiResponse("employee "+e.getName()+" got the leave successfully, the remaining leaves are "+employees.get(i).getAnnualLeave()));
}
}
return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
    }
@GetMapping("/no leaves")
public ResponseEntity<?> getNoAnnualEmp(){
ArrayList<Employee> filter = new ArrayList<>();
for(Employee e:employees) if(e.getAnnualLeave()==0) filter.add(e);
if(filter.isEmpty()) return ResponseEntity.status(400).body(new ApiResponse("All employees have available leave days"));
return ResponseEntity.status(200).body(filter);
    }

@PutMapping("/promote/{fromSupervisor}/{empID}")
 public ResponseEntity<ApiResponse> promoteEmployee(@PathVariable String fromSupervisor,@PathVariable String empID){
    for(int i=0;i<employees.size();i++){
    if(fromSupervisor.equals(employees.get(i).getId()))
        if(employees.get(i).getPosition().equals("supervisor")){
            for(int e=0;e<employees.size();e++){
                if(employees.get(e).getId().equals(empID)) {
                    Employee vald =  employees.get(e);
                    if(vald.getAge()<30) return ResponseEntity.status(400).body(new ApiResponse(employees.get(i).getName()+", the employee you are looking is NOT older than 29"));
                    if(vald.isOnLeave()) return ResponseEntity.status(400).body(new ApiResponse(employees.get(i).getName()+", the employee you are looking is on leave"));
                    if(vald.getPosition().equals("supervisor")) return ResponseEntity.status(400).body(new ApiResponse(employees.get(i).getName()+", the employee you are looking is already a supervisor"));

                    employees.get(e).setPosition("supervisor");
                    return ResponseEntity.status(200).body(new ApiResponse("Employee got promoted!"));
                }


            } return ResponseEntity.status(400).body(new ApiResponse(employees.get(i).getName()+", the employee you are looking for is not found"));
    }
    else return ResponseEntity.status(400).body(new ApiResponse("Not allowed: Employee is NOT a supervisor."));
    }
    return ResponseEntity.status(400).body(new ApiResponse("Supervisor not found"));





 }

}

