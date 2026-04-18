package hammurabi;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hammurabi {

    Random rand = new Random();  
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Hammurabi().playGame();
    }
    
    public void playGame(){
        int population = 100;
        int grain = 2800;
        int land = 1000;
        int landValue = 19;
        int year = 1;
        int grainFed;
        int plant;
        int starved = 0;
        int immigrant = 5;

        while(year<=10){
// O great Hammurabi!
// You are in year 1 of your ten year rule.
// In the previous year 0 people starved to death.
// In the previous year 5 people entered the kingdom.
// The population is now 100.
// We harvested 3000 bushels at 3 bushels per acre.
// Rats destroyed 200 bushels, leaving 2800 bushels in storage.
// The city owns 1000 acres of land.
// Land is currently worth 19 bushels per acre.

            System.out.println("O great Michael!");
            System.out.println("You are in year " + year + " of your ten year rule.");
            System.out.println("In the previous year " + starved + " people starved to death.");
            System.out.println("In the previous year " + immigrant + " people entered the kingdom.");
            System.out.println("The population is now " + population);
            // System.out.println("We harvested 3000 bushels at 3 bushels per acre.");
            System.out.println("Rats destroyed 200 bushels, leaving " + grain + " bushels in storage.");
            System.out.println("The city owns " + land + " acres of land.");
            System.out.println("Land is currently worth " + landValue+ " bushels per acre.");

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
            population -= starved;

            if(uprising(population, starved)) {
                break;
            }

            year++;
        }

    }

    public int askHowManyAcresToBuy(int landValue, int grain) {
        int acres;
        int totalCost;
        while(true){
            acres = getNumber("How many acres of land do you want to buy?\n");
            if(acres < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            totalCost  = landValue * acres;

            if(totalCost > grain) {
                System.out.println("Insufficient grain!");
                continue;
            }
            return acres;
        }
    }

    public int askHowManyAcresToSell(int land) {
        int acres;
        while(true){
            acres = getNumber("How many acres of land do you want to sell?\n");
            if(acres < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            if(acres > land) {
                System.out.println("Insufficient land!");
                continue;
            }
            return acres;
        }
    }


    public int askHowMuchGrainToFeedPeople(int grain) {
        int bushels;
        while(true){
            bushels = getNumber("How much grain do you want to spend to feed people?\n");
            if(bushels < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            if(bushels > grain) {
                System.out.println("Insufficient grain!");
                continue;
            }
            return bushels;
        }
    }

    public int askHowManyAcresToPlant(int land, int population, int grain) {
        int plant;
        while(true){
            plant = getNumber("How many acres of land do you want to plant with grain?\n");
            if(plant < 0) {
                System.out.println("Please enter positive number");
                continue;
            } 

            if(plant > land) {
                System.out.println("Insufficient land!");
                continue;
            }

            if(plant > grain/2) {
                System.out.println("Insufficient grain!");
                continue;
            }

            if(plant > population * 10) {
                System.out.println("Insufficient population!");
                continue;
            }
            return plant;
        }
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

    

}
