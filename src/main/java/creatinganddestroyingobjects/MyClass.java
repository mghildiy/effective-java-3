package creatinganddestroyingobjects;

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
