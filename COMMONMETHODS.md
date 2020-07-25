#Effective Java: Overriding Object's methods

java.lang.Object class is at the root of the java class hierarchy. It has few non-final methods which child classes override.
It is important for classes to obey the general contracts of these methods to keep functioning correctly.

## Overriding equals method

**equals** method in Object class implements the equality check using referential equality. So two objects are equal only
if they are same referentially. 
We may not need to override equals method if class has no notion of logical equality, like Thread class or regex.Pattern class.
Sometimes parent class already has an appropriate implementation of equals, like Set implementations inherits equals implementation
from AbstractSet.

We need to override equals if our class needs to provide logical equality, like value classes in application. For example,
our application may have class Order to represent a business order. Two orders may be different in terms of memory identity,
but they may represent same order functionally(like if hey have same id). Some value objects, like enums, may not need to
implement equals as they exist atmost one and hence memory identity suffices for their equality check.

If we finally decide to implement equals method, then we need to ensure that following contract is obeyed:

1. If x is non-null reference, then x.equals(null) must return false
2. Reflexivity: If x is non-null reference, x.equals(x) must return true
3. Symmetric: For non-null references x and y, x.equals(y) must return same result as y.equals(x)
4. Transitive: For non-null references x,y and z, if x.equals(y) returns true, y.equals(z) returns true, then x.equals(z)
must also return true
5. Consistent: For non-null references x and y, x.equals(y) must return same result when invoked several times, given
the properties used in equals' implementation haven't changed

A good implementation of equals method:

1. Use the == operator to check if the argument is a reference to this object. If so, return true. 
   This optimizes the performance, particularly if comparison is expensive.
2. Use the **instanceof** operator to check if the argument has the correct type.If not, return false. 
   Typically, the correct type is the class in which the method occurs. 
3. Cast the argument to the correct type. As it comes after instanceof test, it is guaranteed to succeed.
4. For each field in the class meant for comparison, check if that field of the argument matches the corresponding field of this object.

For primitive fields whose type is not float or double, use the == operator for comparisons; for object reference fields,
call the equals method recursively;for float fields, use the static Float.compare(float, float) method; and for 
double fields, use Double.compare(double, double).

Once we have implemented equals method, we should check that if meets the requirements mentioned above, and write unit tests.