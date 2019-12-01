import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SIR_Iteration {
    static int NUM = 199;//网络节点个数
    static int ItNum = 100;//SIR迭代次数
    static double deta = 0.8;//康复概率
    static double beta = 0.3;//感染概率
    static int[][] MAT = new  int[NUM][NUM];//节点网络邻接矩阵
    static int[] mat = new int[NUM];//记录每个节点的S、I、R状态函数
    static double[][] Score = new double[ItNum][NUM];//SIR迭代每个节点分数矩阵
    static double[] Score_ItNum = new double[NUM];//求平均后的分数

    static double[] Score_new = new double[NUM];//节点重要性概率模型中每个节点的总分数
    static double[] S_1mat = new double[NUM];//一阶邻居分数
    static double[] S_2mat = new double[NUM];//二阶邻居分数
    static double[] S_3mat = new double[NUM];//三阶邻居分数
    static int[] mat_1 = new int[NUM];//一阶邻居
    static int[][] mat_2 = new int[NUM][NUM];//一阶邻居与二阶邻居的映射
    static int[] mat2to3 = new int[NUM];//二阶邻居
    static int[][] mat_3 = new int[NUM][NUM];//二阶邻居与三阶邻居的映射

    static double n1 = 0;//相关性系数n++
    static double n2 = 0;//相关性系数n--


    //矩阵初始化（归零）
    public static void init_int1(int[] M){
        int len = M.length;
        for(int i = 0; i < len; i++){
            M[i] = 0;
        }
    }

    //矩阵初始化（归零）
    public static void init_db1(double[] M){
        int len = M.length;
        for(int i = 0; i < len; i++){
            M[i] = 0;
        }
    }

    //矩阵初始化（归零）
    public static void init_int2(int[][] M){
        int len = M.length;
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                M[i][j] = 0;
            }
        }
    }

    //1、2、3阶邻居分数求和函数
    public static double SUM(double[] S1,double[] S2, double[] S3){
        double sum = 0;
        int len = S1.length;
        for(int i = 0; i < len; i++){
            sum += S1[i];
        }
        for(int i = 0; i < len; i++){
            sum += S2[i];
        }
        for(int i = 0; i < len; i++){
            sum += S3[i];
        }
        return sum;
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

    //找出一阶邻居
    public static void find1Nub(int n,int[][] M,int[] m){
        int len = M.length;
        for(int i = 0; i < len; i++){
            if(M[n][i] == 1){
                m[i] = 1;
            }
        }
    }

    //求一阶邻居分数
    public static void Score1MAT(int[] m, double[] s){
        int len = m.length;
        for(int i = 0; i < len; i++){
            if(m[i] == 1){
                s[i] = beta;
            }
        }
    }

    //找二阶邻居
    public static void find2Nub(int n,int[][] M,int[] m1,int[][] m2,int[] m2to3){
        int len = M.length;
        for(int i = 0; i < len; i++){
            if(m1[i] == 1){
                for(int j = 0; j < len; j++){
                    if(M[i][j] == 1){
                        m2[i][j] = 1;
                    }
                }
            }
            m2[i][n] = 0;
        }
        for(int i = 0; i < len; i++){
            if(m1[i] == 1){
                for(int j = 0; j < len; j++){
                    m2[j][i] = 0;
                }
            }
        }
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                if(m2[i][j] == 1){
                    m2to3[j] = 1;
                }
            }
        }
    }

    //求二阶邻居分数
    public static void Score2MAT(double[] sm1, int[][] m2, double[] sm2){
        int len = sm1.length;
        double[] sum = new double[len];
        for(int i = 0; i < len; i++){sum[i] = 1;}
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                if(m2[j][i] == 1){
                    sum[i] = sum[i]*(1-sm1[j]*beta);
                }
            }
            sm2[i] = 1 - sum[i];
        }
    }

    //找三阶邻居
    public static void find3Nub(int n, int[][] M, int[][] m3,int[] m1,int[] m2to3){
        int len = M.length;
        for(int i = 0; i < len; i++){
            if(m2to3[i] == 1){
                for(int j = 0; j < len;j++){
                    if(M[i][j] == 1 && m1[j] != 1 && m2to3[j] != 1){
                        m3[i][j] = 1;
                    }
                }
            }
        }
    }

    //求三阶邻居分数
    public static void Score3MAT(double[] sm2,double[] sm3,int[][] m3){
        int len = sm2.length;
        double[] sum = new double[len];
        for(int i = 0; i < len; i++){sum[i] = 1;}
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                if(m3[j][i] == 1){
                    sum[i] = sum[i]*(1-sm2[j]*beta);
                }
            }
            sm3[i] = 1 - sum[i];
        }
    }


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
        return num/len;
    }

    //矩阵初始化（归零）
    public static void InitM(int[] m){
        int len = m.length;
        for(int i = 0; i < len; i++){
            m[i] = 0;
        }
    }


    public static void main(String args[]) throws IOException {
        //将关系网络转换成邻接矩阵形式
        String fn = "C:\\Users\\龚兴SUNNYMAN\\Desktop\\Jazz.txt";
        File file = new File(fn);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tmp = line.trim().split("\\s{2,}|\t");
            int x = Integer.valueOf(tmp[0].trim());
            int y = Integer.valueOf(tmp[1].trim());
            MAT[x][y] = MAT[y][x] = 1;
        }

        //SIR传播仿真模型过程
        for(int n = 0; n < ItNum; n++) {
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
        for(int i = 0; i < NUM; i++){
            Score_ItNum[i] = Averg(i,Score);
        }

        //节点重要性概率模型过程
        for(int i = 0; i < NUM; i++){
            find1Nub(i,MAT,mat_1);
            Score1MAT(mat_1,S_1mat);
            find2Nub(i,MAT,mat_1,mat_2,mat2to3);
            Score2MAT(S_1mat,mat_2,S_2mat);
            find3Nub(i,MAT,mat_3,mat_1,mat2to3);
            Score3MAT(S_2mat,S_3mat,mat_3);
            Score_new[i] = SUM(S_1mat,S_2mat,S_3mat);
            init_int1(mat_1);
            init_int2(mat_2);
            init_int2(mat_3);
            init_db1(S_1mat);
            init_db1(S_2mat);
            init_db1(S_3mat);
        }

        

        //计算相关性的方法
        for(int i = 0; i < NUM; i++){
            for(int j = 0; j < NUM; j++){
                if(i < j){
                    if(((Score_ItNum[i] < Score_ItNum[j]) && (Score_new[i] < Score_new[j]))||((Score_ItNum[i] > Score_ItNum[j]) && (Score_new[i] > Score_new[j]))){
                        n1++;
                    }
                    else if(((Score_ItNum[i] < Score_ItNum[j]) && (Score_new[i] > Score_new[j]))||((Score_ItNum[i] > Score_ItNum[j]) && (Score_new[i] < Score_new[j]))){
                        n2++;
                    }
                }
            }
        }

        System.out.print("n++ = "+n1+"\tn-- = "+n2+"\n");
        double t = 2*(n1-n2)/(NUM*(NUM-1));//相关性值
        System.out.println("相关性值(t) = "+t);

    }

}
