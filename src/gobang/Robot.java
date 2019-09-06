package gobang;

import java.util.Random;
public class Robot {

    private static ChessBoard chess = ChessBoard.getInstance();
    private Robot() {}
    private static int depth = 1;     //深度
    private static int robotColor = chess.BLACK;     //机器棋子颜色为： 黑色
    private static Robot robot = new Robot();

    public static Robot getRobot() {
        return robot;
    }

    /* alpha_beta剪枝搜索 */
    public int alpha_betaFind(int depth, int alpha, int beta, int color, int prex, int prey) {

        if(depth >= Robot.depth || 0 != chess.isEnd(prex, prey, color%2+1)) { //退出是？？
            //深度 当深度大于等于1时就返回ans值		//判断是否结束出现连的五子，未结束返回0，结束返回1或2；
            int ans = chess.reckon(robotColor) - chess.reckon(robotColor%2 + 1);
            //推算    下子当前局盘中位置的最大位2置估值 - 敌方下子全局中的最大估值
            if(depth % 2 == 0)   //0是人的操作min
                ans = -ans;
            return ans;
        }

        for(int x=1; x<=chess.N; x++) {
            for(int y=1; y<=chess.N; y++) {     //遍历棋盘中每一个点
                if(!chess.isEmpty(x, y)) //如果棋盘满了，则退出遍历
                    continue;
                chess.makeMove(x, y, color);   //预测落子
                int val = -alpha_betaFind(depth+1, -beta, -alpha, color%2+1, x, y);
                // 						 棋子变成对手子颜色
                chess.unMove(x, y);  		   //回收落子

                if(val >= beta)				//如果估值（后备节点的alpha值大于祖先节点的beta值）
                    return beta;		//beta剪枝，人走的数值低的估值

                if(val > alpha)
                    alpha = val;
            }
        }
        return alpha;   //alpha剪枝，返回当前最大的
    }


    /* 返回AI走法 */
    public int[] getNext(int color) {
        int rel[] = new int[2];
        int ans = -100000000;

        Random random = new Random();

        for(int x=1; x<=chess.N; x++) {
            for(int y=1; y<=chess.N; y++) {   //遍历棋盘

                if(!chess.isEmpty(x, y))
                    continue;

                chess.makeMove(x, y, color);

                int val = -alpha_betaFind(0, -100000000, 100000000, color%2 + 1, x, y);
                //0是深度；alp ；  bet    ；     颜色取反   ；

                int ra = random.nextInt(100);
                if(val > ans || val == ans && ra >= 50) { //比较出整棋盘的最大估值，保存其位置
                    ans = val;
                    rel[0] = x;
                    rel[1] = y;
                }
                chess.unMove(x, y);
            }
        }
        System.out.println("黑子"+color);
        return rel;   //返回的值数组也就是下子的x,y坐标值。
    }
}