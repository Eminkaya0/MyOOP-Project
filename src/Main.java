import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Employee employee1 = new Employee("Ahmet", "Software Engineer", 5000);
        Employee employee2 = new Employee("Ayse", "Data Scientist", 6000);
        Manager manager = new Manager("Elif", "Engineering Manager", 8000);
        Intern intern = new Intern("Mehmet", "Intern", 2000, 6);

        System.out.println(employee1.getDetails());
        System.out.println(employee2.getDetails());
        System.out.println(manager.getDetails());
        System.out.println(intern.getDetails());

        manager.addTeamMember(employee1);
        manager.addTeamMember(employee2);
        manager.addTeamMember(intern);
        System.out.println(manager.getTeamDetails());

        Director director = new Director("Selin", "Director of Engineering", 15000, 2000);
        director.addManager(manager);
        System.out.println(director.getDetails());
        System.out.println(director.getAllTeamDetails());

        // Application Examples
        System.out.println("\n-- Application Examples --");

        // Sorting team members by salary
        List<Employee> team = manager.getTeamMembers();
        Collections.sort(team, Comparator.comparingDouble(Employee::getSalary).reversed());
        System.out.println("Team Members sorted by salary (descending):");
        for (Employee e : team) {
            System.out.println(e.getDetails());
        }

        // Finding the employee with the highest salary
        Employee highestPaid = Collections.max(team, Comparator.comparingDouble(Employee::getSalary));
        System.out.println("\nHighest paid employee: " + highestPaid.getDetails());

        // Calculating total salary expenses for a manager's team
        double totalSalary = team.stream().mapToDouble(Employee::getSalary).sum();
        System.out.println("\nTotal salary expenses for manager's team: " + totalSalary);

        // Filtering interns from the team
        System.out.println("\nInterns in the team:");
        team.stream().filter(e -> e instanceof Intern).forEach(e -> System.out.println(e.getDetails()));

        // Calculating the average salary of the team members
        double averageSalary = team.stream().mapToDouble(Employee::getSalary).average().orElse(0.0);
        System.out.println("\nAverage salary of the team: " + averageSalary);

        // Finding a specific employee by name
        String searchName = "Ayse";
        Optional<Employee> foundEmployee = team.stream().filter(e -> e.getName().equals(searchName)).findFirst();
        System.out.println("\nSearching for employee named '" + searchName + "':");
        foundEmployee.ifPresentOrElse(
            e -> System.out.println(e.getDetails()),
            () -> System.out.println("Employee not found")
        );

        // Grouping employees by role
        Map<String, List<Employee>> employeesByRole = team.stream().collect(Collectors.groupingBy(Employee::getRole));
        System.out.println("\nEmployees grouped by role:");
        employeesByRole.forEach((role, employees) -> {
            System.out.println("Role: " + role);
            employees.forEach(e -> System.out.println("  " + e.getDetails()));
        });

        // Giving a raise to all employees
        System.out.println("\nGiving a 10% raise to all team members:");
        team.forEach(e -> e.setSalary(e.getSalary() * 1.10));
        team.forEach(e -> System.out.println(e.getDetails()));
    }
}

// Base class representing a generic person
abstract class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getDetails();
}

// Class representing a generic employee
class Employee extends Person {
    private String role;
    private double salary;

    public Employee(String name, String role, double salary) {
        super(name);
        this.role = role;
        this.salary = salary;
    }

    public String getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String getDetails() {
        return "Employee: " + getName() + ", Role: " + role + ", Salary: " + salary;
    }
}

// Class representing a manager who can manage a team
class Manager extends Employee {
    private List<Employee> teamMembers;

    public Manager(String name, String role, double salary) {
        super(name, role, salary);
        teamMembers = new ArrayList<>();
    }

    public void addTeamMember(Employee employee) {
        teamMembers.add(employee);
    }

    @Override
    public String getDetails() {
        return "Manager: " + getName() + ", Role: " + getRole() + ", Salary: " + getSalary();
    }

    public String getTeamDetails() {
        StringBuilder details = new StringBuilder("Team Members:\n");
        for (Employee member : teamMembers) {
            details.append(member.getDetails()).append("\n");
        }
        return details.toString();
    }

    public List<Employee> getTeamMembers() {
        return teamMembers;
    }
}

// Class representing a director who manages multiple managers
class Director extends Employee {
    private List<Manager> managers;
    private double bonus;

    public Director(String name, String role, double salary, double bonus) {
        super(name, role, salary);
        this.bonus = bonus;
        managers = new ArrayList<>();
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    @Override
    public String getDetails() {
        return "Director: " + getName() + ", Role: " + getRole() + ", Salary: " + getSalary() + ", Bonus: " + bonus;
    }

    public String getAllTeamDetails() {
        StringBuilder details = new StringBuilder("All Team Members:\n");
        for (Manager manager : managers) {
            details.append(manager.getDetails()).append("\n");
            details.append(manager.getTeamDetails()).append("\n");
        }
        return details.toString();
    }
}

// Class representing an intern
class Intern extends Employee {
    private int internshipDuration; // in months

    public Intern(String name, String role, double salary, int internshipDuration) {
        super(name, role, salary);
        this.internshipDuration = internshipDuration;
    }

    public int getInternshipDuration() {
        return internshipDuration;
    }

    @Override
    public String getDetails() {
        return "Intern: " + getName() + ", Role: " + getRole() + ", Salary: " + getSalary() + ", Duration: " + internshipDuration + " months";
    }
}
