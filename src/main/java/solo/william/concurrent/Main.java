package solo.william.concurrent;

import static java.util.Objects.hash;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/11/20 19:56
 **/
public class Main {
    public static void main(String[] args){
        Videoconference videoconference = new Videoconference(10);
        Thread threadConference = new Thread(videoconference);
        threadConference.start();
        for(int i=0;i<10;i++){
            Participant p = new Participant(videoconference,"Participant " + i);
            Thread t = new Thread(p);
            t.start();
        }
        //region 测试利用hash值来取得数组元素；结果：不能正确取得
//        String names[] = new String[5];
//        String a= "a";
//        String b= "b";
//        String c= "c";
//        String d= "d";
//        String e= "e";
//        names[0]=a;
//        names[1]=b;
//        names[2]=c;
//        names[3]=d;
//        names[4]=e;
//        System.out.println((names.length-1) & hash(a));
//        System.out.println((names.length-1) & hash(b));
//        System.out.println((names.length-1) & hash(c));
//        System.out.println((names.length-1) & hash(d));
//        System.out.println((names.length-1) & hash(e));
        //endregion
    }
}
