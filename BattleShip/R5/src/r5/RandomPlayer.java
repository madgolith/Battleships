/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package r5;

import battleship.interfaces.BattleshipsPlayer;
import battleship.interfaces.Fleet;
import battleship.interfaces.Position;
import battleship.interfaces.Board;
import battleship.interfaces.Ship;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Tobias
 */
public class RandomPlayer implements BattleshipsPlayer
{

    
    // For shooting.
    private static int fireX;
    private static int fireY;
    private static int tempX;
    private static int tempY;
    private static int hunting = 0; // from 1-4
    private static boolean bulletHit;
    private static boolean huntMode = false;
    private static boolean destroyMode = false;
    private static boolean continueKillPath = false;
    private static int path = 0; // either -1 or +1
    private static boolean enemyOnY = false;
    private static boolean enemyOnX = false;
    public static int[][] fireCoord = new int[10][10];
    public static int[][] priorityBoard = new int[10][10];
    List<String> priorityList = new ArrayList<String>();
    
    // For our board.
    private final static Random rnd = new Random();
    private int sizeX;
    private int sizeY;
    private Board myBoard;
    int[][] preBoard = new int[10][10];


   
    public RandomPlayer()
    {
    }

   
    /**
     * The method called when its time for the AI to place ships on the board 
     * (at the beginning of each round).
     * 
     * The Ship object to be placed  MUST be taken from the Fleet given 
     * (do not create your own Ship objects!).
     * 
     * A ship is placed by calling the board.placeShip(..., Ship ship, ...) 
     * for each ship in the fleet (see board interface for details on placeShip()).
     * 
     * A player is not required to place all the ships. 
     * Ships placed outside the board or on top of each other are wrecked.
     * 
     * @param fleet Fleet all the ships that a player should place. 
     * @param board Board the board were the ships must be placed.
     */
    @Override
    public void placeShips(Fleet fleet, Board board)
    {
       
        
        //preBoard =initPreBoard(preBoard);
        myBoard = board;
        sizeX = board.sizeX();
        sizeY = board.sizeY();
        showPreBoard();
       
        for(int skibsNr = 0; skibsNr < fleet.getNumberOfShips(); ++skibsNr)
        {
            
            System.out.println("SkibsNr: "+skibsNr);
            Ship s = fleet.getShip(skibsNr);
            boolean vertical = rnd.nextBoolean();
            Position pos = new Position(10,10);
            boolean go = false;
            while(go==false)
            {
                if(vertical)
                {

             
                    int x = rnd.nextInt(sizeX);
                    int size = s.size();
                    
                    int y = rnd.nextInt(sizeY-(s.size()-1));
                    pos = new Position(x, y);
                    boolean tempGo = true;
                    System.out.println(" vi har retning true =vertical: "+ vertical+" her kommer pos "+x+y );
                    
                    for (int j = y; j < y+s.size(); j++) 
                    {
                        System.out.println("for loops pos: "+j);
                        if(preBoard[x][j]==1)
                        {
                            System.out.println("auch");
                            tempGo=false;
                        }
                    }

                    go=tempGo;
                    if(tempGo)
                    {
                        for (int j = 0; j < s.size(); j++) 
                        {
                            preBoard[x][y+j]=1;   
                            //showPreBoard();

                        }
                        System.out.println("her kommer preboard");
                    
                        showPreBoard();
                   
                    }
                    
                }
               
                else
                {
                    int x = rnd.nextInt(sizeX-(s.size()-1));
                    int y = rnd.nextInt(sizeY);
                    pos = new Position( x, y );
                    boolean tempGo = true;
                    System.out.println( "vi har retning true = vertical: " + vertical+"her kommer pos " + x + y );
                    
                    
                    for (int j = x; j < x + s.size(); j++) 
                    {
                        System.out.println( "for loops pos: "+j );
                        if( preBoard[j][y] == 1 )
                        {
                            System.out.println("auch2");
                            tempGo=false;
                        }
                    
                    }

                    go=tempGo;
                    if(tempGo) 
                    {
                        for (int j = 0; j < s.size(); j++) 
                        {
                            preBoard[x+j][y]=1;  
                            //showPreBoard();
                        }
                        System.out.println("her kommer preboard");
                    
                        showPreBoard();
                    }
                    

                }
            }
            System.out.println(s.size());
            board.placeShip(pos, s, vertical);
        }
    }

    /**
     * Called every time the enemy has fired a shot.
     * 
     * The purpose of this method is to allow the AI to react to the 
     * enemy's incoming fire and place his/her ships differently next round.
     * 
     * @param pos Position of the enemy's shot 
     */
    @Override
    public void incoming(Position pos)
    {
        //Do nothing
    }
    
    public void showPreBoard()
    {
        for (int i = 9; i > -1; i--) 
        {
            for (int j = 0; j <10; j++) 
            {
                System.out.print(preBoard[j][i]);
            }
            System.out.println("");
        }
    }
    
        public void showPriorityBoard()
    {
        System.out.println("----");
        for (int i = 9; i > -1; i--) // y
        {
            for (int j = 0; j < 10; j++) // x
            {
                
                if( priorityBoard[j][i] >= 10)
                    System.out.print(priorityBoard[j][i]);
                
                if( priorityBoard[j][i] < 10)
                    System.out.print(" " + priorityBoard[j][i]);
                
              System.out.print(", ");
            }
            System.out.println("");
        }
    }
   
    /**
     * Called by the Game application to get the Position of your shot.
     *  hitFeedBack(...) is called right after this method.
     * 
     * @param enemyShips Fleet the enemy's ships. Compare this to the Fleet 
     * supplied in the hitFeedBack(...) method to see if you have sunk any ships.
     * 
     * @return Position of you next shot.
     */
    @Override
    public Position getFireCoordinates(Fleet enemyShips)
    {
        

        
        // Let's try shooting at the four squares around our hitmark
        if( huntMode )
        {
            System.out.println("Hunting");
            if( tempX + 1 < 10 && fireCoord[tempX+1][tempY] == 0 )
            {
                
                fireCoord[tempX+1][tempY] = 1;
                fireX = tempX + 1;
                fireY = tempY;
                hunting = 1;

            }
            else if( tempX - 1 >= 0 && fireCoord[tempX-1][tempY] == 0 )
            {
                
                fireCoord[tempX-1][tempY] = 1;
                fireX = tempX - 1;
                fireY = tempY;
                hunting = 2;

            }
            else if( tempY + 1 < 10 && fireCoord[tempX][tempY+1] == 0 )
            {
                
                fireCoord[tempX][tempY+1] = 1;
                fireX = tempX;
                fireY = tempY + 1;
                hunting = 3;

            }
            else if( tempY - 1 > 0 && fireCoord[tempX][tempY-1] == 0 )
            {
                
                fireCoord[tempX][tempY-1] = 1;
                fireX = tempX;
                fireY = tempY - 1; 
                hunting = 4;

            }
            else 
                huntMode = false;
           
        }    
        
        if( destroyMode )
        {
            System.out.println("Destroy");
            System.out.println(enemyOnX);
            System.out.println(enemyOnY);
            System.out.println("path: " + path);
            
           if( enemyOnX && fireCoord[fireX + path][fireY] != 1 && fireX + path < 10 && fireX + path >= 0 )
           {
              System.out.println("Destroy x : " + path );
              fireX += path; 
              fireCoord[fireX][fireY] = 1;
           }
           else if( enemyOnX && fireCoord[fireX + path][fireY] == 1 )
           {
                fireX = tempX;
                if ( fireCoord[fireX - path][fireY] != 1 )
                {
                  fireX -= path;
                  System.out.println("Destroy x2 : " + path );
                  fireCoord[fireX][fireY] = 1;
                }
                else
                {
                    continueKillPath = false;
                    destroyMode = false;
                    enemyOnY = false;
                    enemyOnX = false;
                }
           }
 
           else if( enemyOnY && fireCoord[fireX][fireY + path] != 1 && fireY + path < 10 && fireY + path >= 0 )
           {
               System.out.println("Destroy y : " + path );
              fireY += path;
              fireCoord[fireX][fireY] = 1;
           }
           else if( enemyOnY && fireCoord[fireX][fireY + path] == 1 )
           {
                fireY = tempY;
                if ( fireCoord[fireX][fireY - path] != 1 )
                {  
                   fireY -= path;
                   System.out.println("Destroy y2: " + path );
                   fireCoord[fireX][fireY] = 1;
                }
                else
                {
                    continueKillPath = false;
                    destroyMode = false;
                    enemyOnY = false;
                    enemyOnX = false;
                }
           }
  
           
           
        }
        
        if( !huntMode && !destroyMode )
        { 
           System.out.println("Search");
            setPriority();
                 showPriorityBoard();

                    setFireCoordinates();
                    fireCoord[fireX][fireY] = 1;    

            clearPriority();
        }
            
         
         
        
         System.out.println( "Shooting at coords:" + " x " + fireX  +  " y " + fireY );
        return new Position(fireX,fireY);
    
    }
   

    public void setFireCoordinates()
    {
    
        int i = 0;
        int j = 0;
        int max = 0;

        
        // Her finder vi hvilket koordinat der har den største værdi.
        for ( i = 9; i > -1; i--) 
        { 
 
            for ( j = 0; j < 10; j++ )
            { 
               
                if ( priorityBoard[j][i] > max )
                { 
                    max = priorityBoard[j][i];
                    fireX = j;
                    fireY = i;
                }
                 
            }
           

         }

        System.out.println( "max value: "+ max + " At coords:" + " x " + fireX  +  " y " + fireY );
        
    }
    public void clearPriority()
    {
                
        for( int i = 9; i > -1; i-- )
        {

           for( int j = 0; j < 10; j++ )
           {
               priorityBoard[j][i] = 0;
           }

        }
                
    }
  
    public void setPriority()
    {
        
    boolean setPriority = false;
    
    int v = 0;
    int s = 0;

        for( int i = 9; i >= 0 ; i--) // y
        {

            for (int j = 0; j < 10; j++) // x
            {

            for( s = 2; s < 6; s++) // <- need something better than this. Gotta know which ships there are.
            {
                if( (j + s) <= 10 ) // Keeps us in bounds on array
                {
                
                    for( int k = j; k < (j + s); k++ )
                    {

                        if( fireCoord[k][i] != 1 )
                        {
                           setPriority = true;
                        }
                        else
                        {
                           priorityBoard[k][i] = 0;
                           setPriority = false;
                        }

                    }


                    if( setPriority )
                    {
                        for (int k = j; k < (j + s); k++) 
                        {

                           priorityBoard[k][i]++;
                         
                        }
                    }
                }
                
                if( (i + s) <= 10 ) // Keeps us in bounds on array
                {
                    for( int k = i; k < (i + s); k++ )
                    {

                        if( fireCoord[j][k] != 1 )
                        {
                           setPriority = true;
                        }
                        else
                        {
                           priorityBoard[j][k] = 0;
                           setPriority = false;
                        }

                    }


                    if( setPriority  )
                    {
                        for (int k = i; k < (i + s); k++) 
                        {

                           priorityBoard[j][k]++;
                         
                        }
                    }
                    
                }
                
               }

            }

        }
    
    }
    /**
     * Called right after getFireCoordinates(...) to let your AI know if you hit
     * something or not. 
     * 
     * Compare the number of ships in the enemyShips with that given in 
     * getFireCoordinates in order to see if you sunk a ship.
     * 
     * @param hit boolean is true if your last shot hit a ship. False otherwise.
     * @param enemyShips Fleet the enemy's ships.
     */
    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips)
    { 
      
        if( hit && !huntMode && !destroyMode)
        {     
            bulletHit = true;
            huntMode = true;
            
            tempX = fireX;
            tempY = fireY;
            
            System.out.println("Ship hit: " + hit );
            System.out.println("Enemy ships: " + enemyShips.getNumberOfShips() );    
        }
        else if( hit && huntMode && !destroyMode)
        {
            
            if( hunting == 1 )
            {
                enemyOnX = true;
                path = 1;
            }
                else if ( hunting == 2 )
                {
                    enemyOnX = true;
                    path = -1;   
                }
                    else if ( hunting == 3 )
                    {
                        enemyOnY = true;
                        path = 1;
                    }
                        else if ( hunting == 4 )
                        {
                            enemyOnY = true;
                            path = -1;   
                        }
            
            bulletHit = true;
            System.out.println("Ship hit: " + hit );
            System.out.println("Enemy ships: " + enemyShips.getNumberOfShips() );  

   
            huntMode = false;
            destroyMode = true; // Now we know which axis the enemy ship is on. So now we destroy it.
            continueKillPath = true;
        }
        
        else if( !hit && huntMode && !destroyMode )
        {
            bulletHit = false;
            System.out.println("Missed shot");
        }
        
        if( hit && destroyMode )
        {
            continueKillPath = true;
        }
        else if( !hit && destroyMode )
        {
            continueKillPath = false;
            path *= -1;
            fireX = tempX;
            fireY = tempY;
        }
        else if( !hit && destroyMode && continueKillPath )
        {
            destroyMode = false;
            path = 0;
        }
        else 
        {
            bulletHit = false;
            System.out.println("Missed shot");
        }
            
      
        //Do nothing 
    }    

    
    /**
     * Called in the beginning of each match to inform about the number of 
     * rounds being played.
     * @param rounds int the number of rounds i a match
     */
    @Override
    public void startMatch(int rounds)
    {
        //Do nothing
    }
    
    
    /**
     * Called at the beginning of each round.
     * @param round int the current round number.
     */
    @Override
    public void startRound(int round)
    {
        //Do nothing
    }

    
    /**
     * Called at the end of each round to let you know if you won or lost.
     * Compare your points with the enemy's to see who won.
     * 
     * @param round int current round number.
     * @param points your points this round: 100 - number of shot used to sink 
     * all of the enemy's ships. 
     *
     * @param enemyPoints int enemy's points this round. 
     */
    @Override
    public void endRound(int round, int points, int enemyPoints)
    {
        //Do nothing
    }
    
    
    /**
     * Called at the end of a match (that usually last 1000 rounds) to let you 
     * know how many losses, victories and draws you scored.
     * 
     * @param won int the number of victories in this match.
     * @param lost int the number of losses in this match.
     * @param draw int the number of draws in this match.
     */
    @Override
    public void endMatch(int won, int lost, int draw)
    {
        //Do nothing
    }
}
