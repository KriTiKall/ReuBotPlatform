package model;

public interface Mailing {

    void addObserver(String id, String nameOfGroup, Sender sender);

    void removeObserver();
}
