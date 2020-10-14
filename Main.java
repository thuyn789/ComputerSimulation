/* Author:  Tin Huynh
 *
 * Purpose:  To simulate how a computer virus spread over the network of 20 computers with the probability of 0.01 using Monte Carlo method
 * Results:  The program will run and calculate the total number of days which is needed to clean the whole network,
 *           as well as the probability that each computer will get infected at least once over the course of the run
 * Expected runtime of 100,000 simulations:  1.5 minutes (run locally) and 5.5 minutes (run online)
 * Online version:  https://repl.it/@thuyn789/ComputerSimulation
 * 
 * Version:  1.0
 */
import java.util.Scanner;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int number_of_simulation = 0;
        double number_of_days = 0.0;
        double total_days = 0.0;
        double infected_once = 0.0;
        double p_infected_once = 0.0;
        double total_infected = 0.0;
        double expected_infected = 0.0;
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Please enter a number of simulations: ");
        number_of_simulation = sc.nextInt();
        sc.close();
        
        System.out.println("");
        System.out.println("Running " + number_of_simulation + " stimulations...");
        System.out.println("");
        
        for (int i = 0; i < number_of_simulation; i++) {
            int number_of_computers = 20; // number of computers in the whole network
            int[] computers = new int[number_of_computers];
            computers[0] = 1; // first computer is always infected by default
            
            int[] infected_list = new int[number_of_computers]; // keep track of how many computer got infected once
            double p = 0.1; // infection rate
            boolean clean = false; 
            
            // loop until the whole network is clean
            while (!clean) {
                
                // the nested "for loop" is from @zipirovich - Thank you
                // check each connection "infected -> noninfected"
                for (int a = 0; a < number_of_computers; a++) {
                    for (int b = 0; b < number_of_computers; b++) {
                        if ((computers[a] == 1) && (computers[b] == 0)) {
                            int x = MyBernoulli(p);
                            if (x == 1) {
                                computers[b] = 2;
                            }
                        }
                    }
                }
                // after infecting all non-infected computers
                // all infected computers are waiting to be clean
                for (int a = 0; a < number_of_computers; a++) {
                    if (computers[a] == 2) {
                        computers[a] = 1;
                    }
                }
                // count the number of computers that get infected once per stimulation 
                for (int a = 0; a < number_of_computers; a++) {
                    if (infected_list[a] == 0 && computers[a] != 0) {
                        infected_list[a] = 1;
                    }
                }
                // keep track of all the numbers of infected computers over the course of days
                int infected = countInfected(computers);
                total_infected += infected;
                
                // if the number of infected computers is less than 5,
                // the technician will clean all 5
                // and the network is clean
                if (infected <= 5) {
                    clean = true;
                }
                
                // if the number of infected computers is more than 5,
                // during the day, the technician will choose 5 random computer to fix
                // whatever leftover is for the next day
                if (infected > 5) {
                    int task = 5;
                    while (task != 0) {
                        // this will generate random location in the array
                        // if the location is 0, meaning that the computer is not infected, then skip to the next random location
                        // the "if statement" is to make sure the computer is in fact infected
                        // this will continue until the technician clean enough 5 random computers
                        int location = random(computers);
                        if (computers[location] != 0) {
                            computers[location] = 0;
                            task--;
                        }
                    }
                }

                // Day end
                // At the end of the day, all newly infected will become infected
                // and will start spreading the next day.
                // total_day will increment by 1 by the end of the day
                // count the total of days running ALL stimulation
                total_days++;
            }
            
            // count the number of stimulations that has all 20 infected computers
            // only count the number of stimulations, and not total days
            if (countInfected(infected_list) == 20) {
                infected_once++;
            }
        }
        
        // Results from all the stimulations
        number_of_days = total_days / number_of_simulation;
        System.out.println("To clean the whole network, it would take " + number_of_days + " days");
        
        p_infected_once = infected_once / number_of_simulation;
        System.out.println("The probability that each computer gets infected at least once: " + p_infected_once);
        
        expected_infected = 20 - (total_infected / total_days);
        System.out.println("The expected number of computers get infected: " + expected_infected);
    }
    
    public static double random () {
        Random u = new Random();
        return u.nextDouble();
    }
    
    public static int random (int[] array) {
        // return random location in the array
        int location = new Random().nextInt(array.length);
        return location;
    }
    // this method is to count the number of infected computers in the given array
    public static int countInfected (int[] numbers) {
        int count = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] != 0) {
                count++;
            }
        }
        return count;
    }
    
    public static int MyBernoulli (double p) {
        double u = random();
        int x = 0;
        if (u < p) {
            x = 1;
        } else {
            x = 0;
        }
        return x;
    }
}