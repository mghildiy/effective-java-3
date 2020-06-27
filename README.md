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

## Item 5: Prefer dependency injection over hardwiring resources

If we have a class which depends on a resource, we may be tempted to program it as:

```
public class Worker {
    
    final Resource res = new Resource();
    
    public void doStuff() {
        res.getIt();
        ....
        ....
    }
}

public class Resource {
    public void getIt(){
        ....
    }
}
```
Problem with above approach is that it assumes Resource to of only one type, while client may actually want Worker to
work with Resource of varying nature, as per the need(like for testing we may want Resource to be a mock). 
We may opt for setting Resource through a setter method, but it is errorprone and won't work in multithreaded environment.
Requirement is to have multiple instances of Worker class with each of them using the Resource provided by client. 
A pattern  which solves this problem is **Dependency Injection**. This states that when we create a class, we also inject the
resources it needs. 
One form of DI is to inject resources via constructor.

```
public class Worker {

    final Resource res;
    
    public Worker(Resource res) {
        this.res = res;
    }

    public void doStuff() {
        res.getIt();
    }
}
```
 Another form of DI is to inject resource factory through contructor, which class can use to create different type of 
 resources as per the client's requirement. Java 8's Supplier<T> interface is a good choice for resource factories.
 
 Dependency injection makes our classes more flexible, reusable and testable.
 
 ## Item 6: Eliminate obsolete object references
 
 Java provides the facility of automatic memory management, thus relieving programmers of an otherwise arduous task.
 Java's garbage collection mechanism keeps looking for those objects which are no more referenced from the program
 and destroy them.
 
 But it may very well happen that there are references in the program which are never used again,and hence objects
 they refer to are never garbage collected. Such references are called **obsolete references**. An obvious result is
 reduced program performance or it may even lead to failures like OutOfMemoryError, though relatively rare.
 To ensure that our program doesn't suffer from such issues, we must ensure to null out obsolete references. Another advantage
 of nulling out obsolete references is that if they are mistakenly dereferenced, program would fail instead of keep
 doing wrong things.
 
 But we must not make it a practise to null out every reference variable. It only clutters the program and is also not needed.
 As soon as a reference variable goes out of scope, it is eliminated, and so we don't need to null it out.
 
 Common sources of obsolete references are caches with objects lying inside even when not more relevant, classes managing
 their own memory like stack implementations, listeners and callbacks as users register them and then forget to deregister them.
 
 ## Item 7: Avoid finalizers
 
 Java offers several mechanisms of releasing resources when no more needed. Commonly used are **try-finally** block
 or **try-with-resources** block. 
 
 Another mechanism for releasing resources **finalize**. Finalize must be avoided as its behaviour is erratic,
 unpredictable and has portability issues. There are sevaral reasons for this. First, Java doesn't guarantee that
 finalizer would be executed at all. This means if our program behaviour relies on execution of finalizer, then it would
 lead to erratic behaviour. Hence, we must not be performing tasks like updating some state in finalizer, like releasing
 lock on a shared resource. Also, Java doesn't ensure that finalizer would be executed as soon as object is ready to be
 garbage collected. It means we can't be sure resources released in finalizer are immediately available. Moreover, the
 promptness with which finalizer is executed depends on GC algorithm which varies from on JVM implementation to other.
 This means program behaviour varies from one JVM implementation to another JVM implementation. This all means that
 we must never perform time critical tasks in finalizer, like releasing file handlers, DB connections, releasing locks.
 Finalizer poses another issue related to exception. Normally, when an uncaught exception is thrown, stack trace is printed
 and thread terminates. But if exception is thrown in finalizer, it is ignored. No warning or error trace is printed.
 This means we may be left with objects with corrupt state, and if threads use those objects it may lead to 
 nondeterministic behaviour.

Finalizer also poses security issues. If an exception is thrown in a constructor, object is left in a malformed state, which
is then garbage collected. But a subclass's finalizer can record a reference to such a malformed object in a static field
and then use it. Final class doesn't have this issue as ti can't be subclassed. In case of non-final class, we can implement
a final finalizer so that it can't be overridden in subclass.

With finalizer having so many issues, a better alternative to release resources is to make class implement **AutoCloseable** interface.
Clients of the class then needs to call **close** method on class instance when it is no more needed, typically with try-with-resources
so that invocation of close is ensured even if there is an exception. Instance needs to keep track that close has been called on it
and if a method is called on it after that, then it may throw appropriate exception, like IllegalStateException.

#Item 8: Prefer try-with-resources over try-finally

Java provides **AutoCloseable** interface for resources which need to be closed. A very common pattern for using it is:

```
public String readFrom(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
}
```

In finally block we close the resources.But this approach has an issue. If an exception is thrown inside both try and 
finally block, first exception is suppressed and stack trace has no record of it, which makes debugging of root cause very difficult.

A much better way of closing resources is to make use of **try-with-resources**.

```
public String readFrom(String path) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(path));) {
            return br.readLine();
        }
}
```

Now, if exception is thrown in both try and finally block, second one is suppressed in favour of first. Suppressed exceptions
are printed in stack trace with notations that they are suppressed. We can access them programmatically using method **getSuppressed**.


 
 
 
  
 



