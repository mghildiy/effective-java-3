package commonmethods;

public final class Employee {
    private final int age;
    private final String name;

    public Employee(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof Employee)) {
            return false;
        }

        Employee other = (Employee) o;

        return this.age == other.age & this.name.equals(other.name);
    }
}