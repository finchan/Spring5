package org.example.patterns.factory.factorymethod;

import java.sql.Connection;
import java.util.Vector;

public class DBConnectionPool extends Pool{
    //  正在使用的连接数
    private int checkedOut;
    //  存放产生的连接对象容器
    private Vector<Connection> freeConnections = new Vector<>();
    private String password = null;
    private String username = null;
    //  空闲连接数
    private static int num = 0;
    //  当前可用的连接数
    private static int numActive = 0;
    //  连接池实例变量
    private static DBConnectionPool pool = null;

    @Override
    public void createPool() {

    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public Connection getConnection(long time) {
        return null;
    }

    @Override
    public void freeConnection(Connection conn) {

    }

    @Override
    public int getNum() {
        return 0;
    }

    @Override
    public int getNumActive() {
        return 0;
    }
}
