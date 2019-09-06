package gobang;
public class ChessBoard {
    public final int N = 15;
    public final int EMPTY = 0;
    public final int BLACK = 1;   //黑子 1
    public final int WHITE = 2;   //白子2

    public int[][] board = new int[N+1][N+1];		//保存棋盘棋子位置

    private ChessBoard() {}
    private static final ChessBoard chess = new ChessBoard();

    /* 返回类单例 */
    public static ChessBoard getInstance() {
        return chess;
    }

    /*  判断该位置是否无子 */
    public boolean isEmpty(int x, int y) {
        return board[x][y] == EMPTY;
    }

    /* 落子 */
    public void makeMove(int x, int y, int color) {
        board[x][y] = color;
    }

    /* 撤子 */
    public void unMove(int x, int y) {
        board[x][y] = EMPTY;
    }

    /*推算*/
    public int reckon(int color) {   //color 为棋子颜色，1黑，2,白

        int dx[] = {1, 0, 1, 1};			//每个dx[i],dy[i]都代表一个方向
        int dy[] = {0, 1, 1, -1};
        int ans = 0;

        for(int x=1; x<=N; x++) {
            for (int y = 1; y <= N; y++) {	//遍历棋盘中所有位置
                if (board[x][y] != color)    //该board[x][y]位置不是同color色棋子
                    continue;					//则继续找其他点

                int num[][] = new int[2][100];

                for (int i = 0; i < 4; i++) {
                    int sum = 1;
                    int flag1 = 0, flag2 = 0;

                    int tx = x + dx[i];
                    int ty = y + dy[i];
                    while (tx > 0 && tx <= N
                            && ty > 0 && ty <= N
                            && board[tx][ty] == color) { //往一个方向寻找同色棋子
                        tx += dx[i];
                        ty += dy[i];
                        ++sum;
                    }

                    if(tx > 0 && tx <= N
                            && ty > 0 && ty <= N
                            && board[tx][ty] == EMPTY)  //到最后一个同色棋子
                        flag1 = 1;  					//末尾没有被堵住，活的

                    tx = x - dx[i];					//回到为空的上一个色系点
                    ty = y - dy[i]; //x,y还是棋盘中当前所在的点
                    while (tx > 0 && tx <= N
                            && ty > 0 && ty <= N
                            && board[tx][ty] == color) { //逆向的寻找同色棋子
                        tx -= dx[i]; 			//逆方向，减回去找首部
                        ty -= dy[i];
                        ++sum;
                    }

                    if(tx > 0 && tx <= N
                            && ty > 0 && ty <= N
                            && board[tx][ty] == EMPTY)//到最首部的第一个同色棋子
                        flag2 = 1;		        //同色串的首部也是活的

                    if(flag1 + flag2 > 0)   //活的，单活 或者 双活
                        ++num[flag1 + flag2 - 1][sum];   //
                }     //num[][]
                //第一个【】中数值只有0或1（0代表单活，1代表双活）
                //成5
                if(num[0][5] + num[1][5] > 0)
                    ans = Math.max(ans, 100000);
                    //活4 | 双死四 | 死4活3
                else if(num[1][4] > 0 || num[0][4] > 1
                        || (num[0][4] > 0 && num[1][3] > 0))
                    ans = Math.max(ans, 10000);
                    //双活3
                else if(num[1][3] > 1)
                    ans = Math.max(ans, 5000);
                    //死3活3
                else if(num[1][3] > 0 && num[0][3] > 0)
                    ans = Math.max(ans, 1000);
                    //死4
                else if(num[0][4] > 0)
                    ans = Math.max(ans, 500);
                    //单活3
                else if(num[1][3] > 0)
                    ans = Math.max(ans, 200);
                    //双活2
                else if(num[1][2] > 1)
                    ans = Math.max(ans, 100);
                    //死3
                else if(num[0][3] > 0)
                    ans = Math.max(ans, 50);
                    //双活2
                else if(num[1][2] > 1)
                    ans = Math.max(ans, 10);
                    //单活2
                else if(num[1][2] > 0)
                    ans = Math.max(ans, 5);
                    //死2
                else if(num[0][2] > 0)
                    ans = Math.max(ans, 1);

            }
        }
        //遍历一遍棋盘全局之后，算出最大估值的ans；
        return ans;
    }

    /* 判断局面是否结束 0未结束 1WHITE赢 2BLACK赢 */
    public int isEnd(int x, int y, int color) {
        int dx[] = {1, 0, 1, 1};
        int dy[] = {0, 1, 1, -1};

        for (int i = 0; i < 4; i++) {
            int sum = 1;				//步数同色子数量，一条直线上的；

            int tx = x + dx[i];
            int ty = y + dy[i];
            while (tx > 0 && tx <= N
                    && ty > 0 && ty <= N
                    && board[tx][ty] == color) {    //同色子判定   往正的那四个方向找
                tx += dx[i];
                ty += dy[i];
                ++sum;
            }

            tx = x - dx[i];					//往反方向找
            ty = y - dy[i];
            while (tx > 0 && tx <= N
                    && ty > 0 && ty <= N
                    && board[tx][ty] == color) {

                tx -= dx[i];
                ty -= dy[i];
                ++sum;
            }
            if(sum >= 5)     //同色系棋子满五颗就返回赢的色号
                return color;
        }
        return 0;
    }
}