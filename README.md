## Item 1: Static factory method

Classes generally have constructors to enable client code to create an instance of the said class.
Another way of enabling clients to have instances of a class is to have a static factory method in the class. 
This approach has several advantages.
1. We can have descriptive names for such a method
2. We can have multiple such  methods with exactly same signature
3. As such a method is not required to create a new instance every time it is invoked, it allows
to cache the instances and use them whenever needed. This helps in ways like, to implement singletons or
ensure that we don't have duplicates of immutable value classes, like numerals.
4. Static factory method can return objects of subtype of declared return type. This helps in developing
an API which is interface based, and clients are not in know of implementation classes. Client just needs to 
know the API defined by interface. Also, class of returned object can be a function of arguments passed.

Some disadvantages of having only static factory methods and no public/protected constructors are:
1. Such classes can't be subclassed.
2. They are hard to locate. This can be handled to an extent by using names like of, valueOf, create, from etc.

## Item 2: Builder when too many constructor parameters

If a class has too many properties to be set, then constructors may become quite cumbersome to write and read as well.
An alternative can be to use constructors for setting mandatory properties and using setter methods for optional properties.
But it means our class is not immutable. An approach can be to use builder class, a static class defined inside the target class.
Constructor for builder class takes mandatory parameters, and has setters for other optional properties, and every setter returns builder
object itself so that calls can be chained, and finally we have a method, typically named 'build', which creates target object and returns it.
Client code creates object of builder class and then chains the setter methods as per need, finally calling build method to get instance
of target class.

```
public class MyClass {
    private final int id;
    private final int version;
    private final String name;
    private final String status;
    private final double lat;
    private final double lon;

    private MyClass(Builder builder) {
        this.id = builder.id;
        this.version = builder.version;
        this.name = builder.name;
        this.status = builder.status;
        this.lat = builder.lat;
        this.lon = builder.lon;
    }

    public static class Builder {
        // mandatory
        private final int id;
        private final int version;

        // optional
        private String name;
        private String status;
        private double lat;
        private double lon;

        public Builder(int id, int version) {
            this.id = id;
            this.version = version;
        }

        public Builder name(String val) {
            this.name = val;
            return this;
        }

        public Builder status(String val) {
            this.status = val;
            return this;
        }

        public Builder lat(double val) {
            this.lat = val;
            return this;
        }

        public Builder lon(double val) {
            this.lon = val;
            return this;
        }

        public MyClass build() {
            return new MyClass(this);
        }
    }
}
```

Client code:
```
MyClass instance = new MyClass.Builder(101, 1).name("Test Name").status("Test Status").lat(12.56).lon(56.12).build();
```

## Item 3: Implement singleton with a private constructor or an enumn type

A singleton class can be instantiated only once, ie application contains a single instance of the class. Singleton has a
major disadvantage that their client code is difficult to unit test as it is impossible to prvide a mock implementation
for a singleton, unless if implements an interface.

Singleton can be implemented in several ways.

One way is to provide a private constructor so that no client code can invoke the constructor, and provide a public
member to provide access to the instance. 

This member can be a field:

```
public class Singleton {
    public static Singleton INSTANCE = new Singleton();
    
    private Singleton() {
        
    }
    
    public void doYourStuff() {
        
    }
}
```
...or, it can be a factory method:

```
public class Singleton {
    private static Singleton INSTANCE = new Singleton();
    
    private Singleton() {
        
    }

    public static Singleton getInstance() {
        return INSTANCE;
    }
    
    public void doYourStuff() {
        
    }
}
```

Field based approach is easier to implement, while factory method based approach has advantage that we can easily make
class non-singleton without changing API of the class.

These approaches have a loophole  that deserialization can cause multiple instances of supposedly singleton class.
To have guarantee singleton,we declare all instance variables transient and provide implementation of readResolve method.
Also, private constructor can be invoked using reflection API, which we can have a workaround by throwing exception in
constructor when invoked again after first time.

```
private Object readResolve() {
    return INSTANCE;
}
```

Another approach to implement singleton is enum based.

```
public enum Singleton {
    INSTANCE;

    public void doYourStuff() {

    }
}
```

This approach is much more clean as it provides all the protection against deserialization and reflection issues.

## Item 4: Enforce noninstantiability using private constructor

If we need a class which contains only static members, like utility classes, then there is no point in having instances
of such classes. We may be tempted to enforce it by making class abstract, but that doesn't work as class can be subclassed
and subclass can be instantiated. We can enforce noninstantiability by making constructor private:

```
public class NonInstantiableClass {
    
    private NonInstantiableClass() {
        throw new AssertionError("This class can't be instantiated");
    }
}
```

With constructor as private, no code outside the class can invoke it. AssertionError ensures that code inside the class also
is not able to instantiate it. Also, this class can't be subclassed as there is no accessible superclass constructor to be
invoked from subclass constructors.


