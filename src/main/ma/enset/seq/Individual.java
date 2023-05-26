package ma.enset.seq;
import java.util.Arrays;
import java.util.Random;
public class Individual implements Comparable {
    private char[] chromosome = new char[GAUtils.CHROMOSOME_SIZE];
    private int fitness;
    public Individual() {
        Random random = new Random();
        for (int i = 0; i < GAUtils.CHROMOSOME_SIZE; i++) {
            chromosome[i] = GAUtils.OPTIONS.charAt(random.nextInt(GAUtils.OPTIONS.length()));
        }
    }
    public Individual(char[] chromosome) {
        this.chromosome = Arrays.copyOf(chromosome, GAUtils.CHROMOSOME_SIZE);
    }
    public void calculateFitness(){
        fitness = 0;
        for(int i = 0; i < GAUtils.CHROMOSOME_SIZE; i++){
            if(GAUtils.SOLUTION.charAt(i) == chromosome[i])
                fitness++;
        }
    }
    public int getFitness() {
        return fitness;
    }
    public char[] getChromosome() {
        return chromosome;
    }
    public void setChromosome(char[] chromosome) {
        this.chromosome = chromosome;
    }
    @Override
    public int compareTo(Object o) {
        Individual individual = (Individual) o;
        if (this.fitness > individual.fitness){
            return  1;
        } else if (this.fitness < individual.fitness) {
            return -1;
        } else {
            return 0;
        }
    }
}