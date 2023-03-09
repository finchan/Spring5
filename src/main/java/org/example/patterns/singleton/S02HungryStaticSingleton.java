package org.example.patterns.singleton;

public class S02HungryStaticSingleton {
    private static S02HungryStaticSingleton hungryStaticSingleton = null;
    static{
        hungryStaticSingleton = new S02HungryStaticSingleton();
    }
    private S02HungryStaticSingleton() {

    }
    public static S02HungryStaticSingleton getInstance() {
        return hungryStaticSingleton;
    }

    public static void main(String[] args) {
        System.out.println(S02HungryStaticSingleton.getInstance());
        System.out.println(S02HungryStaticSingleton.getInstance());
        System.out.println(S02HungryStaticSingleton.getInstance());
    }
}
