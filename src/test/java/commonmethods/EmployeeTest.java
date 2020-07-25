package commonmethods;


import org.junit.Assert;
import org.junit.Test;

public class EmployeeTest {

    @Test
    public void testEquals_NullArgument() {
        Employee emp = new Employee(25, "John Doe");

        Assert.assertFalse(emp.equals(null));
    }

    @Test
    public void testEquals_Reflexivity() {
        Employee emp = new Employee(25, "John Doe");

        Assert.assertTrue(emp.equals(emp));
    }

    @Test
    public void testEquals_Symmetry() {
        Employee emp = new Employee(25, "John Doe");
        Employee emp1 = new Employee(25, "John Doe");

        Assert.assertTrue(emp.equals(emp1));
        Assert.assertTrue(emp1.equals(emp));

        emp1 = new Employee(25, "John Do");

        Assert.assertFalse(emp.equals(emp1));
        Assert.assertFalse(emp1.equals(emp));
    }

    @Test
    public void testEquals_Transitivity() {
        Employee emp = new Employee(25, "John Doe");
        Employee emp1 = new Employee(25, "John Doe");
        Employee emp2 = new Employee(25, "John Doe");

        Assert.assertTrue(emp.equals(emp1));
        Assert.assertTrue(emp1.equals(emp2));
        Assert.assertTrue(emp.equals(emp2));
    }
}
