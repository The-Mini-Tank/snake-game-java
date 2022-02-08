import java.util.ArrayList;
import java.util.Random;

public class IA {

    public int orientation = 0;
    public int direction = 0;
    public int chunks = 0;
    public int b_size = 0;
    String lim = "free";

    public Boolean loaded = false;

    public Game myGame;

    public IA(){

    }

    public ArrayList<ArrayList<String>> paths = new ArrayList<>();

    public void Start(int chunck, int blocSize){
        SetChunck(chunck);
        PathLoad();
        //PrintPath();
    }

    public void PathLoad(){
        if(!loaded){
        for(int i = 0; i < chunks; i++){
            paths.add(new ArrayList<>());
            for(int j = 0; j < chunks; j++){
                if(i%2 == 0 && j == 0){
                    paths.get(i).add("DOWN");
                } else if (i%2 != 0 && j == 0 && i < chunks-1) { paths.get(i).add("D_RT");}

                if(j%2 == 0 && j > 0 && i == 0){
                    paths.get(i).add("D_LF");
                } else if (j%2 != 0 && j > 0 && i == 0) { paths.get(i).add("LEFT"); }

                if(j%2 == 0 && i == chunks-1){
                    paths.get(i).add("RIGT");
                } else if (j%2 != 0 && i == chunks-1 && j !=chunks-1) { paths.get(i).add("U_RT"); }

                //Fechamento
                if(i%2 == 0 && j == chunks-1 && i > 0){
                    paths.get(i).add("U_LF");
                } else if (i%2 != 0 && j == chunks-1){ paths.get(i).add("UPPE"); }

                if(i > 0 && j > 0 && i < chunks-1 && j < chunks-1){
                    if(i%2== 0 && j%2==0){
                    paths.get(i).add("D_LF");
                    } else if ( i%2 != 0 && j%2==0) { paths.get(i).add("U_RT");}

                    if(i%2 !=0 && j%2 == 0){
                        paths.get(i).add("D_RT");
                    } else if (i%2 ==0 && j%2 != 0) { paths.get(i).add("U_LF");}
                }
            }
        }
        }
    }

    public String GetPosition(int x, int y){
        return paths.get(y).get(x);
    }

    // 1 = RT 2 = LF 3 = UP 4 = DW

    public int Behaviour(String bh){
        int d;

        if(bh == "DOWN"){
            return 4;
        } 
        if(bh == "UPPE"){
            return 3;
        }
        if(bh == "RIGT"){
            return 1;
        }
        if(bh == "LEFT"){
            return 2;
        }

        if(bh == "D_RT"){
            if(lim == "R") {return 4;}  
            if(lim == "D") {return 1;} 

            d = new Random().nextInt(2);
            if(d==0){ return 4;}
            if(d==1){ return 1;}
        }
        if(bh == "D_LF"){
            if(lim == "L") { return 4;} 
            if(lim == "D") { return 2;}
            
            d = new Random().nextInt(2);
            if(d==0){ return 4;}
            if(d==1){ return 2;}
        }
        if(bh == "U_RT"){
            if(lim == "R") {return 3;} 
            if(lim == "U") {return 1;}

            d = new Random().nextInt(2);
            if(d==0){ return 3;}
            if(d==1){ return 1;}
        }
        if(bh == "U_LF"){
            if(lim == "L") {return 3;} 
            if(lim == "U") {return 2;}

            d = new Random().nextInt(2);
            if(d==0){ return 3;}
            if(d==1){ return 2;}
        }

        return 4;
    }

    public void PrintPath(){
        for (int i = 0; i < chunks; i++){
            System.out.println(paths.get(i) + " ");
        }
    }

    public void SetChunck(int i){
        chunks = i;
    }

}
