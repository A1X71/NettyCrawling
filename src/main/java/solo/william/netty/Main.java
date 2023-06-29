package solo.william.netty;

/**
 * ----------------------------------------------
 * <p>
 * ----------------------------------------------
 *
 * @Author WangDongxu
 * @Date 2022/12/5 12:11
 **/
public class Main {
    public static void main(String[] args) throws Exception{
        int port = 8080;
        if(args!=null && args.length >0){
            try{
                port = Integer.valueOf(args[0]);
            }catch(NumberFormatException e){

            }
        }
        new TimeServer().bind(port);
    }
}
