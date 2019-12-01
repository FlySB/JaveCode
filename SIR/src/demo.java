import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class demo {

    static int NUM = 4941;//网络节点个数
    static int ItNum = 1000;//SIR迭代次数
    static double deta = 0.8;//康复概率
    static double beta = 0.3;//感染概率
    static int[][] MAT = new  int[NUM][NUM];//节点网络邻接矩阵
    static int[] mat = new int[NUM];//记录每个节点的S、I、R状态函数
    static double[][] Score = new double[ItNum][NUM];//SIR迭代每个节点分数矩阵
    static double[] Score_ItNum = new double[NUM];//求平均后的分数



    //SIR过程，S——>I的过程
    public static void findNubS(int n,int[][] M,int[] m){
        int len = M.length;
        for(int i = 0; i < len; i++){
            if(M[n][i] == 1){
                double prob = Math.random()*1;
                if(prob < beta){
                    m[i] = 1;
                }
            }
        }
    }

    //SIR过程，I——>R的过程
    public static void findNubI(int[] m){
        int len = m.length;
        for(int i = 0; i < len; i++) {
            if (m[i] == 1) {
                double prob = Math.random() * 1;
                if (prob < deta) {
                    m[i] = 2;
                }
            }
        }
    }

    //判断所有节点中是否有I节点
    public static boolean findI(int[] m){
        int len = m.length;
        for(int i = 0; i < len; i++){
            if(m[i] == 1){
                return true;
            }
        }
        return false;
    }

    //计算SIR分数
    public static double score(int[] m){
        double len = m.length;
        double num = 0;
        for(int i = 0; i < len; i++){
            if(m[i] == 2){
                num++;
            }
        }
        return num;
    }

    //矩阵初始化（归零）
    public static void InitM(int[] m){
        int len = m.length;
        for(int i = 0; i < len; i++){
            m[i] = 0;
        }
    }

    //多次SIR分数求平均
    public static double Averg(int n,double[][] M){
        double sum = 0;
        int len = M.length;
        for(int i = 0; i < len; i++){
            sum += M[i][n];
        }
        return sum/ItNum;
    }

    public static int[] Compute(int n, double Input[]){
        int out[] = new int[n];
        for (int i = 0; i < n; i++) {
            double k = 0;
            for (int j = 0; j < Input.length; j++) {
                if (k < Input[j]) {
                    k = Input[j];
                    out[i] = j;
                }
            }
            Input[out[i]] = 0;
        }
        return out;
    }


    public static void main(String args[]) throws IOException {
        //将关系网络转换成邻接矩阵形式
        String fn = "C:\\Users\\龚兴SUNNYMAN\\Desktop\\powernet.txt";
        //String fn = "C:\\Users\\龚兴SUNNYMAN\\Desktop\\Jazz.txt";
        File file = new File(fn);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tmp = line.trim().split("\t"); // "\\s{2,}|\t"
            int x = Integer.valueOf(tmp[0].trim());
            int y = Integer.valueOf(tmp[1].trim());
            MAT[x][y] = MAT[y][x] = 1;
        }

        //SIR传播仿真模型过程
        for (int n = 0; n < ItNum; n++) {
            for (int q = 0; q < NUM; q++) {
                mat[q] = 1;//假设q点感染
                while (findI(mat)) { //当网络中含有I节点，迭代继续
                    for (int i = 0; i < NUM; i++) {
                        if (mat[i] == 1) {
                            findNubS(i, MAT, mat);
                            findNubI(mat);
                        }
                    }
                }
                Score[n][q] = score(mat);
                InitM(mat);
            }
        }

        //经过ItNum次迭代后，求平均
        for (int i = 0; i < NUM; i++) {
            Score_ItNum[i] = Averg(i, Score);
            //System.out.println((i)+" "+Score_ItNum[i]);
        }
        double S[] = new double[NUM];
        for(int i = 0; i < NUM; i++){
            S[i] = Score_ItNum[i];
        }
        int Out[] = new int[100];
        Out = Compute(100, S);
        for(int i = 1; i < 100; i++){
            System.out.println(Out[i] +"\t"+ Score_ItNum[Out[i]]);
        }
    }
}
