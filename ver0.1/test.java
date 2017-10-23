
/**
 * Write a description of class test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class test
{

    public static int Aloop(int n) { 
        int cnt = 0;
        for(int i=1; i<=n; i++)
            for(int j=1; j<=i*i; j++)
                if ( j%i == 0 ) // i.e., when j is a multiple of i  
                    cnt ++ ;
          return cnt;
    }
    
    public static void main(String[] args){
        System.out.println(Aloop(5));
        System.out.println(Aloop(10));
        System.out.println(Aloop(17));
        System.out.println(Aloop(24));
        
    }
    
}
