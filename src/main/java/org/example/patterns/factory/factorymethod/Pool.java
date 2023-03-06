package org.example.patterns.factory.factorymethod;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class Pool {
    private String propertiesName = "connection-INF.properties";

    private static Pool instance = null;

    //  最大连接数
    protected int maxConnect = 100;
    //  保持连接数
    protected int normalConnect = 10;
    //  驱动字符串
    protected String driverName = null;
    //  驱动变量
    protected Driver driver = null;

    protected Pool() {
        try {
            init();
            loadDriver(driverName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void init() throws IOException {
        InputStream is = Pool.class.getResourceAsStream(propertiesName);
        Properties p = new Properties();
        p.load(is);
        this.driverName = p.getProperty("driverName");
        this.maxConnect = Integer.parseInt(p.getProperty("maxConnect"));
        this.normalConnect = Integer.parseInt(p.getProperty("normalConnect"));
    }

    private void loadDriver(String dri) {
        String driverClassName = dri;
        try{
            driver = (Driver) Class.forName(driverClassName).newInstance();
            DriverManager.registerDriver(driver);
            System.out.println("成功注册JDBC驱动程序 " + driverClassName);
        } catch(Exception e) {
            System.out.println("无法注册JDBC驱动程序 " + driverClassName + " error: " + e);
        }
    }

    public abstract void createPool();

    public static synchronized Pool getInstance() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if(instance == null) {
            instance = (Pool) Class.forName("org.example.patterns.factory.factorymethod.Pool").newInstance();
        }
        return instance;
    }

    //  创建一个连接，没有则创建一个连接，且小于最大连接限制
    public abstract Connection getConnection();

    public abstract Connection getConnection(long time);

    //  将连接对象返回给连接池
    public abstract void freeConnection(Connection conn);

    //  返回当前空闲连接数
    public abstract int getNum();

    //  返回当前工作连接数
    public abstract int getNumActive();

    protected synchronized void release() {
        try{
            DriverManager.deregisterDriver(driver);
            System.out.println("撤销JDBC驱动程序 " + driver.getClass().getName());
        } catch (SQLException e) {
            System.out.println("无法撤销JDBC驱动程序的注册：" + driver.getClass().getName());
        }
    }
}
