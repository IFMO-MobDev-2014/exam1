package ru.ya.exam;

/**
 * Created by vanya on 21.01.15.
 */
public class MyType {
    public String lable;
    public boolean flag;

    public MyType() {
    }
    public MyType(String lable, boolean flag) {
        this.lable = lable;
        this.flag = flag;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getLable() {

        return lable;
    }

    public boolean isFlag() {
        return flag;
    }
}
