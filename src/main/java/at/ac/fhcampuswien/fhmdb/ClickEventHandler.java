package at.ac.fhcampuswien.fhmdb;

@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);
}

//functional Interface with exactly one abstract method (more static or default methods allowed)
//onClick(T t) -> takes one input, gives nothing back (Consumer Interface)