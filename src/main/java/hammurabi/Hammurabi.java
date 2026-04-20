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
        boolean impeached;

    public static void main(String[] args) {
        new Hammurabi().playGame();
    }
    
    public void playGame(){
        playerName = getName().toUpperCase();

        do {
            resetGame();
            printWelcome();

            while(year<=10){
                printSummary();
                int buy = askHowManyAcresToBuy(landValue, grain);
                if(buy > 0){
                    grain -= (buy * landValue);
                    land += buy;
                    buyResult();
                } else {
                int sell = askHowManyAcresToSell(land);

                if (sell > 0) {
                    land -= sell;
                    grain += (sell * landValue);
                    sellResult(landValue, sell);
                    }
                }
                grainFed = askHowMuchGrainToFeedPeople(grain);
                grain -= grainFed;

                plant = askHowManyAcresToPlant(land, population, grain);
                grain -= (plant * 2);

                // if (population > 0 && askForWar()) {
                //     resolveWar();
                //     if (population == 0) {
                //         break;
                //     }
                // }

                starved = starvationDeaths(population, grainFed);
                totalStarvDeaths += starved;

                if(uprising(population, starved)) {
                    printImpeachment();
                    impeached = true;
                    break;
                }

                population -= starved;

                pDeath = plagueDeaths(population);
                if (pDeath > 0) {
                    population -= pDeath;
                    announcePlague();
                }

                if (starved > 0) {
                    newImmigrant = 0;
                    } else {
                        newImmigrant = immigrants(population, land, grain);
                    }
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
            if(impeached == true){

            } else if (population == 0) {
            printExtinction();
            } else {
            finalSummary();
            }

        } while(playAgain());
    }

    public void printSummary() {

            System.out.println("\nWE BEG TO REPORT, O great " + playerName + "!");
            System.out.println("You are in year " + year + " of your ten year rule.");
            System.out.println("In the previous year " + starved + " people starved to death.");
            System.out.println("In the previous year " + newImmigrant + " people entered the kingdom.");
            System.out.println("The population is now " + population);
            System.out.println("We harvested " + harvested + " at " + yieldPerAcre + " bushels per acre.");
            System.out.println("Rats destroyed " + eaten + " bushels, leaving " + grain + " bushels in storage.");
            System.out.println("The city owns " + land + " acres of land.");
            System.out.println("Land is currently worth " + landValue+ " bushels per acre.\n");

    }

    public void printExtinction(){
        System.out.println("THE KINGDOM HAS ACHIEVED PERFECT EFFICIENCY.\n" +
                   "THERE ARE NOW ZERO PEOPLE TO FEED.\n\n" +
                   "UNFORTUNATELY, THERE ARE ALSO ZERO PEOPLE LEFT.\n" +
                   "THERE IS NO ONE LEFT TO JUDGE YOU.\n");
    }

    public void printImpeachment() {
        System.out.println(starved + " PEOPLE DIED FROM STARVATION THIS YEAR\n" +
                                    "IT WAS A DISASTAAAH.\n" +
                                    "YOUR RULE HAS BEEN SO DISASTROUS THAT THE PEOPLE HAVE\n" +
                                    "DECIDED THAT EVEN A PIG WITH A CROWN WOULD DO A BETTER JOB.\n" +
                                    "YOU HAVE BEEN REPLACED. BY A PIG!\n\n" +
                                    "HISTORY WILL NOT REMEMBER YOU KINDLY.\n");
    }

    public void finalSummary() {
        int yearsPlayed = year - 1;
        double acresPerPerson = (population == 0) ? 0 : (double) land / population;
        double percentDied = (totalStarvDeaths / (yearsPlayed * 100)) * 100;
        System.out.println("During your reign, " + String.format("%.2f", percentDied) + "% of your people starved each year on average,\n" +
                   "resulting in " + totalStarvDeaths + " total deaths.\n" +
                   "You started with 10 acres per person and now end with " +
                   String.format("%.2f", acresPerPerson) + " acres per person.\n" +
                   "The kingdom has... changed.\n");
		if (percentDied > 33 || acresPerPerson < 7) {
			    System.out.println("YOUR HEAVY-HANDED PERFORMANCE SMACKS OF NERO AND IVAN IV.\n" +
					                "THE PEOPLE (REMAINING) FIND YOU AN UNPLEASANT RULER, AND,\n" +
					                "FRANKLY, HATE YOUR GUTS!");
            } else if (percentDied > 10 || acresPerPerson < 9) {
			        System.out.println("WISDOM HAS BEEN CHASING YOU,\n" +
                                       "BUT YOU HAVE ALWAYS BEEN FASTER.\n" +
                                       "THE PEOPLE REMAIN, BUT THEIR FAITH IN YOU DOES NOT.\n");
            } else if (percentDied > 3 || acresPerPerson < 10) {
			        System.out.println("YOUR PERFORMANCE COULD HAVE BEEN SOMEWHAT BETTER,\n" +
					                    "BUT REALLY WASN'T TOO BAD AT ALL.\n" +
					                    (int)(Math.random() * population * 0.8) + " PEOPLE WOULD " +
					                    "DEARLY LIKE TO SEE YOU ASSASSINATED BUT WE ALL HAVE OUR " +
					                    "TRIVIAL PROBLEMS");
            } else {
			        System.out.println("A FANTASTIC PERFORMANCE!!!\n" +
                                        "CHARLEMANGE, DISRAELI, AND JEFFERSON COMBINED COULD NOT HAVE DONE BETTER!");
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
                            "Nobody will come to the city if people are starving\n" +
                            "Beware plague and disaster!\n" +
                            "People will revolt if more than 45% of the people starve.\n" +
                            "(This will cause you to be immediately thrown out of office, ending the game.)");
    }

    public int askHowManyAcresToBuy(int landValue, int grain) {
        int acres;
        int totalCost;
        while(true){
            acres = getNumber("How many acres of land do you want to buy?\n");
            if(acres < 0) {
                System.out.println("That is not a positive number. Try again, but this time... positively.");
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

    public void buyResult() {
        System.out.println("Congratulations. You now own " + land + " acres and " + grain + " bushels. Even more dirt!");
    }

    public int askHowManyAcresToSell(int land) {
        int acres;
        while(true){
            acres = getNumber("How many acres of land do you want to sell?\n");
            if(acres < 0) {
                System.out.println("That is not a positive number. Try again, but this time... positively.");
                continue;
            } 

            if(acres > land) {
                System.out.println("O great " + playerName + "! Surely You jest! We only have " + land + " acres of land left!");
                continue;
            }
            return acres;
        }
    }

    public void sellResult(int landValue, int sell) {
        System.out.println("You've sold " + sell + " acres for " + (landValue * sell)+  " bushels of grain.\n" +
                           "The map just got smaller, but at least you can eat");
    }

    public int askHowMuchGrainToFeedPeople(int grain) {
        int bushels;
        int minFedPeople = (int) Math.ceil(population * 0.55);
        int safeGrain = minFedPeople * 20;
        while(true){
            bushels = getNumber("How much grain do you want to spend to feed people?\n" +
                                "To keep everyone alive, we require " + (population * 20) + " bushels.\n" +
                                "To avoid revolt, we require " + safeGrain + " bushels.\n");
            if(bushels < 0) {
                System.out.println("That is not a positive number. Try again, but this time... positively.");
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
        int maxPlant = Math.min(land, Math.min(grain / 2, population * 10));
        while(true){
            plant = getNumber("O great " + playerName + "!\n" + 
                              "Your land, grain, and people place limits on your ambition.\n" +
                              "We may plant up to " + maxPlant + " acres with our resources.\n" +  
                              "How many acres of land do you want to plant with grain?\n");
            if(plant < 0) {
                System.out.println("That is not a positive number. Try again, but this time... positively.");
                continue;
            } 

            if(plant > land) {
                System.out.println("O great " + playerName + "! Surely You jest! We only have " + land + " acres of land left!");
                continue;
            }

            if(plant > grain/2) {
                System.out.println("O great " + playerName + "! Surely You jest! We can only plant up to " + (grain/2) + " acres with our grain");
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

    public void announcePlague() {
         System.out.println("A plague has swept the kingdom.\n" +
                               "Half your people are gone. The remaining half are concerned.");
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

    public int immigrants(int population, int land, int grain) {
        if(population == 0){
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
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }
            catch (InputMismatchException e) {
                System.out.println("That's not a whole number. The scribes refuse to calculate that.");
                scanner.next();
            }
        }
    } 

    public String getName() {
        System.out.println("\n\n\nHAMMURABI: KING OF ANCIENT BABYLONIA.\n\n\n" +
                        "O GREAT (OR POTENTIALLY TERRIBLE) RULER.\n" +
                        "WHAT SHALL WE CALL YOU BEFORE HISTORY JUDGES YOU HARSHLY?");
        while (true) {
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("A RULER WITHOUT A NAME? THAT'S SUSPICIOUS. TRY AGAIN.\n");
                continue;
            }
        return name;
        }
    }
    
    public boolean playAgain() {
        while (true) {
            System.out.print("Do you wish to rule again? (Y/N): ");

            String input = scanner.next().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please answer with yes or no.\n");
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
        impeached = false;
    }

    // public boolean askForWar() {
    //     while (true) {
    //         System.out.println("Shall we march to war this year?\n" +
    //                         "or keep our people alive just a little longer? (Y/N)");

    //         String input = scanner.next().trim().toLowerCase();

    //         if (input.equals("y") || input.equals("yes")) {
    //             System.out.println("An ambitious choice. The generals are pleased. The farmers, less so.");
    //             return true;
    //         }

    //         if (input.equals("n") || input.equals("no")) {
    //             System.out.println("Caution wins the day. No one dies… today.");
    //             return false;
    //         }

    //         System.out.println("That was neither yes nor no. The generals are confused.\n");
    //         }
    // }

    // public void resolveWar() {
    //     int rawStrength = population + (grain / 50);
    //     int strength = Math.min(180, rawStrength);

    //     int roll = rand.nextInt(200);

    //     if (roll < strength * 0.5) {
    //         printWarWin();

    //         int grainGain = Math.max(50, grain * 20 / 100);
    //         int landGain = Math.max(10, land * 15 / 100);

    //         grain += grainGain;
    //         land += landGain;

    //         System.out.println("You gained " + grainGain + " grain and " + landGain + " land.");

    //     } else if (roll < strength) {
    //         printWarDraw();

    //         int popLoss = Math.max(5, population * 10 / 100);
    //         int grainLoss = Math.max(50, grain * 10 / 100);

    //         // 🔒 prevent over-subtraction
    //         popLoss = Math.min(population, popLoss);
    //         grainLoss = Math.min(grain, grainLoss);

    //         population -= popLoss;
    //         grain -= grainLoss;

    //         System.out.println(popLoss + " people were lost and " + grainLoss + " grain was wasted.");

    //     } else {
    //         printWarLose();

    //         int popLoss = Math.max(10, population * 20 / 100);
    //         int grainLoss = Math.max(50, grain * 25 / 100);
    //         int landLoss = Math.max(10, land * 15 / 100);

    //         popLoss = Math.min(population, popLoss);
    //         grainLoss = Math.min(grain, grainLoss);
    //         landLoss = Math.min(land, landLoss);

    //         population -= popLoss;
    //         grain -= grainLoss;
    //         land -= landLoss;

    //         System.out.println("You lost " + popLoss + " people, " + grainLoss + " grain, and " + landLoss + " land.");
    //     }

    //     population = Math.max(0, population);
    //     grain = Math.max(0, grain);
    //     land = Math.max(0, land);
    // }

    // public void printWarWin() {
    //     System.out.println("Victory! Veni, vidi, vici. History agrees. Reality is still processing.\n" +
    //                        "The kingdom expands, and the people celebrate.");
    // }

    // public void printWarDraw() {
    //     System.out.println("The battle was fierce, but neither side prevailed.\n" +
    //                        "The land remains unchanged. Your population does not.");
    // }

    // public void printWarLose() {
    //     System.out.println("Defeat! The army has been crushed, and the kingdom pays the price.\n" +
    //                        "Wisdom has been chasing you, but you have always been faster.");
    // }

}
