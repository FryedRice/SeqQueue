/**
* @ Description: Java实现变长数组循环队列数据结构SeqQueue<Item>
    循环队列的容量为 a.length-1，而且本数据结构强制最小容量为10。
    rear指向队尾元素的下一位，front指向队首元素本身。
    队空条件：rear = front
    队满条件：(rear+1)%(a.length) = front
    初始状态：rear = front = 0
* @ Date: Feb.20th 2020
* @ Author: Jay Sonic
*/
import java.util.Iterator;

public class TestSeqQueue {
    public static void main(String[] args) {
        SeqQueue<Integer> test = new SeqQueue<Integer>(32);
        
        System.out.printf("入队测试开始.\n");
        for(int i=0; i<40; ++i){
            test.push(i);
            System.out.printf("当前循环队列容量: %d.\t",test.capacity());
            System.out.printf("当前循环队列元素个数: %d.\n\n",test.size());
        }
        System.out.printf("入队测试结束.\n");
        

        System.out.printf("迭代器测试开始.\n");
        Iterator<Integer> it = test.iterator();
        while(it.hasNext()){
            System.out.printf("%d  ",it.next());
        }System.out.println("");
        System.out.printf("迭代器测试结束.\n");
        
        System.out.printf("出队测试开始.\n");
        int popedItem;
        for(int i=0; i<40; ++i){
            popedItem = test.pop();
            System.out.printf("元素%3d已出队，当前循环队列容量: %d.\t",popedItem,test.capacity());
            System.out.printf("当前循环队列元素个数: %d.\n\n",test.size());
        }
        System.out.printf("出队测试结束.\n");
    }
}

class SeqQueue<Item> implements Iterable<Item>{
    private Item[] a; 
    //private int N = 0;
    private int front = 0;
    private int rear = 0;
    
    public SeqQueue(int max){ //指定初始容量的构造方法
        if(max<10) max = 10; //即使显式指定容量，也至少为10
        a = (Item[]) new Object[max+1]; //注意这里的+1
    }
    public SeqQueue(){ //如未指定，则默认初始容量为10
        this(10);
    }
    
    public boolean isEmpty(){
        return front == rear;
    }
    public int size(){ //返回元素个数
        int temp = rear-front<0? rear-front+a.length : rear-front;
        return temp;
    }
    public int capacity(){//返回当前循环队列的最大容量
        return a.length-1;
    }
    private void reSize(int newMax){ //注意这里的private
        //调整数组的大小
        Item[] temp = (Item[]) new Object[newMax+1];//注意这里的+1
        int x = size();
        for(int i=0; i<x; i++) temp[i] = a[(i+front)%a.length];
        rear = x;
        front = 0;
        a = temp;
    }
    public void push(Item val){ //入队
        if((rear+1)%(a.length) == front) reSize(2*a.length-2); //队满时，先翻倍扩容
        a[rear] = val; //数据入队
        rear = (rear+1)%a.length; //调整队尾指针
    }
    public Item pop(){ //出队
        Item outcome = a[front]; //数据出队
        a[front] = null; //为了利于垃圾对象回收
        front = (front+1)%a.length;//调整队首指针
        //容量>=20，且使用率<=25%时，将容量减半。
        if(a.length>20 && this.size()<=a.length/4) reSize((a.length-1)/2);
        return outcome;
    }
    
    public Iterator<Item> iterator(){//实现Iterable接口
        return new SeqQueueIterator();
    }
    private class SeqQueueIterator implements Iterator<Item>{
        private int tempRear = rear;
        private int tempFront = front;
        public boolean hasNext(){
            return tempRear != tempFront;
        }
        public Item next(){
            Item temp = a[tempFront];
            tempFront = (tempFront+1)%a.length;
            return temp;
        }
        public void remove(){}
    }
}