package org.example.patterns.factory.factorymethod;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
//https://github.com/gupaoedu-tom/spring5-samples

public class DBConnectionPool extends Pool{
    //  正在使用的连接数
    private int checkedOut;
    //  存放产生的连接对象容器
    private Vector<Connection> freeConnections = new Vector<>();
    private String url = null;
    private String password = null;
    private String username = null;
    //  空闲连接数
    private static int num = 0;
    //  当前可用的连接数
    private static int numActive = 0;
    //  连接池实例变量
    private static DBConnectionPool pool = null;

    public static synchronized DBConnectionPool getInstance() {
        if(pool == null) {
            pool = new DBConnectionPool();
        }
        return pool;
    }

    private DBConnectionPool() {
        try{
            init();
            for(int i=0;i<normalConnect;i++) {
                Connection c = newConnection();
                if(c!=null){
                    freeConnections.addElement(c);
                    num++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws IOException {
        InputStream is = DBConnectionPool.class.getResourceAsStream(propertiesName);
        Properties p = new Properties();
        p.load(is);
        this.username = p.getProperty("username");
        this.password = p.getProperty("password");
        this.driverName = p.getProperty("driverName");
        this.url = p.getProperty("url");
        this.maxConnect = Integer.parseInt(p.getProperty("maxConnect"));
        this.normalConnect = Integer.parseInt(p.getProperty("normalConnect"));
    }

    @Override
    public synchronized void freeConnection(Connection con) {
        freeConnections.addElement(con);
        num++;
        checkedOut--;
        numActive--;
        notifyAll();
    }

    private Connection newConnection() {
        Connection con = null;
        try{
            if(username == null) {
                con = DriverManager.getConnection(url);
            } else {
                con = DriverManager.getConnection(url, username, password);
            }
            System.out.println("连接池创建一个新的连接");
        } catch(SQLException e) {
            System.out.println("无法创建这个URL的连接 " + url);
        }
        return con;
    }

    @Override
    public void createPool() {
        pool = new DBConnectionPool();
        if(pool != null) {
            System.out.println("创建连接池成功");
        } else {
            System.out.println("创建连接池失败");
        }
    }

    @Override
    public synchronized Connection getConnection() {
        Connection con = null;
        if(freeConnections.size() > 0) {
            num--;
            con = (Connection) freeConnections.firstElement();
            freeConnections.removeElementAt(0);
            try {
                if(con.isClosed()){
                    System.out.println("从连接池中删除一个无效连接");
                    con = getConnection();
                }
            } catch (SQLException e) {
                System.out.println("从连接池中删除一个无效连接");
                con = getConnection();
            }
        } else if(checkedOut < maxConnect) {
            con = newConnection();
        }
        if(con != null) {
            checkedOut++;
        }
        numActive++;
        return con;
    }

    @Override
    public synchronized Connection getConnection(long time) {
        long startTime = new Date().getTime();
        Connection con;
        while((con = getConnection()) == null) {
            try{
                //线程等待
                wait(time);
            } catch (InterruptedException e) {

            }
            if((new Date().getTime() - startTime) >= time) {
                return null;
            }
        }
        return con;
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public int getNumActive() {
        return numActive;
    }

    @Override
    public synchronized void release() {
        try{
            Enumeration<Connection> allConnections = freeConnections.elements();
            while(allConnections.hasMoreElements()) {
                Connection con = allConnections.nextElement();
                try{
                    con.close();
                    num--;
                } catch(SQLException e) {
                    System.out.println("无法关闭连接池中的连接");
                }
            }
            freeConnections.removeAllElements();
            numActive = 0;
        } finally {
            super.release();
        }
    }
}
