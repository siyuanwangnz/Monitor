package main.java;

public interface Memento {
    Memento NULL = new Memento() {
        public void rollback() {
        }
    };

    void rollback();
}
