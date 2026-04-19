package hammurabi;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hammurabi {

    Random rand = new Random();  
    Scanner scanner = new Scanner(System.in);
    String playerName;
        int population;
        int grain;
        int land;
        int landValue;
        int year;
        int grainFed;
        int plant;
        int pDeath;
        int starved;
        int newImmigrant;
        int harvested;
        int eaten;
        int yieldPerAcre;
        double totalStarvDeaths;

    public static void main(String[] args) {
        new Hammurabi().playGame();
    }
    
    public void playGame(){
        playerName = getName();

        printWelcome();

        do {
            resetGame();
            printWelcome();

            while(year<=10){
                printSummary();
                int buy = askHowManyAcresToBuy(landValue, grain);
                if(buy > 0){
                    grain -= (buy * landValue);
                    land += buy;
                } else {
                    int sell = askHowManyAcresToSell(land);
                    land -= sell;
                    grain += (sell * landValue);
                }
                grainFed = askHowMuchGrainToFeedPeople(grain);
                grain -= grainFed;

                plant = askHowManyAcresToPlant(land, population, grain);
                grain -= (plant * 2);

                starved = starvationDeaths(population, grainFed);
                totalStarvDeaths += starved;
                population -= starved;

                pDeath = plagueDeaths(population);
                population -= pDeath;

                if(uprising(population, starved)) {
                    printBadResult();
                    break;
                }

                newImmigrant = immigrants(population, land, grain, starved);
                population += newImmigrant;

                yieldPerAcre = harvest(land);
                harvested = yieldPerAcre * plant;
                grain += harvested;

                int eatenChance = grainEatenByRats(grain);
                eaten = grain * eatenChance / 100;
                grain -= eaten;

                landValue = newCostOfLand();

                year++;
            }
            if (population == 0) {
            printBadResult();
            } else {
            finalSummary();
            }

        } while(playAgain());
    }

    public void printSummary() {

            System.out.println("\nO great " + playerName + "!");
            System.out.println("You are in year " + year + " of your ten year rule.");
            System.out.println("In the previous year " + starved + " people starved to death.");
            System.out.println("In the previous year " + newImmigrant + " people entered the kingdom.");
            System.out.println("The population is now " + population);
            System.out.println("We harvested " + harvested + " at " + yieldPerAcre + " bushels per acre.");
            System.out.println("Rats destroyed " + eaten + " bushels, leaving " + grain + " bushels in storage.");
            System.out.println("The city owns " + land + " acres of land.");
            System.out.println("Land is currently worth " + landValue+ " bushels per acre.\n");

    }

    public void printBadResult() {
        System.out.println(starved + " PEOPLE DIED FROM STARVATION\n" +
                                    "IT WAS A DISASTAAAH.\n" +
                                    "YOUR RULE HAS BEEN SO DISASTROUS THAT THE PEOPLE HAVE\n" +
                                    "DECIDED THAT EVEN A PIG WITH A CROWN WOULD DO A BETTER JOB.\n" +
                                    "YOU HAVE BEEN REPLACED. BY A PIG!\n\n" +
                                    "HISTORY WILL NOT REMEMBER YOU KINDLY.\n");
    }

    public void finalSummary() {
        int yearsPlayed = year - 1;
        double percentDied = (totalStarvDeaths / (yearsPlayed * 100)) * 100;
        System.out.println("IN YOUR 10-YEAR TERM OF OFFICE, " + percentDied + " PERCENT OF THE\n" +
			               "POPULATION STARVED PER YEAR ON AVERAGE, I.E., A TOTAL OF\n" + 
                            totalStarvDeaths + " PEOPLE DIED!!\n" +
			               "YOU STARTED WITH 10 ACRES PER PERSON AND ENDED WITH\n" +
			                (land / population) + " ACRES PER PERSON\n\n");
		if (percentDied > 33 || land / population < 7) {
			    printBadResult();
            }   else if (percentDied > 10 || land / population < 9) {
			        System.out.println("YOUR HEAVY-HANDED PERFORMANCE SMACKS OF NERO AND IVAN IV.\n" +
					                   "THE PEOPLE (REMAINING) FIND YOU AN UNPLEASANT RULER, AND,\n" +
					                   "FRANKLY, HATE YOUR GUTS!");
                } else if (percentDied > 3 || land / population < 10) {
			        System.out.println("YOUR PERFORMANCE COULD HAVE BEEN SOMEWHAT BETTER, BUT\n" +
					                    "REALLY WASN'T TOO BAD AT ALL.\n" +
					                    Math.random() * population * .8 + " PEOPLE WOULD" +
					                    "DEARLY LIKE TO SEE YOU ASSASSINATED BUT WE ALL HAVE OUR" +
					                    "TRIVIAL PROBLEMS");
                } else {
			        System.out.println("A FANTASTIC PERFORMANCE!!!  CHARLEMANGE, DISRAELI, AND\n" +
					                   "JEFFERSON COMBINED COULD NOT HAVE DONE BETTER!");
		            System.out.println("\n\n\n\n\n\n\n\n\n\nSo long for now.");
                }
    }

    public void printWelcome() {
        System.out.println("\n\nWELCOME, O GREAT " + playerName + "!\n" +
                            "YOU HAVE BEEN HANDED COMPLETE CONTROL OF A KINGDOM FOR THE NEXT 10 YEARS.\n" +
                            "THIS WAS PROBABLY A MISTAKE.\n" +
                            "PLEASE TRY NOT TO KILL EVERYONE!\n\n" +
                            "Remember!\n" +
                            "Each person needs at least 20 bushels of grain per year to survive\n" +
                            "Each person can farm at most 10 acres of land\n" +
                            "It takes 2 bushels of grain to farm an acre of land\n" +
                            "The market price for land fluctuates yearly\n" +
                            "Beware plague and disaster!\n\n");
    }

    public int askHowManyAcresToBuy(int landValue, int grain) {
        int acres;
        int totalCost;
        while(true){
            acres = getNumber("O great " + playerName + "! How many acres of land do you want to buy?\n");
            if(acres < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            totalCost  = landValue * acres;

            if(totalCost > grain) {
                System.out.println("O great " + playerName + "! Surely You jest! We only have " + grain + " bushels of grain left!");
                continue;
            }
            return acres;
        }
    }

    public int askHowManyAcresToSell(int land) {
        int acres;
        while(true){
            acres = getNumber("O great " + playerName + "! How many acres of land do you want to sell?\n");
            if(acres < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            if(acres > land) {
                System.out.println("O great " + playerName + "! Surely You jest! We only have " + land + " acres of land left!");
                continue;
            }
            return acres;
        }
    }


    public int askHowMuchGrainToFeedPeople(int grain) {
        int bushels;
        while(true){
            bushels = getNumber("O great " + playerName + "! How much grain do you want to spend to feed people?\n"
            );
            if(bushels < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            if(bushels > grain) {
                System.out.println("O great " + playerName + "! Surely You jest! We only have " + grain + " bushels of grain left!");
                continue;
            }
            return bushels;
        }
    }

    public int askHowManyAcresToPlant(int land, int population, int grain) {
        int plant;
        while(true){
            plant = getNumber("O great " + playerName + "! How many acres of land do you want to plant with grain?\n");
            if(plant < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            if(plant > land) {
                System.out.println("O great " + playerName + "! Surely You jest! We only have " + land + " acres of land left!");
                continue;
            }

            if(plant > grain/2) {
                System.out.println("O great " + playerName + "! Surely You jest! We can only plant up to " + (grain/2) + " acres with your grain");
                continue;
            }

            if(plant > population * 10) {
                System.out.println("O great " + playerName + "! Surely You jest! Our people can only tend up to " + (population*10) + " acres.");
                continue;
            }
            return plant;
        }
    }

    int plagueDeaths(int population) {
        int rInt = rand.nextInt(100) + 1;
        if (rInt <= 15) {
            return population / 2;
        }
        return 0;
    }

    public int starvationDeaths(int population, int grainFed) {
        int populationFed = grainFed / 20;
        if(populationFed >= population){
            return 0;
        } else  {
            return  population - populationFed;
        }
    }

    public boolean uprising(int population, int starved) {
        if(starved * 100 > population * 45) {
            return true;
        }
        return false;
    }

    public int immigrants(int population, int land, int grain, int starved) {
        if(population == 0){
            return 0;
        } else if(starved > 0){
            return 0;
        } else {
        return (20 * land + grain) / (100 * population) + 1;
        }
    }

    public int harvest(int land) {
        return rand.nextInt(6) + 1;
    }

    public int grainEatenByRats(int grain) {
        int prob = rand.nextInt(10) + 1;
        if(prob <= 4) {
            return (rand.nextInt(30 - 10 + 1) + 10);
        } 
        return 0;
    }

    public int newCostOfLand() {
        return rand.nextInt(23 -17 + 1) + 17;
    }

    public int getNumber(String message) {
        while (true) {
            System.out.print(message);
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("\"" + scanner.next() + "\" isn't a number!");
            }
        }
    } 

    public String getName(){
        while(true) {
            System.out.println("O GREAT (OR POTENTIALLY TERRIBLE) RULER.\n" +
                              "WHAT SHALL WE CALL YOU BEFORE HISTORY JUDGES YOU HARSHLY?");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
            System.out.println("A RULER WITHOUT A NAME? THAT'S SUSPICIOUS. TRY AGAIN.\n");
            continue;
        }
        return name;
        }
    }
    
    public boolean playAgain() {
        while (true) {
            System.out.print("DO YOU WISH TO RULE AGAIN? (Y/N): ");

            String input = scanner.next().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("PLEASE ANSWER WITH YES OR NO.\n");
            }
        }
    }

    public void resetGame() {
        population = 100;
        grain = 2800;
        land = 1000;
        landValue = 19;
        year = 1;

        starved = 0;
        newImmigrant = 5;
        harvested = 0;
        eaten = 0;
        yieldPerAcre = 0;
        totalStarvDeaths = 0;
    }

}
