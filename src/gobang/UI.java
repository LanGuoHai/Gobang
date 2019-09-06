package gobang;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*界面处理*/

public class UI {
    private ChessBoard chess = ChessBoard.getInstance();
    private int userColor = chess.WHITE;       //当前鼠标下子，白色
    private int robotColor = chess.BLACK;
    private Frame frame = new Frame("五子棋盘");
    private MyChess drawArea = new MyChess();
    private Robot robot = Robot.getRobot();
    public boolean end=false;
    public void init(){

        Panel p = new Panel();
        //机器执先
        chess.makeMove(chess.N/2+1, chess.N/2+1, chess.BLACK);

        drawArea.setPreferredSize(new Dimension(720, 720));

        drawArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {   //鼠标监听事件
                if(end==true)return ;
                int x = e.getX();
                int y = e.getY();
                int i = (x-60)/40+1;
                int j = (y-60)/40+1;
                System.out.println(i+"---"+j);        /*人下子*/
                if(chess.isEmpty(i, j)) {

                    chess.makeMove(i, j, userColor);
                    int rel = chess.isEnd(i, j, userColor);
                    if(rel != 0) {
                        System.out.println("玩家胜利");
                        end=true;
                        return;
                    }
                    drawArea.repaint();

                    int rob[] = robot.getNext(robotColor);  //机器获取下子位置


                    chess.makeMove(rob[0], rob[1], robotColor); //机器下子
                    rel = chess.isEnd(rob[0], rob[1], robotColor);   //判断结束函数，返回0未结束，1机器胜，2人胜;
                    if(rel != 0) {
                        System.out.println("机器胜利");
                        end=true;
                        return;
                    }
                    drawArea.repaint();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        frame.add(drawArea);
        frame.setSize(900, 720);
        frame.setVisible(true);
    }
}


class MyChess extends Canvas {

    private ChessBoard chess = ChessBoard.getInstance();
    private final int N = chess.N + 2;
    private final int square = 40;
    private final int stx = square;
    private final int sty = square;
    private final int length = (N-1)*square;

    public void drawPiece(int color, int x, int y, Graphics g) {   //画落子
        if (color == chess.BLACK) {;		 //画黑子
            g.setColor(new Color(0, 0, 0));
            g.fillArc(stx + x * square - 19, sty + y * square - 19, 38, 38, 0, 360);
        } else if (color == chess.WHITE) {   //画白子
            g.setColor(new Color(255, 255, 255));
            g.fillArc(stx + x * square - 19, sty + y * square - 19, 38, 38, 0, 360);
        }
    }

    public void paint(Graphics g) {

        g.setColor(new Color(0, 0, 0));
        g.fillRect(stx-8, sty-8, stx+(N-2)*square+15, sty+(N-2)*square+15);
        g.setColor(new Color(249,214,91));
        g.fillRect(stx-4, sty-4, stx+(N-2)*square+7, sty+(N-2)*square+7);

        g.setColor(new Color(0, 0, 0));

        g.fillArc(stx+8*square-6, sty+8*square-6, 12, 12, 0, 360);
        g.fillArc(stx+4*square-6, sty+4*square-6, 12, 12, 0, 360);
        g.fillArc(stx+4*square-6, sty+12*square-6, 12, 12, 0, 360);
        g.fillArc(stx+12*square-6, sty+4*square-6, 12, 12, 0, 360);
        g.fillArc(stx+12*square-6, sty+12*square-6, 12, 12, 0, 360);

        for(int i = 0; i < N; i++) {		//画15*15的网格线

            g.drawLine(stx+i*square, sty, stx+i*square, sty+length);
            g.drawLine(stx, sty+i*square, stx+length, sty+i*square);
        }

        for(int i=1; i<=chess.N; i++) {
            for(int j=1; j<=chess.N; j++) {
                drawPiece(chess.board[i][j], i, j, g);    //画落子
            }
        }
    }
}