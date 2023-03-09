package org.example.patterns.singleton;

public class S01HungrySingleton {
    private final static S01HungrySingleton hungrySingleton = new S01HungrySingleton();

    private S01HungrySingleton() {

    }

    public static S01HungrySingleton getInstance() {
        return hungrySingleton;
    }

    public static void main(String[] args) {
        System.out.println(S01HungrySingleton.getInstance());
        System.out.println(S01HungrySingleton.getInstance());
        System.out.println(S01HungrySingleton.getInstance());
    }
}