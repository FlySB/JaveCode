import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class test {

    public static int[][] MAT = new int[9][9];
    static double[] S_1mat = new double[9];
    static double[] S_2mat = new double[9];
    static double[] S_3mat = new double[9];
    static double beta = 0.3;
    static int[] mat_1 = new int[9];
    static int[][] mat_2 = new int[9][9];
    static int[][] mat_3 = new int[9][9];
    static double[] Score_new = new double[9];
    static int[] mat2to3 = new int[9];

    public static void init_int1(int[] M){
        int len = M.length;
        for(int i = 0; i < len; i++){
            M[i] = 0;
        }
    }

    public static void init_db1(double[] M){
        int len = M.length;
        for(int i = 0; i < len; i++){
            M[i] = 0;
        }
    }


    public static void init_int2(int[][] M){
        int len = M.length;
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                M[i][j] = 0;
            }
        }
    }


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

    public static void find1Nub(int n,int[][] M,int[] m){
        int len = M.length;
        for(int i = 0; i < len; i++){
            if(M[n][i] == 1){
                m[i] = 1;
            }
        }
    }

    public static void Score1MAT(int[] m, double[] s){
        int len = m.length;
        for(int i = 0; i < len; i++){
            if(m[i] == 1){
                s[i] = beta;
            }
        }
    }



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
            //System.out.println(sm2[i]);
        }
    }

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

    public static void matOUT(int[] m){
        int len = m.length;
        for(int i = 0; i < len; i++){
            System.out.print(m[i]+"\t");
        }
        System.out.println();
    }

    public static void dmatOUT(double[] m){
        int len = m.length;
        for(int i = 0; i < len; i++){
            System.out.print(m[i]+"\t");
        }
        System.out.println();
    }

    public static void MATOUT(int[][] M){
        int len = M.length;
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                System.out.print(M[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String args[]) throws Exception{
        String fn = "/Users/gong/Desktop/shujuwajue shiyam 5/111.txt";
        File file = new File(fn);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] tmp = line.trim().split(" ");
            int x = Integer.valueOf(tmp[0].trim());
            int y = Integer.valueOf(tmp[1].trim());
            MAT[x][y] = MAT[y][x] = 1;
        }
    }
}
