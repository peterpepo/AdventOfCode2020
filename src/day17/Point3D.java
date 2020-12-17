package day17;

public class Point3D {

    private int x;
    private int y;
    private int z;
    

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }

    @Override
    // Taken from JDK's Point
    public int hashCode() {
    	int result = (int) (x ^ (x >>> 32));
    	result = 31* result + (int) (y ^ (y >>> 32));
    	result = 31* result + (int) (z ^ (z >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point3D other = (Point3D) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + getX() + ";" + getY() + ";" + getZ() + "]";
    }

}
