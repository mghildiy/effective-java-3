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
