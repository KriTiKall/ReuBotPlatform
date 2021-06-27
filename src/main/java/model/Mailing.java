package model;

public interface Mailing {
//todo (planning)
    void addObserver(String id, String nameOfGroup, Sender sender);

    void removeObserver();
}
