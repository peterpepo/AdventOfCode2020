package day17;

public class Point4D {

    private int x;
    private int y;
    private int z;
    private int w;
    

    public Point4D(int x, int y, int z, int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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
    
    public int getW() {
    	return w;
    }

    @Override
    // Taken from JDK's Point
    public int hashCode() {
    	int result = (int) (x ^ (x >>> 32));
    	result = 31* result + (int) (y ^ (y >>> 32));
    	result = 31* result + (int) (z ^ (z >>> 32));
    	result = 31* result + (int) (w ^ (w >>> 32));
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
        final Point4D other = (Point4D) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        if (this.w != other.w) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + getX() + ";" + getY() + ";" + getZ() + ";" + getW() + "]";
    }

}
