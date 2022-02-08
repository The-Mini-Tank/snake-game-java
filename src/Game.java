import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.Rectangle;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.sound.sampled.*;

public class Game extends Canvas implements Runnable, KeyListener, ActionListener, ChangeListener{

    int mapSize = 3;
    int startSize = 2;
    int resX = 128*mapSize, resY = 128*mapSize;

    IA myIa = new IA();
    Junkbox junkbox = new Junkbox();
    MyFonts myFonts = new MyFonts();
    MenuGraphics myMenu = new MenuGraphics(this, myFonts, resX, resY);

    File sound_apple = new File("resources/apple_sound.wav");
    File sound_gameOver = new File("resources/game_over.wav");
    File sound_win = new File("resources/game_win.wav");

    ArrayList<Node> blocks = new ArrayList<Node>();
    ArrayList<Integer> nPosX = new ArrayList<Integer>();
    ArrayList<Integer> nPosY = new ArrayList<Integer>();

    Boolean running = false;
    Boolean gameOver = false;
    Boolean win = false;
    Boolean limits = false;
    Boolean ia = false;
    Boolean showPath = false;
    Boolean pathGenerated = false;

    int time = 0, score = 0, moves = 0;
    int direction = 0, orientation = 0;
    int velocity = 15, baseVelocity = 15;

    int blocSize = 30;
    int offset = 32;
    int x = 0, y = 0;
    int chunks = resX/offset;
    int apple_x = 0, apple_y = 0;


    public static void main(String args[]) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        Game game = new Game();
        new Thread(game).start();
    }

    public Game() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        
        myFonts.Load();
        myMenu.Load();

        SnakeLoad();
        AppleSpawn();

        myIa.Start(chunks, offset);
        myIa.loaded = true;
        myIa.myGame = this;

        this.addKeyListener(this);
    }

    public void SnakeLoad(){
        for(int i = 0; i < startSize; i++){
            blocks.add(new Node(0,0));
         }
 
         for(int i = 0; i < blocks.size(); i++){
             nPosX.add(i);
             nPosY.add(i);
         }
    }

    public void Update() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        
        // >> MENU GRAPHICS SETTINGS

        myMenu.text.setText("PTS: " + score + " MOV: " + moves);
        myMenu.check1.setEnabled(!running);
        myMenu.button_restart.setEnabled(!running);
        myMenu.button_ia.setEnabled(!gameOver && !win);
        myMenu.gameOverText.setVisible(gameOver);
        myMenu.winText.setVisible(win);

        limits = myMenu.check1.isSelected();
        junkbox.mute = myMenu.check3.isSelected();
        showPath = myMenu.checkPath.isSelected();

        // -----

        // >> Booleans

        if(running) time = time + 1;

        if(blocks.size() == chunks*chunks && !win) Win();

        if(!win) CheckAppleCollision();

        // -----
        
        if(ia){
            running = true;
            if(!pathGenerated){ myIa.Start(chunks, offset); pathGenerated = true; SnakeMove(4); }
            myMenu.button_ia.setText("Stop");
        } else { myMenu.button_ia.setText("IA");}


        // >> SNAKE MOVEMENT
       
        if(time >= velocity){
            if(!ia) SnakeMove(direction);

            if(ia){
                myIa.lim = ValidateMovement();
                SnakeMove(myIa.Behaviour(myIa.GetPosition(x, y)));
                myIa.lim = ValidateMovement();
            }
        }

        // -----

        // >> LIMITS SWITCH

        if(!gameOver){
            if(blocks.get(0).x < 0){
                if(!limits){blocks.get(0).x = resX-offset; x = chunks;} else { GameOverAction(); } 
            } else if (blocks.get(0).x >= resX){
                if(!limits){blocks.get(0).x = 0; x = 0;} else { GameOverAction(); } 
            } else if(blocks.get(0).y < 0){
                if(!limits){blocks.get(0).y = resY-offset; y = chunks;} else { GameOverAction(); } 
            } else if(blocks.get(0).y >= resY){
                if(!limits){blocks.get(0).y = 0; y = 0;} else { GameOverAction(); }
            }
        }

        // -----

    }

    public void DoMove(int dir){
        direction = dir;
        running = true;
        if(dir != orientation) moves++;
        myMenu.button_ia.setEnabled(false);
    }

    public String ValidateMovement(){

        String output = "free";
        System.out.print(myIa.GetPosition(x, y) + " ");
        System.out.print("x: " + x + " y: " + y + " ");

        if(myIa.GetPosition(x, y) == "U_LF"){
            output = "free";
            for(int i = 1; i < blocks.size()-1; i++){
                if(blocks.get(0).x-offset == blocks.get(i).x && blocks.get(0).y == blocks.get(i).y){
                        output = "L";
                } else if(blocks.get(0).y-offset == blocks.get(i).y && blocks.get(0).x == blocks.get(i).x){
                        output = "U";
                }
            }
            System.out.print(output);
        }
        else if(myIa.GetPosition(x, y) == "U_RT"){
            for(int i = 1; i < blocks.size()-1; i++){
                if(blocks.get(0).x+offset == blocks.get(i).x && blocks.get(0).y == blocks.get(i).y){
                        output = "R";
                } else if ( blocks.get(0).y-offset == blocks.get(i).y && blocks.get(0).x == blocks.get(i).x){
                        output = "U";
                }
            }
            System.out.print(output);
        }
        else if(myIa.GetPosition(x, y) == "D_LF"){
            output = "free";
            for(int i = 1; i < blocks.size()-1; i++){
                if(blocks.get(0).y+offset == blocks.get(i).y && blocks.get(0).x == blocks.get(i).x){
                        output = "D";
                } else if (blocks.get(0).x-offset == blocks.get(i).x && blocks.get(0).y == blocks.get(i).y){
                    output = "L";
                }
            }
            System.out.print(output);
        }
        else if(myIa.GetPosition(x, y) == "D_RT"){
            output = "free";
            for(int i = 1; i < blocks.size()-1; i++){
                if(blocks.get(0).y+offset == blocks.get(i).y && blocks.get(0).x == blocks.get(i).x){
                        output = "D";
                } else if (blocks.get(0).x+offset == blocks.get(i).x && blocks.get(0).y == blocks.get(i).y){
                        output = "R";
                }
            }
            System.out.print(output);
        } 
        else { output ="oneway"; System.out.print(output);}
        
        System.out.println(" "); 

        return output;
    }

    public void SnakeMove(int dir) throws IOException, UnsupportedAudioFileException, LineUnavailableException{

             if (dir == 1) { x++; }
        else if (dir == 2) { x--; }
        else if (dir == 3) { y--; }
        else if (dir == 4) { y++; }

        if(ia) moves++;
        
        for(int i = 0; i < blocks.size(); i++){
            nPosX.set(i,blocks.get(i).x);
            nPosY.set(i,blocks.get(i).y);
        }

        if(dir == 1){
            orientation = 1;
            blocks.get(0).x += offset;
        } 
        else if(dir == 2){
            orientation = 2;
            blocks.get(0).x -= offset;
        }
        else if(dir == 3){
            orientation = 3;
            blocks.get(0).y -= offset;
        }
        else if(dir == 4){
            orientation = 4;
            blocks.get(0).y += offset;
        }

        for(int i = 1; i < blocks.size(); i++){
           blocks.get(i).x = nPosX.get(i-1);
           blocks.get(i).y = nPosY.get(i-1);
        }

        CheckSelfCollision();
        time = 0;

        }
    

    public void CheckAppleCollision() throws  IOException, UnsupportedAudioFileException, LineUnavailableException{
        if(new Rectangle(blocks.get(0).x, blocks.get(0).y, blocSize, blocSize).intersects( new Rectangle(apple_x, apple_y, blocSize, blocSize))){
            Increase();
            junkbox.PlaySound(sound_apple);
            if(blocks.size() < (chunks*chunks)){
                AppleSpawn();
                if(ia) ValidateMovement();
            }
            score++;
        } 
    }

    public void AppleSpawn(){
        apple_x = new Random().nextInt(resX/offset)*offset;
        apple_y = new Random().nextInt(resY/offset)*offset;
        for(int i = 0; i < blocks.size(); i++){
            if(apple_x == blocks.get(i).x && apple_y == blocks.get(i).y){
                AppleSpawn();
                break;
            }
        }
    }

    public void CheckSelfCollision() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        for(int i = 1; i < blocks.size(); i ++){
            if(blocks.get(0).x == blocks.get(i).x && blocks.get(0).y == blocks.get(i).y){
                GameOverAction();
            }
        }
    }

    public void Increase(){
        blocks.add(new Node(blocks.get(blocks.size()-1).x,blocks.get(blocks.size()-1).y));
        blocks.get(blocks.size()-1).x = nPosX.get(nPosX.size()-1);
        blocks.get(blocks.size()-1).y = nPosY.get(nPosY.size()-1);

        nPosX.add(0);
        nPosY.add(0);
        Render();
    }

    public void RestartGame(){
        blocks.clear();
        nPosX.clear();
        nPosY.clear();

        for(int i = 0; i < startSize; i++){
            blocks.add(new Node(7*offset,0));
        }

        for(int i = 0; i < blocks.size(); i++){
            nPosX.add(i);
            nPosY.add(i);
            blocks.get(i).x = 0;
            blocks.get(i).y = 0;
        }

        moves = 0;
        score = 0;
        time = 0;
        x = 0;
        y = 0;
        running = false;
        gameOver = false;
        win = false;
        ia = false;
    
    }

    public void GameOverAction() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        if(!gameOver){
            junkbox.PlaySound(sound_gameOver);
            direction = 0;
            running = false;
            ia = false;
            gameOver = true;
        }
    }

    public void Win() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        win = true;
        running = false;
        gameOver = false;
        ia = false;
        time = 0;
        myMenu.button_restart.setEnabled(true);
        junkbox.PlaySound(sound_win);
    }

    public void Render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        
        // >> BACKGROUND

        g.setColor(Color.getHSBColor(0, 0, 0.45f));
        g.fillRect(0, 0, resX, resY);

        g.setColor(Color.gray);
        for(int i = 0; i < resX/offset; i++){
            for(int j = 0; j < resY/offset; j++){
                g.fillRect(i*offset, j*offset, blocSize, blocSize);
            }
        }
        g.setColor(Color.white);

        // -----


        // >> SNAKE BODY COLOR

        if(!gameOver){
        for(int i = 1; i < blocks.size(); i++){
                g.setColor(Color.black);
                g.fillRect(blocks.get(i).x, blocks.get(i).y, blocSize, blocSize);
            }
        } else {
        for(int i = 1; i < blocks.size(); i++){
                g.setColor(Color.red);
                g.fillRect(blocks.get(i).x, blocks.get(i).y, blocSize, blocSize);
            }   
        }

        // -----

        // >> SNAKE HEAD COLOR

        g.setColor(Color.white);
        g.fillRect(blocks.get(0).x,blocks.get(0).y, blocSize, blocSize);

        // -----

        // >> APPLE

        if(!win){
            g.setColor(Color.red);
            g.fillRect(apple_x, apple_y, blocSize, blocSize);
            g.setColor(Color.white);
            g.fillRect(apple_x+4, apple_y+4, 7, 7);
        }

        // -----

        // >> DRAW PATHS

        if(showPath) { 

            Boolean collumSwitch = false;
            Boolean lineSwitch = false;

            Boolean cSwitch1 = false;
            Boolean cSwitch2 = false;

            g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
            g.setColor(Color.green);

            for (int i = 0; i < chunks; i++){

                for(int j = 0; j < chunks; j++){
                    if(i == 0 && !collumSwitch){
                        g.drawString("▽", 8+i*offset,20+j*offset);
                    } else if(i == 0 && collumSwitch && j != chunks-1){
                        g.drawString("▽▸", 2+i*offset,20+j*offset);
                    }

                    if(i == chunks-1 && collumSwitch && j > 0){
                        g.drawString("△", 10+i*offset,20+j*offset);
                    } else if (i == chunks-1 && !collumSwitch && j > 0){
                        g.drawString("◂△", 0+i*offset,20+j*offset);
                    }

                    if(j == 0 && i>0 && lineSwitch){
                        g.drawString("◁", 10+i*offset,20+j*offset);
                    } else if (j == 0 && i>0 && !lineSwitch){
                        g.drawString("◂▽", 0+i*offset,20+j*offset);
                    }

                    if(j == chunks-1 && i < chunks - 1 && !lineSwitch){
                        g.drawString("▷", 10+i*offset,20+j*offset);
                    } else if (j == chunks-1 && lineSwitch && i < chunks-1){
                        g.drawString("△▸", 2+i*offset,20+j*offset);
                    }  
                    collumSwitch = !collumSwitch;
                }
                lineSwitch = !lineSwitch;
            }

            g.setColor(Color.cyan);

            for(int i = 1; i < chunks-1; i++){
                for(int j = 1; j < chunks-1; j++){
                    if(j > 0 && !cSwitch1){
                        if(!cSwitch2)
                        g.drawString("△▸", 2+i*offset,20+j*offset);
                        if(cSwitch2)
                        g.drawString("◂△", 2+i*offset,20+j*offset);
                    } else if (j > 0 && cSwitch1) { 
                        if(!cSwitch2)
                        g.drawString("▽▸", 2+i*offset,20+j*offset);
                        if(cSwitch2)
                        g.drawString("◂▽", 2+i*offset,20+j*offset);
                    }
                    cSwitch2 = !cSwitch2;
                }
                cSwitch1 = !cSwitch1;
            }
    } 

        // -----

        g.dispose();
        bs.show();
    }


    @Override
    public void run() {
        
        while(true){
            try {
                Update();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                e1.printStackTrace();
            }
            Render();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                System.out.print("Ticks: ■ ");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!gameOver && !win && !ia){
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                if(orientation != 2)
                DoMove(1);
            }
            else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                if(orientation != 1)
                DoMove(2);
            }
            else if(e.getKeyCode() == KeyEvent.VK_UP){
                if(orientation != 4)
                DoMove(3);
            }
            else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                if(orientation != 3)
                DoMove(4);
            }
        }
   
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == myMenu.button_exit){
            System.exit(0);
        }
        if(e.getSource() == myMenu.button_restart){
            RestartGame();
        }
        if(e.getSource() == myMenu.button_ia){
            ia = !ia;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        velocity = baseVelocity - myMenu.mySpeed.getValue();
    }
    
}
