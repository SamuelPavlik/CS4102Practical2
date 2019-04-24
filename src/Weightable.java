public interface Weightable {
    Weightable add(Weightable color);

    Weightable mult(double scalar);

    Weightable copy();

    @Override
    String toString();
}
