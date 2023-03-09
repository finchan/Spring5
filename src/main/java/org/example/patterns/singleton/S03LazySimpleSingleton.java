package org.example.patterns.singleton;

public class S03LazySimpleSingleton {
    private static S03LazySimpleSingleton lazySimpleSingleton = null;
    private S03LazySimpleSingleton() {

    }
    public synchronized static S03LazySimpleSingleton getInstance() {
        if(lazySimpleSingleton == null){
            lazySimpleSingleton = new S03LazySimpleSingleton();
        }
        return lazySimpleSingleton;
    }

    public static void main(String[] args) {

    }
}
