package lock;

import org.junit.Test;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Demo {
    // 实例化一个ReentrantLock对象
    private ReentrantLock lock = new ReentrantLock();
    // 为线程A注册一个Condition
    private Condition conditionA = lock.newCondition();
    @Test
    public void a(){
        print();
        System.out.println("22222");

    }

    private void print() {
        System.out.println("1");
    }
}
