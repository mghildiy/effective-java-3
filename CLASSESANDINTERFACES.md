#Effective Java: Classes and Interfaces

## Minimize the accessibility of classes and members

A well-designed component completely hides it's internal data and implementation details and cleanly separates it's API.
External world then interacts with the component only through it's public API and is totally unaware of component's
internal workings. Hiding internals of components from each other introduces decoupling which enables components to be
developed, tested, modified, used and optimized in isolation without impacting each other. This speeds up development 
process as components can be developed in parallel. Components developed this way are highly reusable and maintainable.

**As a rule, we must try to hide class or class members as much as possible so that they are not part of public API**

This helps in components which are loosely coupled and we can modify them much more easily without worrying about impact
on client code.

For top-level class and interfaces, Java provides two access levels. It can be either public or package-private.
A public class/interface becomes part of public API and hence we need to maintain it forever to maintain compatibility.
So whenever possible, make class/interface package-private so that it is part of internal implementation of the system 
and we can modify ir or remove it without impacting users.
If a package-private top-level class/interface is used by a single class, then we can further reduce its accessibility
by making it private static nested class/interface in the only class that uses it.

For class members(fields, methods, nested classes/interfaces), there are 4 levels of access:

**private**: Accessible only from top level class/interface where it is declared.
**package-private**: Accessible from any class in the containing package.This is default access level. 
**protected**: Accessible from any class in the containing package and subclasses.
**public**: Accessible from anywhere.

protected and public members are part of public API, and hence our efforts must be to have most of the members 
private/package-private

When we override a method from a parent class, we can't change it's access level to a more restrictive one. This rule
is in line with **Liskov Substitution Principle**.

Sometimes developers are tempted to raise the access level of classes, interfaces or class members for testing. This is
not a good practise, though acceptable if we raise the level upto package-private.

Instance fields of public classes should not be public. By making public a nonfinal instance field or a field referring to 
a mutable object, we give up the ability to limit the values that can be stored in the field. This means we sacrifice the ability
to enforce invariants involving the field. Also, we give up the ability to take any action when the field is modified, 
so classes with public mutable fields are not generally thread-safe. Even if a field is final and refers to an 
immutable object, by making it public we give up the flexibility to switch to a new internal data representation in
which the field does not exist.

Various collections and array in Java are mutable and hence it is not a good idea to have a publicly accessible field of
these types, or an accessor that returns such a field. If a class has such a field or accessor, clients will be able to 
modify the contents of the collection/array. This is a common source of security issues.

```
public class AccessTest {

    private final List<String> names = new ArrayList<>();

    public AccessTest(String safeName){
        this.names.add(safeName);
    }

    public List<String> getNames() {
        return this.names;
    }

    public static void main(String[] args) {
        AccessTest test = new AccessTest("Safe name");
        test.getNames().add("Dangerous name");
    }
}
```
Alternate approach to solve this issue would be to return clone of the field from the access method or to have the field
as private and a public immutable copy of it.

```
public class AccessTest {

    private final List<String> names = new ArrayList<>();
    public List<String> UNMOFIDIABLENAMES = Collections.unmodifiableList(names);

    public AccessTest(String safeName){
        this.names.add(safeName);
    }

    public List<String> getNames() {
        List<String> clone = new ArrayList<>(this.names);
        return clone;
    }

    public static void main(String[] args) {
            AccessTest at = new AccessTest("Safe name");
            List clone1 = at.getNames();
            clone1.add("Dangerous name");
            assert at.UNMOFIDIABLENAMES.size() == 1;
            assert at.UNMOFIDIABLENAMES.contains("Safe name");
            List clone2 = at.getNames();
            clone2.set(0, "Dangerous name");
            assert at.UNMOFIDIABLENAMES.size() == 1;
            assert at.UNMOFIDIABLENAMES.contains("Safe name");
    }
}
```

## In public classes, use accessor methods, not public fields

Under certain circumstances, we may create classes which only contain fields.

```
class Point {
    public double x;
    public double y;
}
```

Such classes have no separation between API and internals. It's difficult to take advantages of encapsulation, we can't
enforce invariants and we can't take actions when fields are accessed. We can't modify internals of class without impacting
API. If class is package-private, this approach can be acceptable as it is not part of public API. But if class is public,
it is too difficult to change internal representation. Public classes must have private fields and public access methods.

```
public class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
```

## Minimize mutability

While designing a class, it must be our objective to have the class attain as minimum states as possible. This means
that we must strive to make class immutable, which means than once an instance i screated, it's state can't be modified anymore.
If usecase needs class not to be totally immutable, then we must design it in a way that it has as minimum states as possible.

Large degrees of immutability(total immutability is the objective) brings in many advantages to our application. More immutable
a class is, more easy it is to design, implement and use. Such classes are more secure than mutable classes.

Basic rules to make a class immutable are:

- Class must not provide methods which modify the state of the object
- Make class inextensible(by making it final or making constructors private) so that subclasses can't override methods
- Make fields final
- Make fields private
- If a field is a reference to another mutable object, then ensure that client code doesn't have direct access to it.
Hence, make defensive copies in constructors, access methods, or readObject methods

Immutable objects are inherently thread-safe and they require no synchronization. Hence immutable objects can be shared 
freely. Immutable classes should therefore encourage clients to reuse existing instances wherever possible. One way to do
this is to provide public static final constants for commonly used values. Another way of achieving this is to make
immutable classes provide static factory methods instead of public constructors. This way we can make immutable class
cache the values. This helps in reducing the memory footprint and GC overload. Providing static factory methods instead
of public constructors also make it easy to add caching later on without impacting user.Another consequence of immutable
objects is that we don't need to make defensive copies of them when we share them.
A disadvantage that can be associated with immutable objects is that we need to make an instance for every possible value.
Apart from making a class final to make it immutable, we can also make it's constructor private. This makes it impossible
to extend the class. 
If a class cannot be made immutable, limit its mutability as much as possible.




