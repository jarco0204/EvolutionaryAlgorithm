import java.util.*; // Includes Random
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* YOU NEED TO ADD YOUR CODE TO THIS CLASS, REMOVING ALL DUMMY CODE
 *
 * DO NOT CHANGE THE NAME OR SIGNATURE OF ANY OF THE EXISTING METHODS
 * (Signature includes parameter types, return types and being static)
 *
 * You can add private methods to this class if it makes your code cleaner,
 * but this class MUST work with the UNMODIFIED Tester.java class.
 *
 * This is the ONLY class that you can submit for your assignment.
 *
 * MH 2020
 */
public class Queens
{
    private static int boardSize = 10;
    private static Random rNumber;
    private static ArrayList<String> checks; // This array will contain all the coordinate of queens


    //Inefficient solution
    //private static HashMap<Integer, Integer> checks;


    /**
     * Nested class to aid in the behaviour of the mapping from queen a to queen b, and not queen a and queen b
     */
    private static class Arc
    {
        private static int queenA;
        private static int queenB;
        private static String name;

        public static int getQueenA() {
            return queenA;
        }

        public static int getQueenB() {
            return queenB;
        }

        public static String getName() {
            return name;
        }


        //Constructor
        public Arc(int queen1, int queen2)
        {
            this.queenA= queen1;
            this.queenB= queen2;
            name= Integer.toString(queen1) + ""+ Integer.toString(queen2);

        }


    }
    /**
     * Mian Method to test my code
     * @return void
     */
    public static void main(String[] argvs)
    {

        //crossover(new Integer[]{5,4,2,6,8,9,3,1,7,10}, new Integer[]{6,2,5,7,3,1,4,10,8,9});
        measureFitness(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    }
    
    // creates a valid genotype with random values
    public static Integer [] randomGenotype()
    {
        Integer [] genotype = new Integer [boardSize];
        rNumber= new Random();
        Integer valueUse= 1 + rNumber.nextInt(10); // This would give me values from 1-10
        ArrayList<Integer> numbersPulled = new ArrayList<Integer>(10);
        numbersPulled.add(valueUse); // First value added.
        while(numbersPulled.size() != 10)
        {
            valueUse = 1 + rNumber.nextInt(10); // This would give me values from 1-10
            if(numbersPulled.contains(valueUse))
            {
                ; // pass don't add to the ArrayList of the values
            }
            else
            {
                numbersPulled.add(valueUse);
            }
        }

        for(int i=0; i< boardSize; i++)
        {
            genotype[i]= numbersPulled.get(i);

        }


        
        return genotype;
    }
    
    // swaps 2 genes in the genotype
    // the swap happens with probability p, so if p = 0.8
    // then 8 out of 10 times this method is called, a swap happens
    public static Integer[] mutate(Integer[] genotype, double p)
    {
        //In order to mimic the probability of 0.8, I am going to pull-out a random number from (0,9), if the number is between
        // 0-to 7, then it will lead to a mutation, else it will return the original genotype
        rNumber= new Random();
        int magicNumber = rNumber.nextInt(10); //0-9

        if(magicNumber==8 || magicNumber==9)
        {
            return genotype; // No mutation
        }
        else // Probability favored a mutation
        {
            int firstGene = rNumber.nextInt(10); // 0-9 since we are talking about indexes
            int secondGene = rNumber.nextInt(10);
            while(firstGene == secondGene)
            {
                secondGene= rNumber.nextInt(10); // To guarantee we always get two distinct numbers
            }
            Integer tempV = 0;
            tempV= genotype[firstGene];
            genotype[firstGene]= genotype[secondGene];
            genotype[secondGene]= tempV;
            return genotype;
        }

    }
    
    // creates 2 child genotypes using the 'cut-and-crossfill' method
    public static Integer[][] crossover(Integer[] parent0, Integer[] parent1)
    {
        Integer [][] children = new Integer [2][boardSize]; // Notice how given the two, you only use 0 and 1
        //rNumber= new Random();
        int crossoverPoint = 5; // Crossover-point is supposed to be chosen in the middle

        // Defining the arrays of children that I will be using
        children[0]= new Integer[]{0,0,0,0,0,0,0,0,0,0};
        children[1]= new Integer[]{0,0,0,0,0,0,0,0,0,0};

        // Second part of algorithm: the children have the data to the left of the crossover of the parent.
        System.arraycopy(parent0,0,children[0],0,crossoverPoint);
        System.arraycopy(parent1,0,children[1],0,crossoverPoint);

        //Third Part: Populate the child array with the values to the right of the crossover of the parent.
        // If values already there, then use values from the left part of the opposite parent
       // System.out.println(Arrays.toString(parent0));
        //System.out.println(Arrays.toString(parent1));
        //System.out.println(Arrays.toString(children[0]));
        //System.out.println(Arrays.toString(children[1]));

        //Two Children
        for(int i=0;i<2;i++)
        {
            int numberCorrectInserts= 0; // This value will be as a condition for my while-loop
            int indexToStartOP= 5;
            int indexToInsert= 5;
            while(numberCorrectInserts != 5)
            {
                switch (i){

                    //Manipulating firs
                    case 0:
                    {

                        // To prevent error of outside range
                        if (indexToStartOP==10)
                        {

                            indexToStartOP=0; //Reset to 0, and start trying with the left-side of opposite parent
                        }

                        int valueCompare=  parent1[indexToStartOP];
                        ArrayList<Integer> tempAr= Stream.of(children[i]).collect(Collectors.toCollection(ArrayList::new));
                        //System.out.println("Checking " + valueCompare + " Answer is : "+ tempAr.contains(valueCompare) );
                        if(!tempAr.contains(valueCompare))//Insert element
                        {
                            //System.out.println(numberCorrectInserts);
                            children[i][indexToInsert]= parent1[indexToStartOP];//Transfer element
                            //System.out.println(Arrays.toString(children[i]));
                            indexToInsert++;
                            numberCorrectInserts++;

                        }
                        indexToStartOP++; // Common for both cases
                        break;

                    }
                    //Manipulating second son

                    case 1:
                    {
                        // To prevent error of outside range
                        if (indexToStartOP==10)
                        {

                            indexToStartOP=0; //Reset to 0, and start trying with the left-side of opposite parent
                        }

                        int valueCompare=  parent0[indexToStartOP];
                        ArrayList<Integer> tempAr= Stream.of(children[i]).collect(Collectors.toCollection(ArrayList::new));
                        //System.out.println("Checking " + valueCompare + " Answer is : "+ tempAr.contains(valueCompare) );
                        if(!tempAr.contains(valueCompare))//Insert element
                        {
                            //System.out.println(numberCorrectInserts);
                            children[i][indexToInsert]= parent0[indexToStartOP];//Transfer element
                            //System.out.println(Arrays.toString(children[i]));
                            indexToInsert++;
                            numberCorrectInserts++;

                        }
                        indexToStartOP++; // Common for both cases
                        break;
                    }

                }


            }

        }


        
        return children;
    }
    
    // calculates the fitness of an individual
    public static int measureFitness(Integer [] genotype)
    {
        /* The initial fitness is the maximum pairs of queens
         * that can be in check (all possible pairs in check).
         * So we are using it as the maximum fitness value.
         * We deduct 1 from this value for every pair of queens
         * found to be in check.
         * So, the lower the score, the lower the fitness.
         * For a 10x10 board the maximum fitness is 45 (no checks),
         * and the minimum fitness is 0 (all queens in a line).
         */

        // More efficient solution; Credits to Roham and StackOverFlow
        int fitness = (int) (0.5 * boardSize * (boardSize - 1));
        int checks =0;
        for(int i=0;i<9;i++)
        {
            for(int j= i+1; j<10;j++)
            {
                if(genotype[i].equals(genotype[j]) || j-i==Math.abs(genotype[i]-genotype[j]))
                {
                    checks++;
                }

            }
        }

        /*checks= new ArrayList<String>();
        Integer[][] board = new Integer[10][10]; // Array to simulate a 10X10 board. Full of nulls

        //Place the queen in the correct board position

        for(int i=0; i<10;i++) //Learned: for-each gets the value in random order
        {
            board[genotype[i]-1][i]= genotype[i]; // Value to represent a queen

        }

        //Printing current board
        for(int i=0;i<10;i++)
        {
            System.out.println(Arrays.toString(board[i]));
        }

        //Up to this point the queens have been correctly placed on the board
        //Now check to see how many queens threaten each other
        for(int i=0; i<10;i++)
        {
            Coordinate point = new Coordinate(genotype[i]-1,i); //Using indeces
            checkAdjacentCells(board,point);
            //checkVerticalCells(board,point);
            //System.out.println(i);
            checkDiagonalCells(board,point);

        }


        for(int j=0; j<checks.size();j++)
        {
            for(int i=0;i<checks.size();i++)
            {
                char[] stringCompTo= checks.get(j).toCharArray(); //format "ab"
                char[] stringCompWith= checks.get(i).toCharArray(); //ba"
                if(stringCompTo[0]==stringCompWith[1] && stringCompTo[1]==stringCompWith[0])
                {

                    checks.remove(i); //
                }
            }



        }




        checks.forEach((key,value)-> {
            System.out.println(key + " " + value);
            if (key != value)
                ref.numChecks++;
                 //System.out.println(ref.numChecks);
        });

            var ref = new Object() {
              return ref.numChecks;
        };

        */
        //System.out.println(checks.size());
        return 45-checks;
    }


    //NOTE, I tried to manually code the getFirness level by comparing a cell to every other cell in the ways a queen moves, However, I had a terrible bug somewhere that was not giving me the correct output
    /**
     * Check to see diagonals in 4 directions
     * @param curBoard curState
     * @param point yat
     */
    /**
    private static void checkDiagonalCells(Integer[][] curBoard, Coordinate point) {
        for(int i=0; i<4;i++)
        {
            switch (i)
            {
                case 0: //NE
                {
                    int indeX= point.getX()-1; //Check the next element
                    int indeY= point.getY()+1;

                    while(indeX != -1 && indeY!=10 )
                    {
                        if (curBoard[indeX][indeY] != null) {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[indeX][indeY]);
                            checks.add(nameArc);
                            System.out.println(nameArc);

                        }
                        indeX--;
                        indeY++;
                    }
                    break;
                }
                case 1: //SE
                {
                    int indeX= point.getX()+1; //Check the next element
                    int indeY= point.getY()+1;
                    while(indeX != 10 && indeY!=10 )
                    {
                        if (curBoard[indeX][indeY] != null) {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[indeX][indeY]);
                            checks.add(nameArc);
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[indeX][indeY]);

                        }
                        indeX++;
                        indeY++;
                    }
                    break;
                }
                case 2: //SW
                {
                    int indeX= point.getX()+1; //Check the next element
                    int indeY= point.getY()-1;
                    while(indeX != 10 && indeY !=-1 )
                    {
                        if (curBoard[indeX][indeY] != null) {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[indeX][indeY]);
                            checks.add(nameArc);
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[indeX][indeY]);

                        }
                        indeX++;
                        indeY--;
                    }
                    break;
                }
                case 3: //NW
                {
                    int indeX= point.getX()-1; //Check the next element
                    int indeY= point.getY()-1;
                    while(indeX != -1 && indeY!=-1)
                    {
                        if (curBoard[indeX][indeY] != null) {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[indeX][indeY]);
                            checks.add(nameArc);
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[indeX][indeY]);

                        }
                        indeX--;
                        indeY--;
                    }
                    break;
                }


            }

        }
    }


    /**
     * Function to check the all the vertical cells in both directions (Up and Down)
     * @param curBoard Integer[][]
     * @param point Coordinate
     */
    /**
    private static void checkVerticalCells(Integer[][] curBoard, Coordinate point) {

        for(int i =0; i<2; i++) //UP and DOWN
        {
            try {
                switch (i) {
                    case 0: //UP
                    {
                        int index = point.getX() - 1; //Check element up
                        int yCor = point.getY();
                        while (index != 0) {
                            if (curBoard[index][yCor] != null) {
                                //checks.put(curBoard[index][ycor], curBoard[point.getX()][point.getY()]);
                                String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[index][yCor]);

                            }
                            index--;
                        }
                    }
                    case 1: {
                        int index = point.getX() + 1; //Check element down
                        int ycor = point.getY();
                        while (index != 9) {
                            if (curBoard[index][ycor] != null) {
                                //checks.put(curBoard[index][ycor], curBoard[point.getX()][point.getY()]);
                                String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[index][ycor]);

                            }
                            index++;
                        }
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                continue;
            }
        }
    }

    /**
     * Function to check adjacent cells
     * Given a cell[i][j] check
     * Remember Coordinate is using indexes
     */
    /**
    private static void checkAdjacentCells(Integer[][] curBoard, Coordinate point)
    {
        int numOfCheck=0;
        for(int i=0; i<8;i++) {

            try {
                switch(i)
                {
                    case 0: //North
                    {
                        int xcor= point.getX() -1;
                        int ycor= point.getY();
                        if (curBoard[xcor][ycor] != null)
                        {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);

                        }
                    }
                    case 1: //NE
                    {
                        int xcor= point.getX() -1;
                        int ycor= point.getY()+1;
                        if (curBoard[xcor][ycor] != null)
                        {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                           // checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);
                        }
                    }
                    case 2: //E
                    {
                        int xcor= point.getX() +1;
                        int ycor= point.getY();
                        if (curBoard[xcor][ycor] != null)
                        {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                           // checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);
                        }
                    }
                    case 3: //SE
                    {
                        int xcor= point.getX() +1;
                        int ycor= point.getY() +1;
                        if (curBoard[xcor][ycor] != null)
                        {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);
                        }
                    }
                    case 4: //S
                    {
                        int xcor= point.getX() +1;
                        int ycor= point.getY();
                        if (curBoard[xcor][ycor] != null)
                        {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);
                        }
                    }
                    case 5: //SW
                    {
                        int xcor= point.getX() +1;
                        int ycor= point.getY()-1;
                        if (curBoard[xcor][ycor] != null)
                        {
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);
                        }
                    }
                    case 6: //W
                    {
                        int xcor= point.getX() ;
                        int ycor= point.getY()-1;
                        if (curBoard[xcor][ycor] != null)
                        {
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                        }
                    }
                    case 7: //NW
                    {
                        int xcor= point.getX() -1;
                        int ycor= point.getY()-1 ;
                        if (curBoard[xcor][ycor] != null)
                        {
                            //checks.put(curBoard[point.getX()][point.getY()],curBoard[xcor][ycor]);
                            String nameArc= Integer.toString(curBoard[point.getX()][point.getY()]) + "" + Integer.toString(curBoard[xcor][ycor]);
                            checks.add(nameArc);
                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
     }
     */


}
